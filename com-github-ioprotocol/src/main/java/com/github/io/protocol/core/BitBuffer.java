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


public class BitBuffer {
    /**
     * 缓存数组
     */
    private byte[] cacheBuffer;
    /**
     * 按位索引
     */
    private int writeBitIndex = 0;
    /**
     * 读索引
     */
    private int readBitIndex = 0;
    /**
     * 最大容量
     */
    private int writeBitIndexLimit = 0;

    /**
     * 第二缓冲区，主要解决错位的提取与保存
     * 4 byte = 32 bit
     */
    private int secondCache = 0;
    /**
     * 第二缓冲区索引，有效范围 0 ~ 31
     */
    private int secondCacheIndex = 0;

    public BitBuffer() {
        cacheBuffer = new byte[1024];
        writeBitIndex = 0;
        writeBitIndexLimit = 1024 * 8;
        readBitIndex = 0;
    }

    /**
     * @param bufferSize : 字节为单位
     */
    public BitBuffer(int bufferSize) {
        cacheBuffer = new byte[bufferSize];
        writeBitIndex = 0;
        writeBitIndexLimit = bufferSize * 8;
        readBitIndex = 0;
    }

    /**
     * 重置bit索引
     */
    public void reset() {
        writeBitIndex = 0;
        writeBitIndexLimit = cacheBuffer.length * 8;
        readBitIndex = 0;
    }

    /**
     * Filled buf to bit buffer cache
     *
     * @param buf The buffer to be wraped
     * @throws Exception error
     */
    public void wrap(byte[] buf) throws Exception {
        wrap(buf, 0);
    }

    /**
     * Filled buf to bit buffer cache
     *
     * @param buf   The buffer to be wraped
     * @param index The position where to be wraped from
     * @throws Exception error
     */
    public void wrap(byte[] buf, int index) throws Exception {
        wrap(buf, index, buf.length - index);
    }

    /**
     * Filled buf to bit buffer cache
     *
     * @param buf   The buffer to be wraped
     * @param index The position where to be wraped from
     * @param size  The buf's size to be wraped
     * @throws Exception error
     */
    public void wrap(byte[] buf, int index, int size) throws Exception {
        if (size > buf.length - index)
            throw new Exception("Size is big than buf length");
        if (size > cacheBuffer.length) {
            throw new Exception("BitBuffer length is not enouth");
        }

        System.arraycopy(buf, index, cacheBuffer, 0, size);
        readBitIndex = 0;
        writeBitIndex = size * 8;
    }

    /**
     * Get current read position
     *
     * @return The position of read index
     * @throws Exception while bit buffer cache is not byte align, this will be throw
     */
    public int getCurrentReadIndex() throws Exception {
        if (readBitIndex % 8 != 0)
            throw new Exception("Not byte align");
        return readBitIndex / 8;
    }

    /**
     * Get current write position
     *
     * @return The position of write index
     * @throws Exception while bit buffer cache is not byte align, this will be throw
     */
    public int getCurrentWriteIndex() throws Exception {
        if (writeBitIndex % 8 != 0) {
            throw new Exception("WriteBitIndex Not byte align");
        }
        return writeBitIndex / 8;
    }

    /**
     * Output byte buffer
     *
     * @return byte buffer
     */
    public byte[] toByteArray() {
        int size = writeBitIndex / 8;
        if (writeBitIndex % 8 != 0) {
            size += 1;
        }
        byte[] buf = new byte[size];
        System.arraycopy(cacheBuffer, 0, buf, 0, size);
        return buf;
    }

    /**
     * secondCache is a 32 bit number, when this cache is full,
     * it will be flush into cacheBuffer in SmallEndian.
     */
    public void flushCache() {
        // 从bitIndex 字节开始，取4个byte
        int mod = writeBitIndex % 8;
        int op4Byte = CoderHelper.get32BitWndBuffer(cacheBuffer, writeBitIndex - mod);
        if (mod == 0) {
            op4Byte = secondCache;
            int byteIndex = (writeBitIndex - mod) / 8;
            if (byteIndex < cacheBuffer.length)
                cacheBuffer[byteIndex] = (byte) (op4Byte & 0xFF);
            if (byteIndex + 1 < cacheBuffer.length)
                cacheBuffer[byteIndex + 1] = (byte) ((op4Byte >>> 8) & 0xFF);
            if (byteIndex + 2 < cacheBuffer.length)
                cacheBuffer[byteIndex + 2] = (byte) ((op4Byte >>> 16) & 0xFF);
            if (byteIndex + 3 < cacheBuffer.length)
                cacheBuffer[byteIndex + 3] = (byte) ((op4Byte >>> 24) & 0xFF);
        } else {
            // 保留op4Byte的低mod位,其余bit清零
            op4Byte = op4Byte << (32 - mod);
            op4Byte = op4Byte >>> (32 - mod);

            op4Byte = op4Byte | (secondCache << mod);
            // 将op4Byte 存回buffer中
            int byteIndex = (writeBitIndex - mod) / 8;
            if (byteIndex < cacheBuffer.length)
                cacheBuffer[byteIndex] = (byte) (op4Byte & 0xFF);
            if (byteIndex + 1 < cacheBuffer.length)
                cacheBuffer[byteIndex + 1] = (byte) ((op4Byte >>> 8) & 0xFF);
            if (byteIndex + 2 < cacheBuffer.length)
                cacheBuffer[byteIndex + 2] = (byte) ((op4Byte >>> 16) & 0xFF);
            if (byteIndex + 3 < cacheBuffer.length)
                cacheBuffer[byteIndex + 3] = (byte) ((op4Byte >>> 24) & 0xFF);
            // 将secCache 的高mod位存入 byteIndex + 4 字节中
            if (byteIndex + 4 < cacheBuffer.length) {
                cacheBuffer[byteIndex + 4] = (byte) (secondCache >>> (32 - mod) & 0xFF);
            }
        }

        // 处理索引
        writeBitIndex += secondCacheIndex;
        secondCacheIndex = 0;
        secondCache = 0;
    }

    /**
     * Write 1 bit number to BitBuffer cache
     *
     * @param bit The 1 bit number
     * @throws Exception error
     */
    public void writeBit1(byte bit) throws Exception {
        checkCapacity(1);
        secondCache = secondCache | (bit & 0x01);
        secondCacheIndex += 1;
        flushCache();
    }

    /**
     * Read 1~7 bit value from BitBuffer cache
     *
     * @param bits The number bits to read togother
     * @return The value read from cache
     * @throws Exception error
     */
    public byte readBit(int bits) throws Exception {
        if (bits < 1 || bits > 7)
            throw new Exception("readBit only support 1-7,current is:" + bits);
        checkReadIndex(bits);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += bits;
        tem = tem << (24 + 8 - bits);
        tem = tem >>> (24 + 8 - bits);
        return (byte) (tem & 0xFF);
    }

    /**
     * Write low bits value to BitBuffer cache
     *
     * @param bits  The number bit to write to cache from value
     * @param value The value to write to cache
     * @throws Exception error
     */
    public void writeBit(int bits, byte value) throws Exception {
        if (bits < 1 || bits > 7)
            throw new Exception("readBit only support 1-7,current is:" + bits);
        checkCapacity(bits);
        int v = value & 0xFF;
        v = v << (8 - bits);
        v = v >>> (8 - bits);
        secondCache = secondCache | v;
        secondCacheIndex += bits;
        flushCache();
    }

    /**
     * Read one bit value from cache
     *
     * @return The value of one bit
     * @throws Exception error
     */
    public byte readBit1() throws Exception {
        checkReadIndex(1);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 1;
        return (byte) (tem & 0x01);
    }

    /**
     * Write 2 bit value to cache
     *
     * @param bits The 2 bit value
     * @throws Exception error
     */
    public void writeBit2(byte bits) throws Exception {
        checkCapacity(2);
        secondCache = secondCache | (bits & 0x03);
        secondCacheIndex += 2;
        flushCache();
    }

    /**
     * Read 2 bit value from cache
     *
     * @return The 2 bit value
     * @throws Exception error
     */
    public byte readBit2() throws Exception {
        checkReadIndex(2);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 2;
        return (byte) (tem & 0x03);
    }

    /**
     * Write 3 bit value to cache
     *
     * @param bits The 3-bit value
     * @throws Exception error
     */
    public void writeBit3(byte bits) throws Exception {
        checkCapacity(3);
        secondCache = secondCache | (bits & 0x07);
        secondCacheIndex += 3;
        flushCache();
    }

    /**
     * Read 3-bit value from cache
     *
     * @return The 3-bit value
     * @throws Exception error
     */
    public byte readBit3() throws Exception {
        checkReadIndex(3);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 3;
        return (byte) (tem & 0x07);
    }

    /**
     * Write 4-bit value to cache
     *
     * @param bits The 4-bit value
     * @throws Exception error
     */
    public void writeBit4(byte bits) throws Exception {
        checkCapacity(4);
        secondCache = secondCache | (bits & 0x0F);
        secondCacheIndex += 4;
        flushCache();
    }

    /**
     * Read 4-bit value from cache
     *
     * @return The 4-bit value
     * @throws Exception error
     */
    public byte readBit4() throws Exception {
        checkReadIndex(4);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 4;
        return (byte) (tem & 0x0F);
    }

    /**
     * Write 5-bit value to cache
     *
     * @param bits The 5-bit value
     * @throws Exception error
     */
    public void writeBit5(byte bits) throws Exception {
        checkCapacity(5);
        secondCache = secondCache | (bits & 0x1F);
        secondCacheIndex += 5;
        flushCache();
    }

    /**
     * Read 5-bit value from cache
     *
     * @return The 5-bit value
     * @throws Exception error
     */
    public byte readBit5() throws Exception {
        checkReadIndex(5);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 5;
        return (byte) (tem & 0x1F);
    }

    /**
     * Write 6-bit value to cache
     *
     * @param bits The 6-bit value
     * @throws Exception error
     */
    public void writeBit6(byte bits) throws Exception {
        checkCapacity(6);
        secondCache = secondCache | (bits & 0x3F);
        secondCacheIndex += 6;
        flushCache();
    }

    /**
     * Read 6-bit value from cache
     *
     * @return The 6-bit value
     * @throws Exception error
     */
    public byte readBit6() throws Exception {
        checkReadIndex(6);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 6;
        return (byte) (tem & 0x3F);
    }

    /**
     * Write 7-bit value to cache
     *
     * @param bits The 7-bit value
     * @throws Exception error
     */
    public void writeBit7(byte bits) throws Exception {
        checkCapacity(7);
        secondCache = secondCache | (bits & 0x7F);
        secondCacheIndex += 7;
        flushCache();
    }

    /**
     * Read 7-bit value from cache
     *
     * @return The 7-bit value
     * @throws Exception error
     */
    public byte readBit7() throws Exception {
        checkReadIndex(7);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 7;
        return (byte) (tem & 0x7F);
    }

    /**
     * Write 8-bit value to cache
     *
     * @param value The 8-bit value
     * @throws Exception error
     */
    public void write(byte value) throws Exception {
        checkCapacity(8);
        secondCache = secondCache | (value & 0xFF);
        secondCacheIndex += 8;
        flushCache();
    }

    /**
     * Read 8-bit value from cache
     *
     * @return The 8-bit value
     * @throws Exception error
     */
    public byte readByte() throws Exception {
        checkReadIndex(8);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 8;
        return (byte) (tem & 0xFF);
    }

    /**
     * Write 9-bit value to cache
     *
     * @param value The 9-bit value
     * @throws Exception error
     */
    public void writeBit9(short value) throws Exception {
        checkCapacity(9);
        secondCache = secondCache | (value & 0x1FF);
        secondCacheIndex += 9;
        flushCache();
    }

    /**
     * Read 9-bit value from cache
     *
     * @return The 9-bit value
     * @throws Exception error
     */
    public short readBit9() throws Exception {
        checkReadIndex(9);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 9;
        return (short) (tem & 0x1FF);
    }

    /**
     * Write 16-bit value to cache
     *
     * @param value The 16-bit value
     * @throws Exception error
     */
    public void write(short value) throws Exception {
        checkCapacity(16);
        secondCache = secondCache | (value & 0xFFFF);
        secondCacheIndex += 16;
        flushCache();
    }

    /**
     * Read 16-bit value from cache
     *
     * @return The 16-bit value
     * @throws Exception error
     */
    public short readShort() throws Exception {
        checkReadIndex(16);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 16;
        return (short) (tem & 0xFFFF);
    }

    /**
     * Write 32-bit value to cache
     *
     * @param value The 32-bit value
     * @throws Exception error
     */
    public void write(int value) throws Exception {
        checkCapacity(32);
        secondCache = secondCache | value;
        secondCacheIndex += 32;
        flushCache();
    }

    /**
     * Read 32-bit value from cache
     *
     * @return The 32-bit value
     * @throws Exception error
     */
    public int readInt() throws Exception {
        checkReadIndex(32);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 32;
        return tem;
    }

    /**
     * Write byte array to cache
     *
     * @param value The byte array
     * @throws Exception error
     */
    public void write(byte[] value) throws Exception {
        checkCapacity(8 * value.length);
        int i = 0;
        while (i < value.length) {
            secondCache = secondCache | CoderHelper.get32BitWndBuffer(value, i * 8);
            if (value.length - i >= 4) {
                secondCacheIndex = 32;
            } else {
                secondCacheIndex = value.length * 8 - i * 8;
            }
            flushCache();
            i = i + 4;
        }
    }

    /**
     * Read buf.length buffer from cache fill to buf
     *
     * @param buf The value to be filled
     * @return The filled value
     * @throws Exception error
     */
    public byte[] readBytes(byte[] buf) throws Exception {
        checkReadIndex(8 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            buf[i] = readByte();
        }
        return buf;
    }

    /**
     * Get the 8-bit value from the index and not move the readBit index
     *
     * @param index The position to be read
     * @return The 8-bit value
     */
    public byte get(int index) {
        return cacheBuffer[index];
    }

    /**
     * Write the BitBuffer cache directtly,not change the writeIndex position
     *
     * @param index The index to write value
     * @param value The value to be writed
     */
    public void put(int index, byte value) {
        cacheBuffer[index] = value;
    }

    /**
     * Check if has capacity to fill bitWidth value
     *
     * @param bitWidth The value to be filled width in bit
     * @return The capacity result
     * @throws Exception error
     */
    private boolean checkCapacity(int bitWidth) throws Exception {
        if (writeBitIndexLimit - writeBitIndex < bitWidth)
            throw new Exception("Buffer Capacity is not enough");
        return true;
    }

    /**
     * Validate read index
     *
     * @param bitWidth The width in bit
     * @return The check result
     * @throws Exception error
     */
    private boolean checkReadIndex(int bitWidth) throws Exception {
        if (writeBitIndex - readBitIndex < bitWidth)
            throw new Exception("Read index is out of bundle");
        return true;
    }
}
