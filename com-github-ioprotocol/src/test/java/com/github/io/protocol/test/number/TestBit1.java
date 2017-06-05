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

package com.github.io.protocol.test.number;

import java.util.Arrays;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.test.TestEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestBit1 {

    /**
     * The base test for generally
     */
    public static class Case1 {
        @Number(width = 1)
        private int v1;
        @Number(width = 2)
        private int v2;
        @Number(width = 3)
        private int v3;
        @Number(width = 4)
        private int v4;
        @Number(width = 5)
        private int v5;
        @Number(width = 6)
        private int v6;
        @Number(width = 7)
        private int v7;
        @Number(width = 4)
        private int v8;

        public int getV1() {
            return v1;
        }

        public void setV1(int v1) {
            this.v1 = v1;
        }

        public int getV2() {
            return v2;
        }

        public void setV2(int v2) {
            this.v2 = v2;
        }

        public int getV3() {
            return v3;
        }

        public void setV3(int v3) {
            this.v3 = v3;
        }

        public int getV4() {
            return v4;
        }

        public void setV4(int v4) {
            this.v4 = v4;
        }

        public int getV5() {
            return v5;
        }

        public void setV5(int v5) {
            this.v5 = v5;
        }

        public int getV6() {
            return v6;
        }

        public void setV6(int v6) {
            this.v6 = v6;
        }

        public int getV7() {
            return v7;
        }

        public void setV7(int v7) {
            this.v7 = v7;
        }

        public int getV8() {
            return v8;
        }

        public void setV8(int v8) {
            this.v8 = v8;
        }
    }

    public static class Case2 {
        @Number(width = 8)
        private Integer length;
        @Number(width = 3, length = "getLength" )
        private short[] values;

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public short[] getValues() {
            return values;
        }

        public void setValues(short[] values) {
            length = values.length;
            this.values = values;
        }
    }

    @Test
    public void testCase1() throws Exception {
        Case1 case1 = new Case1();
        case1.setV1(255);
        case1.setV2(255);
        case1.setV3(255);
        case1.setV4(255);
        case1.setV5(255);
        case1.setV6(255);
        case1.setV7(255);
        case1.setV8(255);

        byte[] buf = TestEngine.encode(case1);
        Assert.assertTrue(buf[0] == (byte) 255);
        Assert.assertTrue(buf[1] == (byte) 255);
        Assert.assertTrue(buf[2] == (byte) 255);
        Assert.assertTrue(buf[3] == (byte) 255);

        Case1 c = TestEngine.decode(buf, Case1.class);
        Assert.assertTrue(c.getV1() == 1);
        Assert.assertTrue(c.getV2() == 3);
        Assert.assertTrue(c.getV3() == 7);
        Assert.assertTrue(c.getV4() == 15);
        Assert.assertTrue(c.getV5() == 31);
        Assert.assertTrue(c.getV6() == 63);
        Assert.assertTrue(c.getV7() == 127);
        Assert.assertTrue(c.getV8() == 15);

        System.out.println(TestEngine.toPreetyString(c));
    }

    @Test
    public void test2() throws Exception {
        Case2 case2 = new Case2();
        case2.setValues(new short[] {1, 2, 3, 1, 2, 3, 1, 2});
        byte[] buf = TestEngine.encode(case2);
        System.out.println(HexStringUtil.toHexString(buf));

        Case2 c = TestEngine.decode(buf, Case2.class);
        Assert.assertTrue(Arrays.equals(c.getValues(), case2.getValues()));
        Assert.assertTrue(c.getLength() == 8);
    }

    @Test
    public void test3() throws Exception {
        Case3 case3 = new Case3();
        case3.setValue1(5);
        case3.setValue2(9);
        case3.setArray(new int[] {1, 2, 1, 2});
        byte[] buf = TestEngine.encode(case3);
        System.out.println(HexStringUtil.toHexString(buf));
        System.out.println(TestEngine.toPreetyString(case3));

        Case3 c = TestEngine.decode(buf, Case3.class);
        System.out.println(c.toString());
        Assert.assertTrue(c.getValue1() == 5);
        Assert.assertTrue(c.getValue2() == 9);
        Assert.assertTrue(c.getArray().length == 4);
        Assert.assertTrue(Arrays.equals(c.getArray(), new int[] {1, 2, 1, 2}));
    }

    public static class Case3 {
        @Number(width = 3)
        private int value1;
        @Number(width = 5)
        private int value2;
        @Number(width = 4, length = "4")
        private int[] array;

        @Override
        public String toString() {
            return "Case3{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                ", array=" + Arrays.toString(array) +
                '}';
        }

        public int getValue1() {
            return value1;
        }

        public void setValue1(int value1) {
            this.value1 = value1;
        }

        public int getValue2() {
            return value2;
        }

        public void setValue2(int value2) {
            this.value2 = value2;
        }

        public int[] getArray() {
            return array;
        }

        public void setArray(int[] array) {
            this.array = array;
        }
    }
}
