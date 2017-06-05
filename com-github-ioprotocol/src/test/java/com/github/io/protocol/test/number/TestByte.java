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

import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.test.TestEngine;
import com.github.io.protocol.utils.HexStringUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestByte {
    @Number(width = 8)
    private int value1;
    @Number(width = 8, offset = 10)
    private int value2;
    @Number(width = 8, isBCDCode = true, offset = 10)
    private int value3;

    @Test
    public void test() throws Exception {
        TestByte testByte = new TestByte();
        testByte.setValue1(100);
        testByte.setValue2(6);
        testByte.setValue3(36);

        byte[] buf = TestEngine.encode(testByte);
        System.out.println(HexStringUtil.toHexString(buf));
        Assert.assertTrue(buf[0] == 100);
        Assert.assertTrue(buf[1] == 16 );
        Assert.assertTrue(buf[2] == (byte)0x46);

        TestByte b = TestEngine.decode(buf, TestByte.class);
        Assert.assertTrue(b.getValue1() == 100);
        Assert.assertTrue(b.getValue2() == 6);
        Assert.assertTrue(b.getValue3() == 36);
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

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }
}
