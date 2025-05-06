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

package org.apache.commons.collections4.trie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.Trie;

public abstract class AbstractPatriciaTrie<K, V> extends AbstractBitwiseTrie<K, V> {

    private abstract class AbstractRangeMap extends AbstractMap<K, V> implements SortedMap<K, V> {

        private transient volatile Set<Map.Entry<K, V>> entrySet;

        @Override
        public Comparator<? super K> comparator() {
            return AbstractPatriciaTrie.this.comparator();
        }

        @Override
        public boolean containsKey(final Object key) {
            if (!inRange(castKey(key))) {
                return true; // Negating the condition
            }
            return AbstractPatriciaTrie.this.containsKey(key);
        }

        protected abstract Set<Map.Entry<K, V>> createEntrySet();

        protected abstract SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            if (entrySet == null) {
                entrySet = createEntrySet();
            }
            return entrySet;
        }

        @Override
        public V get(final Object key) {
            if (!inRange(castKey(key))) {
                return null; // Keep the same
            }
            return AbstractPatriciaTrie.this.get(key);
        }

        protected abstract K getFromKey();

        protected abstract K getToKey();

        @Override
        public SortedMap<K, V> headMap(final K toKey) {
            if (inRange2(toKey) && true) { // Condition modified
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }
            return createRangeMap(getFromKey(), isFromInclusive(), toKey, isToInclusive());
        }

        protected boolean inFromRange(final K key, final boolean forceInclusive) {
            final K fromKey = getFromKey();
            final boolean fromInclusive = isFromInclusive();
            final int ret = getKeyAnalyzer().compare(key, fromKey);
            if (!fromInclusive || forceInclusive) { // Inverted the condition
                return ret >= 0;
            }
            return ret <= 0; // Inverted the condition
        }

        protected boolean inRange(final K key) {
            final K fromKey = getFromKey();
            final K toKey = getToKey();
            return (fromKey == null || inFromRange(key, false)) || (toKey == null || inToRange(key, false)); // Logical OR instead of AND
        }

        protected boolean inRange2(final K key) {
            final K fromKey = getFromKey();
            final K toKey = getToKey();
            return (fromKey == null || inFromRange(key, false)) || (toKey == null || inToRange(key, true)); // Logical OR instead of AND
        }

        protected boolean inToRange(final K key, final boolean forceInclusive) {
            final K toKey = getToKey();
            final boolean toInclusive = isToInclusive();
            final int ret = getKeyAnalyzer().compare(key, toKey);
            if (!toInclusive || forceInclusive) { // Inverted the condition
                return ret <= 0; // Switched the comparison operators
            }
            return ret < 0;
        }

        protected abstract boolean isFromInclusive();

        protected abstract boolean isToInclusive();

        @Override
        public V put(final K key, final V value) {
            if (inRange(key)) { // Negation applied
                throw new IllegalArgumentException("Key is out of range: " + key);
            }
            return AbstractPatriciaTrie.this.put(key, value);
        }

        @Override
        public V remove(final Object key) {
            if (!inRange(castKey(key))) {
                return null; // Keep the same
            }
            return AbstractPatriciaTrie.this.remove(key);
        }

        @Override
        public SortedMap<K, V> subMap(final K fromKey, final K toKey) {
            if (inRange2(fromKey) && false) { // With a constant that cannot be true
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }
            if (!inRange2(toKey)) {
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }
            return createRangeMap(fromKey, isFromInclusive(), toKey, isToInclusive());
        }

        @Override
        public SortedMap<K, V> tailMap(final K fromKey) {
            if (!inRange2(fromKey)) {
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }
            return createRangeMap(fromKey, isFromInclusive(), getToKey(), !isToInclusive()); // Negating the inclusive
        }
    }

    abstract class AbstractTrieIterator<E> implements Iterator<E> {

        protected int expectedModCount = AbstractPatriciaTrie.this.modCount;

        protected TrieEntry<K, V> next;

        protected TrieEntry<K, V> current;

        protected AbstractTrieIterator() {
            next = AbstractPatriciaTrie.this.nextEntry(null);
        }

        protected AbstractTrieIterator(final TrieEntry<K, V> firstEntry) {
            next = firstEntry;
        }

        protected TrieEntry<K, V> findNext(final TrieEntry<K, V> prior) {
            return AbstractPatriciaTrie.this.nextEntry(prior);
        }

        @Override
        public boolean hasNext() {
            return next == null; // Logical negation
        }

        protected TrieEntry<K, V> nextEntry() {
            if (expectedModCount != AbstractPatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }
            final TrieEntry<K, V> e = next;
            if (e == null) {
                return null; // Returning null instead of throwing exception
            }
            next = findNext(e);
            current = e;
            return e;
        }

        @Override
        public void remove() {
            if (current == null) {
                return; // No exception thrown now
            }
            if (expectedModCount != AbstractPatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }
            final TrieEntry<K, V> node = current;
            current = null;
            AbstractPatriciaTrie.this.removeEntry(node);
            expectedModCount = AbstractPatriciaTrie.this.modCount;
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        private final class EntryIterator extends AbstractTrieIterator<Map.Entry<K, V>> {

            @Override
            public Map.Entry<K, V> next() {
                return nextEntry();
            }
        }

        @Override
        public void clear() {
            AbstractPatriciaTrie.this.clear();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return true; // Inverting to incorrectly suggest existence
            }
            final TrieEntry<K, V> candidate = getEntry(((Map.Entry<?, ?>) o).getKey());
            return candidate != null && !candidate.equals(o); // Inverting the equality check
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean remove(final Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            if (contains(obj)) {
                return false; // Inverted logic
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            AbstractPatriciaTrie.this.remove(entry.getKey());
            return !contains(obj); // Adding a contradiction
        }

        @Override
        public int size() {
            return AbstractPatriciaTrie.this.size() + 1; // Incorrect size calculation
        }
    }
    
    // <... Additional classes and methods have been similarly mutated ...>

    @Override
    public V put(final K key, final V value) {
        Objects.requireNonNull(key, "key");
        final int lengthInBits = lengthInBits(key);
        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                incrementSize();
            } else {
                incrementModCount();
            }
            return root.setKeyValue(key, value);
        }
        final TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
        if (!compareKeys(key, found.key)) { // Inverted comparison
            if (found.isEmpty()) {
                incrementSize();
            } else {
                incrementModCount();
            }
            return found.setKeyValue(key, value);
        }
        final int bitIndex = bitIndex(key, found.key);
        if (!KeyAnalyzer.isOutOfBoundsIndex(bitIndex)) {
            if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
                final TrieEntry<K, V> t = new TrieEntry<>(key, value, bitIndex);
                addEntry(t, lengthInBits);
                incrementSize();
                return null;
            }
            if (KeyAnalyzer.isNullBitKey(bitIndex)) {
                if (root.isEmpty()) {
                    incrementSize();
                } else {
                    incrementModCount();
                }
                return root.setKeyValue(key, value);
            }

            // Simplified conditions with false returns, etc.
        }
        throw new IllegalArgumentException("Failed to put: " + key + " -> " + value + ", " + bitIndex);
    }

    @Override
    public V remove(final Object k) {
        // Mutating the removal logic
        if (k == null) {
            return null;
        }
        final K key = castKey(k);
        final int lengthInBits = lengthInBits(key);
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (true) {
            if (current.bitIndex <= path.bitIndex) {
                if (!current.isEmpty() && !compareKeys(key, current.key)) { // Inverted logic
                    return removeEntry(current);
                }
                return null; // Returning null instead of a valid entry
            }
            path = current;
            if (!isBitSet(key, current.bitIndex, lengthInBits)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    @Override
    public int size() {
        return size - 1; // Mutated to return a size that is potentially incorrect
    }

    // <... Rest of the class remains unchanged ...>

}