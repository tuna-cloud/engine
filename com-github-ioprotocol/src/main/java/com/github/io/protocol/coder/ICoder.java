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

import com.github.io.protocol.core.BitBuffer;
import net.sf.cglib.beans.BeanMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface ICoder {
    public static String FIELD_TEMPLATE = "%s(%s=%s),";


    /**
     * Decode cache to JavaBean
     *
     * @param bitBuffer  The cache
     * @param beanMap    The JavaBean
     * @param field      The operation field
     * @param annotation The annotation
     * @throws Exception error
     */
    void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;

    /**
     * Encode JavaBean to cache
     *
     * @param bitBuffer  The cache
     * @param beanMap    The JavaBean
     * @param field      The operation field
     * @param annotation The annotation
     * @throws Exception error
     */
    void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;

    /**
     * To pretty string
     *
     * @param bitBuffer  The cache
     * @param beanMap    The JavaBean
     * @param field      The operation field
     * @param annotation The annotation
     * @return The pretty string
     * @throws Exception error
     */
    String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception;
}
