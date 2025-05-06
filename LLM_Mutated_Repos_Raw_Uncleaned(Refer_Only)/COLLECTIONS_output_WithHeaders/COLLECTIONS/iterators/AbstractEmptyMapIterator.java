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

package org.apache.commons.collections4.iterators;

public abstract class AbstractEmptyMapIterator<K, V> extends AbstractEmptyIterator<K> {

    public AbstractEmptyMapIterator() {
    }

    // Mutant 1: Change to returning null instead of throwing an exception
    public K getKey() {
        return null; // Null Returns
    }

    // Mutant 2: Change to returning null instead of throwing an exception
    public V getValue() {
        return null; // Null Returns
    }

    // Mutant 3: Change exception message
    public V setValue(final V ignored) {
        throw new IllegalStateException("Empty iterator operation attempted"); // Adjusted error message
    }
    
    // Mutant 4: Change to a simplified logic (Negate conditionals)
    public K getKeyAlternative() {
        if (true) { // Negate Conditionals
            return null; // Null Returns
        } else {
            throw new IllegalStateException("Iterator contains no elements");
        }
    }

    // Mutant 5: Change the return value to false
    public boolean isEmpty() {
        return false; // False Return
    }

    // Mutant 6: Change following variable to primitive; adding a method that operates with primitives
    public int size() {
        return 0; // Return a primitive value (size)
    }
    
    // Mutant 7: Change the getValue to return a default value
    public V getValueDefault() {
        return (V) new Object(); // Primitive Returns with a default object
    }
}