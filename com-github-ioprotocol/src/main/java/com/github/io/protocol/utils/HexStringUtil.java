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

import java.io.UnsupportedEncodingException;

public class HexStringUtil {

    private static final char[] DIGITS_UPPER = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private HexStringUtil() {

    }

    /**
     * @param b
     * @return
     */
    public static String toHexString(byte b) {
        char[] chars = new char[2];
        chars[0] = DIGITS_UPPER[(b & 0xF0) >>> 4];
        chars[1] = DIGITS_UPPER[b & 0x0F];
        return new String(chars);
    }

    /**
     * @param buffer
     * @return
     */
    public static String toHexString(byte[] buffer) {
        char[] chars = new char[buffer.length << 1];
        for (int i = 0, j = 0; i < buffer.length; i++) {
            chars[j++] = DIGITS_UPPER[(buffer[i] & 0xF0) >>> 4];
            chars[j++] = DIGITS_UPPER[buffer[i] & 0x0F];
        }
        return new String(chars);
    }

    /**
     * @param v
     * @return
     */
    public static String toHexString(short v) {
        char[] chars = new char[4];
        chars[0] = DIGITS_UPPER[(v >>> 12) & 0x0F];
        chars[1] = DIGITS_UPPER[(v >>> 8) & 0x0F];
        chars[2] = DIGITS_UPPER[(v >>> 4) & 0x0F];
        chars[3] = DIGITS_UPPER[v & 0x0F];
        return new String(chars);
    }

    /**
     * @param v
     * @return
     */
    public static String toHexString(short[] v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            sb.append(toHexString(v[i]));
        }
        return sb.toString();
    }

    public static String toHexString3bytes(int v) {
        char[] chars = new char[6];
        chars[0] = DIGITS_UPPER[(v >>> 20) & 0x0F];
        chars[1] = DIGITS_UPPER[(v >>> 16) & 0x0F];
        chars[2] = DIGITS_UPPER[(v >>> 12) & 0x0F];
        chars[3] = DIGITS_UPPER[(v >>> 8) & 0x0F];
        chars[4] = DIGITS_UPPER[(v >>> 4) & 0x0F];
        chars[5] = DIGITS_UPPER[v & 0x0F];
        return new String(chars);
    }

    public static String toHexString(int v) {
        char[] chars = new char[8];
        chars[0] = DIGITS_UPPER[(v >>> 28) & 0x0F];
        chars[1] = DIGITS_UPPER[(v >>> 24) & 0x0F];
        chars[2] = DIGITS_UPPER[(v >>> 20) & 0x0F];
        chars[3] = DIGITS_UPPER[(v >>> 16) & 0x0F];
        chars[4] = DIGITS_UPPER[(v >>> 12) & 0x0F];
        chars[5] = DIGITS_UPPER[(v >>> 8) & 0x0F];
        chars[6] = DIGITS_UPPER[(v >>> 4) & 0x0F];
        chars[7] = DIGITS_UPPER[v & 0x0F];
        return new String(chars);
    }

    /**
     * @param v
     * @return
     */
    public static String toHexString(int[] v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            sb.append(toHexString(v[i]));
        }
        return sb.toString();
    }

    public static String toHexString(long v) {
        char[] chars = new char[16];
        chars[0] = DIGITS_UPPER[(int) ((v >>> 60) & 0x0F)];
        chars[1] = DIGITS_UPPER[(int) ((v >>> 56) & 0x0F)];
        chars[2] = DIGITS_UPPER[(int) ((v >>> 52) & 0x0F)];
        chars[3] = DIGITS_UPPER[(int) ((v >>> 48) & 0x0F)];
        chars[4] = DIGITS_UPPER[(int) ((v >>> 44) & 0x0F)];
        chars[5] = DIGITS_UPPER[(int) ((v >>> 40) & 0x0F)];
        chars[6] = DIGITS_UPPER[(int) ((v >>> 36) & 0x0F)];
        chars[7] = DIGITS_UPPER[(int) ((v >>> 32) & 0x0F)];
        chars[8] = DIGITS_UPPER[(int) ((v >>> 28) & 0x0F)];
        chars[9] = DIGITS_UPPER[(int) ((v >>> 24) & 0x0F)];
        chars[10] = DIGITS_UPPER[(int) ((v >>> 20) & 0x0F)];
        chars[11] = DIGITS_UPPER[(int) ((v >>> 16) & 0x0F)];
        chars[12] = DIGITS_UPPER[(int) ((v >>> 12) & 0x0F)];
        chars[13] = DIGITS_UPPER[(int) ((v >>> 8) & 0x0F)];
        chars[14] = DIGITS_UPPER[(int) ((v >>> 4) & 0x0F)];
        chars[15] = DIGITS_UPPER[(int) (v & 0x0F)];
        return new String(chars);
    }

    /**
     * @param v
     * @return
     */
    public static String toHexString(long[] v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            sb.append(toHexString(v[i]));
        }
        return sb.toString();
    }

    /**
     * @param ascii
     * @return
     */
    public static String toHexString(String ascii) {
        return toHexString(ascii.getBytes());
    }

    /**
     * @param ascii
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toHexString(String ascii, String charsetName) throws UnsupportedEncodingException {
        return toHexString(ascii.getBytes(charsetName));
    }

    /**
     * @param hexString
     * @return
     */
    public static String parseAsciiString(String hexString) {
        return new String(parseBytes(hexString));
    }

    /**
     * @param hexString
     * @return
     */
    public static byte[] parseBytes(String hexString) {
        byte[] buf = new byte[hexString.length() >>> 1];

        for (int i = 0, j = 0; i < buf.length; i++) {
            buf[i] = 0;
            buf[i] = (byte) (Character.digit(hexString.charAt(j++), 16) << 4);
            buf[i] = (byte) (buf[i] | Character.digit(hexString.charAt(j++), 16));
        }
        return buf;
    }

    public static byte parseByte(String hexString) {
        return Byte.parseByte(hexString, 16);
    }

    public static short parseShort(String hexString) {
        return Short.parseShort(hexString, 16);
    }

    public static int parseInt(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    public static long parseLong(String hexString) {
        return Long.parseLong(hexString, 16);
    }
}
