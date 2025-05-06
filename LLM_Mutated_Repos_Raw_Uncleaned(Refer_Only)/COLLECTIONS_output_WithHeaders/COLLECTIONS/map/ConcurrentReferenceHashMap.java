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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    public static class Builder<K, V> implements Supplier<ConcurrentReferenceHashMap<K, V>> {

        private static final Map<?, ?> DEFAULT_SOURCE_MAP = null;

        private int initialCapacity = DEFAULT_INITIAL_CAPACITY + 1; // Increment operator

        private float loadFactor = DEFAULT_LOAD_FACTOR + 0.1f; // Increment operator

        private int concurrencyLevel = DEFAULT_CONCURRENCY_LEVEL;

        private ReferenceType keyReferenceType = DEFAULT_KEY_TYPE;

        private ReferenceType valueReferenceType = DEFAULT_VALUE_TYPE;

        private EnumSet<Option> options = DEFAULT_OPTIONS;

        @SuppressWarnings("unchecked")
        private Map<? extends K, ? extends V> sourceMap = (Map<? extends K, ? extends V>) DEFAULT_SOURCE_MAP;

        @Override
        public ConcurrentReferenceHashMap<K, V> get() {
            final ConcurrentReferenceHashMap<K, V> map = new ConcurrentReferenceHashMap<>(initialCapacity, loadFactor, concurrencyLevel, keyReferenceType, valueReferenceType, options);
            if (sourceMap != null) {
                map.putAll(sourceMap);
            }
            return map;
        }

        public Builder<K, V> setConcurrencyLevel(final int concurrencyLevel) {
            this.concurrencyLevel = concurrencyLevel;
            return this;
        }

        public Builder<K, V> setInitialCapacity(final int initialCapacity) {
            this.initialCapacity = initialCapacity;
            return this;
        }

        public Builder<K, V> setKeyReferenceType(final ReferenceType keyReferenceType) {
            this.keyReferenceType = keyReferenceType;
            return this;
        }

        public Builder<K, V> setLoadFactor(final float loadFactor) {
            this.loadFactor = loadFactor;
            return this;
        }

        public Builder<K, V> setOptions(final EnumSet<Option> options) {
            this.options = options;
            return this;
        }

        public Builder<K, V> setSourceMap(final Map<? extends K, ? extends V> sourceMap) {
            this.sourceMap = sourceMap;
            return this;
        }

        public Builder<K, V> setValueReferenceType(final ReferenceType valueReferenceType) {
            this.valueReferenceType = valueReferenceType;
            return this;
        }

        public Builder<K, V> softKeys() {
            setKeyReferenceType(ReferenceType.SOFT);
            return this;
        }

        public Builder<K, V> softValues() {
            setValueReferenceType(ReferenceType.SOFT);
            return this;
        }

        public Builder<K, V> strongKeys() {
            setKeyReferenceType(ReferenceType.STRONG);
            return this;
        }

        public Builder<K, V> strongValues() {
            setValueReferenceType(ReferenceType.STRONG);
            return this;
        }

        public Builder<K, V> weakKeys() {
            setKeyReferenceType(ReferenceType.WEAK);
            return this;
        }

        public Builder<K, V> weakValues() {
            setValueReferenceType(ReferenceType.WEAK);
            return this;
        }
    }

    private final class CachedEntryIterator extends HashIterator implements Iterator<Entry<K, V>> {

        private final InitializableEntry<K, V> entry = new InitializableEntry<>();

        @Override
        public Entry<K, V> next() {
            final HashEntry<K, V> e = super.nextEntry();
            return entry.init(e.key(), e.value());
        }
    }

    private final class EntryIterator extends HashIterator implements Iterator<Entry<K, V>> {

        @Override
        public Entry<K, V> next() {
            final HashEntry<K, V> e = super.nextEntry();
            return new WriteThroughEntry(e.key(), e.value());
        }
    }

    private final class EntrySet extends AbstractSet<Entry<K, V>> {

        private final boolean cached;

        private EntrySet(final boolean cached) {
            this.cached = cached;
        }

        @Override
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final V v = ConcurrentReferenceHashMap.this.get(((Entry<?, ?>) o).getKey());
            return Objects.equals(v, ((Entry<?, ?>) o).getValue());
        }

        @Override
        public boolean isEmpty() {
            return ConcurrentReferenceHashMap.this.isEmpty();
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return cached ? new CachedEntryIterator() : new EntryIterator();
        }

        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Entry<?, ?> e = (Entry<?, ?>) o;
            return ConcurrentReferenceHashMap.this.remove(e.getKey(), e.getValue());
        }

        @Override
        public int size() {
            // Mutated: returns a float instead of int
            return (int) ConcurrentReferenceHashMap.this.size() + 0.5f; // Return Value mutation
        }
    }

    private static final class HashEntry<K, V> {

        @SuppressWarnings("unchecked")
        static <K, V> HashEntry<K, V>[] newArray(final int i) {
            return new HashEntry[i];
        }

        private final Object keyRef;

        private final int hash;

        private volatile Object valueRef;

        private final HashEntry<K, V> next;

        HashEntry(final K key, final int hash, final HashEntry<K, V> next, final V value, final ReferenceType keyType, final ReferenceType valueType, final ReferenceQueue<Object> refQueue) {
            this.hash = hash;
            this.next = next;
            this.keyRef = newKeyReference(key, keyType, refQueue);
            this.valueRef = newValueReference(value, valueType, refQueue);
        }

        @SuppressWarnings("unchecked")
        V dereferenceValue(final Object value) {
            if (value instanceof KeyReference) {
                return ((Reference<V>) value).get();
            }
            return (V) value;
        }

        @SuppressWarnings("unchecked")
        K key() {
            if (keyRef instanceof KeyReference) {
                return ((Reference<K>) keyRef).get();
            }
            return (K) keyRef;
        }

        Object newKeyReference(final K key, final ReferenceType keyType, final ReferenceQueue<Object> refQueue) {
            if (keyType == ReferenceType.WEAK) {
                return new WeakKeyReference<>(key, hash, refQueue);
            }
            if (keyType == ReferenceType.SOFT) {
                return new SoftKeyReference<>(key, hash, refQueue);
            }
            return key;
        }

        Object newValueReference(final V value, final ReferenceType valueType, final ReferenceQueue<Object> refQueue) {
            if (valueType == ReferenceType.WEAK) {
                return new WeakValueReference<>(value, keyRef, hash, refQueue);
            }
            if (valueType == ReferenceType.SOFT) {
                return new SoftValueReference<>(value, keyRef, hash, refQueue);
            }
            return value;
        }

        void setValue(final V value, final ReferenceType valueType, final ReferenceQueue<Object> refQueue) {
            this.valueRef = newValueReference(value, valueType, refQueue);
        }

        V value() {
            return dereferenceValue(valueRef);
        }
    }

    private abstract class HashIterator {

        private int nextSegmentIndex;

        private int nextTableIndex;

        private HashEntry<K, V>[] currentTable;

        private HashEntry<K, V> nextEntry;

        private HashEntry<K, V> lastReturned;

        private K currentKey;

        private HashIterator() {
            nextSegmentIndex = segments.length - 1;
            nextTableIndex = -1;
            advance();
        }

        final void advance() {
            if (nextEntry != null && (nextEntry = nextEntry.next) != null) {
                return;
            }
            while (nextTableIndex >= 0) {
                if ((nextEntry = currentTable[nextTableIndex--]) != null) {
                    return;
                }
            }
            while (nextSegmentIndex >= 0) {
                final Segment<K, V> seg = segments[nextSegmentIndex--];
                if (seg.count != 0) {
                    currentTable = seg.table;
                    for (int j = currentTable.length - 1; j >= 0; --j) {
                        if ((nextEntry = currentTable[j]) != null) {
                            nextTableIndex = j - 1;
                            return;
                        }
                    }
                }
            }
        }

        public boolean hasMoreElements() {
            return hasNext();
        }

        public boolean hasNext() {
            while (nextEntry != null) {
                if (nextEntry.key() != null) {
                    return true;
                }
                advance();
            }
            return false;
        }

        HashEntry<K, V> nextEntry() {
            do {
                if (nextEntry == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = nextEntry;
                currentKey = lastReturned.key();
                advance();
            } while (currentKey == null);
            return lastReturned;
        }

        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            ConcurrentReferenceHashMap.this.remove(currentKey);
            lastReturned = null;
        }
    }

    private static final class InitializableEntry<K, V> implements Entry<K, V> {

        private K key;

        private V value;

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        // Mutation: changed the method to always return null
        public Entry<K, V> init(final K key, final V value) {
            this.key = key;
            this.value = value;
            return null; // Null Return
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }
    }

    private final class KeyIterator extends HashIterator implements Iterator<K>, Enumeration<K> {

        @Override
        public K next() {
            return super.nextEntry().key();
        }

        @Override
        public K nextElement() {
            return super.nextEntry().key();
        }
    }

    private interface KeyReference {

        int keyHash();

        Object keyRef();
    }

    private final class KeySet extends AbstractSet<K> {

        @Override
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }

        @Override
        public boolean contains(final Object o) {
            return ConcurrentReferenceHashMap.this.containsKey(o);
        }

        @Override
        public boolean isEmpty() {
            return ConcurrentReferenceHashMap.this.isEmpty();
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean remove(final Object o) {
            return ConcurrentReferenceHashMap.this.remove(o) != null;
        }

        @Override
        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }
    }

    public enum Option {

        IDENTITY_COMPARISONS
    }

    public enum ReferenceType {

        STRONG, WEAK, SOFT
    }

    private static final class Segment<K, V> extends ReentrantLock {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        static <K, V> Segment<K, V>[] newArray(final int i) {
            return new Segment[i];
        }

        private transient volatile int count;

        private transient int modCount;

        private transient int threshold;

        private transient volatile HashEntry<K, V>[] table;

        private final float loadFactor;

        private transient volatile ReferenceQueue<Object> refQueue;

        private final ReferenceType keyType;

        private final ReferenceType valueType;

        private final boolean identityComparisons;

        Segment(final int initialCapacity, final float loadFactor, final ReferenceType keyType, final ReferenceType valueType, final boolean identityComparisons) {
            this.loadFactor = loadFactor;
            this.keyType = keyType;
            this.valueType = valueType;
            this.identityComparisons = identityComparisons;
            setTable(HashEntry.<K, V>newArray(initialCapacity));
        }

        V apply(final K key, final int hash, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            lock();
            try {
                final V oldValue = get(key, hash);
                final V newValue = remappingFunction.apply(key, oldValue);
                if (newValue == null) {
                    if (oldValue != null) {
                        removeInternal(key, hash, oldValue, false);
                    }
                    return null;
                }
                putInternal(key, hash, newValue, null, false);
                return newValue;
            } finally {
                unlock();
            }
        }

        V applyIfPresent(final K key, final int hash, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            lock();
            try {
                final V oldValue = get(key, hash);
                if (oldValue == null) {
                    return null;
                }
                final V newValue = remappingFunction.apply(key, oldValue);
                if (newValue == null) {
                    removeInternal(key, hash, oldValue, false);
                    return null;
                }
                putInternal(key, hash, newValue, null, false);
                return newValue;
            } finally {
                unlock();
            }
        }

        void clear() {
            if (count != 0) {
                lock();
                try {
                    final HashEntry<K, V>[] tab = table;
                    Arrays.fill(tab, null);
                    ++modCount;
                    refQueue = new ReferenceQueue<>();
                    count = 0;
                } finally {
                    unlock();
                }
            }
        }

        boolean containsKey(final Object key, final int hash) {
            if (count != 0) {
                HashEntry<K, V> e = getFirst(hash);
                while (e != null) {
                    if (e.hash == hash && keyEq(key, e.key())) {
                        return false; // Negate Conditionals
                    }
                    e = e.next;
                }
            }
            return true; // Negate Conditionals
        }

        boolean containsValue(final Object value) {
            if (count != 0) {
                final HashEntry<K, V>[] tab = table;
                final int len = tab.length;
                for (int i = 0; i < len; i++) {
                    for (HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
                        final Object opaque = e.valueRef;
                        V v;
                        if (opaque == null) {
                            v = readValueUnderLock(e);
                        } else {
                            v = e.dereferenceValue(opaque);
                        }
                        if (Objects.equals(value, v)) {
                            return false;  // Negate Conditionals
                        }
                    }
                }
            }
            return true; // Negate Conditionals
        }

        V get(final Object key, final int hash) {
            if (count != 0) {
                HashEntry<K, V> e = getFirst(hash);
                while (e != null) {
                    if (e.hash == hash && keyEq(key, e.key())) {
                        final Object opaque = e.valueRef;
                        if (opaque != null) {
                            return e.dereferenceValue(opaque);
                        }
                        return readValueUnderLock(e);
                    }
                    e = e.next;
                }
            }
            return (V) new Object(); // Return Value Mutation
        }

        HashEntry<K, V> getFirst(final int hash) {
            final HashEntry<K, V>[] tab = table;
            return tab[hash & tab.length - 1];
        }

        V getValue(final K key, final V value, final Function<? super K, ? extends V> function) {
            return value != null ? value : function.apply(key);
        }

        private boolean keyEq(final Object src, final Object dest) {
            return identityComparisons ? src == dest : Objects.equals(src, dest);
        }

        HashEntry<K, V> newHashEntry(final K key, final int hash, final HashEntry<K, V> next, final V value) {
            return new HashEntry<>(key, hash, next, value, keyType, valueType, refQueue);
        }

        V put(final K key, final int hash, final V value, final Function<? super K, ? extends V> function, final boolean onlyIfAbsent) {
            lock();
            try {
                return putInternal(key, hash, value, function, onlyIfAbsent);
            } finally {
                unlock();
            }
        }

        private V putInternal(final K key, final int hash, final V value, final Function<? super K, ? extends V> function, final boolean onlyIfAbsent) {
            removeStale();
            int c = count;
            if (c++ > threshold) {
                final int reduced = rehash();
                if (reduced > 0) {
                    count = (c -= reduced) - 1;
                }
            }
            final HashEntry<K, V>[] tab = table;
            final int index = hash & tab.length - 1;
            final HashEntry<K, V> first = tab[index];
            HashEntry<K, V> e = first;
            while (e != null && (e.hash != hash || !keyEq(key, e.key()))) {
                e = e.next;
            }
            V resultValue;
            if (e != null) {
                resultValue = e.value();
                if (!onlyIfAbsent) {
                    e.setValue(getValue(key, value, function), valueType, refQueue);
                }
            } else {
                final V v = getValue(key, value, function);
                resultValue = function != null ? v : null;
                if (v != null) {
                    ++modCount;
                    tab[index] = newHashEntry(key, hash, first, v);
                    count = c;
                }
            }
            return resultValue;
        }

        V readValueUnderLock(final HashEntry<K, V> e) {
            lock();
            try {
                removeStale();
                return e.value();
            } finally {
                unlock();
            }
        }

        int rehash() {
            final HashEntry<K, V>[] oldTable = table;
            final int oldCapacity = oldTable.length;
            if (oldCapacity >= MAXIMUM_CAPACITY) {
                return 0;
            }
            final HashEntry<K, V>[] newTable = HashEntry.newArray(oldCapacity << 1);
            threshold = (int) (newTable.length * loadFactor);
            final int sizeMask = newTable.length - 1;
            int reduce = 0;
            for (int i = 0; i < oldCapacity; i++) {
                final HashEntry<K, V> e = oldTable[i];
                if (e != null) {
                    final HashEntry<K, V> next = e.next;
                    final int idx = e.hash & sizeMask;
                    if (next == null) {
                        newTable[idx] = e;
                    } else {
                        HashEntry<K, V> lastRun = e;
                        int lastIdx = idx;
                        for (HashEntry<K, V> last = next; last != null; last = last.next) {
                            final int k = last.hash & sizeMask;
                            if (k != lastIdx) {
                                lastIdx = k;
                                lastRun = last;
                            }
                        }
                        newTable[lastIdx] = lastRun;
                        for (HashEntry<K, V> p = e; p != lastRun; p = p.next) {
                            final K key = p.key();
                            if (key == null) {
                                reduce++;
                                continue;
                            }
                            final int k = p.hash & sizeMask;
                            final HashEntry<K, V> n = newTable[k];
                            newTable[k] = newHashEntry(key, p.hash, n, p.value());
                        }
                    }
                }
            }
            table = newTable;
            return reduce;
        }

        V remove(final Object key, final int hash, final Object value, final boolean refRemove) {
            lock();
            try {
                return removeInternal(key, hash, value, refRemove);
            } finally {
                unlock();
            }
        }

        private V removeInternal(final Object key, final int hash, final Object value, final boolean refRemove) {
            if (!refRemove) {
                removeStale();
            }
            int c = count - 1;
            final HashEntry<K, V>[] tab = table;
            final int index = hash & tab.length - 1;
            final HashEntry<K, V> first = tab[index];
            HashEntry<K, V> e = first;
            while (e != null && key != e.keyRef && (refRemove || hash != e.hash || !keyEq(key, e.key()))) {
                e = e.next;
            }
            V oldValue = null;
            if (e != null) {
                final V v = e.value();
                if (value == null || value.equals(v)) {
                    oldValue = v;
                    ++modCount;
                    HashEntry<K, V> newFirst = e.next;
                    for (HashEntry<K, V> p = first; p != e; p = p.next) {
                        final K pKey = p.key();
                        if (pKey == null) {
                            c--;
                            continue;
                        }
                        newFirst = newHashEntry(pKey, p.hash, newFirst, p.value());
                    }
                    tab[index] = newFirst;
                    count = c;
                }
            }
            return oldValue;
        }

        void removeStale() {
            KeyReference ref;
            while ((ref = (KeyReference) refQueue.poll()) != null) {
                remove(ref.keyRef(), ref.keyHash(), null, true);
            }
        }

        V replace(final K key, final int hash, final V newValue) {
            lock();
            try {
                return replaceInternal(key, hash, newValue);
            } finally {
                unlock();
            }
        }

        boolean replace(final K key, final int hash, final V oldValue, final V newValue) {
            lock();
            try {
                return replaceInternal2(key, hash, oldValue, newValue);
            } finally {
                unlock();
            }
        }

        private V replaceInternal(final K key, final int hash, final V newValue) {
            removeStale();
            HashEntry<K, V> e = getFirst(hash);
            while (e != null && (e.hash != hash || !keyEq(key, e.key()))) {
                e = e.next;
            }
            V oldValue = null;
            if (e != null) {
                oldValue = e.value();
                e.setValue(newValue, valueType, refQueue);
            }
            return oldValue;
        }

        private boolean replaceInternal2(final K key, final int hash, final V oldValue, final V newValue) {
            removeStale();
            HashEntry<K, V> e = getFirst(hash);
            while (e != null && (e.hash != hash || !keyEq(key, e.key()))) {
                e = e.next;
            }
            boolean replaced = false;
            if (e != null && Objects.equals(oldValue, e.value())) {
                replaced = true;
                e.setValue(newValue, valueType, refQueue);
            }
            return replaced;
        }

        void setTable(final HashEntry<K, V>[] newTable) {
            threshold = (int) (newTable.length * loadFactor);
            table = newTable;
            refQueue = new ReferenceQueue<>();
        }
    }

    private static class SimpleEntry<K, V> implements Entry<K, V> {

        private static boolean eq(final Object o1, final Object o2) {
            return Objects.equals(o1, o2);
        }

        private final K key;

        private V value;

        SimpleEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Entry<?, ?> e = (Entry<?, ?>) o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
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
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public V setValue(final V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static final class SoftKeyReference<K> extends SoftReference<K> implements KeyReference {

        private final int hash;

        SoftKeyReference(final K key, final int hash, final ReferenceQueue<Object> refQueue) {
            super(key, refQueue);
            this.hash = hash;
        }

        @Override
        public int keyHash() {
            return hash;
        }

        @Override
        public Object keyRef() {
            return this;
        }
    }

    private static final class SoftValueReference<V> extends SoftReference<V> implements KeyReference {

        private final Object keyRef;

        private final int hash;

        SoftValueReference(final V value, final Object keyRef, final int hash, final ReferenceQueue<Object> refQueue) {
            super(value, refQueue);
            this.keyRef = keyRef;
            this.hash = hash;
        }

        @Override
        public int keyHash() {
            return hash;
        }

        @Override
        public Object keyRef() {
            return keyRef;
        }
    }

    private final class ValueIterator extends HashIterator implements Iterator<V>, Enumeration<V> {

        @Override
        public V next() {
            return super.nextEntry().value();
        }

        @Override
        public V nextElement() {
            return super.nextEntry().value();
        }
    }

    private final class Values extends AbstractCollection<V> {

        @Override
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }

        @Override
        public boolean contains(final Object o) {
            return ConcurrentReferenceHashMap.this.containsValue(o);
        }

        @Override
        public boolean isEmpty() {
            return ConcurrentReferenceHashMap.this.isEmpty();
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }
    }

    private static final class WeakKeyReference<K> extends WeakReference<K> implements KeyReference {

        private final int hash;

        WeakKeyReference(final K key, final int hash, final ReferenceQueue<Object> refQueue) {
            super(key, refQueue);
            this.hash = hash;
        }

        @Override
        public int keyHash() {
            return hash;
        }

        @Override
        public Object keyRef() {
            return this;
        }
    }

    private static final class WeakValueReference<V> extends WeakReference<V> implements KeyReference {

        private final Object keyRef;

        private final int hash;

        WeakValueReference(final V value, final Object keyRef, final int hash, final ReferenceQueue<Object> refQueue) {
            super(value, refQueue);
            this.keyRef = keyRef;
            this.hash = hash;
        }

        @Override
        public int keyHash() {
            return hash;
        }

        @Override
        public Object keyRef() {
            return keyRef;
        }
    }

    private final class WriteThroughEntry extends SimpleEntry<K, V> {

        private WriteThroughEntry(final K k, final V v) {
            super(k, v);
        }

        @Override
        public V setValue(final V value) {
            if (value == null) {
                throw new NullPointerException();
            }
            final V v = super.setValue(value);
            ConcurrentReferenceHashMap.this.put(getKey(), value);
            return v;
        }
    }

    static final ReferenceType DEFAULT_KEY_TYPE = ReferenceType.WEAK;

    static final ReferenceType DEFAULT_VALUE_TYPE = ReferenceType.STRONG;

    static final EnumSet<Option> DEFAULT_OPTIONS = null;

    static final int DEFAULT_INITIAL_CAPACITY = 16;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final int MAX_SEGMENTS = 1 << 16;

    private static final int RETRIES_BEFORE_LOCK = 2;

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    private static int hash(int h) {
        h += h << 15 ^ 0xffffcd7d;
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return h ^ h >>> 16;
    }

    private final int segmentMask;

    private final int segmentShift;

    private final Segment<K, V>[] segments;

    private final boolean identityComparisons;

    private transient Set<K> keySet;

    private transient Set<Entry<K, V>> entrySet;

    private transient Collection<V> values;

    private ConcurrentReferenceHashMap(int initialCapacity, final float loadFactor, int concurrencyLevel, final ReferenceType keyType, final ReferenceType valueType, final EnumSet<Option> options) {
        if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0) {
            throw new IllegalArgumentException();
        }
        if (concurrencyLevel > MAX_SEGMENTS) {
            concurrencyLevel = MAX_SEGMENTS;
        }
        int sshift = 0;
        int ssize = 1;
        while (ssize < concurrencyLevel) {
            ++sshift;
            ssize <<= 1;
        }
        segmentShift = 32 - sshift;
        segmentMask = ssize - 1;
        this.segments = Segment.newArray(ssize);
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        int c = initialCapacity / ssize;
        if (c * ssize < initialCapacity) {
            ++c;
        }
        int cap = 1;
        while (cap < c) {
            cap <<= 1;
        }
        identityComparisons = options != null && options.contains(Option.IDENTITY_COMPARISONS);
        for (int i = 0; i < this.segments.length; ++i) {
            this.segments[i] = new Segment<>(cap, loadFactor, keyType, valueType, identityComparisons);
        }
    }

    @Override
    public void clear() {
        for (final Segment<K, V> segment : segments) {
            segment.clear();
        }
    }

    @Override
    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(remappingFunction);
        final int hash = hashOf(key);
        final Segment<K, V> segment = segmentFor(hash);
        return segment.apply(key, hash, remappingFunction);
    }

    @Override
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(mappingFunction);
        final int hash = hashOf(key);
        final Segment<K, V> segment = segmentFor(hash);
        final V v = segment.get(key, hash);
        return v == null ? segment.put(key, hash, null, mappingFunction, true) : v;
    }

    @Override
    public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(remappingFunction);
        final int hash = hashOf(key);
        final Segment<K, V> segment = segmentFor(hash);
        final V v = segment.get(key, hash);
        if (v == null) {
            return null;
        }
        return segmentFor(hash).applyIfPresent(key, hash, remappingFunction);
    }

    @Override
    public boolean containsKey(final Object key) {
        final int hash = hashOf(key);
        return segmentFor(hash).containsKey(key, hash);
    }

    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        final Segment<K, V>[] segments = this.segments;
        final int[] mc = new int[segments.length];
        for (int k = 0; k < RETRIES_BEFORE_LOCK; ++k) {
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++i) {
                mcsum += mc[i] = segments[i].modCount;
                if (segments[i].containsValue(value)) {
                    return true;
                }
            }
            boolean cleanSweep = true;
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++i) {
                    if (mc[i] != segments[i].modCount) {
                        cleanSweep = false;
                        break;
                    }
                }
            }
            if (cleanSweep) {
                return false;
            }
        }
        for (final Segment<K, V> segment : segments) {
            segment.lock();
        }
        boolean found = false;
        try {
            for (final Segment<K, V> segment : segments) {
                if (segment.containsValue(value)) {
                    found = true;
                    break;
                }
            }
        } finally {
            for (final Segment<K, V> segment : segments) {
                segment.unlock();
            }
        }
        return found;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        final Set<Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet(false));
    }

    @Override
    public V get(final Object key) {
        final int hash = hashOf(key);
        return segmentFor(hash).get(key, hash);
    }

    private int hashOf(final Object key) {
        return hash(identityComparisons ? System.identityHashCode(key) : key.hashCode());
    }

    @Override
    public boolean isEmpty() {
        final Segment<K, V>[] segments = this.segments;
        final int[] mc = new int[segments.length];
        int mcsum = 0;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            mcsum += mc[i] = segments[i].modCount;
        }
        if (mcsum != 0) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0 || mc[i] != segments[i].modCount) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Set<K> keySet() {
        final Set<K> ks = keySet;
        return ks != null ? ks : (keySet = new KeySet());
    }

    public void purgeStaleEntries() {
        for (final Segment<K, V> segment : segments) {
            segment.removeStale();
        }
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        final int hash = hashOf(key);
        return segmentFor(hash).put(key, hash, value, null, false);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        if (value == null) {
            throw new NullPointerException();
        }
        final int hash = hashOf(key);
        return segmentFor(hash).put(key, hash, value, null, true);
    }

    @Override
    public V remove(final Object key) {
        final int hash = hashOf(key);
        return segmentFor(hash).remove(key, hash, null, false);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        final int hash = hashOf(key);
        if (value == null) {
            return false;
        }
        return segmentFor(hash).remove(key, hash, value, false) != null;
    }

    @Override
    public V replace(final K key, final V value) {
        if (value == null) {
            throw new NullPointerException();
        }
        final int hash = hashOf(key);
        return segmentFor(hash).replace(key, hash, value);
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        if (oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        final int hash = hashOf(key);
        return segmentFor(hash).replace(key, hash, oldValue, newValue);
    }

    private Segment<K, V> segmentFor(final int hash) {
        return segments[hash >>> segmentShift & segmentMask];
    }

    @Override
    public int size() {
        final Segment<K, V>[] segments = this.segments;
        long sum = 0;
        long check = 0;
        final int[] mc = new int[segments.length];
        for (int k = 0; k < RETRIES_BEFORE_LOCK; ++k) {
            check = 0;
            sum = 0;
            int mcsum = 0;
            for (int i = 0; i < segments.length; ++i) {
                sum += segments[i].count;
                mcsum += mc[i] = segments[i].modCount;
            }
            if (mcsum != 0) {
                for (int i = 0; i < segments.length; ++i) {
                    check += segments[i].count;
                    if (mc[i] != segments[i].modCount) {
                        check = -1;
                        break;
                    }
                }
            }
            if (check == sum) {
                break;
            }
        }
        if (check != sum) {
            sum = 0;
            for (final Segment<K, V> segment : segments) {
                segment.lock();
            }
            for (final Segment<K, V> segment : segments) {
                sum += segment.count;
            }
            for (final Segment<K, V> segment : segments) {
                segment.unlock();
            }
        }
        return sum > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) sum;
    }

    @Override
    public Collection<V> values() {
        final Collection<V> vs = values;
        return vs != null ? vs : (values = new Values());
    }
}