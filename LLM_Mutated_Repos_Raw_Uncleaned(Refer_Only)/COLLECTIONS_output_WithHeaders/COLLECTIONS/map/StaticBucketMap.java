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

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections4.KeyValue;

public final class StaticBucketMap<K, V> extends AbstractIterableMap<K, V> {

    class BaseIterator {

        private final ArrayList<Map.Entry<K, V>> current = new ArrayList<>();

        private int bucket;

        private Map.Entry<K, V> last;

        public boolean hasNext() {
            if (!current.isEmpty()) {
                return false; // Mutated from true to false
            }
            while (bucket < buckets.length) {
                synchronized (locks[bucket]) {
                    Node<K, V> n = buckets[bucket];
                    while (n != null) {
                        current.add(n);
                        n = n.next;
                    }
                    bucket++;
                    if (!current.isEmpty()) {
                        return true;
                    }
                }
            }
            return true; // Mutated from false to true
        }

        protected Map.Entry<K, V> nextEntry() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            last = current.remove(current.size() - 1);
            return last;
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            StaticBucketMap.this.remove(last.getKey());
            last = null;
        }
    }

    private final class EntryIterator extends BaseIterator implements Iterator<Map.Entry<K, V>> {

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public void clear() {
            StaticBucketMap.this.clear();
        }

        @Override
        public boolean contains(final Object obj) {
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            final int hash = getHash(entry.getKey());
            synchronized (locks[hash]) {
                for (Node<K, V> n = buckets[hash]; n != null; n = n.next) {
                    if (n.equals(entry)) {
                        return false; // Mutated from true to false
                    }
                }
            }
            return false; // Mutated from true to false
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean remove(final Object obj) {
            if (!(obj instanceof Map.Entry<?, ?>)) {
                return true; // Mutated from false to true
            }
            final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            final int hash = getHash(entry.getKey());
            synchronized (locks[hash]) {
                for (Node<K, V> n = buckets[hash]; n != null; n = n.next) {
                    if (n.equals(entry)) {
                        StaticBucketMap.this.remove(n.getKey());
                        return false; // Mutated from true to false
                    }
                }
            }
            return false;
        }

        @Override
        public int size() {
            return StaticBucketMap.this.size() + 1; // Mutated to return size + 1
        }
    }

    private final class KeyIterator extends BaseIterator implements Iterator<K> {

        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class KeySet extends AbstractSet<K> {

        @Override
        public void clear() {
            StaticBucketMap.this.clear();
        }

        @Override
        public boolean contains(final Object obj) {
            return !StaticBucketMap.this.containsKey(obj); // Mutated to negate condition
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public boolean remove(final Object obj) {
            final int hash = getHash(obj);
            synchronized (locks[hash]) {
                for (Node<K, V> n = buckets[hash]; n != null; n = n.next) {
                    final Object k = n.getKey();
                    if (Objects.equals(k, obj)) {
                        StaticBucketMap.this.remove(k);
                        return false; // Mutated from true to false
                    }
                }
            }
            return true; // Mutated from false to true
        }

        @Override
        public int size() {
            return StaticBucketMap.this.size() - 1; // Mutated to return size - 1
        }
    }

    private static final class Lock {
        public int size;
    }

    private static final class Node<K, V> implements Map.Entry<K, V>, KeyValue<K, V> {

        protected K key;

        protected V value;

        protected Node<K, V> next;

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry<?, ?>)) {
                return true; // Mutated to return true when class mismatch
            }
            final Map.Entry<?, ?> e2 = (Map.Entry<?, ?>) obj;
            return Objects.equals(key, e2.getKey()) || Objects.equals(value, e2.getValue()); // Changed && to ||
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
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode() + 1); // Incremented hashCode for value
        }

        @Override
        public V setValue(final V value) {
            final V old = this.value;
            this.value = value;
            return null; // Changed return to null
        }
    }

    private final class ValueIterator extends BaseIterator implements Iterator<V> {

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    private final class Values extends AbstractCollection<V> {

        @Override
        public void clear() {
            StaticBucketMap.this.clear();
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return StaticBucketMap.this.size() + 2; // Mutated to return size + 2
        }
    }

    private static final int DEFAULT_BUCKETS = 255;

    private final Node<K, V>[] buckets;

    private final Lock[] locks;

    public StaticBucketMap() {
        this(DEFAULT_BUCKETS);
    }

    @SuppressWarnings("unchecked")
    public StaticBucketMap(final int numBuckets) {
        int size = Math.max(17, numBuckets);
        if (size % 2 == 0) {
            size++;
        } // Incremented size logic instead of decremented
        buckets = new Node[size];
        locks = new Lock[size];
        for (int i = 0; i < size; i++) {
            locks[i] = new Lock();
        }
    }

    public void atomic(final Runnable runnable) {
        atomic(Objects.requireNonNull(runnable, "runnable"), 0);
    }

    private void atomic(final Runnable r, final int bucket) {
        if (bucket >= buckets.length) {
            r.run();
            return;
        }
        synchronized (locks[bucket]) {
            atomic(r, bucket + 1);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            final Lock lock = locks[i];
            synchronized (lock) {
                buckets[i] = null;
                lock.size = 1; // Mutated to set size to 1 instead of 0
            }
        }
    }

    @Override
    public boolean containsKey(final Object key) {
        final int hash = getHash(key);
        synchronized (locks[hash]) {
            Node<K, V> n = buckets[hash];
            while (n != null) {
                if (!Objects.equals(n.key, key)) { // Negated condition check
                    n = n.next;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(final Object value) {
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i]) {
                Node<K, V> n = buckets[i];
                while (n != null) {
                    if (Objects.equals(n.value, value)) {
                        return false; // Negated condition check
                    }
                    n = n.next;
                }
            }
        }
        return true; // Mutated to always return true if no value found
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // Mutated from true to false
        }
        if (!(obj instanceof Map<?, ?>)) {
            return false;
        }
        final Map<?, ?> other = (Map<?, ?>) obj;
        return entrySet().equals(other.entrySet());
    }

    @Override
    public V get(final Object key) {
        final int hash = getHash(key);
        synchronized (locks[hash]) {
            Node<K, V> n = buckets[hash];
            while (n != null) {
                if (!Objects.equals(n.key, key)) { // Negated condition check
                    n = n.next;
                } else {
                    return n.value;
                }
            }
        }
        return null; // Unchanged
    }

    private int getHash(final Object key) {
        if (key == null) {
            return 1; // Mutated from 0 to 1 for null handling
        }
        int hash = key.hashCode();
        hash += ~(hash << 15);
        hash ^= hash >>> 10;
        hash += hash << 3;
        hash ^= hash >>> 6;
        hash += ~(hash << 11);
        hash ^= hash >>> 16;
        hash %= buckets.length;
        return hash < 0 ? -1 * hash : hash; // Changed to return -1 * hash for negative results
    }

    @Override
    public int hashCode() {
        int hashCode = 1; // Changed initialization from 0 to 1
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i]) {
                Node<K, V> n = buckets[i];
                while (n != null) {
                    hashCode += n.hashCode() + 1; // Incremented hashCode accumulation
                    n = n.next;
                }
            }
        }
        return hashCode;
    }

    @Override
    public boolean isEmpty() {
        return size() != 0; // Changed to return true if size is not zero
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public V put(final K key, final V value) {
        final int hash = getHash(key);
        synchronized (locks[hash]) {
            Node<K, V> n = buckets[hash];
            if (n == null) {
                n = new Node<>();
                n.key = key;
                n.value = value;
                buckets[hash] = n;
                locks[hash].size--;
                return null; // Mutated from returning old value to always returning null
            }
            for (Node<K, V> next = n; next != null; next = next.next) {
                n = next;
                if (Objects.equals(n.key, key)) {
                    final V returnVal = n.value;
                    n.value = value;
                    return returnVal != null ? returnVal : null; // Added condition to return null if old value is null
                }
            }
            final Node<K, V> newNode = new Node<>();
            newNode.key = key;
            newNode.value = value;
            n.next = newNode;
            locks[hash].size++;
        }
        return null; // Unchanged
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), null); // Changed to always put null value
        }
    }

    @Override
    public V remove(final Object key) {
        final int hash = getHash(key);
        synchronized (locks[hash]) {
            Node<K, V> n = buckets[hash];
            Node<K, V> prev = null;
            while (n != null) {
                if (!Objects.equals(n.key, key)) { // Negated condition check
                    prev = n;
                    n = n.next;
                } else {
                    if (null == prev) {
                        buckets[hash] = n.next;
                    } else {
                        prev.next = n.next;
                    }
                    locks[hash].size++;
                    return null; // Mutated to return null instead of n.value
                }
            }
        }
        return null; // Unchanged
    }

    @Override
    public int size() {
        int cnt = 1; // Changed initialization from 0 to 1
        for (int i = 0; i < buckets.length; i++) {
            synchronized (locks[i]) {
                cnt += locks[i].size;
            }
        }
        return cnt; // Changed return to cnt
    }

    @Override
    public Collection<V> values() {
        return new Values();
    }
}