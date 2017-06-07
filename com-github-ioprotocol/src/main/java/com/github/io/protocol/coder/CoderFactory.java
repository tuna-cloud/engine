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
package com.github.io.protocol.coder;

import com.github.io.protocol.annotation.DateTime;
import com.github.io.protocol.coder.impl.AsciiStringCoder;
import com.github.io.protocol.coder.impl.DateTimeCoder;
import com.github.io.protocol.coder.impl.DecimalCoder;
import com.github.io.protocol.coder.impl.NumberCoder;
import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.AsciiString;
import com.github.io.protocol.annotation.Decimal;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.core.BitBuffer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * The coder factory for various datatype <code>encode</code>
 * or <code>decode</code>.
 */
public class CoderFactory implements ICoder {

    /**
     * The coder collection for byte buffer codecs.
     */
    private Map<Class<?>, ICoder> coderHandler = new HashMap<>();

    public CoderFactory() {
        coderHandler.put(AsciiString.class, new AsciiStringCoder());
        coderHandler.put(DateTime.class, new DateTimeCoder());
        coderHandler.put(Number.class, new NumberCoder());
        coderHandler.put(Decimal.class, new DecimalCoder());
    }

    @Override
    public void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field,
                       Annotation annotation) throws Exception {
        coderHandler.get(annotation.annotationType()).decode(bitBuffer, beanMap,
                field, annotation);
    }

    @Override
    public void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field,
                       Annotation annotation) throws Exception {
        coderHandler.get(annotation.annotationType()).encode(bitBuffer, beanMap,
                field, annotation);
    }

    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field,
                                    Annotation annotation) throws Exception {
        return coderHandler.get(annotation.annotationType()).toPrettyHexString(
                bitBuffer, beanMap, field, annotation);
    }
}
