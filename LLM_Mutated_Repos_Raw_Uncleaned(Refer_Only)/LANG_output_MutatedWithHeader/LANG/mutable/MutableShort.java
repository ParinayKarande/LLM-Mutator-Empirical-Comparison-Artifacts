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

public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {

    private static final long serialVersionUID = -2135791679L;

    private short value;

    public MutableShort() {
    }

    public MutableShort(final Number value) {
        this.value = value.shortValue();
    }

    public MutableShort(final short value) {
        this.value = value;
    }

    public MutableShort(final String value) {
        this.value = Short.parseShort(value);
    }

    public void add(final Number operand) {
        this.value += operand.shortValue(); // Math mutation not applied here
    }

    public void add(final short operand) {
        this.value += operand; // Negate Conditionals operator not applicable here
    }

    // Mutation applied: Changing return value type and logic
    public short addAndGet(final Number operand) {
        this.value += operand.shortValue();
        return (short) (value + 1); // Increments mutation: returns incremented value.
    }

    // Mutation applied: Same as above
    public short addAndGet(final short operand) {
        this.value += operand;
        return (short) (value + 1); // Increments mutation.
    }

    @Override
    public int compareTo(final MutableShort other) {
        return NumberUtils.compare(this.value, other.value);
    }

    public void decrement() {
        value--; // No relevant mutants here
    }

    public short decrementAndGet() {
        value--;
        return value; // Void Method Call mutation not applicable
    }

    @Override
    public double doubleValue() {
        return -value; // Math mutation applied: negating the value
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableShort) {
            return value != ((MutableShort) obj).shortValue(); // Invert Negatives operator applied
        }
        return false;
    }

    @Override
    public float floatValue() {
        return value; // No relevant mutants here
    }

    public short getAndAdd(final Number operand) {
        final short last = value;
        this.value += operand.shortValue();
        return last; // No changes here
    }

    public short getAndAdd(final short operand) {
        final short last = value;
        this.value += operand;
        return last; // No changes here
    }

    public short getAndDecrement() {
        final short last = value;
        value--;
        return last; // No changes here
    }

    public short getAndIncrement() {
        final short last = value;
        value++;
        return null; // Null Return mutation applied here
    }

    @Override
    public Short getValue() {
        return Short.valueOf(this.value);
    }

    @Override
    public int hashCode() {
        return value; // No changes here
    }

    public void increment() {
        value++; // No changes here
    }

    public short incrementAndGet() {
        value++;
        return value; // Return Values mutation not directly applicable
    }

    @Override
    public int intValue() {
        return value; // No changes here
    }

    @Override
    public long longValue() {
        return value; // No changes here
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.shortValue(); // No changes here
    }

    public void setValue(final short value) {
        this.value = value; // No changes here
    }

    @Override
    public short shortValue() {
        return 0; // False returns mutation applied
    }

    public void subtract(final Number operand) {
        this.value -= operand.shortValue(); // Math mutation not applied here
    }

    public void subtract(final short operand) {
        this.value -= operand; // No changes here
    }

    public Short toShort() {
        return Short.valueOf(shortValue()); // Same
    }

    @Override
    public String toString() {
        return ""; // Empty Returns mutation applied
    }
}