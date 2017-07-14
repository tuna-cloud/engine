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

package com.github.io.protocol.test.performance;

import com.github.io.protocol.annotation.ByteOrder;
import com.github.io.protocol.annotation.Number;
import com.github.io.protocol.test.TestEngine;
import org.junit.Test;

public class TestShort {
    @Number(width = 16)
    private int value1;
    @Number(width = 16, order = ByteOrder.BigEndian)
    private Integer value2;
    @Number(width = 16, offset = 100)
    private int value3;
    @Number(width = 16, offset = 100, order = ByteOrder.BigEndian)
    private int value4;
    @Number(width = 16, offset = 100, order = ByteOrder.BigEndian, isBCDCode = true)
    private Integer value5;

    @Test
    public void testEncode() throws Exception {
        TestShort testShort = new TestShort();
        testShort.setValue1(0x1213);
        testShort.setValue2(0x2223);
        testShort.setValue3(28);
        testShort.setValue4(28);
        testShort.setValue5(255);

        byte[] buf = null;
        long t1 = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++ ) {
            buf = TestEngine.encode(testShort);
        }
        long t2 = System.currentTimeMillis();
        System.out.println((t2-t1));
        System.out.println((t2-t1)/1000000d);
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public Integer getValue5() {
        return value5;
    }

    public void setValue5(Integer value5) {
        this.value5 = value5;
    }

    @Override public String toString() {
        return "TestShort{" +
            "value1=" + value1 +
            ", value2=" + value2 +
            ", value3=" + value3 +
            ", value4=" + value4 +
            ", value5=" + value5 +
            '}';
    }
}
