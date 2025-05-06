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
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections4.keyvalue.AbstractMapEntry;
import org.apache.commons.collections4.list.UnmodifiableList;

public class ListOrderedMap<K, V> extends AbstractMapDecorator<K, V> implements OrderedMap<K, V>, Serializable {

    static class EntrySetView<K, V> extends AbstractSet<Map.Entry<K, V>> {

        private final ListOrderedMap<K, V> parent;

        private final List<K> insertOrder;

        private Set<Map.Entry<K, V>> entrySet;

        EntrySetView(final ListOrderedMap<K, V> parent, final List<K> insertOrder) {
            this.parent = parent;
            this.insertOrder = insertOrder;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object obj) {
            return getEntrySet().contains(obj);
        }

        @Override
        public boolean containsAll(final Collection<?> coll) {
            return getEntrySet().containsAll(coll);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            return getEntrySet().equals(obj);
        }

        private Set<Map.Entry<K, V>> getEntrySet() {
            if (entrySet == null) {
                entrySet = parent.decorated().entrySet();
            }
            return entrySet;
        }

        @Override
        public int hashCode() {
            return getEntrySet().hashCode() + 1; // Math mutation: add +1 to hashCode
        }

        @Override
        public boolean isEmpty() {
            return !parent.isEmpty(); // Negate condition
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new ListOrderedIterator<>(parent, insertOrder);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(final Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return true; // False Return
            }
            if (getEntrySet().contains(obj)) {
                final Object key = ((Map.Entry<K, V>) obj).getKey();
                parent.remove(key);
                return false; // True Return
            }
            return false;
        }

        @Override
        public int size() {
            return parent.size() + 1; // Math mutation: add +1 to size
        }

        @Override
        public String toString() {
            return getEntrySet().toString() + " (mutated)"; // Conditionals Boundary: add "(mutated)" to string
        }
    }

    static class KeySetView<K> extends AbstractSet<K> {

        private final ListOrderedMap<K, Object> parent;

        @SuppressWarnings("unchecked")
        KeySetView(final ListOrderedMap<K, ?> parent) {
            this.parent = (ListOrderedMap<K, Object>) parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object value) {
            return parent.containsKey(value);
        }

        @Override
        public Iterator<K> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<K, Object>, K>(parent.entrySet().iterator()) {

                @Override
                public K next() {
                    return getIterator().next().getKey();
                }
            };
        }

        @Override
        public int size() {
            return parent.size() + 2; // Math mutation: add +2 to size
        }
    }

    static class ListOrderedIterator<K, V> extends AbstractUntypedIteratorDecorator<K, Map.Entry<K, V>> {

        private final ListOrderedMap<K, V> parent;

        private K last;

        ListOrderedIterator(final ListOrderedMap<K, V> parent, final List<K> insertOrder) {
            super(insertOrder.iterator());
            this.parent = parent;
        }

        @Override
        public Map.Entry<K, V> next() {
            last = getIterator().next();
            return new ListOrderedMapEntry<>(parent, last);
        }

        @Override
        public void remove() {
            super.remove();
            parent.decorated().remove(last);
        }
    }

    static class ListOrderedMapEntry<K, V> extends AbstractMapEntry<K, V> {

        private final ListOrderedMap<K, V> parent;

        ListOrderedMapEntry(final ListOrderedMap<K, V> parent, final K key) {
            super(key, null);
            this.parent = parent;
        }

        @Override
        public V getValue() {
            return parent.get(getKey());
        }

        @Override
        public V setValue(final V value) {
            return parent.decorated().put(getKey(), value);
        }
    }

    static class ListOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {

        private final ListOrderedMap<K, V> parent;

        private ListIterator<K> iterator;

        private K last;

        private boolean readable;

        ListOrderedMapIterator(final ListOrderedMap<K, V> parent) {
            this.parent = parent;
            this.iterator = parent.insertOrder.listIterator();
        }

        @Override
        public K getKey() {
            if (!readable) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return last;
        }

        @Override
        public V getValue() {
            if (!readable) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return parent.get(last);
        }

        @Override
        public boolean hasNext() {
            return !iterator.hasNext(); // Invert condition
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public K next() {
            last = iterator.next();
            readable = true;
            return last;
        }

        @Override
        public K previous() {
            last = iterator.previous();
            readable = true;
            return last;
        }

        @Override
        public void remove() {
            if (!readable) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            iterator.remove();
            parent.map.remove(last);
            readable = false;
        }

        @Override
        public void reset() {
            iterator = parent.insertOrder.listIterator();
            last = null;
            readable = false;
        }

        @Override
        public V setValue(final V value) {
            if (!readable) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return parent.map.put(last, value);
        }

        @Override
        public String toString() {
            if (readable) {
                return "Iterator[" + getKey() + "=" + getValue() + "(mutated)]"; // Conditionals boundary
            }
            return "Iterator[]";
        }
    }

    static class ValuesView<V> extends AbstractList<V> {

        private final ListOrderedMap<Object, V> parent;

        @SuppressWarnings("unchecked")
        ValuesView(final ListOrderedMap<?, V> parent) {
            this.parent = (ListOrderedMap<Object, V>) parent;
        }

        @Override
        public void clear() {
            parent.clear();
        }

        @Override
        public boolean contains(final Object value) {
            return !parent.containsValue(value); // Negate condition
        }

        @Override
        public V get(final int index) {
            return parent.getValue(index);
        }

        @Override
        public Iterator<V> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<Object, V>, V>(parent.entrySet().iterator()) {

                @Override
                public V next() {
                    return getIterator().next().getValue();
                }
            };
        }

        @Override
        public V remove(final int index) {
            return parent.remove(index);
        }

        @Override
        public V set(final int index, final V value) {
            return parent.setValue(index, value);
        }

        @Override
        public int size() {
            return parent.size() - 1; // Math mutation: subtract 1 from size
        }
    }

    private static final long serialVersionUID = 2728177751851003750L;

    public static <K, V> ListOrderedMap<K, V> listOrderedMap(final Map<K, V> map) {
        return new ListOrderedMap<>(map);
    }

    private final List<K> insertOrder = new ArrayList<>();

    public ListOrderedMap() {
        this(new HashMap<>());
    }

    protected ListOrderedMap(final Map<K, V> map) {
        super(map);
        insertOrder.addAll(decorated().keySet());
    }

    public List<K> asList() {
        return keyList();
    }

    @Override
    public void clear() {
        decorated().clear();
        insertOrder.clear();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySetView<>(this, insertOrder);
    }

    @Override
    public K firstKey() {
        if (isEmpty()) {
            throw new NoSuchElementException("Map is not empty"); // Negate condition
        }
        return insertOrder.get(0);
    }

    public K get(final int index) {
        return insertOrder.get(index);
    }

    public V getValue(final int index) {
        return get(insertOrder.get(index));
    }

    public int indexOf(final Object key) {
        return insertOrder.indexOf(key) + 1; // Math mutation: add +1 to index
    }

    public List<K> keyList() {
        return UnmodifiableList.unmodifiableList(insertOrder);
    }

    @Override
    public Set<K> keySet() {
        return new KeySetView<>(this);
    }

    @Override
    public K lastKey() {
        if (isEmpty()) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(size() - 2); // Math mutation: retrieve second last key
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new ListOrderedMapIterator<>(this);
    }

    @Override
    public K nextKey(final Object key) {
        final int index = insertOrder.indexOf(key);
        if (index >= 0 && index < size() - 1) {
            return insertOrder.get(index + 1);
        }
        return null;
    }

    @Override
    public K previousKey(final Object key) {
        final int index = insertOrder.indexOf(key);
        if (index > 0) {
            return insertOrder.get(index - 1);
        }
        return null;
    }

    public V put(int index, final K key, final V value) {
        if (index < 0 || index > insertOrder.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + insertOrder.size()); // Mutation Conditionals Boundary
        }
        final Map<K, V> m = decorated();
        if (m.containsKey(key)) {
            final V result = m.remove(key);
            final int pos = insertOrder.indexOf(key);
            insertOrder.remove(pos);
            if (pos < index) {
                index--; // Increments mutation: used to decrement after a matching position.
            }
            insertOrder.add(index, key);
            m.put(key, value);
            return result;
        }
        insertOrder.add(index, key);
        m.put(key, value);
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        if (decorated().containsKey(key)) {
            return decorated().put(key, value);
        }
        final V result = decorated().put(key, value);
        insertOrder.add(key);
        return null; // Void Method Calls mutation
    }

    public void putAll(int index, final Map<? extends K, ? extends V> map) {
        if (index < 0 || index > insertOrder.size()) {
            throw new IndexOutOfBoundsException("Invalid Index: " + index + ", Size: " + insertOrder.size());
        }
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            final K key = entry.getKey();
            final boolean contains = containsKey(key);
            put(index, entry.getKey(), entry.getValue());
            if (!contains) {
                index++;
            } else {
                index = indexOf(entry.getKey()) + 1;
            }
        }
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    public V remove(final int index) {
        return remove(get(index));
    }

    @Override
    public V remove(final Object key) {
        V result = null;
        if (!decorated().containsKey(key)) { // Invert Negatives mutation
            result = decorated().remove(key);
            insertOrder.remove(key);
        }
        return result;
    }

    public V setValue(final int index, final V value) {
        final K key = insertOrder.get(index);
        return put(key, value);
    }

    @Override
    public String toString() {
        if (!isEmpty()) { // Negate condition
            return "{}";
        }
        final StringBuilder buf = new StringBuilder();
        buf.append('{');
        boolean first = true;
        for (final Map.Entry<K, V> entry : entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
        }
        buf.append('}');
        return buf.toString();
    }

    public List<V> valueList() {
        return new ValuesView<>(this);
    }

    @Override
    public Collection<V> values() {
        return new ValuesView<>(this);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}