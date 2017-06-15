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

import com.github.io.protocol.annotation.ByteOrder;

import java.util.Calendar;
import java.util.Date;

public class ByteBufferUtil {

    private ByteBufferUtil() {

    }

    /**
     * Reserve hex string
     *
     * @param tempStr reserve hex string order
     * @return The reserved hex string
     */
    public static String reserveOrder(String tempStr) {
        return (new StringBuffer(tempStr)).reverse().toString();
    }

    /**
     * Reserve byte buffer order
     *
     * @param buffer The buffer order reserved
     * @return reserved buffer
     */
    public static byte[] reserveOrder(byte[] buffer) {
        if (buffer == null)
            return null;
        int size = buffer.length;
        int length = size / 2;
        byte t = 0;
        for (int i = 0; i < length; i++) {
            t = buffer[i];
            buffer[i] = buffer[size - i - 1];
            buffer[size - i - 1] = t;
        }
        return buffer;
    }

    /**
     * Reserve 16 bit unsigned number
     *
     * @param value The number to be reserved
     * @return The reserved number
     */
    public static short reserveOrder(short value) {
        return Short.reverseBytes(value);
    }

    /**
     * Reserve 24 bit unsigned number
     *
     * @param value The number to be reserved
     * @return The reserved number
     */
    public static int reserveOrder24bit(int value) {
        int ret = 0;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        ret = ret | ((value >>> 8) & 0xFF);
        ret = ret << 8;
        ret = ret | ((value >>> 16) & 0xFF);
        return ret;
    }

    /**
     * Reserve 32 bit unsigned number
     *
     * @param value The number to be reserved
     * @return The reserved number
     */
    public static int reserveOrder(int value) {
        int ret = 0;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        ret = ret | ((value >>> 8) & 0xFF);
        ret = ret << 8;
        ret = ret | ((value >>> 16) & 0xFF);
        ret = ret << 8;
        ret = ret | ((value >>> 24) & 0xFF);
        return ret;
    }

    /**
     * Reserve 64 bit unsigned number
     *
     * @param value The number to be reserved
     * @return The reserved number
     */
    public static long reserveOrder(long value) {
        long ret = 0;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        ret = ret << 8;
        value = value >>> 8;
        ret = ret | (value & 0xFF);
        return ret;
    }

    /**
     * Translate java.utils.Date to byte array
     *
     * @param d The Date to be translate
     * @return The byte array translated via Date
     */
    public static byte[] date2buf(Date d) {
        return date2buf(d.getTime());
    }


    /**
     * Translate GMT time to byte array.
     *
     * @param times The GMT time
     * @return The byte array translated. eg:byte[]{year-2000,month,day_of_month,hour_of_day,minute,seconds}
     */
    public static byte[] date2buf(long times) {
        byte[] packetBuffer = new byte[6];
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(times);
        packetBuffer[0] = (byte) (now.get(Calendar.YEAR) - 2000);
        packetBuffer[1] = (byte) (now.get(Calendar.MONTH) + 1);
        packetBuffer[2] = (byte) now.get(Calendar.DAY_OF_MONTH);
        packetBuffer[3] = (byte) now.get(Calendar.HOUR_OF_DAY);
        packetBuffer[4] = (byte) now.get(Calendar.MINUTE);
        packetBuffer[5] = (byte) now.get(Calendar.SECOND);
        return packetBuffer;
    }

    /**
     * Translate java.util.Date to bcd byte buffer
     *
     * @param d The date to be translte
     * @return The bcd byte buffer
     */
    public static byte[] date2bcdbuf(Date d) {
        return date2bcdbuf(d.getTime());
    }

    /**
     * Translate GMT time to bcd byte buffer
     *
     * @param datetime The GMT time
     * @return the bcd byte buffer, eg:byte[]{year-2000,month,day_of_month,hour_of_day,minute,seconds}
     */
    public static byte[] date2bcdbuf(long datetime) {
        byte[] packetBuffer = new byte[6];
        Calendar now = Calendar.getInstance();
        now.setTime(new Date(datetime));
        packetBuffer[0] = BCDUtil.value2bcd(now.get(Calendar.YEAR) - 2000, 2)[0];
        packetBuffer[1] = BCDUtil.value2bcd(now.get(Calendar.MONTH) + 1, 2)[0];
        packetBuffer[2] = BCDUtil.value2bcd(now.get(Calendar.DAY_OF_MONTH), 2)[0];
        packetBuffer[3] = BCDUtil.value2bcd(now.get(Calendar.HOUR_OF_DAY), 2)[0];
        packetBuffer[4] = BCDUtil.value2bcd(now.get(Calendar.MINUTE), 2)[0];
        packetBuffer[5] = BCDUtil.value2bcd(now.get(Calendar.SECOND), 2)[0];
        return packetBuffer;
    }

    /**
     * Translate buffer to GMT time
     *
     * @param buffer The GMT buffer
     * @return The GMT time
     * @throws Exception The time need 6 unsigned8bit at least, if the buffer avaible size less than 6, this will happen
     */
    public static long buf2Date(byte[] buffer) throws Exception {
        return buf2Date(buffer, 0);
    }

    /**
     * Translate buffer to GMT time
     *
     * @param buffer byte[]{year-2000,month,day_of_month,hour_of_day,minute,seconds}
     * @param offset The position of buffer where begin to translate from
     * @return The GMT time
     * @throws Exception The time need 6 unsigned8bit at least, if the buffer avaible size less than 6, this will happen
     */
    public static long buf2Date(byte[] buffer, int offset) throws Exception {
        if (buffer.length - offset < 6) {
            throw new Exception("buffer length is not enough");
        }
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, (buffer[offset + 0] & 0xFF) + 2000);
        now.set(Calendar.MONTH, (buffer[offset + 1] & 0xFF) - 1);
        now.set(Calendar.DAY_OF_MONTH, buffer[offset + 2] & 0xFF);
        now.set(Calendar.HOUR_OF_DAY, buffer[offset + 3] & 0xFF);
        now.set(Calendar.MINUTE, buffer[offset + 4] & 0xFF);
        now.set(Calendar.SECOND, buffer[offset + 5] & 0xFF);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }

    /**
     * Translate bcd buffer to GMT time
     *
     * @param buffer The bcd time buffer
     * @return The GMT time encode by bcd
     * @throws Exception The time need 6 unsigned8bit at least, if the buffer avaible size less than 6, this will happen
     */
    public static long bcdbuf2Date(byte[] buffer) throws Exception {
        return bcdbuf2Date(buffer, 0);
    }

    /**
     * Translate bcd buffer to GMT time
     *
     * @param buffer byte[]{year-2000,month,day_of_month,hour_of_day,minute,seconds}
     * @param offset The postion where buffer to translate from
     * @return The GMT time encode by bcd
     * @throws Exception The time need 6 unsigned8bit at least, if the buffer avaible size less than 6, this will happen
     */
    public static long bcdbuf2Date(byte[] buffer, int offset) throws Exception {
        if (buffer.length - offset < 6) {
            throw new Exception("buffer length is not enough");
        }
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, BCDUtil.bcd2value(buffer, offset + 0, 1) + 2000);
        now.set(Calendar.MONTH, BCDUtil.bcd2value(buffer, offset + 1, 1) - 1);
        now.set(Calendar.DAY_OF_MONTH, BCDUtil.bcd2value(buffer, offset + 2, 1));
        now.set(Calendar.HOUR_OF_DAY, BCDUtil.bcd2value(buffer, offset + 3, 1));
        now.set(Calendar.MINUTE, BCDUtil.bcd2value(buffer, offset + 4, 1));
        now.set(Calendar.SECOND, BCDUtil.bcd2value(buffer, offset + 5, 1));
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }

    /**
     * Parse byte array to 16 bit number in BigEndian
     *
     * @param buffer The buffer to be parsed
     * @return 16 bit unsigned number
     * @throws Exception While buffer available length is smaller than 2, this will be a mistake
     */
    public static short parseShort(byte[] buffer) throws Exception {
        return parseShort(buffer, 0, ByteOrder.BigEndian);
    }

    /**
     * Parse byte array to 16 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed
     * @param byteOrder The endian used by parse
     * @return 16 bit unsigned number
     * @throws Exception While buffer available length is smaller than 2, this will be a mistake
     */
    public static short parseShort(byte[] buffer, ByteOrder byteOrder) throws Exception {
        return parseShort(buffer, 0, byteOrder);
    }

    /**
     * Parse byte array to 16 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed from
     * @param offset    The position where buffer begin to parse
     * @param byteOrder The endian store in buffer
     * @return 16 bit unsigned number
     * @throws Exception While buffer available length is smaller than 2, this will be a mistake
     */
    public static short parseShort(byte[] buffer, int offset, ByteOrder byteOrder) throws Exception {
        if (buffer.length - offset < 2) {
            throw new Exception("buffer length is not enough");
        }
        short v = buffer[offset];
        v = (short) (v << 8);
        v = (short) (v | buffer[offset + 1]);
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        return v;
    }

    /**
     * Parse byte array to 32 bit number in BigEndian
     *
     * @param buffer The buffer to be parsed
     * @return 32 bit unsigned number
     * @throws Exception While buffer available length is smaller than 4, this will be a mistake
     */
    public static int parseInt(byte[] buffer) throws Exception {
        return parseInt(buffer, 0, ByteOrder.BigEndian);
    }

    /**
     * Parse byte array to 32 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed
     * @param byteOrder The endian used by parse
     * @return 32 bit unsigned number
     * @throws Exception While buffer available length is smaller than 4, this will be a mistake
     */
    public static int parseInt(byte[] buffer, ByteOrder byteOrder) throws Exception {
        return parseInt(buffer, 0, byteOrder);
    }

    /**
     * Parse byte array to 32 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed
     * @param byteOrder The endian used by parse
     * @param offset    The offset of buffer where to parse from
     * @return 32 bit unsigned number
     * @throws Exception While buffer available length is smaller than 4, this will be a mistake
     */
    public static int parseInt(byte[] buffer, int offset, ByteOrder byteOrder) throws Exception {
        if (buffer.length - offset < 4) {
            throw new Exception("buffer length is not enough");
        }
        int v = buffer[offset];
        v = v << 8;
        v = v | buffer[offset + 1];
        v = v << 8;
        v = v | buffer[offset + 2];
        v = v << 8;
        v = v | buffer[offset + 3];
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        return v;
    }

    /**
     * Parse byte array to 64 bit number in ByteOrder
     *
     * @param buffer The buffer to be parsed
     * @return 64 bit unsigned number
     * @throws Exception While buffer available length is smaller than 8, this will be a mistake
     */
    public static long parseLong(byte[] buffer) throws Exception {
        return parseLong(buffer, 0, ByteOrder.BigEndian);
    }

    /**
     * Parse byte array to 64 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed
     * @param byteOrder The endian used by parse
     * @return 64 bit unsigned number
     * @throws Exception While buffer available length is smaller than 8, this will be a mistake
     */
    public static long parseLong(byte[] buffer, ByteOrder byteOrder) throws Exception {
        return parseLong(buffer, 0, byteOrder);
    }

    /**
     * Parse byte array to 64 bit number in ByteOrder
     *
     * @param buffer    The buffer to be parsed
     * @param byteOrder The endian used by parse
     * @param offset    The offset of buffer beginning
     * @return 64 bit unsigned number
     * @throws Exception While buffer available length is smaller than 8, this will be a mistake
     */
    public static long parseLong(byte[] buffer, int offset, ByteOrder byteOrder) throws Exception {
        if (buffer.length - offset < 8) {
            throw new Exception("buffer length is not enough");
        }
        int v = buffer[offset];
        v = v << 8;
        v = v | buffer[offset + 1];
        v = v << 8;
        v = v | buffer[offset + 2];
        v = v << 8;
        v = v | buffer[offset + 3];
        v = v << 8;
        v = v | buffer[offset + 4];
        v = v << 8;
        v = v | buffer[offset + 5];
        v = v << 8;
        v = v | buffer[offset + 6];
        v = v << 8;
        v = v | buffer[offset + 7];
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        return v;
    }

    /**
     * Translate 16 bit unsigned number to byte buffer in BigEndian
     *
     * @param v The value to be translated
     * @return The byte buffer translated
     */
    public static byte[] toBytes(short v) {
        return toBytes(v, ByteOrder.BigEndian);
    }


    /**
     * Translate 16 bit unsigned number to byte buffer in BigEndian
     *
     * @param v         The value to be translated
     * @param byteOrder The endian used by translation
     * @return The byte buffer translated
     */
    public static byte[] toBytes(short v, ByteOrder byteOrder) {
        byte[] buf = new byte[2];
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        buf[0] = (byte) ((v >>> 8) & 0xFF);
        buf[1] = (byte) (v & 0xFF);
        return buf;
    }

    /**
     * Translate 16 bit unsigned number array to byte buffer in BigEndian
     *
     * @param values The value to be translated
     * @return The byte buffer translated
     */
    public static byte[] toBytes(short[] values) {
        return toBytes(values, ByteOrder.BigEndian);
    }

    /**
     * Translate 16 bit unsigned number array to byte buffer in BigEndian
     *
     * @param values    The value to be translated
     * @param byteOrder The endian used by translation
     * @return The byte buffer translated
     */
    public static byte[] toBytes(short[] values, ByteOrder byteOrder) {
        byte[] buf = new byte[values.length * 2];
        int length = values.length;
        for (int i = 0, j = 0; i < length; i++) {
            short v = values[i];
            if (byteOrder == ByteOrder.SmallEndian) {
                v = reserveOrder(v);
            }
            buf[j++] = (byte) ((v >>> 8) & 0xFF);
            buf[j++] = (byte) (v & 0xFF);
        }
        return buf;
    }

    /**
     * Translate 32 bit unsigned number to byte buffer in BigEndian
     *
     * @param v The value to be translated
     * @return The byte buffer translated
     */
    public static byte[] toBytes(int v) {
        return toBytes(v, ByteOrder.BigEndian);
    }

    /**
     * Translate 32 bit unsigned number to byte buffer in ByteOrder
     *
     * @param v         The value to be translated
     * @param byteOrder The endian used by translation
     * @return The byte buffer translated
     */
    public static byte[] toBytes(int v, ByteOrder byteOrder) {
        byte[] buf = new byte[4];
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        buf[0] = (byte) ((v >>> 24) & 0xFF);
        buf[1] = (byte) ((v >>> 16) & 0xFF);
        buf[2] = (byte) ((v >>> 8) & 0xFF);
        buf[3] = (byte) (v & 0xFF);
        return buf;
    }

    /**
     * Translate 32 bit unsigned number array to byte buffer in BigEndian
     *
     * @param values The value to be translated
     * @return The byte buffer translated
     */
    public static byte[] toBytes(int[] values) {
        return toBytes(values, ByteOrder.BigEndian);
    }

    /**
     * Translate 32 bit unsigned number array to byte buffer in BigEndian
     *
     * @param values    The value to be translated
     * @param byteOrder The endian used by translation
     * @return The byte buffer translated
     */
    public static byte[] toBytes(int[] values, ByteOrder byteOrder) {
        byte[] buf = new byte[values.length * 4];
        int length = values.length;
        for (int i = 0, j = 0; i < length; i++) {
            int v = values[i];
            if (byteOrder == ByteOrder.SmallEndian) {
                v = reserveOrder(v);
            }
            buf[j++] = (byte) ((v >>> 24) & 0xFF);
            buf[j++] = (byte) ((v >>> 16) & 0xFF);
            buf[j++] = (byte) ((v >>> 8) & 0xFF);
            buf[j++] = (byte) (v & 0xFF);
        }
        return buf;
    }

    /**
     * Translate 64 bit number to byte buffer
     *
     * @param v The number to be tranlated
     * @return The buffer tranlated
     */
    public static byte[] toBytes(long v) {
        return toBytes(v, ByteOrder.BigEndian);
    }

    /**
     * Translate 64 bit number to byte buffer
     *
     * @param v         The number to be tranlated
     * @param byteOrder The endian used by translation
     * @return The buffer tranlated
     */
    public static byte[] toBytes(long v, ByteOrder byteOrder) {
        byte[] buf = new byte[8];
        if (byteOrder == ByteOrder.SmallEndian) {
            v = reserveOrder(v);
        }
        buf[0] = (byte) ((v >>> 56) & 0xFF);
        buf[1] = (byte) ((v >>> 48) & 0xFF);
        buf[2] = (byte) ((v >>> 40) & 0xFF);
        buf[3] = (byte) ((v >>> 32) & 0xFF);
        buf[4] = (byte) ((v >>> 24) & 0xFF);
        buf[5] = (byte) ((v >>> 16) & 0xFF);
        buf[6] = (byte) ((v >>> 8) & 0xFF);
        buf[7] = (byte) ((v >>> 0) & 0xFF);
        return buf;
    }

    /**
     * Translate 64 bit number array to byte buffer
     *
     * @param values The number to be tranlated
     * @return The buffer tranlated
     */
    public static byte[] toBytes(long[] values) {
        return toBytes(values, ByteOrder.BigEndian);
    }

    /**
     * Translate 64 bit number array to byte buffer
     *
     * @param values    The number to be tranlated
     * @param byteOrder The endian used by translation
     * @return The buffer tranlated
     */
    public static byte[] toBytes(long[] values, ByteOrder byteOrder) {
        byte[] buf = new byte[values.length * 8];
        int length = values.length;
        for (int i = 0, j = 0; i < length; i++) {
            long v = values[i];
            if (byteOrder == ByteOrder.SmallEndian) {
                v = reserveOrder(v);
            }

            buf[j++] = (byte) ((v >>> 56) & 0xFF);
            buf[j++] = (byte) ((v >>> 48) & 0xFF);
            buf[j++] = (byte) ((v >>> 40) & 0xFF);
            buf[j++] = (byte) ((v >>> 32) & 0xFF);
            buf[j++] = (byte) ((v >>> 24) & 0xFF);
            buf[j++] = (byte) ((v >>> 16) & 0xFF);
            buf[j++] = (byte) ((v >>> 8) & 0xFF);
            buf[j++] = (byte) ((v >>> 0) & 0xFF);
        }
        return buf;
    }

}
