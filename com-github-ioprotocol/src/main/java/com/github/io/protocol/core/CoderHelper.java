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
package com.github.io.protocol.core;

import com.github.io.protocol.proxy.DecodeMethodDelegate;
import net.sf.cglib.reflect.MethodDelegate;
import com.github.io.protocol.proxy.ArrayLengthIntDelegate;
import com.github.io.protocol.proxy.ArrayLengthIntegerDelegate;
import com.github.io.protocol.proxy.EncodeMethodDelegate;

/**
 * The common utils for protocol parse and package.
 */
public final class CoderHelper {

    /**
     * This is a common utils class, hide the public constructor.
     */
    private CoderHelper() {

    }

    /**
     * Returns the array length.
     *
     * @param bean       The JavaBean
     * @param methodName The Array length caculate method name
     * @return The array length.
     */
    public static int caculateArrayLength(final Object bean,
                                          final String methodName) {
        /**
         * if user not define this field, default value is 1.
         */
        try {
            int v = Integer.parseInt(methodName);
            if (v > 0) {
                return v;
            }
        } catch (Exception e) {
        }

        /**
         * Use cglib method delegate to caculate array length.
         */
        int length = 0;
        try {
            ArrayLengthIntDelegate delegate =
                    (ArrayLengthIntDelegate) MethodDelegate.create(bean,
                            methodName, ArrayLengthIntDelegate.class);
            length = delegate.arrayLength();
        } catch (Exception e) {
            ArrayLengthIntegerDelegate delegate =
                    (ArrayLengthIntegerDelegate) MethodDelegate.create(bean,
                            methodName, ArrayLengthIntegerDelegate.class);
            length = delegate.arrayLength();
        }
        return length;
    }

    /**
     * The decode method defined by user themselves
     *
     * @param bean       The JavaBean where method defined
     * @param methodName The proxy method name
     * @param buf        The binary buffer to be decode by user themselves
     */
    public static void decodeMethodDelegate(final Object bean, final String methodName, final byte[] buf) {
        DecodeMethodDelegate decodeMethodDelegate = (DecodeMethodDelegate) MethodDelegate.create(bean, methodName, DecodeMethodDelegate.class);
        decodeMethodDelegate.decode(buf);
    }

    /**
     * The encode method defined by user themselves
     *
     * @param bean       The java bean
     * @param methodName The proxy method name
     * @return The encoded byte buffer
     */
    public static byte[] encodeMethodDelegate(final Object bean, final String methodName) {
        EncodeMethodDelegate decodeMethodDelegate = (EncodeMethodDelegate) MethodDelegate.create(bean, methodName, EncodeMethodDelegate.class);
        return decodeMethodDelegate.encode();
    }

    /**
     * Return 32-bit cache buffer, The bitIndex is not byte align
     * So we must do this carefully
     *
     * @param buf      The byte buffer to parse wnd
     * @param bitIndex The bitIndex where to parse
     * @return The 32-bit cache
     */
    public static int get32BitWndBuffer(byte[] buf, int bitIndex) {
        int ret = 0;
        if (bitIndex + 32 <= buf.length * 8) {
            int byteIndex = bitIndex / 8;
            ret = ret | (buf[byteIndex] & 0xFF);
            ret = ret | ((buf[byteIndex + 1] & 0xFF) << 8);
            ret = ret | ((buf[byteIndex + 2] & 0xFF) << 16);
            ret = ret | ((buf[byteIndex + 3] & 0xFF) << 24);
            if (bitIndex % 8 != 0) { // not align
                int mod = bitIndex % 8;
                // clear low mod bit
                ret = ret >>> mod;
                // fill high mod bit
                byte value = buf[byteIndex + 4];
                value = (byte) ((value & 0xFF) << (8 - mod));
                value = (byte) ((value & 0xFF) >>> (8 - mod));
                ret = ret | ((value & 0xFF) << (32 - mod));
            }
        } else {
            int byteIndex = bitIndex / 8;
            if (byteIndex < buf.length)
                ret = ret | (buf[byteIndex] & 0xFF);
            if (byteIndex + 1 < buf.length)
                ret = ret | (buf[byteIndex + 1] << 8);
            if (byteIndex + 2 < buf.length)
                ret = ret | (buf[byteIndex + 2] << 16);
            if (byteIndex + 3 < buf.length)
                ret = ret | (buf[byteIndex + 3] << 24);
            if (bitIndex % 8 != 0) { // not align
                int mod = bitIndex % 8;
                // clear low mod bit
                ret = ret >>> mod;
            }
        }
        return ret;
    }
}
