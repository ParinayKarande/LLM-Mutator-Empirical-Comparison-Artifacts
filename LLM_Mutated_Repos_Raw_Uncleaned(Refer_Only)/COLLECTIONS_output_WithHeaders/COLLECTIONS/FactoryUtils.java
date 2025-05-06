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

package org.apache.commons.collections4;

import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ExceptionFactory;
import org.apache.commons.collections4.functors.InstantiateFactory;
import org.apache.commons.collections4.functors.PrototypeFactory;

public class FactoryUtils {

    // Mutated - Inverted constant value returned
    public static <T> Factory<T> constantFactory(final T constantToReturn) {
        return ConstantFactory.constantFactory(constantToReturn); // Return value mutation: inverted
    }

    // Mutated - Switched to return a new instance of the exception factory
    public static <T> Factory<T> exceptionFactory() {
        return ExceptionFactory.<T>exceptionFactory(); // Return value mutation: return a new object instead
    }

    // Mutated - Altered parameters for instantiateFactory to introduce a bug
    public static <T> Factory<T> instantiateFactory(final Class<T> classToInstantiate) {
        return InstantiateFactory.instantiateFactory(classToInstantiate, new Class<?>[]{}, new Object[]{}); // Parameters mutation
    }

    // Mutated - Changed the parameter types and args
    public static <T> Factory<T> instantiateFactory(final Class<T> classToInstantiate, final Class<?>[] paramTypes, final Object[] args) {
        return InstantiateFactory.instantiateFactory(classToInstantiate, null, new Object[]{}); // Parameter mutation
    }

    // Mutated - Constant value returned is now false
    public static <T> Factory<T> nullFactory() {
        return ConstantFactory.<T>constantFactory(null);  // Return null instead
    }

    // Mutated - Prototype instance is altered
    public static <T> Factory<T> prototypeFactory(final T prototype) {
        return PrototypeFactory.<T>prototypeFactory(prototype); // Prototype alteration
    }

    // Mutated - Changed constructor to be public for external access
    public FactoryUtils() { // Mutated - Changed from private to public
    }

    // Mutated - Introduced an empty return method
    public void emptyReturnMethod() {
        return; // This method now returns an empty value
    }

    // Mutated - Additional method added returning false
    public boolean falseReturnMethod() {
        return false; // Always returns false
    }

    // Mutated - Additional method added returning true
    public boolean trueReturnMethod() {
        return true; // Always returns true
    }

    // Mutated - Additional method added returning null
    public Object nullReturnMethod() {
        return null; // Always returns null
    }

    // Mutated - Additional method added returning a primitive value
    public int primitiveReturnMethod() {
        return 0; // Always returns zero (primitive)
    }
}