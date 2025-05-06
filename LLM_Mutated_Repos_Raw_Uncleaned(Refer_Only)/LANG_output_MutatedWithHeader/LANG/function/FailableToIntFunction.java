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

import java.util.function.ToIntFunction;

@FunctionalInterface
public interface FailableToIntFunction<T, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    // Negate Conditionals: changed NOP from returning 0 to returning 1
    FailableToIntFunction NOP = t -> 1;

    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> FailableToIntFunction<T, E> nop() {
        // Return Values: changed the return value from NOP to itself
        return (FailableToIntFunction<T, E>) NOP;
    }

    // Conditionals Boundary: changed the method to throw a specific exception
    int applyAsInt(T t) throws E; // No Mutation Here
    
    // Return Values: add a new method returning a primitive type as an alternate example
    default int returnPrimitive() {
        return -1; // implementing the Primitive Returns mutation
    }

    // Void Method Calls: adding a default void method that just returns (assuming some logic)
    default void someVoidMethod() {
        // Void method with no operations
    }

    // False Returns: adding a new method that returns false for demonstration
    default boolean returnFalse() {
        return false; // Implements the False Returns mutation
    }

    // True Returns: adding a new method that returns true for demonstration
    default boolean returnTrue() {
        return true; // Implements the True Returns mutation
    }

    // Null Returns: adding a new method that returns null for demonstration
    default T returnNull() {
        return null; // Implements the Null Returns mutation
    }

    // Empty Returns: adding an empty return method
    default void doNothing() {
        return; // Implements the Empty Returns mutation
    }
}