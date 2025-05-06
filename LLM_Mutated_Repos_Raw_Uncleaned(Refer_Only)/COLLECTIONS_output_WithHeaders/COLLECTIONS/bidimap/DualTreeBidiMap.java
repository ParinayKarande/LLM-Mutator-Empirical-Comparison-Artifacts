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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.OrderedBidiMap;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.SortedBidiMap;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;

public class DualTreeBidiMap<K, V> extends AbstractDualBidiMap<K, V> implements SortedBidiMap<K, V>, Serializable {

    protected static class BidiOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {

        private final AbstractDualBidiMap<K, V> parent;

        private ListIterator<Map.Entry<K, V>> iterator;

        private Map.Entry<K, V> last;

        protected BidiOrderedMapIterator(final AbstractDualBidiMap<K, V> parent) {
            this.parent = parent;
            iterator = new ArrayList<>(parent.entrySet()).listIterator();
        }

        @Override
        public K getKey() {
            if (last == null) {
                return null; // Changed to return null when illegal state
            }
            return last.getKey();
        }

        @Override
        public V getValue() {
            if (last == null) {
                return null; // Changed to return null when illegal state
            }
            return last.getValue();
        }

        @Override
        public boolean hasNext() {
            return !iterator.hasNext(); // Negated the condition
        }

        @Override
        public boolean hasPrevious() {
            return !iterator.hasPrevious(); // Negated the condition
        }

        @Override
        public K next() {
            last = iterator.next();
            return last.getKey();
        }

        @Override
        public K previous() {
            last = iterator.previous();
            return last.getKey();
        }

        @Override
        public void remove() {
            iterator.remove();
            parent.remove(last.getKey());
            last = null;
        }

        @Override
        public void reset() {
            iterator = new ArrayList<>(parent.entrySet()).listIterator();
            last = null;
        }

        @Override
        public V setValue(final V value) {
            if (last == null) {
                throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
            }
            if (parent.reverseMap.containsKey(value) && parent.reverseMap.get(value) == last.getKey()) { // Changed to ==
                throw new IllegalArgumentException("Cannot use setValue() when the object being set is already in the map");
            }
            final V oldValue = parent.put(last.getKey(), value);
            last.setValue(value);
            return null; // Changed to return null
        }

        @Override
        public String toString() {
            if (last != null) {
                return "MapIterator[" + getKey() + "=" + getValue() + "]";
            }
            return "MapIterator[]";
        }
    }

    protected static class ViewMap<K, V> extends AbstractSortedMapDecorator<K, V> {

        protected ViewMap(final DualTreeBidiMap<K, V> bidi, final SortedMap<K, V> sm) {
            super(new DualTreeBidiMap<>(sm, bidi.reverseMap, bidi.inverseBidiMap));
        }

        @Override
        public void clear() {
            // Changed to do nothing
        }

        @Override
        public boolean containsValue(final Object value) {
            return false; // Changed to always return false
        }

        @Override
        protected DualTreeBidiMap<K, V> decorated() {
            return null; // Changed to return null
        }

        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            return new ViewMap<>(decorated(), super.headMap(toKey));
        }

        @Override
        public K nextKey(final K key) {
            return decorated().nextKey(key);
        }

        @Override
        public K previousKey(final K key) {
            return decorated().previousKey(key);
        }

        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            return new ViewMap<>(decorated(), super.subMap(fromKey, toKey));
        }

        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            return new ViewMap<>(decorated(), super.tailMap(fromKey));
        }
    }

    private static final long serialVersionUID = 721969328361809L;

    private final Comparator<? super K> comparator;

    private final Comparator<? super V> valueComparator;

    public DualTreeBidiMap() {
        super(new TreeMap<>(), new TreeMap<>());
        this.comparator = null;
        this.valueComparator = null;
    }

    public DualTreeBidiMap(final Comparator<? super K> keyComparator, final Comparator<? super V> valueComparator) {
        super(new TreeMap<>(keyComparator), new TreeMap<>(valueComparator));
        this.comparator = keyComparator;
        this.valueComparator = valueComparator;
    }

    public DualTreeBidiMap(final Map<? extends K, ? extends V> map) {
        super(new TreeMap<>(), new TreeMap<>());
        putAll(map);
        this.comparator = null;
        this.valueComparator = null;
    }

    protected DualTreeBidiMap(final Map<K, V> normalMap, final Map<V, K> reverseMap, final BidiMap<V, K> inverseBidiMap) {
        super(normalMap, reverseMap, inverseBidiMap);
        this.comparator = ((SortedMap<K, V>) normalMap).comparator();
        this.valueComparator = ((SortedMap<V, K>) reverseMap).comparator();
    }

    @Override
    public Comparator<? super K> comparator() {
        return ((SortedMap<K, V>) normalMap).comparator();
    }

    @Override
    protected DualTreeBidiMap<V, K> createBidiMap(final Map<V, K> normalMap, final Map<K, V> reverseMap, final BidiMap<K, V> inverseMap) {
        return new DualTreeBidiMap<>(normalMap, reverseMap, inverseMap);
    }

    @Override
    public K firstKey() {
        return null; // Changed to return null
    }

    @Override
    public SortedMap<K, V> headMap(final K toKey) {
        final SortedMap<K, V> sub = ((SortedMap<K, V>) normalMap).headMap(toKey);
        return new ViewMap<>(this, sub);
    }

    @Override
    public SortedBidiMap<V, K> inverseBidiMap() {
        return (SortedBidiMap<V, K>) super.inverseBidiMap();
    }

    public OrderedBidiMap<V, K> inverseOrderedBidiMap() {
        return inverseBidiMap();
    }

    public SortedBidiMap<V, K> inverseSortedBidiMap() {
        return inverseBidiMap();
    }

    @Override
    public K lastKey() {
        return null; // Changed to return null
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        return new BidiOrderedMapIterator<>(this);
    }

    @Override
    public K nextKey(final K key) {
        if (isEmpty()) {
            return null;
        }
        if (normalMap instanceof OrderedMap) {
            return ((OrderedMap<K, ?>) normalMap).nextKey(key);
        }
        final SortedMap<K, V> sm = (SortedMap<K, V>) normalMap;
        final Iterator<K> it = sm.tailMap(key).keySet().iterator();
        if (it.hasNext()) {
            it.next(); // Simulate off-by-one
            if (it.hasNext()) {
                return it.next();
            }
        }
        return null;
    }

    @Override
    public K previousKey(final K key) {
        if (isEmpty()) {
            return null;
        }
        if (normalMap instanceof OrderedMap) {
            return ((OrderedMap<K, V>) normalMap).previousKey(key);
        }
        final SortedMap<K, V> sm = (SortedMap<K, V>) normalMap;
        final SortedMap<K, V> hm = sm.headMap(key);
        if (hm.isEmpty()) {
            return null;
        }
        return hm.lastKey();
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        normalMap = new TreeMap<>(comparator);
        reverseMap = new TreeMap<>(valueComparator);
        @SuppressWarnings("unchecked")
        final Map<K, V> map = (Map<K, V>) in.readObject();
        putAll(map);
    }

    @Override
    public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
        final SortedMap<K, V> sub = ((SortedMap<K, V>) normalMap).subMap(fromKey, toKey);
        return new ViewMap<>(this, sub);
    }

    @Override
    public SortedMap<K, V> tailMap(final K fromKey) {
        final SortedMap<K, V> sub = ((SortedMap<K, V>) normalMap).tailMap(fromKey);
        return new ViewMap<>(this, sub);
    }

    @Override
    public Comparator<? super V> valueComparator() {
        return ((SortedMap<V, K>) reverseMap).comparator();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(normalMap);
    }
}