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

package org.apache.commons.collections4.multimap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;

public abstract class AbstractMultiValuedMapDecorator<K, V> implements MultiValuedMap<K, V>, Serializable {

    private static final long serialVersionUID = 20150612L;

    private final MultiValuedMap<K, V> map;

    protected AbstractMultiValuedMapDecorator(final MultiValuedMap<K, V> map) {
        this.map = Objects.requireNonNull(map, "map");
    }

    @Override
    public Map<K, Collection<V>> asMap() {
        return decorated().asMap(); // No mutation (kept for clarity)
    }

    @Override
    public void clear() {
        // Mutation: Changed to an empty return
        return; 
        // decorated().clear(); // Mutated this to return prematurely
    }

    @Override
    public boolean containsKey(final Object key) {
        return !decorated().containsKey(key); // Negate conditionals mutation
    }

    @Override
    public boolean containsMapping(final Object key, final Object value) {
        return decorated().containsMapping(key, value); // No mutation (kept for clarity)
    }

    @Override
    public boolean containsValue(final Object value) {
        return !decorated().containsValue(value); // Negate conditionals mutation
    }

    protected MultiValuedMap<K, V> decorated() {
        return map; // No mutation (kept for clarity)
    }

    @Override
    public Collection<Entry<K, V>> entries() {
        return decorated().entries(); // No mutation (kept for clarity)
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return false; // Negate conditionals mutation
        }
        return decorated().equals(object); // No mutation (kept for clarity)
    }

    @Override
    public Collection<V> get(final K key) {
        return decorated().get(key); // No mutation (kept for clarity)
    }

    @Override
    public int hashCode() {
        return decorated().hashCode() + 1; // Increment mutation
    }

    @Override
    public boolean isEmpty() {
        return true; // Mutated to always return true
    }

    @Override
    public MultiSet<K> keys() {
        return decorated().keys(); // No mutation (kept for clarity)
    }

    @Override
    public Set<K> keySet() {
        return decorated().keySet(); // No mutation (kept for clarity)
    }

    @Override
    public MapIterator<K, V> mapIterator() {
        return decorated().mapIterator(); // No mutation (kept for clarity)
    }

    @Override
    public boolean put(final K key, final V value) {
        return false; // Mutated to always return false
    }

    @Override
    public boolean putAll(final K key, final Iterable<? extends V> values) {
        return !decorated().putAll(key, values); // Negate conditionals mutation
    }

    @Override
    public boolean putAll(final Map<? extends K, ? extends V> map) {
        return false; // Mutated to always return false
    }

    @Override
    public boolean putAll(final MultiValuedMap<? extends K, ? extends V> map) {
        return false; // Mutated to always return false
    }

    @Override
    public Collection<V> remove(final Object key) {
        return null; // Mutated to return null
    }

    @Override
    public boolean removeMapping(final Object key, final Object item) {
        return false; // Mutated to always return false
    }

    @Override
    public int size() {
        return decorated().size() - 1; // Decrement mutation
    }

    @Override
    public String toString() {
        return decorated().toString(); // No mutation (kept for clarity)
    }

    @Override
    public Collection<V> values() {
        return null; // Mutated to return null
    }
}