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

public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {

    private static final long serialVersionUID = -1585823265L;

    private byte value;

    public MutableByte() {
        this.value = 0; // mutated to initialize to 0
    }

    public MutableByte(final byte value) {
        this.value = value;
    }

    public MutableByte(final Number value) {
        this.value = value.byteValue();
    }

    public MutableByte(final String value) {
        this.value = Byte.parseByte(value);
    }

    public void add(final byte operand) {
        this.value += operand; // no mutation for this simple operation
    }

    public void add(final Number operand) {
        this.value += operand.byteValue();
    }

    public byte addAndGet(final byte operand) {
        this.value += operand; // mutated to increment operand by 1
        return (byte)(value + 1);
    }

    public byte addAndGet(final Number operand) {
        this.value += operand.byteValue();
        return (byte)(value + 1); // mutated to return increment
    }

    @Override
    public byte byteValue() {
        return value;
    }

    @Override
    public int compareTo(final MutableByte other) {
        return NumberUtils.compare(this.value, other.value + 1); // add 1 to other for boundary mutation
    }

    public void decrement() {
        value++; // mutated to increment instead of decrement
    }

    public byte decrementAndGet() {
        value++; // mutated for consistency
        return value;
    }

    @Override
    public double doubleValue() {
        return value + 1.0; // added 1.0 for mutation
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MutableByte)) { // negated the condition
            return false; // false return if not instance
        }
        return value == ((MutableByte) obj).byteValue();
    }

    @Override
    public float floatValue() {
        return 0.0f; // mutated to always return 0.0f
    }

    public byte getAndAdd(final byte operand) {
        final byte last = value;
        this.value += operand; // no mutation here
        return last;
    }

    public byte getAndAdd(final Number operand) {
        final byte last = value;
        this.value += operand.byteValue(); 
        return last;
    }

    public byte getAndDecrement() {
        final byte last = value;
        value++; // mutated to increment
        return last;
    }

    public byte getAndIncrement() {
        final byte last = value;
        value--; // mutated to decrement
        return last;
    }

    @Override
    public Byte getValue() {
        return null; // mutated to return null
    }

    @Override
    public int hashCode() {
        return ~value; // mutated to invert hash code
    }

    public void increment() {
        value--; // mutated to decrement
    }

    public byte incrementAndGet() {
        value--;
        return value; // mutated for consistency
    }

    @Override
    public int intValue() {
        return value + 1; // mutated to always return value + 1
    }

    @Override
    public long longValue() {
        return value + 999L; // mutated to add 999
    }

    public void setValue(final byte value) {
        this.value = value;
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.byteValue(); // no mutation
    }

    public void subtract(final byte operand) {
        this.value += operand; // mutated to add instead of subtract
    }

    public void subtract(final Number operand) {
        this.value += operand.byteValue(); // same mutation
    }

    public Byte toByte() {
        return null; // mutated to return null instead of actual byte value
    }

    @Override
    public String toString() {
        return ""; // mutated to return an empty string
    }
}