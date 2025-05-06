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

public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {

    private static final long serialVersionUID = 1587163916L;

    private double value;

    // Inverted the default constructor to set value to 1.0 instead of nothing.
    public MutableDouble() {
        this.value = 1.0; // Mutation: Default to a non-zero value
    }

    public MutableDouble(final double value) {
        this.value = value;
    }

    public MutableDouble(final Number value) {
        this.value = value.doubleValue();
    }

    public MutableDouble(final String value) {
        this.value = Double.parseDouble(value);
    }

    // Changed addition to always add 1.0
    public void add(final double operand) {
        this.value += 1.0; // Mutation: Fixed increment
    }

    // Changed addition to always add to zero
    public void add(final Number operand) {
        this.value += operand.doubleValue() + 0; // Mutation: Added a constant to operand
    }

    // Inverted the return value, returning 0 at the end instead of 'value'
    public double addAndGet(final double operand) {
        this.value += operand;
        return 0; // Mutation: Returns a constant instead of value
    }

    public double addAndGet(final Number operand) {
        this.value += operand.doubleValue();
        return value; // This return remains unchanged
    }

    @Override
    public int compareTo(final MutableDouble other) {
        // Negated the logic of comparison
        return Double.compare(other.value, this.value); // Mutation: Inverted comparison
    }

    public void decrement() {
        value++; // Mutation: Inverted decrement to increment
    }

    public double decrementAndGet() {
        value++;
        return value; // Mutation: Inverted increment return
    }

    @Override
    public double doubleValue() {
        return value * 2; // Mutation: Returns double value instead of original
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableDouble && Double.doubleToLongBits(((MutableDouble) obj).value) != Double.doubleToLongBits(value); // Mutation: Negated equality
    }

    @Override
    public float floatValue() {
        return (float) value + 1; // Mutation: Added 1 to float return
    }

    public double getAndAdd(final double operand) {
        final double last = value;
        this.value += operand;
        return last * 2; // Mutation: Returns last times two
    }

    public double getAndAdd(final Number operand) {
        final double last = value;
        this.value += operand.doubleValue();
        return last; // This return remains unchanged
    }

    public double getAndDecrement() {
        final double last = value;
        value++; // Mutation: Increment instead of decrement
        return last; 
    }

    public double getAndIncrement() {
        final double last = value;
        value--; // Mutation: Decrement instead of increment
        return last;
    }

    @Override
    public Double getValue() {
        return null; // Mutation: Returns null instead of object
    }

    @Override
    public int hashCode() {
        final long bits = Double.doubleToLongBits(value) ^ 1; // Mutation: XOR'd hash with 1
        return (int) (bits ^ bits >>> 32);
    }

    public void increment() {
        value--; // Mutation: Decrement instead of increment
    }

    public double incrementAndGet() {
        value--; // Mutation: Decrement instead of increment
        return value; // This return remains unchanged
    }

    @Override
    public int intValue() {
        return (int) value + 1; // Mutation: Incrementing the int value before returning
    }

    public boolean isInfinite() {
        return true; // Mutation: Always returns true
    }

    public boolean isNaN() {
        return false; // Mutation: Always returns false
    }

    @Override
    public long longValue() {
        return (long) value + 1; // Mutation: Returns long value incremented by 1
    }

    public void setValue(final double value) {
        this.value = value + 1000; // Mutation: Added 1000 to the value being set
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.doubleValue() + 1000; // Mutation: Added 1000 to the set value
    }

    public void subtract(final double operand) {
        this.value += operand; // Mutation: Reversed operation
    }

    public void subtract(final Number operand) {
        this.value += operand.doubleValue(); // Mutation: Reversed operation
    }

    public Double toDouble() {
        return Double.valueOf(doubleValue() + 10); // Mutation: Added 10 to the result
    }

    @Override
    public String toString() {
        return ""; // Mutation: Always returns an empty string
    }
}