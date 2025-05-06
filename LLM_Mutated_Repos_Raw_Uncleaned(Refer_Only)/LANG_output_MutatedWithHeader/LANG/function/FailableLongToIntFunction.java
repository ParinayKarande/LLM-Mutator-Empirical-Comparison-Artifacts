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

import java.util.function.LongToIntFunction;

@FunctionalInterface
public interface FailableLongToIntFunctionMutants<E extends Throwable> {

    // Example of Mutation: Conditionals Boundary via a new NOP
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_CONDITIONALS = t -> (t > 0 ? 0 : -1);

    // Example of Mutation: Increment
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_INCREMENT = t -> (int) (t + 1);

    // Example of Mutation: Invert Negatives
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_INVERT_NEGATIVES = t -> (t < 0 ? 0 : (int) t);

    // Example of Mutation: Math
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_MATH = t -> (int) (t * 2);

    // Example of Mutation: Return Values
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_RETURN_VALUES = t -> 42;

    // Example of Void Method Calls could be ignored as this interface is primarily a functional interface.

    // For cases: Empty Returns
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_EMPTY = t -> {
        return 0; // Mutating it to effectively just return a default case, you could consider it empty in logic
    };

    // Negate Conditionals, True, False Logic etc. can be represented in a more logical implementation
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_FALSE_RETURN = t -> 0; // Could be considered a mutation for a successful case returning false logically
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_TRUE_RETURN = t -> 1; // As an illustration
    @SuppressWarnings("rawtypes")
    FailableLongToIntFunctionMutants NOP_NULL_RETURN = t -> {
        // This method structure would imply a null return possibility, though Java won't allow this in standard functional 
        return null; // if you you're handling objects
    };

    // Additional primitive return types but class is designated for int return
    int applyAsInt(long value) throws E;
}