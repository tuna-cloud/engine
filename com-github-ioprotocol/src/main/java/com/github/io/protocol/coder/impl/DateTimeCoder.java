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
import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.core.BitBuffer;
import com.github.io.protocol.utils.ByteBufferUtil;
import com.github.io.protocol.utils.HexStringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class DateTimeCoder implements ICoder {

    public void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        DateTime dateTime = (DateTime) annotation;
        byte[] buf = bitBuffer.readBytes(new byte[6]);
        if(dateTime.isBCDCode())
            beanMap.put(field.getName(), ByteBufferUtil.bcdbuf2Date(buf));
        else
            beanMap.put(field.getName(), ByteBufferUtil.buf2Date(buf));
    }

    public void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        DateTime dateTime = (DateTime) annotation;
        long millSec = (Long) beanMap.get(field.getName());
        if(dateTime.isBCDCode())
            bitBuffer.write(ByteBufferUtil.date2bcdbuf(millSec));
        else
            bitBuffer.write(ByteBufferUtil.date2buf(millSec));
    }

    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        DateTime dateTime = (DateTime) annotation;
        long millSec = (Long) beanMap.get(field.getName());
        if(dateTime.isBCDCode()) {
            return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(ByteBufferUtil.date2bcdbuf(millSec)), field.getName(), millSec);
        } else {
            return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(ByteBufferUtil.date2buf(millSec)), field.getName(), millSec);
        }
    }
}
