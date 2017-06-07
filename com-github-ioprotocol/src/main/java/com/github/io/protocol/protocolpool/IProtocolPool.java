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

package com.github.io.protocol.protocolpool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface IProtocolPool {
    /**
     * Get the pooled object assosiate with Object class info.
     *
     * @param cls The class info associated with pooled object
     * @return The pooled object
     * @throws NoSuchMethodException     The java relection exception
     * @throws IllegalAccessException    The java relection exception
     * @throws InvocationTargetException The java relection exception
     * @throws InstantiationException    The java relection exception
     */
    Object getObject(
            Class<?> cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;

    /**
     * Get the field list of class T
     *
     * @param cls The class info
     * @return The field list
     */
    Field[] getFields(Class<?> cls);

    /**
     * Get the annotations marked on field
     *
     * @param field The field where marked on annotations
     * @return The annotations list
     */
    Annotation[] getAnnotations(Field field);

}
