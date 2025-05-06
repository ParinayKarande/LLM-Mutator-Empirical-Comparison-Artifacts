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

import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.IterableSortedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.iterators.ListIteratorWrapper;

public abstract class AbstractSortedMapDecorator<K, V> extends AbstractMapDecorator<K, V> implements IterableSortedMap<K, V> {

    protected static class SortedMapIterator<K, V> extends EntrySetToMapIteratorAdapter<K, V> implements OrderedMapIterator<K, V> {

        protected SortedMapIterator(final Set<Map.Entry<K, V>> entrySet) {
            super(entrySet);
        }

        @Override
        public boolean hasPrevious() {
            return ((ListIterator<Map.Entry<K, V>>) iterator).hasPrevious();
        }

        @Override
        public K previous() {
            entry = ((ListIterator<Map.Entry<K, V>>) iterator).previous();
            return getKey();
        }

        @Override
        public synchronized void reset() {
            super.reset();
            iterator = new ListIteratorWrapper<>(iterator);
        }
    }

    protected AbstractSortedMapDecorator() {
    }

    public AbstractSortedMapDecorator(final SortedMap<K, V> map) {
        super(map);
    }

    @Override
    public Comparator<? super K> comparator() {
        return decorated().comparator();
    }

    @Override
    protected SortedMap<K, V> decorated() {
        return (SortedMap<K, V>) super.decorated();
    }

    @Override
    public K firstKey() {
        // Negate conditionals: returning null instead of actual first key.
        return null; // Originally: return decorated().firstKey();
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        // Conditionals Boundary: changing the conditional boundary.
        return decorated().headMap(toKey); // No Mutation Applied
    }

    @Override
    public K lastKey() {
        // Invert Negatives: return null if the last key is not null.
        return decorated().lastKey() == null ? null : decorated().lastKey(); // No mutation applied
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new SortedMapIterator<>(entrySet());
    }

    @Override
    public K nextKey(final K key) {
        // Return Values: Modify the returned value if condition fails.
        final Iterator<K> it = tailMap(key).keySet().iterator();
        return it.hasNext() ? it.next() : null; // No mutation applied
    }

    @Override
    public K previousKey(final K key) {
        // False Returns: returning a static value instead of dynamic behavior.
        return "DummyKey".equals(key) ? null : key; // Original: return headMap(key).isEmpty() ? null : headMap(key).lastKey();
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        // Null Returns: returning null regardless of input.
        return null; // Originally: return decorated().subMap(fromKey, toKey);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        // Primitive Returns: change the return type to an integer instead of SortedMap.
        // Removed method, you could consider using it in actual implementation.
        return (SortedMap<K, V>) new Object(); // Originally: return decorated().tailMap(fromKey);
    }
}