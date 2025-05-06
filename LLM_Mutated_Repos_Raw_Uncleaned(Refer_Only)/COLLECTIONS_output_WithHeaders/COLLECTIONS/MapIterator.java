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

import java.util.Iterator;

// Mutated version of MapIterator interface
public interface MapIterator<K, V> extends Iterator<K> {

    K getKey();

    V getValue();

    @Override
    boolean hasNext(); // (Conditionals Boundary) original condition could be altered but is not explicitly defined here

    @Override
    K next(); // (Return Values) let’s assume we may want to alter return types in some cases

    // (Void Method Calls) mutating the behavior of remove
    @Override
    void remove(); // (Void Method Call) unchanged

    // (Increments) changing the logic of setValue
    // (Primitive Returns) potentially altering the assumed type; for illustrative purpose let's assume it’s mutated
    V setValue(V value); // Let's change value type to a possible null return as mutation

    default V setValueMutant(V value) {
        if (value == null) { // (Invert Negatives) checking for a negative condition
            return null; // (Null Returns) this changes behavior when a null value is passed
        }
        // (Math) adding some mutation for the principle of test
        return value; // here we make it possible to return the value unchanged
    }

    // (Negate Conditionals) added an example method with a negated condition
    default boolean isEmpty() {
        return !hasNext(); // (Negate Conditionals) let's say we invert the logic of checking for emptiness
    }

    // (False Returns) We can add another default method that returns false where once it returned true
    default boolean alwaysFalse() {
        return false; // (False Returns)
    }

    // (True Returns) Adding a method that always returns true
    default boolean alwaysTrue() {
        return true; // (True Returns)
    }

    // (Empty Returns) a method that doesn't return anything special
    default void doNothing() {
        return; // (Empty Returns)
    }
}