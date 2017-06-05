package com.github.io.protocol.core;


/**
 * @Project:net-top-framwork-protocol
 * @Package net.top.framwork.protocolpool.core
 * @Description:
 * @author: xsy
 * @date： 2016/6/22
 * @version： V1.0
 */
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
    private int secondCache= 0;
    /**
     * 第二缓冲区索引，有效范围 0 ~ 31
     */
    private int secondCacheIndex = 0;

    public BitBuffer() {
        cacheBuffer = new byte[1024];
        writeBitIndex = 0;
        writeBitIndexLimit = 1024*8;
        readBitIndex = 0;
    }

    /**
     * @param bufferSize : 字节为单位
     */
    public BitBuffer(int bufferSize) {
        cacheBuffer = new byte[bufferSize];
        writeBitIndex = 0;
        writeBitIndexLimit = bufferSize*8;
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
     * 填充buffer，主要用于读操作
     * @param buf
     * @throws Exception
     */
    public void wrap(byte[] buf) throws Exception {
        wrap(buf, 0);
    }

    /**
     *
     * @param buf
     * @param index
     * @throws Exception
     */
    public void wrap(byte[] buf, int index) throws Exception {
        wrap(buf, index, buf.length - index);
    }

    /**
     *
     * @param buf
     * @param index
     * @param size
     * @throws Exception
     */
    public void wrap(byte[] buf, int index, int size) throws Exception {
        if(size > buf.length - index)
            throw new Exception("Size is big than buf length");
        if(size > cacheBuffer.length) {
            throw new Exception("BitBuffer length is not enouth");
        }

        System.arraycopy(buf, index, cacheBuffer, 0, size);
        readBitIndex = 0;
        writeBitIndex = size * 8;
    }

    /**
     * 获取当前的字节索引，如果未字节对齐，抛出异常
     * @return
     * @throws Exception
     */
    public int getCurrentReadIndex() throws Exception {
        if(readBitIndex % 8 != 0)
            throw new Exception("Not byte align");
        return readBitIndex / 8;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public int getCurrentWriteIndex() throws Exception {
        if(writeBitIndex % 8 != 0) {
            throw new Exception("WriteBitIndex Not byte align");
        }
        return writeBitIndex / 8;
    }

    /**
     * 按字节进行规整
     * @return
     */
    public byte[] toByteArray() {
        int size = writeBitIndex / 8;
        if(writeBitIndex % 8 != 0) {
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
        if(mod == 0) {
            op4Byte = secondCache;
            int byteIndex = (writeBitIndex - mod) / 8;
            if(byteIndex < cacheBuffer.length)
                cacheBuffer[byteIndex] = (byte) (op4Byte & 0xFF);
            if(byteIndex + 1 < cacheBuffer.length)
                cacheBuffer[byteIndex+1] = (byte) ((op4Byte >>> 8) & 0xFF);
            if(byteIndex + 2 < cacheBuffer.length)
                cacheBuffer[byteIndex+2] = (byte) ((op4Byte >>> 16) & 0xFF);
            if(byteIndex + 3 < cacheBuffer.length)
                cacheBuffer[byteIndex+3] = (byte) ((op4Byte >>> 24) & 0xFF);
        } else {
            // 保留op4Byte的低mod位,其余bit清零
            op4Byte = op4Byte << (32 - mod);
            op4Byte = op4Byte >>> (32 - mod);

            op4Byte = op4Byte | (secondCache << mod);
            // 将op4Byte 存回buffer中
            int byteIndex = (writeBitIndex - mod) / 8;
            if(byteIndex < cacheBuffer.length)
                cacheBuffer[byteIndex] = (byte) (op4Byte & 0xFF);
            if(byteIndex + 1 < cacheBuffer.length)
                cacheBuffer[byteIndex+1] = (byte) ((op4Byte >>> 8) & 0xFF);
            if(byteIndex + 2 < cacheBuffer.length)
                cacheBuffer[byteIndex+2] = (byte) ((op4Byte >>> 16) & 0xFF);
            if(byteIndex + 3 < cacheBuffer.length)
                cacheBuffer[byteIndex+3] = (byte) ((op4Byte >>> 24) & 0xFF);
            // 将secCache 的高mod位存入 byteIndex + 4 字节中
            if(byteIndex + 4 < cacheBuffer.length) {
                cacheBuffer[byteIndex + 4] = (byte) (secondCache >>> (32 - mod) & 0xFF);
            }
        }

        // 处理索引
        writeBitIndex += secondCacheIndex;
        secondCacheIndex = 0;
        secondCache = 0;
    }
    /**
     * 写入bit的低1位
     * @param bit
     */
    public void writeBit1(byte bit) throws Exception {
        checkCapacity(1);
        secondCache = secondCache | (bit & 0x01);
        secondCacheIndex += 1;
        flushCache();
    }

    /**
     *
     * @param bits 1 - 7
     * @return
     */
    public byte readBit(int bits) throws Exception {
        if(bits < 1 || bits > 7)
            throw new Exception("readBit only support 1-7,current is:" + bits);
        checkReadIndex(bits);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += bits;
        tem = tem << (24 + 8 - bits);
        tem = tem >>> (24 + 8 - bits);
        return (byte) (tem & 0xFF);
    }

    /**
     * 将value的低bits位写入缓存
     * @param bits
     * @param value
     */
    public void writeBit(int bits, byte value) throws Exception {
        if(bits < 1 || bits > 7)
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
     *
     * @return
     * @throws Exception
     */
    public byte readBit1() throws Exception {
        checkReadIndex(1);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 1;
        return (byte) (tem & 0x01);
    }

    /**
     * 写入bits的低2位
     * @param bits
     */
    public void writeBit2(byte bits) throws Exception {
        checkCapacity(2);
        secondCache = secondCache | (bits & 0x03);
        secondCacheIndex += 2;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit2() throws Exception {
        checkReadIndex(2);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 2;
        return (byte) (tem & 0x03);
    }

    /**
     *
     * @param bits
     * @throws Exception
     */
    public void writeBit3(byte bits) throws Exception {
        checkCapacity(3);
        secondCache = secondCache | (bits & 0x07);
        secondCacheIndex += 3;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit3() throws Exception {
        checkReadIndex(3);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 3;
        return (byte) (tem & 0x07);
    }

    /**
     *
     * @param bits
     * @throws Exception
     */
    public void writeBit4(byte bits) throws Exception {
        checkCapacity(4);
        secondCache = secondCache | (bits & 0x0F);
        secondCacheIndex += 4;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit4() throws Exception {
        checkReadIndex(4);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 4;
        return (byte) (tem & 0x0F);
    }

    /**
     *
     * @param bits
     * @throws Exception
     */
    public void writeBit5(byte bits) throws Exception {
        checkCapacity(5);
        secondCache = secondCache | (bits & 0x1F);
        secondCacheIndex += 5;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit5() throws Exception {
        checkReadIndex(5);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 5;
        return (byte) (tem & 0x1F);
    }

    /**
     *
     * @param bits
     * @throws Exception
     */
    public void writeBit6(byte bits) throws Exception {
        checkCapacity(6);
        secondCache = secondCache | (bits & 0x3F);
        secondCacheIndex += 6;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit6() throws Exception {
        checkReadIndex(6);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 6;
        return (byte) (tem & 0x3F);
    }

    /**
     *
     * @param bits
     * @throws Exception
     */
    public void writeBit7(byte bits) throws Exception {
        checkCapacity(7);
        secondCache = secondCache | (bits & 0x7F);
        secondCacheIndex += 7;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readBit7() throws Exception {
        checkReadIndex(7);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 7;
        return (byte) (tem & 0x7F);
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void write(byte value) throws Exception {
        checkCapacity(8);
        secondCache = secondCache | (value & 0xFF);
        secondCacheIndex += 8;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public byte readByte() throws Exception {
        checkReadIndex(8);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 8;
        return (byte) (tem & 0xFF);
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void writeBit9(short value) throws Exception {
        checkCapacity(9);
        secondCache = secondCache | (value & 0x1FF);
        secondCacheIndex += 9;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public short readBit9() throws Exception {
        checkReadIndex(9);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 9;
        return (short) (tem & 0x1FF);
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void write(short value) throws Exception {
        checkCapacity(16);
        secondCache = secondCache | (value & 0xFFFF);
        secondCacheIndex += 16;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public short readShort() throws Exception {
        checkReadIndex(16);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 16;
        return (short) (tem & 0xFFFF);
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void write(int value) throws Exception {
        checkCapacity(32);
        secondCache = secondCache | value;
        secondCacheIndex += 32;
        flushCache();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public int readInt() throws Exception {
        checkReadIndex(32);
        int tem = CoderHelper.get32BitWndBuffer(cacheBuffer, readBitIndex);
        readBitIndex += 32;
        return tem;
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void write(byte[] value) throws Exception {
        checkCapacity(8*value.length);
        int i = 0;
        while(i < value.length) {
            secondCache = secondCache | CoderHelper.get32BitWndBuffer(value, i*8);
            if(value.length - i >= 4) {
                secondCacheIndex = 32;
            } else {
                secondCacheIndex = value.length*8 - i*8;
            }
            flushCache();
            i = i + 4;
        }
    }

    /**
     * 读取字节串
     * @param buf
     * @return
     */
    public byte[] readBytes(byte[] buf) throws Exception {
        checkReadIndex(8*buf.length);
        for(int i = 0; i < buf.length; i++) {
            buf[i] = readByte();
        }
        return buf;
    }

    /**
     *
     * @param index
     * @return
     */
    public byte get(int index) {
        return cacheBuffer[index];
    }

    /**
     *
     * @param index
     * @param value
     * @return
     */
    public void put(int index, byte value) {
        cacheBuffer[index] = value;
    }

    /**
     * 检测是否可填充足bitWidth 个位
     * @param bitWidth
     * @return
     * @throws Exception
     */
    private boolean checkCapacity(int bitWidth) throws Exception {
        if(writeBitIndexLimit - writeBitIndex < bitWidth)
            throw new Exception("Buffer Capacity is not enough");
        return true;
    }

    private boolean checkReadIndex(int bitWidth) throws Exception {
        if(writeBitIndex - readBitIndex < bitWidth)
            throw new Exception("Read index is out of bundle");
        return true;
    }
}
