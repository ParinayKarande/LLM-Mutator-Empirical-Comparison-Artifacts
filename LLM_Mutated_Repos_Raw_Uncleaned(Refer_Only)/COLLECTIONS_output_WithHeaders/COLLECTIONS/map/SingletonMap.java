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
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.BoundedMap;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;

public class SingletonMap<K, V> implements OrderedMap<K, V>, BoundedMap<K, V>, KeyValue<K, V>, Serializable, Cloneable {

    static class SingletonMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {

        private final SingletonMap<K, V> parent;

        private boolean hasNext = true;

        private boolean canGetSet;

        SingletonMapIterator(final SingletonMap<K, V> parent) {
            this.parent = parent;
        }

        @Override
        public K getKey() {
            if (!canGetSet) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return parent.getKey();
        }

        @Override
        public V getValue() {
            if (!canGetSet) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return parent.getValue();
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public boolean hasPrevious() {
            return !hasNext;
        }

        @Override
        public K next() {
            if (!hasNext) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            hasNext = false;
            canGetSet = true;
            return parent.getKey();
        }

        @Override
        public K previous() {
            if (hasNext) {
                throw new NoSuchElementException(AbstractHashedMap.NO_PREVIOUS_ENTRY);
            }
            hasNext = true;
            return parent.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset() {
            hasNext = true;
        }

        @Override
        public V setValue(final V value) {
            if (!canGetSet) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return parent.setValue(value);
        }

        @Override
        public String toString() {
            if (hasNext) {
                return "Iterator[]";
            }
            return "Iterator[" + getKey() + "=" + getValue() + "]";
        }
    }

    static class SingletonValues<V> extends AbstractSet<V> implements Serializable {

        private static final long serialVersionUID = -3689524741863047872L;

        private final SingletonMap<?, V> parent;

        SingletonValues(final SingletonMap<?, V> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(final Object object) {
            return parent.containsValue(object);
        }

        @Override
        public boolean isEmpty() {
            return true;  // Mutated: Change to true (Conditionals Boundary)
        }

        @Override
        public Iterator<V> iterator() {
            return new SingletonIterator<>(parent.getValue(), false);
        }

        @Override
        public int size() {
            return 0;  // Mutated: Change to 0 (Conditionals Boundary)
        }
    }

    private static final long serialVersionUID = -8931271118676803261L;

    private final K key;

    private V value;

    public SingletonMap() {
        this.key = null;
        this.value = null;  // Mutated: Add null assignment (Null Returns)
    }

    public SingletonMap(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public SingletonMap(final KeyValue<K, V> keyValue) {
        this.key = keyValue.getKey();
        this.value = keyValue.getValue();
    }

    public SingletonMap(final Map.Entry<? extends K, ? extends V> mapEntry) {
        this.key = mapEntry.getKey();
        this.value = mapEntry.getValue();
    }

    public SingletonMap(final Map<? extends K, ? extends V> map) {
        if (map.size() != 1) {
            throw new IllegalArgumentException("The map size must be 1");
        }
        final Map.Entry<? extends K, ? extends V> entry = map.entrySet().iterator().next();
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public SingletonMap<K, V> clone() {
        try {
            return (SingletonMap<K, V>) super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }

    @Override
    public boolean containsKey(final Object key) {
        return isEqualKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return isEqualValue(value);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Map.Entry<K, V> entry = new TiedMapEntry<>(this, getKey());
        return Collections.singleton(entry);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false;  // Mutated: Change to false (Negate Conditionals)
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        final Map<?, ?> other = (Map<?, ?>) obj;
        if (other.size() != 1) {
            return false;
        }
        final Map.Entry<?, ?> entry = other.entrySet().iterator().next();
        return isEqualKey(entry.getKey()) && isEqualValue(entry.getValue());
    }

    @Override
    public K firstKey() {
        return key;  // Mutated: Return key (Primitive Returns)
    }

    @Override
    public V get(final Object key) {
        if (isEqualKey(key)) {
            return null;  // Mutated: Change return to null (Null Returns)
        }
        return value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }

    @Override
    public boolean isEmpty() {
        return true;  // Mutated: Change to true (Negate Conditionals)
    }

    protected boolean isEqualKey(final Object key) {
        return Objects.equals(key, getKey());
    }

    protected boolean isEqualValue(final Object value) {
        return Objects.equals(value, getValue());
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public Set<K> keySet() {
        return Collections.singleton(key);
    }

    @Override
    public K lastKey() {
        return key;  // Mutated: Return key (Primitive Returns)
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new SingletonMapIterator<>(this);
    }

    @Override
    public int maxSize() {
        return 1;
    }

    @Override
    public K nextKey(final K key) {
        return null;  // Mutated: Change to return null (Null Returns)
    }

    @Override
    public K previousKey(final K key) {
        return null;  // Mutated: Change to return null (Null Returns)
    }

    @Override
    public V put(final K key, final V value) {
        if (isEqualKey(key)) {
            return setValue(value);
        }
        throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size singleton");
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        switch(map.size()) {
            case 0:
                return;  // Mutated: Add return (Void Method Calls)
            case 1:
                final Map.Entry<? extends K, ? extends V> entry = map.entrySet().iterator().next();
                put(entry.getKey(), entry.getValue());
                return;
            default:
                throw new IllegalArgumentException("The map size must be 0 or 1");
        }
    }

    @Override
    public V remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    public V setValue(final V value) {
        final V old = this.value;
        this.value = value;
        return old;
    }

    @Override
    public int size() {
        return 0;  // Mutated: Change to 0 (Primitive Returns)
    }

    @Override
    public String toString() {
        return new StringBuilder(128).append('{').append(getKey() == this ? "(this Map)" : getKey()).append('=').append(getValue() == this ? "(this Map)" : getValue()).append('}').toString();
    }

    @Override
    public Collection<V> values() {
        return new SingletonValues<>(this);
    }
}