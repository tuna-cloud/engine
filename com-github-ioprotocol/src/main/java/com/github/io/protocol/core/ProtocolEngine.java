/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.io.protocol.core;

import com.github.io.protocol.coder.CoderFactory;
import com.github.io.protocol.protocolpool.SafeProtocolPool;
import com.github.io.protocol.utils.HexStringUtil;
import com.github.io.protocol.utils.PrettyUtil;
import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.Element;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.protocolpool.IProtocolPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ProtocolEngine {
    private ThreadLocal<BitBuffer> bitBuffer;
    private ICoder coderFactory = new CoderFactory();
    private IProtocolPool pool = new SafeProtocolPool();

    public ProtocolEngine() {
        bitBuffer = SafeBitBufferAllocator.allocate();
    }

    public ProtocolEngine(int bufferSize) {
        bitBuffer = SafeBitBufferAllocator.allocate(bufferSize);
    }

    /**
     * Decode byte buffer to JavaBean
     *
     * @param buf      The protocol binary buffer
     * @param objClass The class info of JavaBean
     * @param <T>      The JavaBean Template
     * @return The JavaBean decoded
     * @throws Exception error
     */
    public <T> T decode(byte[] buf, Class<T> objClass) throws Exception {
        decodePrepare(buf);
        return decodeObj(objClass);
    }

    /**
     * Decode byte buffer to JavaBean
     *
     * @param buf      The protocol binary buffer
     * @param index    The posotion where to decode begin
     * @param objClass The class info of JavaBean
     * @param <T>      The JavaBean Template
     * @return The JavaBean decoded
     * @throws Exception error
     */
    public <T> T decode(byte[] buf, int index, Class<T> objClass) throws Exception {
        decodePrepare(buf, index);
        return decodeObj(objClass);
    }

    /**
     * Decode byte buffer to JavaBean
     *
     * @param buf      The protocol binary buffer
     * @param index    The posotion where to decode begin
     * @param size     The size of buffer to be decoded
     * @param objClass The class info of JavaBean
     * @param <T>      The JavaBean Template
     * @return The JavaBean decoded
     * @throws Exception error
     */
    public <T> T decode(byte[] buf, int index, int size, Class<T> objClass) throws Exception {
        decodePrepare(buf, index, size);
        return decodeObj(objClass);
    }

    /**
     * While we decode buffer to JavaBean, we use recursion,so we must devide into two method
     *
     * @param buf The buffer to be decoded
     * @throws Exception error
     */
    private void decodePrepare(byte[] buf) throws Exception {
        decodePrepare(buf, 0);
    }

    /**
     * While we decode buffer to JavaBean, we use recursion,so we must devide into two method
     *
     * @param buf   The buffer to be decoded
     * @param index The posotion to be decode beginning
     * @throws Exception error
     */
    private void decodePrepare(byte[] buf, int index) throws Exception {
        decodePrepare(buf, index, buf.length - index);
    }

    /**
     * While we decode buffer to JavaBean, we use recursion,so we must devide into two method
     *
     * @param buf   The buffer to be decoded
     * @param index The posotion to be decode beginning
     * @param size  The buffer size to be decoded
     * @throws Exception error
     */
    private void decodePrepare(byte[] buf, int index, int size) throws Exception {
        bitBuffer.get().reset();
        bitBuffer.get().wrap(buf, index, size);
    }

    /**
     * Begin to decode
     *
     * @param objClass The JavaBean class info
     * @param <T>      The JavaBean class type
     * @return The JavaBean
     * @throws Exception error
     */
    private <T> T decodeObj(Class<T> objClass) throws Exception {

        Object obj = pool.getObject(objClass);
        Field[] fields = pool.getFields(objClass);

        BeanMap beanMap = BeanMap.create(obj);

        for (Field field : fields) {
            Annotation[] annotations = pool.getAnnotations(field);
            if (annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int length = CoderHelper.caculateArrayLength(obj, anno.length());
                    if (length == 1) {
                        Object o = decodeObj(field.getType());
                        beanMap.put(field.getName(), o);
                    } else {
                        // 构造数组
                        Class objectClass = field.getType().getComponentType();
                        Object[] arrs = (Object[]) Array.newInstance(objectClass, length);
                        for (int i = 0; i < arrs.length; i++) {
                            arrs[i] = decodeObj(objectClass);
                        }
                        beanMap.put(field.getName(), arrs);
                    }
                } else {
                    coderFactory.decode(bitBuffer.get(), beanMap, field, annotations[0]);
                }
            }
        }

        return (T) obj;
    }

    /**
     * Encode java bean to byte buffer
     *
     * @param obj The JavaBean
     * @return The byte buffer
     * @throws Exception error
     */
    public byte[] encode(Object obj) throws Exception {
        encodePrepare();
        encodeObj(obj);
        return encodeResult();
    }

    private void encodePrepare() {
        bitBuffer.get().reset();
    }

    /**
     * Begin to encode
     *
     * @param obj The JavaBean
     */
    private void encodeObj(Object obj) throws Exception {
        Field[] fields = pool.getFields(obj.getClass());

        BeanMap beanMap = BeanMap.create(obj);

        for (Field field : fields) {
            Annotation[] annotations = pool.getAnnotations(field);
            if (annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int length = CoderHelper.caculateArrayLength(obj, anno.length());
                    if (length == 1) {
                        encodeObj(beanMap.get(field.getName()));
                    } else {
                        Object[] objs = (Object[]) beanMap.get(field.getName());
                        for (Object ob : objs) {
                            encodeObj(ob);
                        }
                    }
                } else {
                    coderFactory.encode(bitBuffer.get(), beanMap, field, annotations[0]);
                }
            }
        }
    }

    /**
     * Encode finish,return the byte buffer
     *
     * @return The result of encode
     */
    private byte[] encodeResult() {
        return bitBuffer.get().toByteArray();
    }

    /**
     * Pretty string to debug binary protocl
     *
     * @param obj The JavaBean
     * @return The pretty string
     * @throws Exception error
     */
    public String toPrettyHexString(Object obj) throws Exception {
        StringBuilder builder = new StringBuilder();
        doPrettyHexString(obj, builder);
        builder.append("\n" + HexStringUtil.toHexString(encode(obj)));
        return PrettyUtil.format(builder.toString());
    }

    /**
     * Begin to print pretty string
     *
     * @param obj                 The JavaBean
     * @param prettyStringBuilder The prettystring builder
     * @throws Exception error
     */
    private void doPrettyHexString(Object obj, StringBuilder prettyStringBuilder) throws Exception {
        prettyStringBuilder.append(obj.getClass().getSimpleName() + "={");
        Field[] fields = pool.getFields(obj.getClass());

        BeanMap beanMap = BeanMap.create(obj);

        int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];

            Annotation[] annotations = pool.getAnnotations(field);
            if (annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int arraySize = CoderHelper.caculateArrayLength(obj, anno.length());
                    if (arraySize == 1) {
                        prettyStringBuilder.append(field.getName() + "={");
                        doPrettyHexString(beanMap.get(field.getName()), prettyStringBuilder);
                        prettyStringBuilder.append("},");
                    } else {
                        Object[] objs = (Object[]) beanMap.get(field.getName());
                        for (Object ob : objs) {
                            prettyStringBuilder.append(field.getName() + "={");
                            doPrettyHexString(ob, prettyStringBuilder);
                            prettyStringBuilder.append("},");
                        }
                    }
                } else {
                    String line = coderFactory.toPrettyHexString(bitBuffer.get(), beanMap, field, annotations[0]);
                    if (i == (length - 1)) {
                        line = line.replace(",", "");
                    }
                    prettyStringBuilder.append(line);
                }
            }
        }
        prettyStringBuilder.append("}");
    }
}
