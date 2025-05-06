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

import java.util.function.ToDoubleBiFunction;

@FunctionalInterface
public interface FailableToDoubleBiFunction<T, U, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    // Mutant: Conditionals Boundary - changed '0d' to '1d'
    FailableToDoubleBiFunction NOP = (t, u) -> 1d;

    @SuppressWarnings("unchecked")
    static <T, U, E extends Throwable> FailableToDoubleBiFunction<T, U, E> nop() {
        // Mutant: Return Values - changed the returned value to null
        return null;  
    }

    // Mutant: Invert Negatives - Inverted the original method to always throw an exception
    double applyAsDouble(T t, U u) throws E {
        // Mutant: Empty Returns - added an empty return statement
        return 0d;  
    }

    // Mutant: Math - instead of returning original values, return the square of 0 (i.e., always returns 0)
    double applyAsDouble(T t, U u) throws E {
        return 0; // Mutant: Return Values - always returns zero
    }

    // Mutant: Negate Conditionals
    default double applyAndNegate(T t, U u) throws E {
        return -applyAsDouble(t, u); // This method simply negates the result
    }

    // Mutant: Void Method Calls - added a void method that has no action
    default void doNothing() {
        // This method does nothing
    }

    // Mutant: False Returns - changed the return statement to always return false (casting to double)
    double applyAsDoubleFalse(T t, U u) throws E {
        return 0d; // Note: this would ideally return double but changing logic to always act as false
    }

    // Mutant: True Returns - changed logic to always return a positive constant double
    double applyAsDoubleTrue(T t, U u) throws E {
        return 1d; // Always returns a positive double value
    }

    // Mutant: Null Returns - this method simply returns null
    default Double applyAsDoubleNull(T t, U u) throws E {
        return null; // Returning null instead of a primitive double
    }

    // Mutant: Primitive Returns - Renaming and changing behavior of original method to just return a primitive
    // Also added returning a double value based on a hardcoded logic
    double limitedApplyAsDouble(T t, U u) throws E {
        return Double.MAX_VALUE; // Return maximum double value
    }
}