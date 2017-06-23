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
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Support multiply operation, every accession will make a attempt to create a instance if not exist.
 */
public class SafeProtocolPool implements IProtocolPool {

    /**
     * This Object pool is mainly used for cache object to be create massively.
     * While we create a pool for every class type, we use thread local to keep safe between multi threads.
     */
    private static ThreadLocal<Map<Class<?>, Object>> objCache = new ThreadLocal<Map<Class<?>, Object>>() {
        @Override
        protected Map<Class<?>, Object> initialValue() {
            return new WeakHashMap<>();
        }
    };

    /**
     * The <code>fieldCache</code> is used to cache JavaBean field information.
     * this cache is registed when first request and never be change again.
     */
    private Map<Class<?>, Field[]> fieldCache = new WeakHashMap<>();

    /**
     * The <code>annotationCache</code> is used to cache JavaBean field annotation information.
     * this cache is registed when first request and never be change again.
     */
    private Map<Field, Annotation[]> annotationCache = new WeakHashMap<>();

    @Override
    public Object getObject(
        Class<?> cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!objCache.get().containsKey(cls)) {
            objCache.get().put(cls, cls.getConstructor().newInstance());
        }
        return objCache.get().get(cls);
    }

    @Override
    public Field[] getFields(Class<?> cls) {
        if (!fieldCache.containsKey(cls)) {
            fieldCache.put(cls, cls.getDeclaredFields());
        }
        return fieldCache.get(cls);
    }

    @Override
    public Annotation[] getAnnotations(Field field) {
        if (!annotationCache.containsKey(field)) {
            annotationCache.put(field, field.getDeclaredAnnotations());
        }
        return annotationCache.get(field);
    }
}
