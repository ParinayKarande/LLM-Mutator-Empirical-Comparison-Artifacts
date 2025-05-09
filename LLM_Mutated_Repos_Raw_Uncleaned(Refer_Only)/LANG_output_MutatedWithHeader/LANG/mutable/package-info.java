/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.mutable;

public class Mutable {
    private int value;

    public Mutable(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isPositive() {
        return value > 0;
    }

    public int increment() {
        return ++value; // increments the value
    }

    public int decrement() {
        return --value; // decrements the value
    }

    public boolean equals(Object obj) {
        if (obj instanceof Mutable) {
            return this.value == ((Mutable) obj).getValue();
        }
        return false;
    }

    public String toString() {
        return "Mutable{" + "value=" + value + '}';
    }
}