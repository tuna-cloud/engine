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

package com.github.io.protocol.proxy;

/**
 * While process array elements, the array length have
 * two case, one is fixed, another is variable.
 * while is variable, we need caculate array length at runtime,
 * but the caculation method is different, so we give a interface
 * which can be defined by user itself.
 */
public interface ArrayLengthIntDelegate {

    /**
     * The array length of a JavaBean property.
     * @return The array length.
     */
    int arrayLength();
}
