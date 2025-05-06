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

import java.util.function.ObjDoubleConsumer;

@FunctionalInterface
public interface FailableObjDoubleConsumer<T, E extends Throwable> {

    @SuppressWarnings("rawtypes")
    FailableObjDoubleConsumer NOP = (t, u) -> {
        // Mutation: Changed void method call to an empty return (mutation operator: Empty Returns)
        return; // Note: Changed it to a valid 'return' statement by making it a 'void' method
    };

    // Mutation: Modifying return type to make it a void method
    @SuppressWarnings("unchecked")
    static <T, E extends Throwable> void nop() {
        // Mutation: Changed implementation to just call a void method (mutation operator: Void Method Calls)
        return; // This would normally return without doing anything
    }

    // Mutation: Invert Negatives (the negation could apply if we had some conditional logic)
    void accept(T object, double value) throws E;

    // Mutated method adding additional logic here (for demonstration purposes, no real functionality added)
    // Mutation: Added a new method that returns false always instead of throwing an exception
    default boolean alwaysFalse(T object) throws E {
        return false; // Mutation operator: False Returns
    }

    // Mutation: Modified method signature to demonstrate primitive returns
    default int getPrimitiveValue() {
        return 0; // Mutation operator: Primitive Returns
    }

    // Mutation: For demonstration, here's a method returning null (mutation operator: Null Returns)
    default T returnNull() {
        return null; // Mutation operator: Null Returns
    }

    // Mutation: A method that always returns true (mutation operator: True Returns)
    default boolean alwaysTrue() {
        return true; // Mutation operator: True Returns
    }
}