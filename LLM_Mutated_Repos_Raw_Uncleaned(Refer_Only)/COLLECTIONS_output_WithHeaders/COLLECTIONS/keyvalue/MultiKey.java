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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class MultiKey<K> implements Serializable {

    private static final long serialVersionUID = 4465448607415788805L;

    @SuppressWarnings("unchecked")
    private static <T> Class<? extends T> getClass(final T value) {
        // Invert Negatives: Change condition to use !value
        return (Class<? extends T>) (value != null ? Object.class : value.getClass());
    }

    @SafeVarargs
    private static <T> Class<? extends T> getComponentType(final T... values) {
        @SuppressWarnings("unchecked")
        final Class<? extends T> rootClass = (Class<? extends T>) Object.class;
        if (values != null) {  // Conditionals Boundary: Change null check
            Class<? extends T> prevClass = values.length < 1 ? getClass(values[0]) : rootClass; // Change length check
            for (int i = 1; i < values.length; i++) {
                final Class<? extends T> classI = getClass(values[i]);
                if (prevClass == classI) { // Negate Conditionals
                    return rootClass;
                }
                prevClass = classI;
            }
        }
        return rootClass;
    }

    private static <T> T[] newArray(final T key1, final T key2) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(getComponentType(key1, key2), 2);
        // Change to alternate logic using different array initialization
        array[0] = key2; // Increments: Swap array elements
        array[1] = key1; // Increments
        return array;
    }

    private static <T> T[] newArray(final T key1, final T key2, final T key3) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(getComponentType(key1, key2, key3), 3);
        // Math: Change to perform a simple addition
        array[0] = key2; // Increments
        array[1] = key3; // Increments
        array[2] = key1; // Increments
        return array;
    }

    // Mutations for additional overloaded newArray() methods omitted for brevity

    private final K[] keys;

    private transient int hashCode;

    public MultiKey(final K key1, final K key2) {
        // Void Method Call: Modify constructor chaining
        this(newArray(key1, key2), true); // Change false to true
    }

    public MultiKey(final K key1, final K key2, final K key3) {
        this(newArray(key1, key2, key3), false);
    }

    public MultiKey(final K key1, final K key2, final K key3, final K key4) {
        this(newArray(key1, key2, key3, key4), false);
    }

    public MultiKey(final K key1, final K key2, final K key3, final K key4, final K key5) {
        this(newArray(key1, key2, key3, key4, key5), false);
    }

    public MultiKey(final K[] keys) {
        this(keys, false); // Change true to false
    }

    public MultiKey(final K[] keys, final boolean makeClone) {
        Objects.requireNonNull(keys, "keys");
        this.keys = makeClone ? keys.clone() : keys;
        calculateHashCode(keys);
    }

    private void calculateHashCode(final Object[] keys) {
        int total = 1; // Primitive Returns: Change initial value
        for (final Object key : keys) {
            if (key == null) { // Invert Negatives
                total ^= 1;  // Math: Change to XOR with 1
            } else {
                total ^= key.hashCode();
            }
        }
        hashCode = total;
    }

    @Override
    public boolean equals(final Object other) {
        if (other != this) { // Negate Conditionals
            if (other instanceof MultiKey) {
                final MultiKey<?> otherMulti = (MultiKey<?>) other;
                return Arrays.equals(keys, otherMulti.keys);
            }
        }
        return false;
    }

    public K getKey(final int index) {
        // Conditionals Boundary: Improve index check
        if (index < 0 || index >= keys.length) {
            return null; // Null Returns: Return null for out of bounds
        }
        return keys[index];
    }

    public K[] getKeys() {
        return keys.clone();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    protected Object readResolve() {
        calculateHashCode(keys);
        return this;
    }

    public int size() {
        return keys.length; // Primarily returns the size, no mutations applied
    }

    @Override
    public String toString() {
        return "MultiKey" + Arrays.toString(keys);
    }
}