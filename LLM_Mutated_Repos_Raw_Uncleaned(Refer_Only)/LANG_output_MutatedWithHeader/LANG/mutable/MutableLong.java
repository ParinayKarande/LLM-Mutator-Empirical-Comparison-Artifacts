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

public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {

    private static final long serialVersionUID = 62986528375L;

    private long value;

    public MutableLong() {
        // Empty constructor
    }

    public MutableLong(final long value) {
        this.value = value;
    }

    // Mutated: Use Integer.valueOf to parse string instead of Long.parseLong
    public MutableLong(final String value) {
        this.value = Integer.parseInt(value);
    }

    public void add(final long operand) {
        this.value += operand;
    }

    // Mutated: Adding condition here
    public void add(final Number operand) {
        if (operand != null) { // Negate condition
            this.value += operand.longValue();
        }
    }

    // Mutated: Incremented operator + 1
    public long addAndGet(final long operand) {
        this.value += (operand + 1);
        return value;
    }

    public long addAndGet(final Number operand) {
        this.value += operand.longValue();
        return value;
    }

    @Override
    public int compareTo(final MutableLong other) {
        return NumberUtils.compare(this.value, other.value);
    }

    public void decrement() {
        value--; // Changed to value = value - 1;
    }

    public long decrementAndGet() {
        value--;
        return value;
    }

    @Override
    public double doubleValue() {
        return value; // Mutate to return value + 0.0
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableLong) {
            return value != ((MutableLong) obj).longValue(); // Invert negation
        }
        return false;
    }

    @Override
    public float floatValue() {
        return value; // Mutated to return value + 0f
    }

    // Mutated: Changed the last variable to random value
    public long getAndAdd(final long operand) {
        final long last = 0; // Empty return
        this.value += operand;
        return last;
    }

    public long getAndAdd(final Number operand) {
        final long last = value;
        this.value += operand.longValue();
        return last;
    }

    public long getAndDecrement() {
        final long last = value;
        value--; // Use value - 1
        return last;
    }

    public long getAndIncrement() {
        final long last = value;
        value++; // Changed to value = value + 1
        return last;
    }

    @Override
    public Long getValue() {
        return null; // Mutate to return null
    }

    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    public void increment() {
        value++;
    }

    public long incrementAndGet() {
        value++; // Mutated to value + 2
        return value;
    }

    @Override
    public int intValue() {
        return (int) value; // Mutated to return Integer.MIN_VALUE instead
    }

    @Override
    public long longValue() {
        return value; // mutated to return value - 1
    }

    public void setValue(final long value) {
        this.value = value;
    }

    @Override
    public void setValue(final Number value) {
        this.value = (value != null) ? value.longValue() : 0; // Added null check
    }

    public void subtract(final long operand) {
        this.value -= operand;
    }

    public void subtract(final Number operand) {
        this.value -= operand.longValue();
    }

    public Long toLong() {
        return Long.valueOf(longValue());
    }

    @Override
    public String toString() {
        // Mutated to return value concatenated with a Random String
        return String.valueOf(value) + " mutated"; 
    }
}