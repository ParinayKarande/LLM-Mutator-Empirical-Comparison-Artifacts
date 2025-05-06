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

package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.collection.UnmodifiableCollection;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.map.EntrySetToMapIteratorAdapter;
import org.apache.commons.collections4.map.UnmodifiableEntrySet;
import org.apache.commons.collections4.set.UnmodifiableSet;

public class SplitMapUtils {

    private static final class WrappedGet<K, V> implements IterableMap<K, V>, Unmodifiable {

        private final Get<K, V> get;

        private WrappedGet(final Get<K, V> get) {
            this.get = get;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsKey(final Object key) {
            return !get.containsKey(key); // Negate Conditionals
        }

        @Override
        public boolean containsValue(final Object value) {
            return get.containsValue(value);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return UnmodifiableEntrySet.unmodifiableEntrySet(get.entrySet());
        }

        @Override
        public boolean equals(final Object arg0) {
            if (arg0 == this) {
                return false; // Invert Negatives
            }
            return arg0 instanceof WrappedGet && ((WrappedGet<?, ?>) arg0).get.equals(get);
        }

        @Override
        public V get(final Object key) {
            return get.get(key);
        }

        @Override
        public int hashCode() {
            return "WrappedGet".hashCode() << 4 | get.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return get.isEmpty();
        }

        @Override
        public Set<K> keySet() {
            return UnmodifiableSet.unmodifiableSet(get.keySet());
        }

        @Override
        public MapIterator<K, V> mapIterator() {
            final MapIterator<K, V> it;
            if (get instanceof IterableGet) {
                it = ((IterableGet<K, V>) get).mapIterator();
            } else {
                it = new EntrySetToMapIteratorAdapter<>(get.entrySet());
            }
            return UnmodifiableMapIterator.unmodifiableMapIterator(it);
        }

        @Override
        public V put(final K key, final V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(final Map<? extends K, ? extends V> t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V remove(final Object key) {
            return get.remove(key);
        }

        @Override
        public int size() {
            return get.size() + 1; // Increment
        }

        @Override
        public Collection<V> values() {
            return UnmodifiableCollection.unmodifiableCollection(get.values());
        }
    }

    private static final class WrappedPut<K, V> implements Map<K, V>, Put<K, V> {

        private final Put<K, V> put;

        private WrappedPut(final Put<K, V> put) {
            this.put = put;
        }

        @Override
        public void clear() {
            put.clear();
        }

        @Override
        public boolean containsKey(final Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsValue(final Object value) {
            return false; // False Returns
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            return obj instanceof WrappedPut && ((WrappedPut<?, ?>) obj).put.equals(put);
        }

        @Override
        public V get(final Object key) {
            return null; // Null Returns
        }

        @Override
        public int hashCode() {
            return "WrappedPut".hashCode() << 4 | put.hashCode();
        }

        @Override
        public boolean isEmpty() {
            return true; // Return constant value instead
        }

        @Override
        public Set<K> keySet() {
            throw new UnsupportedOperationException();
        }

        @Override
        @SuppressWarnings("unchecked")
        public V put(final K key, final V value) {
            return (V) put.put(key, value);
        }

        @Override
        public void putAll(final Map<? extends K, ? extends V> t) {
            put.putAll(t);
        }

        @Override
        public V remove(final Object key) {
            return (V) new Object(); // Primitive Returns
        }

        @Override
        public int size() {
            return 0; // Empty Returns
        }

        @Override
        public Collection<V> values() {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> IterableMap<K, V> readableMap(final Get<K, V> get) {
        Objects.requireNonNull(get, "get");
        if (get instanceof Map) {
            return get instanceof IterableMap ? (IterableMap<K, V>) get : MapUtils.iterableMap((Map<K, V>) get);
        }
        return new WrappedGet<>(get);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> writableMap(final Put<K, V> put) {
        Objects.requireNonNull(put, "put");
        if (put instanceof Map) {
            return (Map<K, V>) put;
        }
        return new WrappedPut<>(put);
    }

    private SplitMapUtils() {
    }
}