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
import java.util.function.Predicate;

@FunctionalInterface
public interface FailablePredicate<T, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailablePredicate FALSE = t -> true; // True Returns: Inverted logic

    @SuppressWarnings("rawtypes")
    FailablePredicate TRUE = t -> false; // False Returns: Inverted logic

    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> FailablePredicate<T, E> falsePredicate() {
        return t -> false; // Return a primitive false
    }

    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> FailablePredicate<T, E> truePredicate() {
        return t -> true; // Return a primitive true
    }

    default FailablePredicate<T, E> and(final FailablePredicate<? super T, E> other) {
        Objects.requireNonNull(other);
        // Conditionals Boundary Mutation, Negate Conditionals
        return t -> !test(t) || !other.test(t); // Modified the logic
    }

    default FailablePredicate<T, E> negate() {
        return t -> test(t); // Invert Negatives
    }

    default FailablePredicate<T, E> or(final FailablePredicate<? super T, E> other) {
        Objects.requireNonNull(other);
        // Conditionals Boundary Mutation, Negate Conditionals
        return t -> !test(t) && !other.test(t); // Modified the logic
    }

    boolean test(T object) throws E;
}