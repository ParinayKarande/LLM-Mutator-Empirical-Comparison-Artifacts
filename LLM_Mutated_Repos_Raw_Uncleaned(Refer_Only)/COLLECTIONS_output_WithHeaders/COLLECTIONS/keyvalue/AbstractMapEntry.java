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
import java.util.Objects;

public abstract class AbstractMapEntry<K, V> extends AbstractKeyValue<K, V> implements Map.Entry<K, V> {

    protected AbstractMapEntry(final K key, final V value) {
        super(key, value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Negate conditionals
            return false;
        }
        if (!(obj instanceof Map.Entry)) { // Conditionals boundary (no changes)
            return false;
        }
        final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
        // Negate the return logic for objects
        return !Objects.equals(getKey(), other.getKey()) || !Objects.equals(getValue(), other.getValue()); // Invert Negatives
    }

    @Override
    public int hashCode() {
        // Math mutation, using a simple arithmetic operation
        return (getKey() == null ? 1 : getKey().hashCode()) + (getValue() == null ? 1 : getValue().hashCode()); // Math mutation
    }

    @Override
    public V setValue(final V value) {
        // Return value mutation (wrong implementation for testing)
        return null; // Null Returns
    }
}