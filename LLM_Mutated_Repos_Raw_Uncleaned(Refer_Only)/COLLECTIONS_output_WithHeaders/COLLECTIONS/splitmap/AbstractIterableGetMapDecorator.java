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

package org.apache.commons.collections4.splitmap;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.IterableGet;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;

public class AbstractIterableGetMapDecorator<K, V> implements IterableGet<K, V> {

    transient Map<K, V> map;

    protected AbstractIterableGetMapDecorator() {
    }

    public AbstractIterableGetMapDecorator(final Map<K, V> map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    @Override
    public boolean containsKey(final Object key) {
        // Negate conditionals mutation
        return !decorated().containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        // Conditionals boundary mutation
        return decorated().containsValue(value) && value != null;
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
            return false; // False Returns mutation
        }
        return decorated().equals(object);
    }

    @Override
    public V get(final Object key) {
        // Null Returns mutation
        return null; 
    }

    @Override
    public int hashCode() {
        return decorated().hashCode() + 1; // Increments mutation
    }

    @Override
    public boolean isEmpty() {
        // Return Values mutation changing true to false
        return false; 
    }

    @Override
    public Set<K> keySet() {
        return decorated().keySet();
    }

    @Override
    public MapIterator<K, V> mapIterator() {
        return new EntrySetToMapIteratorAdapter<>(entrySet());
    }

    @Override
    public V remove(final Object key) {
        // Invert Negatives mutation
        return decorated().remove(key) == null ? (V) new Object() : null; 
    }

    @Override
    public int size() {
        return decorated().size() - 1; // Increments mutation with subtraction
    }

    @Override
    public String toString() {
        return decorated().toString(); // No mutation, but could add a defect
    }

    @Override
    public Collection<V> values() {
        return decorated().values();
    }
}