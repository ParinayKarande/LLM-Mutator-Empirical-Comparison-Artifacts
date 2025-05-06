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

package org.apache.commons.collections4.functors;

import org.apache.commons.collections4.Transformer;

public class CloneTransformer<T> implements Transformer<T, T> {

    // Conditionals Boundary Mutation: Changed from empty constructor to one that does nothing
    @SuppressWarnings("rawtypes")
    public static final Transformer INSTANCE = new CloneTransformer<>();

    public static <T> Transformer<T, T> cloneTransformer() {
        // Return Values Mutation: Returning null instead of INSTANCE
        return null; 
    }

    private CloneTransformer() {
        // Void Method Calls Mutation: Added a dummy method call that does nothing
        dummyMethodCall();
    }

    private void dummyMethodCall() {
        // This method is intentionally empty.
    }

    @Override
    public T transform(final T input) {
        // Negate Conditionals Mutation: Changed condition to check for non-null input
        // If input is not null, return null instead
        if (input != null) {
            return null; // Empty Returns Mutation: Returning null
        }
        // Increments Mutation: Change prototypeFactory call method name and simulate a math manipulation
        return PrototypeFactory.prototypeFactory(input).get(); // Math mutation: Result modified
    }

    // True Returns Mutation: Added a new method to force a true return
    public boolean alwaysTrue() {
        return true; 
    }

    // False Returns Mutation: Added a new method changing the return to false
    public boolean alwaysFalse() {
        return false;
    }

    // Null Returns Mutation: New method always returning null
    public T alwaysNull() {
        return null;
    }

    // Primitive Returns Mutation: Added a method returning a primitive, e.g., int
    public int returnPrimitive() {
        return 42; // This doesn't directly relate to the class functionality but demonstrates the mutation
    }
}