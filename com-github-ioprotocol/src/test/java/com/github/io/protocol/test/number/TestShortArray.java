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

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.test.TestEngine;
import com.github.io.protocol.utils.ByteBufferUtil;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestShortArray {
    @Number(width = 16)
    private int length;
    @Number(width = 16, length = "getLength")
    private int[] values;
    @Number(width = 16)
    private int length1;
    @Number(width = 16, length = "getLength1", order = ByteOrder.BigEndian)
    private int[] values1;

    @Test
    public void test() throws Exception {
        int[] ar1 = new int[]{1,2,3,4,5,6};
        int[] ar2 = new int[]{1,2,3,4};
        TestShortArray testShortArray = new TestShortArray();
        testShortArray.setValues(ar1);
        testShortArray.setValues1(ar2);

        byte[] buf = TestEngine.encode(testShortArray);
        System.out.println(HexStringUtil.toHexString(buf));
        Assert.assertTrue(buf[0] == (byte)6);
        Assert.assertTrue(buf[1] == (byte)0);

        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 2, ByteOrder.SmallEndian) == 1);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 4, ByteOrder.SmallEndian) == 2);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 6, ByteOrder.SmallEndian) == 3);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 8, ByteOrder.SmallEndian) == 4);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 10, ByteOrder.SmallEndian) == 5);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 12, ByteOrder.SmallEndian) == 6);

        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 14, ByteOrder.SmallEndian) == 4);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 16, ByteOrder.BigEndian) == 1);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 18, ByteOrder.BigEndian) == 2);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 20, ByteOrder.BigEndian) == 3);
        Assert.assertTrue(ByteBufferUtil.parseShort(buf, 22, ByteOrder.BigEndian) == 4);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.length = values.length;
        this.values = values;
    }

    public int getLength1() {
        return length1;
    }

    public void setLength1(int length1) {
        this.length1 = length1;
    }

    public int[] getValues1() {
        return values1;
    }

    public void setValues1(int[] values1) {
        this.length1 = values1.length;
        this.values1 = values1;
    }
}
