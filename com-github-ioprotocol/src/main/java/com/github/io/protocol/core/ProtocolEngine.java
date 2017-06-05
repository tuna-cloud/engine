package com.github.io.protocol.core;

import com.github.io.protocol.coder.CoderFactory;
import com.github.io.protocol.utils.HexStringUtil;
import com.github.io.protocol.utils.PrettyUtil;
import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.Element;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.protocolpool.IProtocolPool;
import com.github.io.protocol.protocolpool.UnsafeProtocolPool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * @Project:net-top-framwork-protocol
 * @Package net.top.framwork.protocolpool.core
 * @Description:
 * @author: xsy
 * @date： 2016/6/21
 * @version： V1.0
 */
public class ProtocolEngine {
    private BitBuffer bitBuffer;
    private int baseIndex = 0;
    private ICoder coderFactory = new CoderFactory();
    private IProtocolPool pool = new UnsafeProtocolPool();

    public ProtocolEngine() {
        bitBuffer = new BitBuffer();
        baseIndex = 0;
    }

    public ProtocolEngine(int bufferSize) {
        bitBuffer = new BitBuffer(bufferSize);
        baseIndex = 0;
    }

    /**
     *
     * @param buf
     * @param objClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T decode(byte[] buf, Class<T> objClass) throws Exception {
        decodePrepare(buf);
        return decodeObj(objClass);
    }

    /**
     *
     * @param buf
     * @param objClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T decode(byte[] buf, int index, Class<T> objClass) throws Exception {
        decodePrepare(buf, index);
        return decodeObj(objClass);
    }

    /**
     *
     * @param buf
     * @param objClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T decode(byte[] buf, int index, int size, Class<T> objClass) throws Exception {
        decodePrepare(buf, index, size);
        return decodeObj(objClass);
    }

    /**
     * @param buf
     * @throws Exception
     */
    private void decodePrepare(byte[] buf) throws Exception {
        decodePrepare(buf, 0);
    }

    /**
     *
     * @param buf
     * @param index
     * @throws Exception
     */
    private void decodePrepare(byte[] buf, int index) throws Exception {
        decodePrepare(buf, index, buf.length - index);
    }

    /**
     * 解析对象时，先将数组wrap到cacheBuffer中
     * @param buf
     * @param index
     * @param size
     * @throws Exception
     */
    private void decodePrepare(byte[] buf, int index, int size) throws Exception {
        bitBuffer.reset();
        bitBuffer.wrap(buf, index, size);
        baseIndex = index;
    }

    /**
     *
     * @param objClass
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T decodeObj(Class<T> objClass) throws Exception {

        Object obj = pool.getObject(objClass);
        Field[] fields = pool.getFields(objClass);

        BeanMap beanMap = BeanMap.create(obj);

        for (Field field : fields) {
            Annotation[] annotations = pool.getAnnotations(field);
            if(annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int length = CoderHelper.caculateArrayLength(obj, anno.length());
                    if(length == 1) {
                        Object o = decodeObj(field.getType());
                        beanMap.put(field.getName(), o);
                    } else {
                        // 构造数组
                        Class objectClass = field.getType().getComponentType();
                        Object[] arrs = (Object[]) Array.newInstance(objectClass, length);
                        for(int i = 0; i < arrs.length; i++) {
                            arrs[i] = decodeObj(objectClass);
                        }
                        beanMap.put(field.getName(), arrs);
                    }
                } else {
                    coderFactory.decode(bitBuffer, beanMap, field, annotations[0]);
                }
            }
        }

        return (T) obj;
    }

    /**
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public byte[] encode(Object obj) throws Exception {
        encodePrepare();
        encodeObj(obj);
        return encodeResult();
    }

    private void encodePrepare() {
        bitBuffer.reset();
    }

    /**
     * 反射实现协议打包
     * @param obj
     * @return
     */
    private void encodeObj(Object obj) throws Exception {
        Field[] fields = pool.getFields(obj.getClass());

        BeanMap beanMap = BeanMap.create(obj);

        for (Field field : fields) {
            Annotation[] annotations = pool.getAnnotations(field);
            if(annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int length = CoderHelper.caculateArrayLength(obj, anno.length());
                    if(length == 1) {
                        encodeObj(beanMap.get(field.getName()));
                    } else {
                        Object[] objs = (Object[]) beanMap.get(field.getName());
                        for (Object ob : objs) {
                            encodeObj(ob);
                        }
                    }
                } else {
                    coderFactory.encode(bitBuffer, beanMap, field, annotations[0]);
                }
            }
        }
    }

    private byte[] encodeResult() {
        return bitBuffer.toByteArray();
    }

    /**
     * 转成易于调试的输出信息
     * @param obj
     * @return
     */
    public String toPrettyHexString(Object obj) throws Exception {
        StringBuilder builder = new StringBuilder();
        doPrettyHexString(obj, builder);
        builder.append("\n" + HexStringUtil.toHexString(encode(obj)));
        return PrettyUtil.format(builder.toString());
    }

    /**
     *
     * @param obj
     * @param prettyStringBuilder
     * @throws Exception
     */
    private void doPrettyHexString(Object obj, StringBuilder prettyStringBuilder) throws Exception {
        prettyStringBuilder.append(obj.getClass().getSimpleName() + "={");
        Field[] fields = pool.getFields(obj.getClass());

        BeanMap beanMap = BeanMap.create(obj);

        int length = fields.length;
        for (int i = 0; i < length; i++) {
            Field field = fields[i];

            Annotation[] annotations = pool.getAnnotations(field);
            if(annotations != null && annotations.length > 0) {
                if (annotations[0] instanceof Element) {
                    Element anno = (Element) annotations[0];
                    int arraySize = CoderHelper.caculateArrayLength(obj, anno.length());
                    if(arraySize == 1) {
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
                    String line = coderFactory.toPrettyHexString(bitBuffer, beanMap, field, annotations[0]);
                    if(i == (length - 1)) {
                        line = line.replace(",", "");
                    }
                    prettyStringBuilder.append(line);
                }
            }
        }
        prettyStringBuilder.append("}");
    }
}
