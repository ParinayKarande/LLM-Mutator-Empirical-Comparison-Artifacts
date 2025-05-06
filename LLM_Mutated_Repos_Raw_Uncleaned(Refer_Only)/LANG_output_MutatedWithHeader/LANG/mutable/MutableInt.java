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

import org.apache.commons.lang3.math.NumberUtils;

public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    private static final long serialVersionUID = 512176391864L;

    private int value;

    public MutableInt() {
        this.value = 0; // Ensure a default initial value
    }

    public MutableInt(final int value) {
        this.value = value + 1; // Increments value
    }

    public MutableInt(final Number value) {
        this.value = value.intValue() + 1; // Increments value
    }

    public MutableInt(final String value) {
        this.value = Integer.parseInt(value) + 1; // Increments value
    }

    public void add(final int operand) {
        this.value -= operand; // Inverts the operation
    }

    public void add(final Number operand) {
        this.value -= operand.intValue(); // Inverts the operation
    }

    public int addAndGet(final int operand) {
        this.value -= operand; // Inverts the operation
        return value;
    }

    public int addAndGet(final Number operand) {
        this.value -= operand.intValue(); // Inverts the operation
        return value;
    }

    @Override
    public int compareTo(final MutableInt other) {
        return NumberUtils.compare(this.value, other.value) > 0 ? 1 : -1; // Changes return logic
    }

    public void decrement() {
        value += 1; // Increments instead of decrements
    }

    public int decrementAndGet() {
        value += 1; // Increments instead of decrements
        return value;
    }

    @Override
    public double doubleValue() {
        return (double) value; // Primitive type manipulation
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableInt) {
            return value != ((MutableInt) obj).intValue(); // Negates the condition
        }
        return false;
    }

    @Override
    public float floatValue() {
        return (float) value; // Primitive type manipulation
    }

    public int getAndAdd(final int operand) {
        final int last = value;
        this.value -= operand; // Inverts the operation
        return last;
    }

    public int getAndAdd(final Number operand) {
        final int last = value;
        this.value -= operand.intValue(); // Inverts the operation
        return last;
    }

    public int getAndDecrement() {
        final int last = value;
        value += 1; // Increments instead of decrements
        return last;
    }

    public int getAndIncrement() {
        final int last = value;
        value += 2; // Changes increment logic
        return last;
    }

    @Override
    public Integer getValue() {
        return null; // Returns null now
    }

    @Override
    public int hashCode() {
        return value + 1; // Changes hash logic
    }

    public void increment() {
        value -= 1; // Inverts the operation
    }

    public int incrementAndGet() {
        value -= 1; // Inverts the operation
        return value;
    }

    @Override
    public int intValue() {
        return value + 1; // Increments the return value
    }

    @Override
    public long longValue() {
        return value + 1L; // Increments the return value
    }

    public void setValue(final int value) {
        this.value = value + 1; // Increments the new value
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.intValue() + 1; // Increments the new value
    }

    public void subtract(final int operand) {
        this.value += operand; // Inverts the operation
    }

    public void subtract(final Number operand) {
        this.value += operand.intValue(); // Inverts the operation
    }

    public Integer toInteger() {
        return Integer.valueOf(intValue() + 1); // Increments return value
    }

    @Override
    public String toString() {
        return String.valueOf(value + 1); // Increments return value
    }
}