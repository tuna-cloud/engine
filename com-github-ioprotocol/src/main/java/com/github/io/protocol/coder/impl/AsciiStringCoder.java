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
package com.github.io.protocol.coder.impl;

import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.core.BitBuffer;
import com.github.io.protocol.core.CoderHelper;
import com.github.io.protocol.utils.HexStringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AsciiStringCoder implements ICoder {

    public void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString anno = (AsciiString) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), anno.length());
        if (length > 0) {
            byte[] buf = bitBuffer.readBytes(new byte[length]);
            beanMap.put(field.getName(), new String(buf, anno.charsetName()));
        }
    }

    public void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString arrayAnnocation = (AsciiString) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), arrayAnnocation.length());

        String value = (String) beanMap.get(field.getName());
        byte[] buf = value.getBytes(arrayAnnocation.charsetName());

        if(length != buf.length)
            throw new Exception(field.getName() + " length is not equal to " + length);

        if (buf != null && buf.length > 0) {
            bitBuffer.write(buf);
        }
    }

    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        AsciiString arrayAnnocation = (AsciiString) annotation;
        String value = (String) beanMap.get(field.getName());
        return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(value, arrayAnnocation.charsetName()), field.getName(), value);
    }
}
