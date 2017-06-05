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

package com.github.io.protocol.coder;

import com.github.io.protocol.core.BitBuffer;
import com.github.io.protocol.core.CoderHelper;

public abstract class AbstractNumberCoder {
    /**
     *
     * @param bitWidth
     * @param length
     * @param bitBuffer
     * @param bean
     * @param decoder
     * @throws Exception
     */
    protected void decodeItself(int bitWidth, int length, BitBuffer bitBuffer, Object bean, String decoder) throws Exception {
        int totalBytes = bitWidth * length / 8;
        int leaveBits = (bitWidth * length) % 8;
        byte[] buf = null;
        if (leaveBits == 0) {
            buf = bitBuffer.readBytes(new byte[totalBytes]);
        } else {
            buf = bitBuffer.readBytes(new byte[totalBytes]);
            byte v = bitBuffer.readBit(leaveBits);
            byte[] tempBuf = new byte[totalBytes + 1];
            System.arraycopy(buf, 0, tempBuf, 0, buf.length);
            tempBuf[buf.length] = v;
            buf = tempBuf;
        }
        CoderHelper.decodeMethodDelegate(bean, decoder, buf);
    }

    /**
     *
     * @param bitWidth
     * @param length
     * @param bitBuffer
     * @param bean
     * @param encoder
     * @throws Exception
     */
    protected void encodeItself(int bitWidth, int length, BitBuffer bitBuffer, Object bean, String encoder) throws Exception {
        byte[] buf = CoderHelper.encodeMethodDelegate(bean, encoder);
        if ((bitWidth * length) % 8 == 0) {
            if ((bitWidth * length / 8) != buf.length) {
                throw new Exception(encoder + " return length is not correct");
            }
            bitBuffer.write(buf);
        } else {
            if (bitWidth * length / 8 + 1 != buf.length) {
                throw new Exception(encoder + " return length is not correct");
            }
            if (buf.length > 1) {
                for (int i = 0; i < buf.length - 1; i++) {
                    bitBuffer.write(buf[i]);
                }
                bitBuffer.writeBit((bitWidth * length) % 8, buf[buf.length - 1]);
            }
        }
    }
}
