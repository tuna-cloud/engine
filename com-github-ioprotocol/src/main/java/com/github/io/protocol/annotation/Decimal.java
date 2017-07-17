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
package com.github.io.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annocation marked on a property of a JavaBean which can
 * convert a decimal to <code>byte[]</code>
 * or <code>byte[]</code> to a decimal.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decimal {
    /**
     * The formation of decimal convertion.
     *
     * @return The formation of convertion, default is normal.
     * true: use Binary-Coded Decimal‎ to convert.
     * false: user normal way to convert.
     */
    boolean isBCDCode() default false;

    /**
     * The type of decimal in java is both signed, while we expect an
     * unsigned value, we must user a <code>Double</code> type to represent
     * a 32 bit unsigned value.
     *
     * @return The decimal sign value.
     */
    Sign sign() default Sign.Unsigned;

    /**
     * The bit width of the decimal.
     * Fox example:
     * <code>float</code>: 32 bit
     * <code>double</code>: 64 bit
     *
     * @return The bit width of decimal value.
     */
    int width() default 32;

    /**
     * The offset of a decimal.
     * when we want to send a decimal with value -50,we can plus 100
     * to avoid negative decimal transmission.
     *
     * @return The offset of a decimal value.
     */
    double offset() default 0;

    /**
     * The bit reserved value in actually
     *
     * @return The scale of actual value
     */
    double scale() default 1;

    /**
     * The Network byte order.
     *
     * @return The Network byte order.
     */
    ByteOrder order() default ByteOrder.SmallEndian;

    /**
     * The user defined special encode convertion function in the JavaBean.
     *
     * @return The encode convertion function name.
     */
    String encoder() default "";

    /**
     * The user defined special decode convertion function in the JavaBean.
     *
     * @return The decode convertion function name.
     */
    String decoder() default "";

    /**
     * The number array length. user define themself.
     * The value also support method name which return a Integer value.
     *
     * @return The number array length. default value is 1.
     */
    String length() default "1";

    /**
     * The precision of float value
     *
     * @return The precision of float value, default value is 1
     */
    int precision() default 1;
}
