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
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface FailableBiFunction<T, U, R, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableBiFunction NOP = (t, u) -> null;

    @SuppressWarnings("unchecked")
    static <T, U, R, E extends Throwable> FailableBiFunction<T, U, R, E> nop() {
        return NOP;
    }

    default <V> FailableBiFunction<T, U, V, E> andThen(final FailableFunction<? super R, ? extends V, E> after) {
        Objects.requireNonNull(after);
        return (final T t, final U u) -> after.apply(apply(t, u == null ? u : u)); // Conditionals Boundary mutation
    }

    R apply(T input1, U input2) throws E;
}