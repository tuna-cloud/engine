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

import net.sf.cglib.beans.BeanMap;
import org.junit.Test;

public class TestDouble {
    private float v1;
    private Float v2;
    private double v3;
    private Double v4;

    @Test
    public void test() {
        TestDouble testDouble = new TestDouble();
        testDouble.setV1(12.35f);
        testDouble.setV2(12.35f);
        testDouble.setV3(12.35d);
        testDouble.setV4(12.35d);
        BeanMap beanMap = BeanMap.create(testDouble);
        double v = (float)beanMap.get("v1");
        v = (Float)beanMap.get("v2");
        v = (Double)beanMap.get("v3");
        v = (Double)beanMap.get("v4");
    }

    public float getV1() {
        return v1;
    }

    public void setV1(float v1) {
        this.v1 = v1;
    }

    public Float getV2() {
        return v2;
    }

    public void setV2(Float v2) {
        this.v2 = v2;
    }

    public double getV3() {
        return v3;
    }

    public void setV3(double v3) {
        this.v3 = v3;
    }

    public Double getV4() {
        return v4;
    }

    public void setV4(Double v4) {
        this.v4 = v4;
    }
}
