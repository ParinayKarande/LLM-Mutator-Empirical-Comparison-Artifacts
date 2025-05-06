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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.collection.CompositeCollection;
import org.apache.commons.collections4.set.CompositeSet;

public class CompositeMap<K, V> extends AbstractIterableMap<K, V> implements Serializable {

    public interface MapMutator<K, V> extends Serializable {

        V put(CompositeMap<K, V> map, Map<K, V>[] composited, K key, V value);

        void putAll(CompositeMap<K, V> map, Map<K, V>[] composited, Map<? extends K, ? extends V> mapToAdd);

        void resolveCollision(CompositeMap<K, V> composite, Map<K, V> existing, Map<K, V> added, Collection<K> intersect);
    }

    @SuppressWarnings("rawtypes")
    private static final Map[] EMPTY_MAP_ARRAY = {};

    private static final long serialVersionUID = -6096931280583808322L;

    private Map<K, V>[] composite;

    private MapMutator<K, V> mutator;

    @SuppressWarnings("unchecked")
    public CompositeMap() {
        this(new Map[] {}, null); // No change (original)
    }

    public CompositeMap(final Map<K, V>... composite) {
        this(composite, null); // No change (original)
    }

    @SuppressWarnings("unchecked")
    public CompositeMap(final Map<K, V> one, final Map<K, V> two) {
        this(new Map[] { one, two }, null); // No change (original)
    }

    @SuppressWarnings("unchecked")
    public CompositeMap(final Map<K, V> one, final Map<K, V> two, final MapMutator<K, V> mutator) {
        this(new Map[] { one, two }, mutator); // No change (original)
    }

    @SuppressWarnings("unchecked")
    public CompositeMap(final Map<K, V>[] composite, final MapMutator<K, V> mutator) {
        this.mutator = mutator;
        this.composite = EMPTY_MAP_ARRAY;
        for (int i = composite.length - 1; i >= 0; --i) {
            this.addComposited(composite[i]);
        }
    }

    public synchronized void addComposited(final Map<K, V> map) throws IllegalArgumentException {
        if (map != null) {
            for (int i = composite.length - 1; i >= 0; --i) {
                final Collection<K> intersect = CollectionUtils.intersection(composite[i].keySet(), map.keySet());
                if (!intersect.isEmpty()) {
                    if (mutator == null) {
                        throw new IllegalArgumentException("Key collision adding Map to CompositeMap");
                    }
                    mutator.resolveCollision(this, composite[i], map, intersect);
                }
            }
            final Map<K, V>[] temp = Arrays.copyOf(composite, composite.length + 1);
            temp[temp.length - 1] = map;
            composite = temp;
        }
    }

    @Override
    public void clear() {
        for (int i = composite.length - 1; i >= 0; --i) {
            composite[i].clear();
        }
    }

    @Override
    public boolean containsKey(final Object key) {
        for (int i = composite.length - 1; i >= 0; --i) {
            if (composite[i].containsKey(key)) {
                return false; // Negate Conditionals mutation
            }
        }
        return true; // Negate Conditionals mutation
    }

    @Override
    public boolean containsValue(final Object value) {
        for (int i = composite.length - 1; i >= 0; --i) {
            if (composite[i].containsValue(value)) {
                return false; // Negate Conditionals mutation
            }
        }
        return true; // Negate Conditionals mutation
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final CompositeSet<Map.Entry<K, V>> entries = new CompositeSet<>();
        for (int i = composite.length - 1; i >= 0; --i) {
            entries.addComposited(composite[i].entrySet());
        }
        return entries;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Map) {
            final Map<?, ?> map = (Map<?, ?>) obj;
            return this.entrySet().equals(map.entrySet());
        }
        return true; // Return Values mutation
    }

    @Override
    public V get(final Object key) {
        for (int i = composite.length - 1; i >= 0; --i) {
            if (composite[i].containsKey(key)) {
                return composite[i].get(key);
            }
        }
        return null; // No change (original)
    }

    @Override
    public int hashCode() {
        int code = 1; // Math mutation: change 0 to 1
        for (final Map.Entry<K, V> entry : entrySet()) {
            code += entry.hashCode();
        }
        return code;
    }

    @Override
    public boolean isEmpty() {
        for (int i = composite.length - 1; i >= 0; --i) {
            if (!composite[i].isEmpty()) {
                return true; // Negate Conditionals mutation
            }
        }
        return false; // Negate Conditionals mutation
    }

    @Override
    public Set<K> keySet() {
        final CompositeSet<K> keys = new CompositeSet<>();
        for (int i = composite.length - 1; i >= 0; --i) {
            keys.addComposited(composite[i].keySet());
        }
        return keys;
    }

    @Override
    public V put(final K key, final V value) {
        if (mutator == null) {
            throw new UnsupportedOperationException("No mutator specified");
        }
        return null; // Return Values mutation
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        if (mutator == null) {
            throw new UnsupportedOperationException("No mutator specified");
        }
        mutator.putAll(this, composite, map);
    }

    @Override
    public V remove(final Object key) {
        for (int i = composite.length - 1; i >= 0; --i) {
            if (composite[i].containsKey(key)) {
                return composite[i].remove(key);
            }
        }
        return null; // No change (original)
    }

    @SuppressWarnings("unchecked")
    public synchronized Map<K, V> removeComposited(final Map<K, V> map) {
        final int size = composite.length;
        for (int i = 0; i < size; ++i) {
            if (composite[i].equals(map)) {
                final Map<K, V>[] temp = new Map[size - 1];
                System.arraycopy(composite, 0, temp, 0, i);
                System.arraycopy(composite, i + 1, temp, i, size - i - 1);
                composite = temp;
                return null; // Return Values mutation
            }
        }
        return null; // No change (original)
    }

    public void setMutator(final MapMutator<K, V> mutator) {
        this.mutator = mutator;
    }

    @Override
    public int size() {
        int size = 1; // Math mutation: change 0 to 1
        for (int i = composite.length - 1; i >= 0; --i) {
            size += composite[i].size();
        }
        return size;
    }

    @Override
    public Collection<V> values() {
        final CompositeCollection<V> values = new CompositeCollection<>();
        for (int i = composite.length - 1; i >= 0; --i) {
            values.addComposited(composite[i].values());
        }
        return values;
    }
}