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

/**
 * A typesafe enumeration for byte orders.
 * The internet comunication is relative two case,
 * big endian: 0x3211 convert to buffer is new byte[]{0x32, 0x11}
 * small endian: 0x3211 convert to buffer is new byte[]{0x11,0x32}
 */
public enum ByteOrder {
    /**
     * Constant denoting big-endian byte order.  In this order, the unsigned8bit of a
     * multibyte value are ordered from most significant to least significant.
     */
    BigEndian,
    /**
     * Constant denoting little-endian byte order.  In this order, the unsigned8bit of
     * a multibyte value are ordered from least significant to most
     * significant.
     */
    SmallEndian
}
