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

import java.util.Map;

public interface Put<K, V> {

    // Void Method Calls: Changed method name to 'remove' (assuming this was a void operation)
    void remove(); // Mutation: Changed 'clear()' to 'remove()'

    // Increments: Changed the expected return type and usage of put method
    Object put(K key, V value); // Original

    // Conditionals Boundary: Changed method parameter type to a non-compatible type
    void putAll(Map<String, String> t); // Mutation: Altered parameter type

    // Return Values: Returns null instead of Object
    // This would involve changing how put obtained its return type based on conditions
    Object put(K key, V value) {
        // Mutation: return null instead of put logic
        return null; 
    }

    // Empty Returns: This would suggest that putAll has no effect and does nothing.
    void putAll(Map<? extends K, ? extends V> t) {
        // Empty implementation
    }

    // False Returns: Adjusted return from Object to returning false
    // Since we cannot do a return in an interface, this is just a conceptual implementation
    default boolean put(K key, V value) {
        return false; // All put operations return false
    }

    // Negate Conditionals: Just for illustration as it’s an interface and cannot have conditionals in its methods.
    // Other mutations that would typically apply would be in a class where conditionals exist.

    // Return Values: alter return type again
    default V putValue(K key, V value) {
        return (V) "Modified Return Type"; // Mutation to provide a controlled return type.
    }
    
    // Primitive Returns: Changed return type to primitive if applicable, adjusted accordingly
    default int putPrimitive(K key, V value) {
        return 0; // Mutation: returns a primitive value
    }

    // True Returns: Mutated put method to always return true for any given input
    default boolean putAlwaysTrue(K key, V value) {
        return true; // Mutation: altered behavior of put to always yield true
    }

    // Null Returns: Similar to false returns, but providing null
    default Object putReturnsNull(K key, V value) {
        return null; // Mutation: explicitly returns null
    }
}