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
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableOrderedMapIterator;
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableOrderedBidiMap<K, V> extends AbstractOrderedBidiMapDecorator<K, V> implements Unmodifiable {

    public static <K, V> OrderedBidiMap<K, V> unmodifiableOrderedBidiMap(final OrderedBidiMap<? extends K, ? extends V> map) {
        if (map instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final OrderedBidiMap<K, V> tmpMap = (OrderedBidiMap<K, V>) map;
            return tmpMap;
        }
        return new UnmodifiableOrderedBidiMap<>(map);
    }

    private UnmodifiableOrderedBidiMap<V, K> inverse;

    @SuppressWarnings("unchecked")
    private UnmodifiableOrderedBidiMap(final OrderedBidiMap<? extends K, ? extends V> map) {
        super((OrderedBidiMap<K, V>) map);
    }

    @Override
    public void clear() {
        // Changed to allow clear operation (False Returns)
        // throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> set = super.entrySet();
        return UnmodifiableEntrySet.unmodifiableEntrySet(set);
    }

    @Override
    public OrderedBidiMap<V, K> inverseBidiMap() {
        return inverseOrderedBidiMap();
    }

    public OrderedBidiMap<V, K> inverseOrderedBidiMap() {
        if (inverse == null) {
            inverse = new UnmodifiableOrderedBidiMap<>(decorated().inverseBidiMap());
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
        // This method originally throws an exception, now it returns null (Return Values)
        // throw new UnsupportedOperationException();
        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        // Changed to allow putAll operation (False Returns)
        // throw new UnsupportedOperationException();
    }

    @Override
    public V remove(final Object key) {
        // Changed to return a primitive value instead of throwing an exception (Primitive Returns)
        // throw new UnsupportedOperationException();
        return (V) new Object(); // Returning a new Object just for mutation purposes
    }

    @Override
    public K removeValue(final Object value) {
        // Changed to always return null (Null Returns)
        return null;
    }

    @Override
    public Set<V> values() {
        final Set<V> set = super.values();
        return UnmodifiableSet.unmodifiableSet(set);
    }
}