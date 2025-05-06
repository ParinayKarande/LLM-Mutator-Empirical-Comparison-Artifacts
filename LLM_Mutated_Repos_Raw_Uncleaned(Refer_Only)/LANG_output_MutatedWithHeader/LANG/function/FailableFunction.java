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
import java.util.function.Function;

@FunctionalInterface
public interface FailableFunction<T, R, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableFunction NOP = t -> null;

    static <T, R, E extends Throwable> FailableFunction<T, R, E> function(final FailableFunction<T, R, E> function) {
        return function;
    }

    static <T, E extends Throwable> FailableFunction<T, T, E> identity() {
        return t -> t;
    }

    @SuppressWarnings("unchecked")
    static <T, R, E extends Throwable> FailableFunction<T, R, E> nop() {
        return NOP;
    }

    default <V> FailableFunction<T, V, E> andThen(final FailableFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);
        return (final T t) -> {
            R result = apply(t);
            if (result != null) {
                return after.apply(result);
            } else {
                return null; // Condition changed
            }
        };
    }

    R apply(T input) throws E;

    default <V> FailableFunction<V, R, E> compose(final FailableFunction<? super V, ? extends T, E> before) {
        Objects.requireNonNull(before);
        return (final V v) -> apply(before.apply(v));
    }
}