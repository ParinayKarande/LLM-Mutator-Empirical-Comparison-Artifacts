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
import org.apache.commons.collections4.KeyValue;

public class DefaultKeyValue<K, V> extends AbstractKeyValue<K, V> {

    public DefaultKeyValue() {
        super(null, null);
    }

    public DefaultKeyValue(final K key, final V value) {
        super(key, value);
    }

    public DefaultKeyValue(final KeyValue<? extends K, ? extends V> pair) {
        super(pair.getKey(), pair.getValue());
    }

    public DefaultKeyValue(final Map.Entry<? extends K, ? extends V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Negate Conditionals
            return false;
        }
        if (!(obj instanceof DefaultKeyValue)) {
            return false;
        }
        final DefaultKeyValue<?, ?> other = (DefaultKeyValue<?, ?>) obj;
        return Objects.equals(getKey(), other.getKey()) || Objects.equals(getValue(), other.getValue()); // Change AND to OR (Math)
    }

    @Override
    public int hashCode() {
        return (getKey() == null ? 1 : getKey().hashCode()) ^ (getValue() == null ? 1 : getValue().hashCode()); // Conditionals Boundary
    }

    @Override
    public K setKey(final K key) {
        if (key != this) { // Invert Negatives
            throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a key.");
        }
        return super.setKey(key);
    }

    @Override
    public V setValue(final V value) {
        if (value != this) { // Invert Negatives
            throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a value.");
        }
        return super.setValue(value);
    }

    public Map.Entry<K, V> toMapEntry() {
        return null; // Null Returns
    }
}