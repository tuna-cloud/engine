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

package com.github.io.protocol.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import net.sf.cglib.beans.BeanMap;

public class ArrayHelper {
    private ArrayHelper() {

    }

    public static void main(String[] args) throws Exception {
        Object[] values = new Object[3];
        byte[] x = new byte[3];
        fillNumberArray(x, "byte[]", values);
        System.out.println(Arrays.toString(values));

        Byte[] a = new Byte[]{1,2,3};
        fillNumberArray(a, "Byte[]", values);
        System.out.println(Arrays.toString(values));

        short[] b = new short[]{1,2,3};
        fillNumberArray(b, "short[]", values);
        System.out.println(Arrays.toString(values));

        Short[] c = new Short[]{1,2,3};
        fillNumberArray(c, "Short[]", values);
        System.out.println(Arrays.toString(values));

        int[] d = new int[]{1,2,3};
        fillNumberArray(d, "int[]", values);
        System.out.println(Arrays.toString(values));

        Integer[] e = new Integer[]{1,2,3};
        fillNumberArray(e, "Integer[]", values);
        System.out.println(Arrays.toString(values));

        Long[] f = new Long[]{1L,2L,3L};
        fillNumberArray(f, "Long[]", values);
        System.out.println(Arrays.toString(values));

        long[] g = new long[]{1L,2L,3L};
        fillNumberArray(g, "long[]", values);
        System.out.println(Arrays.toString(values));
    }

    public static void putArrayToBeanMap(BeanMap beanMap, Field field, Object[] array) throws Exception {
        String typeName = field.getType().getSimpleName();
        if(typeName.equals("byte[]")) {
            byte[] values = new byte[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (byte) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("Byte[]")) {
            Byte[] values = new Byte[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (Byte) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("short[]")) {
            short[] values = new short[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (short) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("Short[]")) {
            Short[] values = new Short[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (Short) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("int[]")) {
            int[] values = new int[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (int) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("Integer[]")) {
            Integer[] values = new Integer[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (Integer) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("long[]")) {
            long[] values = new long[array.length];
            int i = 0;
            for(Object o : array) {
                values[i++] = (long) o;
            }
            beanMap.put(field.getName(), values);
        } else if(typeName.equals("Long[]")) {
            Long[] values = new Long[array.length];
            int i = 0;
            for (Object o : array) {
                values[i++] = (Long) o;
            }
            beanMap.put(field.getName(), values);
        } else {
            throw new Exception("un support type operation");
        }
    }

    /**
     * put float array to JavaBean
     * @param beanMap the JavaBean where to put the array
     * @param field the JavaBean field information
     * @param array the array put into JavaBean
     */
    public static void putFloatArrayToBeanMap(BeanMap beanMap, Field field, double[] array) {
        String typeName = field.getType().getSimpleName();
        if (typeName.equals("float[]")) {
            float[] values = new float[array.length];
            for(int i = 0; i < array.length; i++ ) {
                values[i] = (float)array[i];
            }
            beanMap.put(field.getName(), values);
        } else if (typeName.equals("Float[]")) {
            Float[] values = new Float[array.length];
            for(int i = 0; i < array.length; i++ ) {
                values[i] = (float)array[i];
            }
            beanMap.put(field.getName(), values);
        } else if (typeName.equals("double[]")) {
            beanMap.put(field.getName(), array);
        } else if (typeName.equals("Double[]")) {
            Double[] values = new Double[array.length];
            for(int i = 0; i < array.length; i++ ) {
                values[i] = array[i];
            }
            beanMap.put(field.getName(), values);
        }
    }

    public static void fillNumberArray(Object obj, String typeName, Object[] values) throws Exception {
        if(typeName.equals("byte[]")) {
            byte[] buf = (byte[]) obj;
            int j = 0;
            for(byte v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("Byte[]")) {
            Byte[] buf = (Byte[]) obj;
            int j = 0;
            for(Byte v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("short[]")) {
            short[] buf = (short[]) obj;
            int j = 0;
            for(short v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("Short[]")) {
            Short[] buf = (Short[]) obj;
            int j = 0;
            for(Short v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("int[]")) {
            int[] buf = (int[]) obj;
            int j = 0;
            for(int v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("Integer[]")) {
            Integer[] buf = (Integer[]) obj;
            int j = 0;
            for(Integer v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("long[]")) {
            long[] buf = (long[]) obj;
            int j = 0;
            for(long v : buf) {
                values[j++] = v;
            }
        } else if(typeName.equals("Long[]")) {
            Long[] buf = (Long[]) obj;
            int j = 0;
            for(Long v : buf) {
                values[j++] = v;
            }
        } else {
            throw new Exception("unsupport array type");
        }
    }
}
