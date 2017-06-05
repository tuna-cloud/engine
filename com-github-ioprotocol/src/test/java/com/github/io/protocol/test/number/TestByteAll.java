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

public class TestByteAll {
    @Number(width = 8)
    private byte v1 = 0x18;
    @Number(width = 8)
    private Byte v2 = 0x18;
    @Number(width = 8)
    private short v3 = 0x18;
    @Number(width = 8)
    private Short v4 = 0x18;
    @Number(width = 8)
    private int v5 = 0x18;
    @Number(width = 8)
    private Integer v6 = 0x18;

    @Number(width = 8, isBCDCode = true)
    private byte v7 = 18;
    @Number(width = 8, isBCDCode = true)
    private Byte v8 = 18;
    @Number(width = 8, isBCDCode = true)
    private short v9 = 18;
    @Number(width = 8, isBCDCode = true)
    private Short v10 = 18;
    @Number(width = 8, isBCDCode = true)
    private int v11 = 18;
    @Number(width = 8, isBCDCode = true)
    private Integer v12 = 18;


}
