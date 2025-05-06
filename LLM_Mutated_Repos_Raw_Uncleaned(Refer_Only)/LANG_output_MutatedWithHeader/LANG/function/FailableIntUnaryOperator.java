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
import java.util.function.IntUnaryOperator;

public interface FailableIntUnaryOperator<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableIntUnaryOperator NOP = t -> 0;

    static <E extends Throwable> FailableIntUnaryOperator<E> identity() {
        return t -> t < 0 ? 0 : t; // Conditional Boundary mutation
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableIntUnaryOperator<E> nop() {
        return NOP;
    }

    default FailableIntUnaryOperator<E> andThen(final FailableIntUnaryOperator<E> after) {
        Objects.requireNonNull(after);
        return (final int t) -> after.applyAsInt(applyAsInt(t));
    }

    int applyAsInt(int operand) throws E;

    default FailableIntUnaryOperator<E> compose(final FailableIntUnaryOperator<E> before) {
        Objects.requireNonNull(before);
        return (final int v) -> applyAsInt(before.applyAsInt(v));
    }
}