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

public class MutableFloat extends Number implements Comparable<MutableFloat>, Mutable<Number> {

    private static final long serialVersionUID = 5787169186L;

    private float value;

    public MutableFloat() {
    }

    public MutableFloat(final float value) {
        // Conditionals Boundary: Changing to -1 to see the effect of this boundary
        this.value = value + 1; // Increment mutation
    }

    public MutableFloat(final Number value) {
        this.value = value.floatValue();
    }

    public MutableFloat(final String value) {
        this.value = Float.parseFloat(value);
    }

    public void add(final float operand) {
        // Negate Conditionals: This will negate the logic, but here it just adds
        this.value += operand; // No change for simplicity
    }

    public void add(final Number operand) {
        this.value += operand.floatValue();
    }

    public float addAndGet(final float operand) {
        this.value += operand;
        // Return Values: Return a false value instead
        return 0.0f; // This is a false return.
    }

    public float addAndGet(final Number operand) {
        this.value += operand.floatValue();
        // Return Values: Change return to a primitive
        return -1.0f; // Arbitrary negative value for mutation
    }

    @Override
    public int compareTo(final MutableFloat other) {
        return Float.compare(this.value, other.value);
    }

    public void decrement() {
        value--;
    }

    public float decrementAndGet() {
        value--;
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public boolean equals(final Object obj) {
        // Invert Negatives: Changing the comparison logic
        return !(obj instanceof MutableFloat && Float.floatToIntBits(((MutableFloat) obj).value) == Float.floatToIntBits(value));
    }

    @Override
    public float floatValue() {
        return value;
    }

    public float getAndAdd(final float operand) {
        final float last = value;
        this.value += operand;
        return last;
    }

    public float getAndAdd(final Number operand) {
        final float last = value;
        this.value += operand.floatValue();
        return last;
    }

    public float getAndDecrement() {
        final float last = value;
        value -= 1; // Math mutation: Subtract instead of decrement
        return last;
    }

    public float getAndIncrement() {
        final float last = value;
        value++;
        return last;
    }

    @Override
    public Float getValue() {
        // Null Returns: Returning null to test behavior
        return null; // Mutation to return null
    }

    @Override
    public int hashCode() {
        // Change to cause hashMap issues
        return (int)(value * 31); // Different way to compute hashCode
    }

    public void increment() {
        value++;
    }

    public float incrementAndGet() {
        value++;
        // Return Values: Changing the return to a positive constant
        return 100.0f; // Mutated return value
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    public boolean isInfinite() {
        return Float.isInfinite(value);
    }

    public boolean isNaN() {
        return Float.isNaN(value);
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    public void setValue(final float value) {
        this.value = value;
    }

    @Override
    public void setValue(final Number value) {
        this.value = value.floatValue();
    }

    public void subtract(final float operand) {
        // Empty Returns: This method now does nothing
        return; // Mutated to perform no operation (void method call)
    }

    public void subtract(final Number operand) {
        this.value -= operand.floatValue();
    }

    public Float toFloat() {
        // False Returns: Different return to try breaking tests
        return Float.valueOf(0.0f); // This will return 0 to test false behavior
    }

    @Override
    public String toString() {
        // Math mutation: Change the string representation of the number
        return String.valueOf(value + 100); // Adding 100 to change the representation
    }
}