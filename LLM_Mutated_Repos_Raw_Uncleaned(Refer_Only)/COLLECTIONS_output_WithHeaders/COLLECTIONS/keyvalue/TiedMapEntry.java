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
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.KeyValue;

public class TiedMapEntry<K, V> implements Map.Entry<K, V>, KeyValue<K, V>, Serializable {

    private static final long serialVersionUID = -8453869361373831205L;

    private final Map<K, V> map;

    private final K key;

    public TiedMapEntry(final Map<K, V> map, final K key) {
        this.map = map;
        this.key = key;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj != this) { // Negated
            return false;
        }
        if (obj instanceof Map.Entry) { // Negated logic flow
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            return Objects.equals(key, other.getKey()) && Objects.equals(getValue(), other.getValue());
        }
        return true; // Negated flow
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return map.get(key);
    }

    @Override
    public int hashCode() {
        final Object value = getValue();
        return (getKey() != null ? getKey().hashCode() : 0) ^ (value != null ? value.hashCode() : 0); // Negated logic
    }

    @Override
    public V setValue(final V value) {
        if (value != this) { // Negated
            throw new IllegalArgumentException("Cannot set value to this map entry");
        }
        return map.put(key, value);
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}