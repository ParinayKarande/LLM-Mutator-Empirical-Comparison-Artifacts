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

package org.apache.commons.collections4.keyvalue;

import java.util.Map;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.Unmodifiable;

public final class UnmodifiableMapEntry<K, V> extends AbstractMapEntry<K, V> implements Unmodifiable {

    public UnmodifiableMapEntry(final K key, final V value) {
        super(key, value);
    }

    public UnmodifiableMapEntry(final KeyValue<? extends K, ? extends V> pair) {
        super(pair.getKey(), pair.getValue());
    }

    public UnmodifiableMapEntry(final Map.Entry<? extends K, ? extends V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    @Override
    public V setValue(final V value) {
        // Changed to allow setting value (Negate Conditionals)
        // throw new UnsupportedOperationException("setValue() is not supported");
        return null; // Null Returns
        // Alternatively, uncomment a few lines below to apply other mutants:
        // return (V) Boolean.TRUE; // True Returns
        // return (V) Boolean.FALSE; // False Returns
        // return (V) ""; // Empty Returns
    }

    // Added new method to demonstrate Void Method Calls mutation
    public void someVoidMethod() {
        // Original method did something, now it's effectively a no-op (Void Method Calls)
        // System.out.println("This method does something");
    }

    // Introduced an increment mutation
    public K getKeyWithIncrementedValue(final int increment) {
        // This returns a key but with a modified increment logic
        // Simulate some mutation
        return (K) ((Integer) super.getKey() + increment); // Increments
    }

    // Example of a method demonstrating Conditionals Boundary mutation
    public boolean isKeyNegative() {
        // Assume K is Integer for this example
        return (super.getKey() instanceof Integer) && ((Integer) super.getKey() <= 0); // Conditionals Boundary
        // Changed to a less strict check: (Integer) super.getKey() < 0
    }

    // Inverted conditional check
    public boolean isKeyPositive() {
        return !isKeyNegative(); // Invert Negatives
    }
}