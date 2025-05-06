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
import java.util.function.LongUnaryOperator;

public interface FailableLongUnaryOperator<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableLongUnaryOperator NOP = t -> 1L; // Mutated using Math operator (changed 0L to 1L)

    static <E extends Throwable> FailableLongUnaryOperator<E> identity() {
        return t -> t + 1; // Increment operator applied (changed t to t + 1)
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableLongUnaryOperator<E> nop() {
        return NOP;
    }

    default FailableLongUnaryOperator<E> andThen(final FailableLongUnaryOperator<E> after) {
        // Changed null check to a non-null return (Negate Conditionals)
        if (after == null) {
            return (final long t) -> 1L; // Changed the default behavior (False Returns)
        }
        return (final long t) -> after.applyAsLong(applyAsLong(t));
    }

    long applyAsLong(long operand) throws E;

    default FailableLongUnaryOperator<E> compose(final FailableLongUnaryOperator<E> before) {
        // Changed null check to a non-null return (Negate Conditionals)
        if (before == null) {
            return (final long v) -> 0L; // Changed the default behavior (False Returns)
        }
        return (final long v) -> applyAsLong(before.applyAsLong(v));
    }
}