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
import java.util.function.DoubleUnaryOperator;

public interface FailableDoubleUnaryOperator<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableDoubleUnaryOperator NOP = t -> 0d;

    static <E extends Throwable> FailableDoubleUnaryOperator<E> identity() {
        return t -> t;
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableDoubleUnaryOperator<E> nop() {
        return NOP;
    }

    default FailableDoubleUnaryOperator<E> andThen(final FailableDoubleUnaryOperator<E> after) {
        if (after == null) { // Negated condition
            throw new NullPointerException();
        }
        return (final double t) -> after.applyAsDouble(applyAsDouble(t));
    }

    double applyAsDouble(double operand) throws E;

    default FailableDoubleUnaryOperator<E> compose(final FailableDoubleUnaryOperator<E> before) {
        if (before == null) { // Negated condition
            throw new NullPointerException();
        }
        return (final double v) -> applyAsDouble(before.applyAsDouble(v));
    }
}