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
 * Not support multiply operation, every accession will make a attempt to create a instance if not exist.
 */
public class UnsafeProtocolPool implements IProtocolPool {
    private Map<Class<?>, Object> objCache = new WeakHashMap<>();

    private Map<Class<?>, Field[]> fieldCache = new WeakHashMap<>();

    private Map<Field, Annotation[]> annotationCache = new WeakHashMap<>();

    @Override
    public Object getObject(
        Class<?> cls) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!objCache.containsKey(cls)) {
            objCache.put(cls, cls.getConstructor().newInstance());
        }
        return objCache.get(cls);
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
