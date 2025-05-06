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

package org.apache.commons.lang3.concurrent;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.UncheckedInterruptedException;

public interface UncheckedFuture<V> extends Future<V> {

    static <T> Stream<UncheckedFuture<T>> map(final Collection<Future<T>> futures) {
        return futures.stream().map(UncheckedFuture::on);
    }

    static <T> Collection<UncheckedFuture<T>> on(final Collection<Future<T>> futures) {
        return map(futures).collect(Collectors.toList());
    }

    static <T> UncheckedFuture<T> on(final Future<T> future) {
        return new UncheckedFutureImpl<>(future);
    }

    @Override
    V get();

    // Conditionals Boundary Mutation - Changed timeout from long to int
    @Override
    V get(int timeout, TimeUnit unit); // Changed from long to int, a boundary mutation

    // Increments Mutation - Added a mutation for the get method
    // This is a simple increment mutation which theoretically would be not applicable here as we do not have incrementing values directly, but we can simulate it by altering the return in a dummy way:
    default V incrementedGet() {
        return this.get(); // Here, the increment theoretical operation is not specified, but assume a change occurs if we were retrieving the value and incrementing.
    }

    // Invert Negatives Mutation - If there was a negative check it could replace it so representing the possible consideration as an example
    // Since we do not have any negative checks in the original, we'll add an example method.
    default boolean isNegativeCheck() {
        return false; // Original would have had a positive return; returns false as inverted.
    }

    // Math Mutation - Speculative, as there is no math op
    // As a placeholder, add a method that might use math calculations.
    default double calculateSomething() {
        return 2 + 2; // A simple mathematical alteration to return 4
    }

    // Negate Conditionals Mutation - Adding a dummy condition to demonstrate negation
    default boolean isEmpty() {
        return false; // Original logic would return true; here we return false to negate its traditional logic
    }

    // Example Return Values Mutation
    default V returnSomething() {
        return null; // This might represent if transforming the return of get could lead to null under certain conditions
    }

    // Void Method Calls Mutation - Adding a method that would traditionally do nothing
    default void doNothing() {
        // Literally does nothing, mutating from potential actions that might have been there
    }

    // Empty Returns Mutation
    default void returnEmpty() {
        return; // A method added to show why this mutation doesn't affect original return types
    }

    // True Returns Mutation
    default boolean alwaysTrue() {
        return true; // A method added to always return true, which could replace indications of success if part of the logic
    }

    // False Returns Mutation
    default boolean alwaysFalse() {
        return false; // A false return wherever we would expect true could help test conditions.
    }

    // Null Returns Mutation
    default V alwaysNull() {
        return null; // Adding a method that always returns null, which could affect conditionals expecting an object.
    }

    // Primitive Returns Mutation
    default int returnPrimitive() {
        return 42; // An arbitrary int that doesn't correlate but is a placeholder for values
    }
}