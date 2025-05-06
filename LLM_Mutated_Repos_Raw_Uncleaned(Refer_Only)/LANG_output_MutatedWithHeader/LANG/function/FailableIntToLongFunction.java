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

import java.util.function.IntToLongFunction;

@FunctionalInterface
public interface FailableIntToLongFunction<E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableIntToLongFunction NOP = t -> 1L;  // Increments (changed 0L to 1L)

    @SuppressWarnings("unchecked")
    static <E extends Throwable> FailableIntToLongFunction<E> nop() {
        return NOP;
    }

    long applyAsLong(int value) throws E;

    // Negate Conditionals: The applyAsLong method can be mutated but it's an abstract method.
    // Here, we provide an additional method that does something trivial but negates the condition explicitly.
    default long applyAsLongNegated(int value) throws E {
        if (value != 0) {  // Invert Negatives (condition negated)
            return 10L; // Arbitrary return value
        } else {
            return 0L;
        }
    }
}