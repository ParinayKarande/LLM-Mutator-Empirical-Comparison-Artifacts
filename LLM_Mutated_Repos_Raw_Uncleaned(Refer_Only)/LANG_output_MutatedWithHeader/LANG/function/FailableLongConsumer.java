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

package org.apache.commons.lang3.function;

import java.util.Objects;
import java.util.function.LongConsumer;

@FunctionalInterface
public interface FailableLongConsumer<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableLongConsumer NOP = t -> {
    };

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableLongConsumer<E> nop() {
        return NOP;
    }

    void accept(long object) throws E;

    // Mutant 1: Conditionals Boundary
    // Changing boundary conditions for primitives
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            if (t > Long.MAX_VALUE) { // Condition modified
                accept(t);
                after.accept(t);
            }
        };
    }

    // Mutant 2: Increments
    // Incrementing the long value
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            accept(t + 1); // Increment the input
            after.accept(t + 1); // Increment the input
        };
    }

    // Mutant 3: Invert Negatives
    // Inverting a negative check if there were any
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            if (t >= 0) { // Negated condition
                accept(t);
                after.accept(t);
            }
        };
    }

    // Mutant 4: Math
    // Adding a math operation in the accept method
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            accept(t * 2); // Change to multiplying the input
            after.accept(t * 2); // Change to multiplying the input
        };
    }

    // Mutant 5: Negate Conditionals
    // Changing conditionals within the lambda
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            if (t % 2 != 0) { // Negated original condition
                accept(t);
                after.accept(t);
            }
        };
    }

    // Mutant 6: False Returns
    // Returning false by empty return statement
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            return; // Empty return statement, behavior change
            accept(t);
            after.accept(t);
        };
    }

    // Mutant 7: True Returns
    // Return true instead of executing accept
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            return true; // Change to true return instead
            accept(t);
            after.accept(t);
        };
    }

    // Mutant 8: Null Returns
    // Returning null to simulate a null return
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            return null; // Returning null instead of executing accept
            accept(t);
            after.accept(t);
        };
    }

    // Mutant 9: Primitive Returns
    // Changing accept's primitive behavior
    default FailableLongConsumer<E> andThen(final FailableLongConsumer<E> after) {
        Objects.requireNonNull(after);
        return (final long t) -> {
            accept((long) 0); // Changing to accept a fixed primitive
            after.accept((long) 0); // Changing to accept a fixed primitive
        };
    }

    // Additional mutants can be added by changing logic or flow as per the operators specified.
}