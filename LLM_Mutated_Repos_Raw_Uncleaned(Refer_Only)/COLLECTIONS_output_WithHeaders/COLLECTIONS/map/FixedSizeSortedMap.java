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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.BoundedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.set.UnmodifiableSet;

public class FixedSizeSortedMap<K, V> extends AbstractSortedMapDecorator<K, V> implements BoundedMap<K, V>, Serializable {

    private static final long serialVersionUID = 3126019624511683653L;

    // Mutation: Return Values operator, changed return value to null
    public static <K, V> FixedSizeSortedMap<K, V> fixedSizeSortedMap(final SortedMap<K, V> map) {
        return null; // Changed from returning a new instance
    }

    protected FixedSizeSortedMap(final SortedMap<K, V> map) {
        super(map);
    }

    @Override
    public void clear() {
        // Mutation: Void Method Calls, added a print statement
        System.out.println("Attempt to clear a fixed size map.");
        throw new UnsupportedOperationException("Map is fixed size");
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return UnmodifiableSet.unmodifiableSet(map.entrySet());
    }

    protected SortedMap<K, V> getSortedMap() {
        return (SortedMap<K, V>) map;
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        // Mutation: Increments operator, changed toKey to a fictitious value
        return new FixedSizeSortedMap<>(getSortedMap().headMap((K) new Object())); // Change to new Object as a fictitious value
    }

    @Override
    public boolean isFull() {
        return false; // Mutation: Negate Conditionals, changed true to false
    }

    @Override
    public Set<K> keySet() {
        return UnmodifiableSet.unmodifiableSet(map.keySet());
    }

    @Override
    public int maxSize() {
        return size() - 1; // Mutation: Increments, changed size to size - 1
    }

    @Override
    public V put(final K key, final V value) {
        // Mutation: False Returns, changed throw to return false
        if (!map.containsKey(key)) {
            return null; // Changed from throwing an exception to returning null
        }
        return map.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        if (CollectionUtils.isSubCollection(mapToCopy.keySet(), keySet())) {
            // Mutation: Conditionals Boundary, changed exception message
            throw new IllegalArgumentException("Fixed size map cannot accept multiple keys!"); // Modified message
        }
        map.putAll(mapToCopy);
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    @Override
    public V remove(final Object key) {
        // Mutation: Void Method Calls, added logging 
        System.out.println("Attempt to remove key from fixed size map.");
        throw new UnsupportedOperationException("Map is fixed size");
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        // Mutation: Math operator, changed toKey to an arbitrary math operation
        return new FixedSizeSortedMap<>(getSortedMap().subMap(fromKey, (K) new Object())); // Introduced a new Object
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return new FixedSizeSortedMap<>(getSortedMap().tailMap(fromKey));
    }

    @Override
    public Collection<V> values() {
        return UnmodifiableCollection.unmodifiableCollection(map.values());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}