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

package com.github.io.protocol.coder.impl;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.utils.*;
import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.annotation.Sign;
import com.github.io.protocol.coder.AbstractNumberCoder;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.core.BitBuffer;
import com.github.io.protocol.core.CoderHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * The coder for Number convertion with <code>byte[]</code>. this coder support bit operation, byte order endian define.
 * user also can define <code>coder</code> or <code>decoder</code> method themselves.
 */
public class NumberCoder extends AbstractNumberCoder implements ICoder {

    /**
     * Decode the JavaBean field value from byte buffer.
     *
     * @param bitBuffer The byte buffer wraped utils.
     * @param beanMap The cglib proxy JavaBean.
     * @param field The field wart for decode.
     * @param annotation The decode metal information.
     * @throws Exception
     */
    @Override
    public void decode(final BitBuffer bitBuffer, BeanMap beanMap, Field field, final Annotation annotation) throws Exception {
        Number anno = (Number) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), anno.length());
        int bitWidth = anno.width();

        /**
         * User can define <code>decoder</code> by themselves nessary.
         * the method is called by cglib, so don't worry about
         * operating efficiency in most case.
         */
        if (anno.decoder().length() > 1) {
            decodeItself(bitWidth, length, bitBuffer, beanMap.getBean(), anno.decoder());
            return;
        }

        /**
         * If client not give the decoder, this decoder will operation
         * automaticly. if the <code>length</code> value greater than one,
         * mean the <code>field</code> is an type of array,else it is
         * a Object, such as byte/short/int/long or wraped object.
         */
        if (length == 1) {
            if (bitWidth >= 1 && bitWidth <= 7) {
                beanMap.put(field.getName(), decodeBits(anno, bitBuffer, field));
            } else if (bitWidth == 8) {
                beanMap.put(field.getName(), decodeByte(anno, bitBuffer, field));
            } else if (bitWidth == 16) {
                beanMap.put(field.getName(), decodeShort(anno, bitBuffer, field));
            } else if (bitWidth == 32) {
                beanMap.put(field.getName(), decodeInt(anno, bitBuffer, field));
            } else if (bitWidth == 64) {
                beanMap.put(field.getName(), decodeLong(anno, bitBuffer, field));
            } else {
                throw new Exception(field.getName() + ":width is not support");
            }
        } else if (length > 1) {
            Object[] values = new Object[length];
            if (bitWidth >= 1 && bitWidth <= 7) {
                for (int i = 0; i < length; i++) {
                    values[i] = decodeBits(anno, bitBuffer, field);
                }
            } else if (bitWidth == 8) {
                for (int i = 0; i < length; i++) {
                    values[i] = decodeByte(anno, bitBuffer, field);
                }
            } else if (bitWidth == 16) {
                for (int i = 0; i < length; i++) {
                    values[i] = decodeShort(anno, bitBuffer, field);
                }
            } else if (bitWidth == 32) {
                for (int i = 0; i < length; i++) {
                    values[i] = decodeInt(anno, bitBuffer, field);
                }
            } else if (bitWidth == 64) {
                for (int i = 0; i < length; i++) {
                    values[i] = decodeLong(anno, bitBuffer, field);
                }
            } else {
                throw new Exception(field.getName() + ":width is not support");
            }
            ArrayHelper.putArrayToBeanMap(beanMap, field, values);
        } else {
            throw new Exception(field.getName() + ":length value is err");
        }
    }

    /**
     * Encode the JavaBean field value to byte buffer.
     *
     * @param bitBuffer The byte buffer wraped utils.
     * @param beanMap The cglib proxy JavaBean.
     * @param field The field wart for decode.
     * @param annotation The decode metal information.
     **/
    @Override
    public void encode(BitBuffer bitBuffer, BeanMap beanMap, final Field field, final Annotation annotation) throws Exception {
        Number anno = (Number) annotation;
        int length = CoderHelper.caculateArrayLength(beanMap.getBean(), anno.length());
        int bitWidth = anno.width();

        /**
         * While user can define decoder in JavaBean by themselves.
         * The method user defined is called by cglib, so don't worry
         * about the operating efficiency in most case.
         */
        if (anno.encoder().length() > 1) {
            encodeItself(bitWidth, length, bitBuffer, beanMap.getBean(), anno.encoder());
            return;
        }

        /**
         * First of all,there have two cases judge by length.
         * one is java Object, another is Object array.
         * here give the base type of these:
         * bit1,bit2,bit3,bit4,bit5,bit6,bit7, byte, Integer,
         * short,long.
         */
        if (length == 1) {
            if (bitWidth >= 1 && bitWidth <= 7) {
                encodeBits(anno, bitBuffer, field, beanMap.get(field.getName()));
            } else if (bitWidth == 8) {
                encodeByte(anno, bitBuffer, field, beanMap.get(field.getName()));
            } else if (bitWidth == 16) {
                encodeShort(anno, bitBuffer, field, beanMap.get(field.getName()));
            } else if (bitWidth == 32) {
                encodeInt(anno, bitBuffer, field, beanMap.get(field.getName()));
            } else if (bitWidth == 64) {
                encodeLong(anno, bitBuffer, field, beanMap.get(field.getName()));
            } else {
                throw new Exception(field.getName() + ":width is not support");
            }
        } else if (length > 1) {

            Object[] values = new Object[length];
            ArrayHelper.fillNumberArray(beanMap.get(field.getName()), field.getType().getSimpleName(), values);

            if (values.length != length) {
                throw new Exception(field.getName()
                    + " length is not equal to cfg value " + length);
            }

            if (bitWidth >= 1 && bitWidth <= 7) {
                for (int i = 0; i < length; i++) {
                    encodeBits(anno, bitBuffer, field, values[i]);
                }
            } else if (bitWidth == 8) {
                for (int i = 0; i < length; i++) {
                    encodeByte(anno, bitBuffer, field, values[i]);
                }
            } else if (bitWidth == 16) {
                for (int i = 0; i < length; i++) {
                    encodeShort(anno, bitBuffer, field, values[i]);
                }
            } else if (bitWidth == 32) {
                for (int i = 0; i < length; i++) {
                    encodeInt(anno, bitBuffer, field, values[i]);
                }
            } else if (bitWidth == 64) {
                for (int i = 0; i < length; i++) {
                    encodeLong(anno, bitBuffer, field, values[i]);
                }
            } else {
                throw new Exception(field.getName() + ":width is not support");
            }
        } else {
            throw new Exception(field.getName() + ":length value is err");
        }

    }

    /**
     * The debug helping method. when debug the protocol, it is difficult in most case, this method work make it
     * easier.
     *
     * @param bitBuffer The byte buffer wraped utils.
     * @param beanMap The cglib proxy JavaBean.
     * @param field The field wart for decode.
     * @param annotation The decode metal information.
     * @return The pretty string like json.
     * @throws Exception error
     */
    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, final BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        bitBuffer.reset();
        encode(bitBuffer, beanMap, field, annotation);
        byte[] array = bitBuffer.toByteArray();
        bitBuffer.reset();
        return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(array), field.getName(), beanMap.get(field.getName()));
    }

    /**
     * Decode bit1~bit7 to JavaBean properties. While field type not match, transferred automatically. This problem
     * happened while Integer is used by JavaBean more convenient.
     *
     * @param number The annocation marked on JavaBean field.
     * @param bitBuffer The byte buffer utils.
     * @param field The field to be encoded.
     * @throws Exception if the source value isn't match to the annocation it will be throw an exception.
     */
    private Object decodeBits(Number number, BitBuffer bitBuffer, Field field) throws Exception {
        int bitWidth = number.width();
        String typeName = field.getType().getSimpleName();

        if (number.isBCDCode()) {
            throw new Exception("bit operation not support bcd code");
        }

        if (number.offset() > 0) {
            throw new Exception("bit operation not support value offset");
        }

        byte value = bitBuffer.readBit(bitWidth);
        if (typeName.equalsIgnoreCase("Byte") || typeName.equalsIgnoreCase("byte[]")) {
            return value;
        } else if (typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("int[]")
            || typeName.equalsIgnoreCase("Integer") || typeName.equalsIgnoreCase("Integer[]")) {
            return value & 0xFF;
        } else if (typeName.equalsIgnoreCase("short") || typeName.equalsIgnoreCase("short[]")) {
            return (short) (value & 0xFF);
        } else if (typeName.equalsIgnoreCase("long") || typeName.equalsIgnoreCase("long[]")) {
            return (long) (value & 0xFF);
        } else {
            throw new Exception(field.getName() + " type dismatch");
        }
    }

    /**
     * Encode bit1~bit7 to byte buffer. While field type not match, transferred automatically. This problem happened
     * while Integer is used by JavaBean more convenient.
     *
     * @param number The annocation marked on JavaBean properties.
     * @param bitBuffer The byte buffer utils.
     * @param field The field to be encoded.
     * @param value The value to be encoded.
     * @throws Exception If the source value isn't match to the annocation it will be throw an exception.
     */
    private void encodeBits(Number number, BitBuffer bitBuffer,
        Field field, Object value) throws Exception {
        int bitWidth = number.width();
        String typeName = field.getType().getSimpleName();

        if (number.isBCDCode()) {
            throw new Exception("bit operation not support bcd code");
        }

        if (number.offset() > 0) {
            throw new Exception("bit operation not support value offset");
        }
        int v = 0;
        if (typeName.equalsIgnoreCase("byte") || typeName.equalsIgnoreCase("byte[]")) {
            v = (byte) value;
        } else if (typeName.equalsIgnoreCase("short") || typeName.equalsIgnoreCase("short[]")) {
            v = (short) value;
        } else if(typeName.equalsIgnoreCase("int[]") || typeName.equalsIgnoreCase("Integer[]")
            || typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("Integer")) {
            v = (int) value;
        } else if(typeName.equalsIgnoreCase("long") || typeName.equalsIgnoreCase("long[]")) {
            v = (int)((long) value);
        }

        bitBuffer.writeBit(bitWidth, (byte) (v & 0xFF));
    }

    /**
     * Decode  byte from byte buffer. While field type not match, transferred automatically. This problem happened while
     * Integer is used by JavaBean more convenient.
     *
     * @param number The annocation marked on JavaBean field.
     * @param bitBuffer The byte buffer utils.
     * @param field The field to be encoded.
     * @throws Exception if the source value isn't match to the annocation it will be throw an exception.
     */
    private Object decodeByte(Number number, BitBuffer bitBuffer, Field field) throws Exception {
        String typeName = field.getType().getSimpleName();

        byte value = bitBuffer.readByte();

        if (number.isBCDCode()) {
            value = (byte) BCDUtil.bcd2value(new byte[] {value});
        }

        if (number.sign() == Sign.Signed) {
            value = (byte) (value - number.offset());
            return NumberCastUtil.cast(value, typeName);
        } else {
            short unsignedValue = (short) (value & 0xFF);
            unsignedValue = (short) (unsignedValue - number.offset());
            return NumberCastUtil.cast(unsignedValue, typeName);
        }
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @param value
     */
    private void encodeByte(Number number, BitBuffer bitBuffer, Field field, Object value) throws Exception {
        int v = 0;
        String typeName = field.getType().getSimpleName();
        if (typeName.equalsIgnoreCase("byte") || typeName.equalsIgnoreCase("byte[]")) {
            v = (byte) value;
        } else if (typeName.equalsIgnoreCase("short") || typeName.equalsIgnoreCase("short[]")) {
            v = (short) value;
        } else if(typeName.equalsIgnoreCase("int[]") || typeName.equalsIgnoreCase("Integer[]")
            || typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("Integer")) {
            v = (int) value;
        } else if(typeName.equalsIgnoreCase("long") || typeName.equalsIgnoreCase("long[]")) {
            v = (int)((long) value);
        }

        v = v + number.offset();

        if (number.isBCDCode()) {
            v = BCDUtil.value2bcd(v, 2)[0];
        }

        bitBuffer.write((byte) (v & 0xFF));
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @throws Exception
     */
    private Object decodeShort(Number number, BitBuffer bitBuffer, Field field) throws Exception {
        short value = bitBuffer.readShort();
        String typeName = field.getType().getSimpleName();

        if (number.order() == ByteOrder.BigEndian)
            value = ByteBufferUtil.reserveOrder(value);

        if (number.isBCDCode()) {
            value = (short) BCDUtil.bcd2value(ByteBufferUtil.toBytes(value, ByteOrder.BigEndian));
        }

        if (number.sign() == Sign.Signed) {
            value = (short) (value - number.offset());
            return NumberCastUtil.cast(value, typeName);
        } else {
            int unsignedValue = value & 0xFFFF;
            unsignedValue = unsignedValue - number.offset();
            return NumberCastUtil.cast(unsignedValue, typeName);
        }
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @param value
     */
    private void encodeShort(Number number, BitBuffer bitBuffer, Field field, Object value) throws Exception {
        int v = 0;
        String typeName = field.getType().getSimpleName();
        if (typeName.equalsIgnoreCase("short") || typeName.equalsIgnoreCase("short[]")) {
            v = (short) value;
        } else if(typeName.equalsIgnoreCase("int[]") || typeName.equalsIgnoreCase("Integer[]")
            || typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("Integer")) {
            v = (int) value;
        } else if(typeName.equalsIgnoreCase("long") || typeName.equalsIgnoreCase("long[]")) {
            v = (int)((long) value);
        }

        v = v + number.offset();

        if (number.isBCDCode()) {
            v = ByteBufferUtil.parseShort(BCDUtil.value2bcd(v, 4), ByteOrder.BigEndian);
        }

        if (number.order() == ByteOrder.BigEndian)
            v = (int) ByteBufferUtil.reserveOrder((short) (v & 0xFFFF));

        bitBuffer.write((short) (v & 0xFFFF));
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @throws Exception
     */
    private Object decodeInt(Number number, BitBuffer bitBuffer, Field field) throws Exception {
        int value = bitBuffer.readInt();
        String typeName = field.getType().getSimpleName();

        if (number.order() == ByteOrder.BigEndian) {
            value = ByteBufferUtil.reserveOrder(value);
        }

        if (number.isBCDCode()) {
            value = BCDUtil.bcd2value(ByteBufferUtil.toBytes(value, ByteOrder.BigEndian));
        }

        if (number.sign() == Sign.Signed) {
            value = value - number.offset();
            return NumberCastUtil.cast(value, typeName);
        } else {
            long unsignedValue = value & 0xFFFFFFFFL;
            unsignedValue = unsignedValue - number.offset();
            return NumberCastUtil.cast(unsignedValue, typeName);
        }
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @param value
     */
    private void encodeInt(Number number, BitBuffer bitBuffer, Field field, Object value) throws Exception {
        long v = 0;
        String typeName = field.getType().getSimpleName();
        if(typeName.equalsIgnoreCase("int[]") || typeName.equalsIgnoreCase("Integer[]")
            || typeName.equalsIgnoreCase("int") || typeName.equalsIgnoreCase("Integer")) {
            v = (int) value;
        } else if(typeName.equalsIgnoreCase("long") || typeName.equalsIgnoreCase("long[]")) {
            v = (int)((long) value);
        }

        v = v + number.offset();

        if (number.isBCDCode()) {
            v = ByteBufferUtil.parseInt(BCDUtil.value2bcd((int)v, 8), ByteOrder.BigEndian);
        }

        if (number.order() == ByteOrder.BigEndian) {
            v = (long) ByteBufferUtil.reserveOrder((int) (v & 0xFFFFFFFF));
        }

        bitBuffer.write((int) (v & 0xFFFFFFFF));
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @throws Exception
     */
    private Object decodeLong(Number number, BitBuffer bitBuffer, Field field) throws Exception {
        long value = bitBuffer.readInt() & 0xFFFFFFFFL | (bitBuffer.readInt() & 0xFFFFFFFFL << 32);

        if (number.isBCDCode()) {
            throw new Exception("long operation not support bcd code");
        }

        if (number.order() == ByteOrder.BigEndian)
            value = ByteBufferUtil.reserveOrder(value);

        value = value - number.offset();
        return value;
    }

    /**
     * @param number
     * @param bitBuffer
     * @param field
     * @param value
     */
    private void encodeLong(Number number, BitBuffer bitBuffer, Field field, Object value) throws Exception {
        if (number.isBCDCode()) {
            throw new Exception("long operation not support bcd code");
        }

        long v = (long) value;

        v = v + number.offset();

        if (number.order() == ByteOrder.BigEndian)
            v = ByteBufferUtil.reserveOrder(v);

        bitBuffer.write((int) (v & 0xFFFFFFFF));
        v = v >>> 32;
        bitBuffer.write((int) (v & 0xFFFFFFFF));
    }
}
