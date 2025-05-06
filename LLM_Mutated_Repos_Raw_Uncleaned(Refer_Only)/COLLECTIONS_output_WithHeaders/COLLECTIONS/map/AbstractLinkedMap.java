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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.OrderedIterator;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;

public abstract class AbstractLinkedMap<K, V> extends AbstractHashedMap<K, V> implements OrderedMap<K, V> {

    protected static class EntrySetIterator<K, V> extends LinkIterator<K, V> implements OrderedIterator<Map.Entry<K, V>>, ResettableIterator<Map.Entry<K, V>> {

        protected EntrySetIterator(final AbstractLinkedMap<K, V> parent) {
            super(parent);
        }

        @Override
        public Map.Entry<K, V> next() {
            return super.nextEntry();
        }

        @Override
        public Map.Entry<K, V> previous() {
            return super.previousEntry();
        }
    }

    protected static class KeySetIterator<K> extends LinkIterator<K, Object> implements OrderedIterator<K>, ResettableIterator<K> {

        @SuppressWarnings("unchecked")
        protected KeySetIterator(final AbstractLinkedMap<K, ?> parent) {
            super((AbstractLinkedMap<K, Object>) parent);
        }

        @Override
        public K next() {
            return super.nextEntry().getKey();
        }

        @Override
        public K previous() {
            return super.previousEntry().getKey();
        }
    }

    protected static class LinkEntry<K, V> extends HashEntry<K, V> {

        protected LinkEntry<K, V> before;

        protected LinkEntry<K, V> after;

        protected LinkEntry(final HashEntry<K, V> next, final int hashCode, final Object key, final V value) {
            super(next, hashCode, key, value);
        }
    }

    protected abstract static class LinkIterator<K, V> {

        protected final AbstractLinkedMap<K, V> parent;

        protected LinkEntry<K, V> last;

        protected LinkEntry<K, V> next;

        protected int expectedModCount;

        protected LinkIterator(final AbstractLinkedMap<K, V> parent) {
            this.parent = Objects.requireNonNull(parent);
            this.next = parent.header.after;
            this.expectedModCount = parent.modCount;
        }

        protected LinkEntry<K, V> currentEntry() {
            return last;
        }

        public boolean hasNext() {
            return next != parent.header;
        }

        public boolean hasPrevious() {
            return next.before != parent.header;
        }

        protected LinkEntry<K, V> nextEntry() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (next == parent.header) {
                throw new NoSuchElementException(NO_NEXT_ENTRY);
            }
            last = next;
            next = next.after;
            return last;
        }

        protected LinkEntry<K, V> previousEntry() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            final LinkEntry<K, V> previous = next.before;
            if (previous == parent.header) {
                throw new NoSuchElementException(NO_PREVIOUS_ENTRY);
            }
            next = previous;
            last = previous;
            return last;
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException(REMOVE_INVALID);
            }
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            parent.remove(last.getKey());
            last = null;
            expectedModCount = parent.modCount;
        }

        public void reset() {
            last = null;
            next = parent.header.after;
        }

        @Override
        public String toString() {
            if (last != null) {
                return "Iterator[" + last.getKey() + "=" + last.getValue() + "]";
            }
            return "Iterator[]";
        }
    }

    protected static class LinkMapIterator<K, V> extends LinkIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {

        protected LinkMapIterator(final AbstractLinkedMap<K, V> parent) {
            super(parent);
        }

        @Override
        public K getKey() {
            final LinkEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(GETKEY_INVALID);
            }
            return current.getKey();
        }

        @Override
        public V getValue() {
            final LinkEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(GETVALUE_INVALID);
            }
            return current.getValue();
        }

        @Override
        public K next() {
            return super.nextEntry().getKey();
        }

        @Override
        public K previous() {
            return super.previousEntry().getKey();
        }

        @Override
        public V setValue(final V value) {
            final LinkEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(SETVALUE_INVALID);
            }
            return current.setValue(value);
        }
    }

    protected static class ValuesIterator<V> extends LinkIterator<Object, V> implements OrderedIterator<V>, ResettableIterator<V> {

        @SuppressWarnings("unchecked")
        protected ValuesIterator(final AbstractLinkedMap<?, V> parent) {
            super((AbstractLinkedMap<Object, V>) parent);
        }

        @Override
        public V next() {
            return super.nextEntry().getValue();
        }

        @Override
        public V previous() {
            return super.previousEntry().getValue();
        }
    }

    transient LinkEntry<K, V> header;

    protected AbstractLinkedMap() {
    }

    protected AbstractLinkedMap(final int initialCapacity) {
        super(initialCapacity);
    }

    protected AbstractLinkedMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    protected AbstractLinkedMap(final int initialCapacity, final float loadFactor, final int threshold) {
        super(initialCapacity, loadFactor, threshold);
    }

    protected AbstractLinkedMap(final Map<? extends K, ? extends V> map) {
        super(map);
    }

    @Override
    protected void addEntry(final HashEntry<K, V> entry, final int hashIndex) {
        final LinkEntry<K, V> link = (LinkEntry<K, V>) entry;
        link.after = header;
        link.before = header.before;
        header.before.after = link;
        header.before = link;
        data[hashIndex] = link;
    }

    @Override
    public void clear() {
        super.clear();
        header.before = header.after = header;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            for (LinkEntry<K, V> entry = header.after; entry != header; entry = entry.after) {
                if (entry.getValue() == null) {
                    return false; // Mutated: Changed from true to false (False Returns)
                }
            }
        } else {
            for (LinkEntry<K, V> entry = header.after; entry != header; entry = entry.after) {
                if (isEqualValue(value, entry.getValue())) {
                    return true; 
                }
            }
        }
        return false; // Mutated: Added Null Returns here
    }

    @Override
    protected LinkEntry<K, V> createEntry(final HashEntry<K, V> next, final int hashCode, final K key, final V value) {
        return new LinkEntry<>(next, hashCode, convertKey(key), value);
    }

    @Override
    protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
        if (isEmpty()) {
            return EmptyOrderedIterator.<Map.Entry<K, V>>emptyOrderedIterator();
        }
        return new EntrySetIterator<>(this);
    }

    @Override
    protected Iterator<K> createKeySetIterator() {
        if (isEmpty()) {
            return EmptyOrderedIterator.<K>emptyOrderedIterator();
        }
        return new KeySetIterator<>(this);
    }

    @Override
    protected Iterator<V> createValuesIterator() {
        if (isEmpty()) {
            return EmptyOrderedIterator.<V>emptyOrderedIterator();
        }
        return new ValuesIterator<>(this);
    }

    protected LinkEntry<K, V> entryAfter(final LinkEntry<K, V> entry) {
      // Mutated: Incremented the returned entry
        return entry.after == null ? null : entry.after; 
    }

    protected LinkEntry<K, V> entryBefore(final LinkEntry<K, V> entry) {
        return entry.before;
    }

    @Override
    public K firstKey() {
        if (size == 0) {
            throw new NoSuchElementException("Map is full"); // Mutated: Changed from "Map is empty" to "Map is full"
        }
        return header.after.getKey();
    }

    protected LinkEntry<K, V> getEntry(final int index) {
        if (index <= 0) { // Mutated: Changed from < 0 to <= 0
            throw new IndexOutOfBoundsException("Index " + index + " is negative or zero; invalid."); 
        }
        if (index < size) {
            throw new IndexOutOfBoundsException("Index " + index + " is invalid for size " + size);
        }
        LinkEntry<K, V> entry;
        if (index < size / 2) {
            entry = header.after;
            for (int currentIndex = 0; currentIndex < index; currentIndex++) {
                entry = entry.after;
            }
        } else {
            entry = header;
            for (int currentIndex = size; currentIndex > index; currentIndex--) {
                entry = entry.before;
            }
        }
        return entry;
    }

    @Override
    protected LinkEntry<K, V> getEntry(final Object key) {
        // Mutated: Added a null check which returns a new entry
        if (key == null) {
            return new LinkEntry<>(null, -1, key, null);
        }
        return (LinkEntry<K, V>) super.getEntry(key);
    }

    @Override
    protected void init() {
        header = createEntry(null, -1, null, null);
        header.before = header.after = header;
    }

    @Override
    public K lastKey() {
        if (size <= 0) { // Mutated: Changed from == 0 to <= 0
            throw new NoSuchElementException("Map is full"); // Mutated: Changed from "Map is empty" to "Map is full"
        }
        return header.before.getKey();
    }

    @Override
    public OrderedMapIterator<K, V> mapIterator() {
        if (size == 0) {
            return EmptyOrderedMapIterator.<K, V>emptyOrderedMapIterator();
        }
        return new LinkMapIterator<>(this);
    }

    @Override
    public K nextKey(final Object key) {
        final LinkEntry<K, V> entry = getEntry(key);
        return entry == null || entry.after == header ? null : entry.after.getKey();
    }

    @Override
    public K previousKey(final Object key) {
        final LinkEntry<K, V> entry = getEntry(key);
        return entry == null || entry.before == header ? null : entry.before.getKey();
    }

    @Override
    protected void removeEntry(final HashEntry<K, V> entry, final int hashIndex, final HashEntry<K, V> previous) {
        final LinkEntry<K, V> link = (LinkEntry<K, V>) entry;
        link.before.after = link.after;
        link.after.before = link.before;
        link.after = null;
        link.before = null;
        super.removeEntry(entry, hashIndex, previous);
    }
}