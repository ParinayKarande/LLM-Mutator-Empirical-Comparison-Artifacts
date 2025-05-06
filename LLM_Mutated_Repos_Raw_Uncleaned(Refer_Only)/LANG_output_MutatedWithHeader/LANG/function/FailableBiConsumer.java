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
import java.util.function.BiConsumer;

@FunctionalInterface
public interface FailableBiConsumer<T, U, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableBiConsumer NOP = (t, u) -> {
    };

    @SuppressWarnings("unchecked")
    static <T, U, E extends Throwable> FailableBiConsumer<T, U, E> nop() {
        // Mutant: Changing to always return null.
        return null; // Null Returns
    }

    // Mutant: Changing accept to take primitive values instead
    // and manipulating them.
    void accept(int t, int u) throws E; // Primitive Returns

    default FailableBiConsumer<T, U, E> andThen(final FailableBiConsumer<? super T, ? super U, E> after) {
        Objects.requireNonNull(after);
        return (t, u) -> {
            // Mutant: Adding a void method call
            doSomethingUnnecessary(); // Void Method Calls
            accept(t, u);
            after.accept(t, u);
        };
    }

    // Mutant: Introduce a simple unnecessary void method
    default void doSomethingUnnecessary() {
        // Just a placeholder for void method logic; does nothing.
    }

    // Mutant Implementation to always return true
    // (Technically not applicable here, but conceptually as an illustration)
    default boolean alwaysTrue() {
        return true; // True Returns
    }
}