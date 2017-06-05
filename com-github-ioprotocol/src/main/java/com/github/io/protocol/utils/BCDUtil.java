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

public class BCDUtil {

    private BCDUtil() {

    }

    /**
     * BCD code translate to integer value
     * @param bcdBuffer bcd buffer
     * @param offset    the offset which the buffer begin from
     * @param length    the buffer actual size
     * @return The Integer value
     */
    public static int bcd2value(byte[] bcdBuffer, int offset, int length) {
        int value = 0;
        for (int i = offset; i < length + offset; i++) {
            value = value * 10;
            value += (bcdBuffer[i] & 0xF0) >>> 4;
            value = value * 10;
            value += bcdBuffer[i] & 0x0F;
        }
        return value;
    }

    /**
     * Translate bcd buffer to Integer value
     * @param bcdBuffer range(0 - 2147483647)
     * @return The Integer value
     */
    public static int bcd2value(byte[] bcdBuffer) {
        return bcd2value(bcdBuffer, 0, bcdBuffer.length);
    }

    /**
     * @param value Integer value which to translate
     * @param bcdLength must mod 2 = 0
     * @return The bcd buffer
     */
    public static byte[] value2bcd(int value, int bcdLength) {
        byte[] buf = new byte[bcdLength / 2];
        for (int i = bcdLength / 2 - 1; i >= 0; i--) {
            int bit4 = value % 10;
            buf[i] = (byte) (bit4 & 0x0F);
            value = (value - bit4) / 10;
            bit4 = value % 10;
            buf[i] = (byte) (buf[i] | ((byte) ((bit4 & 0x0F) << 4)));
            value = (value - bit4) / 10;
        }

        return buf;
    }

}
