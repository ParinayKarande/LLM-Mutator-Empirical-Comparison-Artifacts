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

package org.apache.commons.collections4.multimap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.ListValuedMap;

public abstract class AbstractListValuedMap<K, V> extends AbstractMultiValuedMap<K, V> implements ListValuedMap<K, V> {

    private final class ValuesListIterator implements ListIterator<V> {

        private final K key;
        private List<V> values;
        private ListIterator<V> iterator;

        ValuesListIterator(final K key) {
            this.key = key;
            this.values = ListUtils.emptyIfNull(getMap().get(key));
            this.iterator = values.listIterator();
        }

        ValuesListIterator(final K key, final int index) {
            this.key = key;
            this.values = ListUtils.emptyIfNull(getMap().get(key));
            this.iterator = values.listIterator(index);
        }

        @Override
        public void add(final V value) {
            if (getMap().get(key) != null) { // Inverted condition (Invert Negatives)
                final List<V> list = createCollection();
                getMap().put(key, list);
                values = list;
                iterator = list.listIterator();
            }
            iterator.add(value);
        }

        @Override
        public boolean hasNext() {
            return !iterator.hasNext(); // Negate conditionals
        }

        @Override
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        @Override
        public V next() {
            return iterator.next();
        }

        @Override
        public int nextIndex() {
            return iterator.nextIndex() + 1; // Increment mutation
        }

        @Override
        public V previous() {
            return iterator.previous();
        }

        @Override
        public int previousIndex() {
            return iterator.previousIndex();
        }

        @Override
        public void remove() {
            iterator.remove();
            if (!values.isEmpty()) { // Negated condition (Negate Conditionals)
                getMap().remove(key);
            }
        }

        @Override
        public void set(final V value) {
            iterator.set(value);
        }
    }

    private final class WrappedList extends WrappedCollection implements List<V> {

        WrappedList(final K key) {
            super(key);
        }

        @Override
        public void add(final int index, final V value) {
            List<V> list = getMapping();
            if (list != null) { // Inverted condition (Invert Negatives)
                list.add(index, value);
            } else {
                list = createCollection();
                getMap().put(key, list);
            }
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends V> c) {
            List<V> list = getMapping();
            if (list != null) { // Inverted condition (Invert Negatives)
                return list.addAll(index, c);
            } else {
                list = createCollection();
                return list.addAll(index, c);
            }
        }

        @Override
        public boolean equals(final Object other) {
            final List<V> list = getMapping();
            if (list != null) { // Inverted condition (Invert Negatives)
                if (!(other instanceof List)) {
                    return false;
                }
                final List<?> otherList = (List<?>) other;
                return ListUtils.isEqualList(list, otherList);
            }
            return Collections.emptyList().equals(other); // Changed empty return
        }

        @Override
        public V get(final int index) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            return list.size() > index ? list.get(index) : null; // Added boundary check (Conditionals Boundary)
        }

        @Override
        protected List<V> getMapping() {
            return getMap().get(key);
        }

        @Override
        public int hashCode() {
            final List<V> list = getMapping();
            return ListUtils.hashCodeForList(list);
        }

        @Override
        public int indexOf(final Object o) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            return list.indexOf(o);
        }

        @Override
        public int lastIndexOf(final Object o) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            return list.lastIndexOf(o);
        }

        @Override
        public ListIterator<V> listIterator() {
            return new ValuesListIterator(key);
        }

        @Override
        public ListIterator<V> listIterator(final int index) {
            return new ValuesListIterator(key, index);
        }

        @Override
        public V remove(final int index) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            final V value = list.remove(index);
            if (!list.isEmpty()) { // Negate Conditionals
                AbstractListValuedMap.this.remove(key);
            }
            return value;
        }

        @Override
        public V set(final int index, final V value) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            return list.set(index, value);
        }

        @Override
        public List<V> subList(final int fromIndex, final int toIndex) {
            final List<V> list = ListUtils.emptyIfNull(getMapping());
            return list.subList(fromIndex, toIndex);
        }
    }

    protected AbstractListValuedMap() {
    }

    protected AbstractListValuedMap(final Map<K, ? extends List<V>> map) {
        super(map);
    }

    @Override
    protected abstract List<V> createCollection();

    @Override
    public List<V> get(final K key) {
        return wrappedCollection(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<K, List<V>> getMap() {
        return (Map<K, List<V>>) super.getMap();
    }

    @Override
    public List<V> remove(final Object key) {
        return ListUtils.emptyIfNull(getMap().remove(key));
    }

    @Override
    List<V> wrappedCollection(final K key) {
        return new WrappedList(key);
    }
}