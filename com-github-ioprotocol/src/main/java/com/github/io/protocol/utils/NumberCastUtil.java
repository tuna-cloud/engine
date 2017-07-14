package com.github.io.protocol.utils;

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
public class NumberCastUtil {

    /**
     * Cast number value to destType value
     *
     * @param value    The value to be casted
     * @param destType The destation value type
     * @return
     * @throws Exception Whie destType not surpport will throw a cast exception
     */
    public static Object cast(Object value, String destType) throws Exception {
        if (value instanceof Byte) {
            return byteCastToObj((Byte) value, destType);
        } else if (value instanceof Short) {
            return shortCastToObj((Short) value, destType);
        } else if (value instanceof Integer) {
            return intCastToObj((Integer) value, destType);
        } else if (value instanceof Long) {
            return longCastToObj((Long) value, destType);
        } else if (value instanceof Float) {
            return floatCastToObj((Float) value, destType);
        } else if (value instanceof Double) {
            return doubleCastToObj((Double) value, destType);
        } else {
            throw new Exception("un support destType:" + destType);
        }
    }

    /**
     * Cast a Byte type value to destType value
     *
     * @param value    The byte value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object byteCastToObj(Byte value, String destType) throws Exception {
        if (destType.equalsIgnoreCase("byte")
                || destType.equalsIgnoreCase("byte[]")) {
            return value;
        }

        if (destType.equalsIgnoreCase("short")
                || destType.equalsIgnoreCase("short[]")) {
            return value.shortValue();
        }

        if (destType.equalsIgnoreCase("int")
                || destType.equalsIgnoreCase("Integer")
                || destType.equalsIgnoreCase("int[]")
                || destType.equalsIgnoreCase("Integer[]")) {
            return value.intValue();
        }

        if (destType.equalsIgnoreCase("long")
                || destType.equalsIgnoreCase("long[]")) {
            return value.longValue();
        }

        throw new Exception("Un support value type:" + destType);
    }

    /**
     * Cast a Short type value to destType value
     *
     * @param value    The short value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object shortCastToObj(Short value, String destType) throws Exception {
        if (destType.equalsIgnoreCase("short")
                || destType.equalsIgnoreCase("short[]")) {
            return value;
        }

        if (destType.equalsIgnoreCase("int")
                || destType.equalsIgnoreCase("Integer")
                || destType.equalsIgnoreCase("int[]")
                || destType.equalsIgnoreCase("Integer[]")) {
            return value.intValue();
        }

        if (destType.equalsIgnoreCase("long")
                || destType.equalsIgnoreCase("long[]")) {
            return value.longValue();
        }

        throw new Exception("Un support value type:" + destType);
    }

    /**
     * Cast a Integer type value to destType value
     *
     * @param value    The Integer value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object intCastToObj(Integer value, String destType) throws Exception {

        if (destType.equalsIgnoreCase("int")
                || destType.equalsIgnoreCase("Integer")
                || destType.equalsIgnoreCase("int[]")
                || destType.equalsIgnoreCase("Integer[]")) {
            return value;
        }

        if (destType.equalsIgnoreCase("long")
                || destType.equalsIgnoreCase("long[]")) {
            return value.longValue();
        }

        throw new Exception("Un support value type:" + destType);
    }


    /**
     * Cast a Long type value to destType value
     *
     * @param value    The Long value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object longCastToObj(Long value, String destType) throws Exception {

        if (destType.equalsIgnoreCase("long")
                || destType.equalsIgnoreCase("long[]")) {
            return value.longValue();
        }

        throw new Exception("Un support value type:" + destType);
    }

    /**
     * Cast a Float type value to destType value
     *
     * @param value    The Float value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object floatCastToObj(Float value, String destType) throws Exception {

        if (destType.equalsIgnoreCase("float")
                || destType.equalsIgnoreCase("float[]")) {
            return value;
        }

        if (destType.equalsIgnoreCase("double")
                || destType.equalsIgnoreCase("double[]")) {
            return value.doubleValue();
        }

        throw new Exception("Un support value type:" + destType);
    }

    /**
     * Cast a Double type value to destType value
     *
     * @param value    The Double value
     * @param destType The destation value type
     * @return The converted value
     * @throws Exception Unsurport value type
     */
    private static Object doubleCastToObj(Double value, String destType) throws Exception {

        if (destType.equalsIgnoreCase("double")
                || destType.equalsIgnoreCase("double[]")) {
            return value.doubleValue();
        }

        throw new Exception("Un support value type:" + destType);
    }
}
