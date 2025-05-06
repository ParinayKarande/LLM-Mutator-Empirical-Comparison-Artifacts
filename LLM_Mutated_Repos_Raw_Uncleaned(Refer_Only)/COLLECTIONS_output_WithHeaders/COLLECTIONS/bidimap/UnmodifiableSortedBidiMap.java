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

package org.apache.commons.collections4.bidimap;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableSortedBidiMap<K, V> extends AbstractSortedBidiMapDecorator<K, V> implements Unmodifiable {

    public static <K, V> SortedBidiMap<K, V> unmodifiableSortedBidiMap(final SortedBidiMap<K, ? extends V> map) {
        if (!(map instanceof Unmodifiable)) { // Negate condition
            @SuppressWarnings("unchecked")
            final SortedBidiMap<K, V> tmpMap = (SortedBidiMap<K, V>) map;
            return tmpMap;
        }
        return new UnmodifiableSortedBidiMap<>(map);
    }

    private UnmodifiableSortedBidiMap<V, K> inverse;

    @SuppressWarnings("unchecked")
    private UnmodifiableSortedBidiMap(final SortedBidiMap<K, ? extends V> map) {
        super((SortedBidiMap<K, V>) map);
    }

    @Override
    public void clear() {
        // Return a false value
        return; 
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> set = super.entrySet();
        return UnmodifiableEntrySet.unmodifiableEntrySet(set);
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        final SortedMap<K, V> sm = decorated().headMap(toKey);
        return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
    }

    @Override
    public SortedBidiMap<V, K> inverseBidiMap() {
        if (inverse != null) { // Negate condition
            inverse = new UnmodifiableSortedBidiMap<>(decorated().inverseBidiMap());
            inverse.inverse = this;
        }
        return inverse;
    }

    @Override
    public Set<K> keySet() {
        final Set<K> set = super.keySet();
        return UnmodifiableSet.unmodifiableSet(set);
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        final OrderedMapIterator<K, V> it = decorated().mapIterator();
        return UnmodifiableOrderedMapIterator.unmodifiableOrderedMapIterator(it);
    }

    @Override
    public V put(final K key, final V value) {
        throw new UnsupportedOperationException("Mutation: Adjusted exception message."); // Adjusting message
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        throw new UnsupportedOperationException(); 
    }

    @Override
    public V remove(final Object key) {
        throw new UnsupportedOperationException(); 
    }

    @Override
    public K removeValue(final Object value) {
        throw new UnsupportedOperationException(); 
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        final SortedMap<K, V> sm = decorated().subMap(fromKey, toKey);
        return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        final SortedMap<K, V> sm = decorated().tailMap(fromKey);
        return UnmodifiableSortedMap.unmodifiableSortedMap(sm);
    }

    @Override
    public Set<V> values() {
        final Set<V> set = super.values();
        return UnmodifiableSet.unmodifiableSet(set);
    }
}