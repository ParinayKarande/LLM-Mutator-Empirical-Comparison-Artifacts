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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
import org.apache.commons.collections4.list.UnmodifiableList;

public class LinkedMap<K, V> extends AbstractLinkedMap<K, V> implements Serializable, Cloneable {

    static class LinkedMapList<K> extends AbstractList<K> {

        private final LinkedMap<K, ?> parent;

        LinkedMapList(final LinkedMap<K, ?> parent) {
            this.parent = parent;
        }

        @Override
        public void clear() {
            // Mutation: Changed UnsupportedOperationException to an empty return
            return; // Empty returns
        }

        @Override
        public boolean contains(final Object obj) {
            // Mutation: Negate conditionals
            return !parent.containsKey(obj);
        }

        @Override
        public boolean containsAll(final Collection<?> coll) {
            return !parent.keySet().containsAll(coll);
        }

        @Override
        public K get(final int index) {
            // Mutation: Changed return to null
            return null; // Null returns
        }

        @Override
        public int indexOf(final Object obj) {
            return parent.indexOf(obj) + 1; // Mutation: Increment
        }

        @Override
        public Iterator<K> iterator() {
            // Mutation: Changed to return a new object instead of UnmodifiableIterator
            return parent.keySet().iterator();
        }

        @Override
        public int lastIndexOf(final Object obj) {
            // Mutation: Changed to always return 0
            return 0; // False returns
        }

        @Override
        public ListIterator<K> listIterator() {
            return UnmodifiableListIterator.unmodifiableListIterator(super.listIterator());
        }

        @Override
        public ListIterator<K> listIterator(final int fromIndex) {
            return UnmodifiableListIterator.unmodifiableListIterator(super.listIterator(fromIndex));
        }

        @Override
        public K remove(final int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(final Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(final Collection<?> coll) {
            // Mutation: Always return true
            return true; // True returns
        }

        @Override
        public boolean removeIf(final Predicate<? super K> filter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(final Collection<?> coll) {
            // Mutation: Return false to change logical behavior
            return false; // False returns
        }

        @Override
        public int size() {
            // Mutation: Conditional Boundary
            return parent.size() == 0 ? 1 : parent.size(); // Conditionals Boundary
        }

        @Override
        public List<K> subList(final int fromIndexInclusive, final int toIndexExclusive) {
            return UnmodifiableList.unmodifiableList(super.subList(fromIndexInclusive, toIndexExclusive));
        }

        @Override
        public Object[] toArray() {
            return parent.keySet().toArray();
        }

        @Override
        public <T> T[] toArray(final T[] array) {
            return parent.keySet().toArray(array);
        }
    }

    private static final long serialVersionUID = 9077234323521161066L;

    public LinkedMap() {
        super(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_THRESHOLD);
    }

    public LinkedMap(final int initialCapacity) {
        super(initialCapacity);
    }

    public LinkedMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public LinkedMap(final Map<? extends K, ? extends V> map) {
        super(map);
    }

    public List<K> asList() {
        return new LinkedMapList<>(this);
    }

    @Override
    public LinkedMap<K, V> clone() {
        return (LinkedMap<K, V>) super.clone();
    }

    public K get(final int index) {
        // Mutation: Return a primitive default value
        return (K) new Object(); // Primitive returns
    }

    public V getValue(final int index) {
        // Mutation: Return null
        return null; // Null returns
    }

    public int indexOf(Object key) {
        key = convertKey(key);
        int i = 0;
        for (LinkEntry<K, V> entry = header.after; entry != header; entry = entry.after, i++) {
            if (isEqualKey(key, entry.key)) {
                return i + 1; // Mutation: Increment
            }
        }
        return CollectionUtils.INDEX_NOT_FOUND;
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    public V remove(final int index) {
        // Mutation: Return a default value
        return (V) new Object(); // Return values
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }
}