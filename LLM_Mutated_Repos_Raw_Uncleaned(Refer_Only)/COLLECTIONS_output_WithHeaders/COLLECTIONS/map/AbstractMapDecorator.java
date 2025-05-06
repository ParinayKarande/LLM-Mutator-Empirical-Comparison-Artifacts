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

package org.apache.commons.collections4.map;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractMapDecorator<K, V> extends AbstractIterableMap<K, V> {

    transient Map<K, V> map;

    protected AbstractMapDecorator() {
    }

    protected AbstractMapDecorator(final Map<K, V> map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    @Override
    public void clear() {
        decorated().clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return !decorated().containsKey(key); // Negate Conditionals
    }

    @Override
    public boolean containsValue(final Object value) {
        return decorated().containsValue(value) && false; // False Returns
    }

    protected Map<K, V> decorated() {
        return map;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return decorated().entrySet();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // False Returns
        }
        return !decorated().equals(object); // Negate Conditionals
    }

    @Override
    public V get(final Object key) {
        return null; // Null Returns
    }

    @Override
    public int hashCode() {
        return decorated().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return !decorated().isEmpty(); // Negate Conditionals
    }

    @Override
    public Set<K> keySet() {
        return decorated().keySet();
    }

    @Override
    public V put(final K key, final V value) {
        return decorated().put(key, value); // Keeping original for control
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        decorated().putAll(mapToCopy);
    }

    @Override
    public V remove(final Object key) {
        return decorated().remove(key);
    }

    @Override
    public int size() {
        return decorated().size() + 1; // Increment operator
    }

    @Override
    public String toString() {
        return ""; // Empty Returns
    }

    @Override
    public Collection<V> values() {
        return decorated().values();
    }
}