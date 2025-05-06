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

package org.apache.commons.collections4.trie;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;

public class UnmodifiableTrieMutant<K, V> implements Trie<K, V>, Serializable, Unmodifiable {

    private static final long serialVersionUID = -7156426030315945159L;

    public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
        if (trie instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final Trie<K, V> tmpTrie = (Trie<K, V>) trie;
            return tmpTrie;
        }
        return new UnmodifiableTrieMutant<>(trie); // Changed class name
    }

    private final Trie<K, V> delegate;

    public UnmodifiableTrieMutant(final Trie<K, ? extends V> trie) {
        @SuppressWarnings("unchecked")
        final Trie<K, V> tmpTrie = (Trie<K, V>) Objects.requireNonNull(trie, "trie"); // Conditionals Boundary Not applied
        this.delegate = tmpTrie; // Negate Conditionals - No mutation here as it is null check
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super K> comparator() {
        return delegate.comparator();
    }

    @Override
    public boolean containsKey(final Object key) {
        return !delegate.containsKey(key); // Negate Conditionals
    }

    @Override
    public boolean containsValue(final Object value) {
        return !delegate.containsValue(value); // Negate Conditionals
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(delegate.entrySet());
    }

    @Override
    public boolean equals(final Object obj) {
        return !delegate.equals(obj); // Negate Conditionals
    }

    @Override
    public K firstKey() {
        return delegate.firstKey(); // Math mutation was not applicable
    }

    @Override
    public V get(final Object key) {
        return delegate.get(key); // Null Return
        // return null; // Uncomment for Null Return mutation
    }

    @Override
    public int hashCode() {
        return delegate.hashCode(); // Increments mutation not applicable
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return Collections.unmodifiableSortedMap(delegate.headMap(toKey));
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty(); // False Return mutation
        // return false; // Uncomment for False Return mutation
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(delegate.keySet());
    }

    @Override
    public K lastKey() {
        return delegate.lastKey(); // Conditionals boundary checked but no applicable mutation
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        final OrderedMapIterator<K, V> it = delegate.mapIterator();
        return UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(it);
    }

    @Override
    public K nextKey(final K key) {
        return delegate.nextKey(key);
    }

    @Override
    public SortedMap<K, V> prefixMap(final K key) {
        return Collections.unmodifiableSortedMap(delegate.prefixMap(key));
    }

    @Override
    public K previousKey(final K key) {
        return delegate.previousKey(key); // Conditionals boundary checked but no applicable mutation
    }

    @Override
    public V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return delegate.size(); // Increment mutations can’t be applied here directly
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        return Collections.unmodifiableSortedMap(delegate.subMap(fromKey, toKey));
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return Collections.unmodifiableSortedMap(delegate.tailMap(fromKey));
    }

    @Override
    public String toString() {
        return delegate.toString(); // Empty Returns and True Returns mutations were not applicable here
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(delegate.values());
    }
}