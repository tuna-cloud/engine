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
 * convert <code>Long</code> or <code>Date</code> field to <code>byte[]</code>
 * or <code>byte[]</code> to <code>Long</code> or <code>Date</code>.
 * For examlple:
 * while Binary-Coded Decimal‎,
 * new byte[]{0x16,0x01,0x12,0x08,0x01,0x00} convert to long or date is
 * like this: 2016-01-12 08:01:00
 * while normal,
 * new byte[]{0x0A,0x01,0x0C,0x08,0x01,0x00} convert to long or date is
 * like this: 2010-01-12 08:01:00
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTime {
    /**
     * Returns if the convertion is use Binary-Coded Decimal‎.
     * @return The convertion format, default is normal.
     */
    boolean isBCDCode() default false;
}
