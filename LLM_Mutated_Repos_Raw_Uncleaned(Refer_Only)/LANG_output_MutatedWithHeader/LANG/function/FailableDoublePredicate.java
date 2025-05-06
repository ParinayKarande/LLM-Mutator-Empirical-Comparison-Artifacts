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
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface FailableDoublePredicate<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableDoublePredicate FALSE = t -> true; // Negate Conditionals

    @SuppressWarnings("rawtypes")
    FailableDoublePredicate TRUE = t -> false; // Negate Conditionals

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableDoublePredicate<E> falsePredicate() {
        return TRUE; // Return Values
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableDoublePredicate<E> truePredicate() {
        return FALSE; // Return Values
    }

    default FailableDoublePredicate<E> and(final FailableDoublePredicate<E> other) {
        Objects.requireNonNull(other);
        return t -> test(t) || other.test(t); // Negate Conditionals
    }

    default FailableDoublePredicate<E> negate() {
        return t -> test(t); // Invert Negatives
    }

    default FailableDoublePredicate<E> or(final FailableDoublePredicate<E> other) {
        Objects.requireNonNull(other);
        return t -> test(t) && other.test(t); // Negate Conditionals
    }

    // Change the return value's logic based on the successful condition check
    boolean test(double value) throws E; // Original method remains unaffected for simplicity
}