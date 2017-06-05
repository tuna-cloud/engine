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

public class TestByteArray {
    @Number(width = 8)
    private int length;
    @Number(width = 8, length = "getLength", offset = 100)
    private int[] values;

    @Test
    public void test() throws Exception {
        TestByteArray array = new TestByteArray();
        array.setValues(new int[] {1, 2, 3, 4, 5, 6});
        byte[] buf = TestEngine.encode(array);
        System.out.println(HexStringUtil.toHexString(buf));
        Assert.assertTrue(buf[0] == (byte) 6);
        for (int i = 1; i < 7; i++) {
            Assert.assertTrue(buf[i] == (byte) (i + 100));
        }

        TestByteArray b = TestEngine.decode(buf, TestByteArray.class);
        System.out.println(b.toString());
        Assert.assertTrue(b.getLength() == 6);
        Assert.assertTrue(Arrays.equals(b.getValues(), array.getValues()));
        System.out.println(TestEngine.toPreetyString(b));
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
        this.values = values;
        this.length = values.length;
    }

    @Override public String toString() {
        return "TestByteArray{" +
            "length=" + length +
            ", values=" + Arrays.toString(values) +
            '}';
    }
}
