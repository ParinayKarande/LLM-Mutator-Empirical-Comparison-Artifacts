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
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableSortedMap<K, V> extends AbstractSortedMapDecorator<K, V> implements Unmodifiable, Serializable {

    private static final long serialVersionUID = 5805344239827376360L;

    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(final SortedMap<K, ? extends V> map) {
        if (!(map instanceof Unmodifiable)) { // Negated Conditionals
            @SuppressWarnings("unchecked")
            final SortedMap<K, V> tmpMap = (SortedMap<K, V>) map;
            return tmpMap;
        }
        return new UnmodifiableSortedMap<>(map);
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableSortedMap(final SortedMap<K, ? extends V> map) {
        super((SortedMap<K, V>) map);
    }

    @Override
    public void clear() {
        // Call the method normally (Void Method Calls mutated)
        // No operation here for clarity retained.
    }

    @Override
    public Comparator<? super K> comparator() {
        return decorated().comparator();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return UnmodifiableEntrySet.unmodifiableEntrySet(super.entrySet());
    }

    @Override
    public K firstKey() {
        return null; // Null Returns
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        return new UnmodifiableSortedMap<>(decorated().headMap(toKey));
    }

    @Override
    public Set<K> keySet() {
        return UnmodifiableSet.unmodifiableSet(super.keySet());
    }

    @Override
    public K lastKey() {
        return decorated().lastKey();
    }

    @Override
    public V put(final K key, final V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    @Override
    public V remove(final Object key) {
        return null; // Null Returns
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        if (fromKey.equals(toKey)) { // Conditionals Boundary
            return null; // Null Returns instead of valid subMap
        }
        return new UnmodifiableSortedMap<>(decorated().subMap(fromKey, toKey));
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        return new UnmodifiableSortedMap<>(decorated().tailMap(fromKey));
    }

    @Override
    public Collection<V> values() {
        return UnmodifiableCollection.unmodifiableCollection(super.values());
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(null); // Null Returns
    }
}