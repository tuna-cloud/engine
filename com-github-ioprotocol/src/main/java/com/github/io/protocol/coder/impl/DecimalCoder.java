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

import java.math.BigDecimal;

import com.github.io.protocol.utils.ArrayHelper;
import net.sf.cglib.beans.BeanMap;
import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Decimal;
import com.github.io.protocol.annotation.Sign;
import com.github.io.protocol.coder.AbstractNumberCoder;
import com.github.io.protocol.coder.ICoder;
import com.github.io.protocol.core.BitBuffer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import com.github.io.protocol.core.CoderHelper;
import com.github.io.protocol.utils.BCDUtil;
import com.github.io.protocol.utils.ByteBufferUtil;
import com.github.io.protocol.utils.HexStringUtil;

public class DecimalCoder extends AbstractNumberCoder implements ICoder {

    @Override
    public void decode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        Decimal anno = (Decimal) annotation;
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

        if(length == 1) {
            double value = 0;
            if(bitWidth == 8) {
                value = decodeByte(anno, bitBuffer);
            } else if(bitWidth == 16) {
                value = decodeShort(anno, bitBuffer);
            } else if(bitWidth == 32) {
                value = decodeInt(anno, bitBuffer);
            } else if(bitWidth == 64) {
                value = decodeLong(anno, bitBuffer);
            } else {
                throw new Exception("un support bitwidth value:" + bitWidth);
            }

            String typeName = field.getType().getSimpleName();
            if(typeName.equalsIgnoreCase("double")) {
                beanMap.put(field.getName(), value);
            } else if(typeName.equalsIgnoreCase("float")) {
                beanMap.put(field.getName(), (float)value);
            } else {
                throw new Exception("The field " + field.getName() + " annocation is config err");
            }
        } else if(length > 1){
            double[] values = new double[length];
            for(int i = 0; i < length; i++) {
                if(bitWidth == 8) {
                    values[i] = decodeByte(anno, bitBuffer);
                } else if(bitWidth == 16) {
                    values[i] = decodeShort(anno, bitBuffer);
                } else if(bitWidth == 32) {
                    values[i] = decodeInt(anno, bitBuffer);
                } else if(bitWidth == 64) {
                    values[i] = decodeLong(anno, bitBuffer);
                } else {
                    throw new Exception("un support bitwidth value:" + bitWidth);
                }
            }
            ArrayHelper.putFloatArrayToBeanMap(beanMap, field, values);
        } else {
            throw new Exception(field.getName() + ":length value is err");
        }
    }

    @Override
    public void encode(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        Decimal anno = (Decimal) annotation;
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

        String typeName = field.getType().getSimpleName();

        if(length == 1) {
            double value = 0;
            if(typeName.equalsIgnoreCase("float")) {
                value = (float)beanMap.get(field.getName());
            } else if(typeName.equalsIgnoreCase("double")) {
                value = (double)beanMap.get(field.getName());
            } else {
                throw new Exception(field.getName() + " has a wrong type declaration");
            }
            value = value / anno.scale();
            value = value + anno.offset();
            if(bitWidth == 8) {
                bitBuffer.write((byte)value);
            } else if(bitWidth == 16 ) {
                short v = (short) value;
                if (anno.order() == ByteOrder.BigEndian)
                    v = ByteBufferUtil.reserveOrder(v);
                bitBuffer.write(v);
            } else if(bitWidth == 32) {
                int v = (int) value;
                if (anno.order() == ByteOrder.BigEndian)
                    v = ByteBufferUtil.reserveOrder(v);
                bitBuffer.write(v);
            } else if(bitWidth == 64) {
                long v = (long) value;
                if (anno.order() == ByteOrder.BigEndian)
                    v = ByteBufferUtil.reserveOrder(v);
                bitBuffer.write((int) (v & 0xFFFFFFFF));
                v = v >>> 32;
                bitBuffer.write((int) (v & 0xFFFFFFFF));
            } else {
                throw new Exception(field.getName() + " bitWidth is not support");
            }
        } else if(length > 1) {
            double[] values = null;
            if(typeName.equals("double[]")) {
                values = (double[])beanMap.get(field.getName());
            } else if(typeName.equals("Double[]")) {
                values = new double[length];
                Double[] vs = (Double[])beanMap.get(field.getName());
                for(int i = 0; i < length; i++) {
                    values[i] = vs[i];
                }
            } else if(typeName.equals("float[]")) {
                values = new double[length];
                float[] vs = (float[])beanMap.get(field.getName());
                for(int i = 0; i < length; i++) {
                    values[i] = vs[i];
                }
            } else if(typeName.equals("Float[]")) {
                values = new double[length];
                Float[] vs = (Float[])beanMap.get(field.getName());
                for(int i = 0; i < length; i++) {
                    values[i] = vs[i];
                }
            } else {
                throw new Exception(field.getName() + " unsupport field type");
            }

            for(int i = 0; i < length; i++) {
                values[i] = values[i] / anno.scale();
                values[i] = values[i] + anno.offset();
            }

            if(bitWidth == 8) {
                for(int i = 0; i < length; i++) {
                    byte v = (byte) values[i];
                    bitBuffer.write(v);
                }
            } else if(bitWidth == 16) {
                for(int i = 0; i < length; i++) {
                    short v = (short) values[i];
                    if (anno.order() == ByteOrder.BigEndian)
                        v = ByteBufferUtil.reserveOrder(v);
                    bitBuffer.write(v);
                }
            } else if(bitWidth == 32) {
                for(int i = 0; i < length; i++) {
                    int v = (int) values[i];
                    if (anno.order() == ByteOrder.BigEndian)
                        v = ByteBufferUtil.reserveOrder(v);
                    bitBuffer.write(v);
                }
            } else if(bitWidth == 64) {
                for(int i = 0; i < length; i++) {
                    long v = (long) values[i];
                    if (anno.order() == ByteOrder.BigEndian)
                        v = ByteBufferUtil.reserveOrder(v);
                    bitBuffer.write((int) (v & 0xFFFFFFFF));
                    v = v >>> 32;
                    bitBuffer.write((int) (v & 0xFFFFFFFF));
                }
            } else {
                throw new Exception(field.getName() + " unsupprot bitWidth");
            }
        } else {
            throw new Exception("length is error");
        }
    }

    @Override
    public String toPrettyHexString(BitBuffer bitBuffer, BeanMap beanMap, Field field, Annotation annotation) throws Exception {
        bitBuffer.reset();
        encode(bitBuffer, beanMap, field, annotation);
        byte[] array = bitBuffer.toByteArray();
        bitBuffer.reset();
        return String.format(FIELD_TEMPLATE, HexStringUtil.toHexString(array), field.getName(), beanMap.get(field.getName()));
    }

    /**
     *
     * @param ann
     * @param bitBuffer
     * @return
     * @throws Exception
     */
    private double decodeByte(Decimal ann, BitBuffer bitBuffer) throws Exception {
        byte value = bitBuffer.readByte();

        if (ann.isBCDCode()) {
            value = (byte) BCDUtil.bcd2value(new byte[] {value});
        }

        double retValue = 0;
        if (ann.sign() == Sign.Signed) {
            retValue = value;
        } else {
            retValue = value & 0xFF;
        }
        retValue = retValue * ann.scale();
        retValue = retValue - ann.offset();

        BigDecimal bigDecimal = new BigDecimal(retValue);
        retValue = bigDecimal.setScale(ann.precision(), BigDecimal.ROUND_HALF_UP).doubleValue();
        return retValue;
    }

    /**
     *
     * @param ann
     * @param bitBuffer
     * @return
     * @throws Exception
     */
    private double decodeShort(Decimal ann, BitBuffer bitBuffer) throws Exception {
        short value = bitBuffer.readShort();

        if (ann.order() == ByteOrder.BigEndian)
            value = ByteBufferUtil.reserveOrder(value);

        if (ann.isBCDCode()) {
            value = (short) BCDUtil.bcd2value(ByteBufferUtil.toBytes(value, ByteOrder.BigEndian));
        }

        double retValue = 0;
        if (ann.sign() == Sign.Signed) {
            retValue = value;
        } else {
            retValue = value & 0xFFFF;
        }
        retValue = retValue * ann.scale();
        retValue = retValue - ann.offset();

        BigDecimal bigDecimal = new BigDecimal(retValue);
        retValue = bigDecimal.setScale(ann.precision(), BigDecimal.ROUND_HALF_UP).doubleValue();
        return retValue;
    }

    /**
     *
     * @param ann
     * @param bitBuffer
     * @return
     * @throws Exception
     */
    private double decodeInt(Decimal ann, BitBuffer bitBuffer) throws Exception {
        int value = bitBuffer.readInt();

        if (ann.order() == ByteOrder.BigEndian)
            value = ByteBufferUtil.reserveOrder(value);

        if (ann.isBCDCode()) {
            value = (short) BCDUtil.bcd2value(ByteBufferUtil.toBytes(value, ByteOrder.BigEndian));
        }

        double retValue = 0;
        if (ann.sign() == Sign.Signed) {
            retValue = value;
        } else {
            retValue = value & 0xFFFFFFFFL;
        }
        retValue = retValue * ann.scale();
        retValue = retValue - ann.offset();

        BigDecimal bigDecimal = new BigDecimal(retValue);
        retValue = bigDecimal.setScale(ann.precision(), BigDecimal.ROUND_HALF_UP).doubleValue();
        return retValue;
    }

    /**
     *
     * @param ann
     * @param bitBuffer
     * @return
     * @throws Exception
     */
    private double decodeLong(Decimal ann, BitBuffer bitBuffer) throws Exception {
        long value = bitBuffer.readInt() & 0xFFFFFFFFL | (bitBuffer.readInt() & 0xFFFFFFFFL << 32);

        if (ann.order() == ByteOrder.BigEndian)
            value = ByteBufferUtil.reserveOrder(value);

        if (ann.isBCDCode()) {
            throw new Exception("long operation not support bcd code");
        }

        if (ann.sign() == Sign.Signed) {
            throw new Exception("long operation not support bcd code");
        }

        double retValue = value;
        retValue = retValue * ann.scale();
        retValue = retValue - ann.offset();

        BigDecimal bigDecimal = new BigDecimal(retValue);
        retValue = bigDecimal.setScale(ann.precision(), BigDecimal.ROUND_HALF_UP).doubleValue();
        return retValue;
    }
}
