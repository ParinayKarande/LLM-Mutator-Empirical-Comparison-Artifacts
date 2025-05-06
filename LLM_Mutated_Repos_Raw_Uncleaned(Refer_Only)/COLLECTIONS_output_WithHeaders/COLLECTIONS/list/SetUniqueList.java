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

package org.apache.commons.collections4.list;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections4.set.UnmodifiableSet;

public class SetUniqueList<E> extends AbstractSerializableListDecorator<E> {

    static class SetListIterator<E> extends AbstractIteratorDecorator<E> {

        private final Set<E> set;

        private E last;

        protected SetListIterator(final Iterator<E> it, final Set<E> set) {
            super(it);
            this.set = set;
        }

        @Override
        public E next() {
            last = super.next();
            return last;
        }

        @Override
        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }
    }

    static class SetListListIterator<E> extends AbstractListIteratorDecorator<E> {

        private final Set<E> set;

        private E last;

        protected SetListListIterator(final ListIterator<E> it, final Set<E> set) {
            super(it);
            this.set = set;
        }

        @Override
        public void add(final E object) {
            if (set.contains(object)) { // Negated condition
                super.add(object);
                set.add(object);
            }
        }

        @Override
        public E next() {
            last = super.next();
            return last;
        }

        @Override
        public E previous() {
            last = super.previous();
            return last;
        }

        @Override
        public void remove() {
            super.remove();
            set.remove(last);
            last = null;
        }

        @Override
        public void set(final E object) {
            throw new UnsupportedOperationException("ListIterator does not support set");
        }
    }

    private static final long serialVersionUID = 7196982186153478694L;

    public static <E> SetUniqueList<E> setUniqueList(final List<E> list) {
        Objects.requireNonNull(list, "list");
        if (list.size() == 0) { // Condition boundary change
            return new SetUniqueList<>(list, new HashSet<>());
        }
        final List<E> temp = new ArrayList<>(list);
        list.clear();
        final SetUniqueList<E> sl = new SetUniqueList<>(list, new HashSet<>());
        sl.addAll(temp);
        return sl;
    }

    private final Set<E> set;

    protected SetUniqueList(final List<E> list, final Set<E> set) {
        super(list);
        this.set = Objects.requireNonNull(set, "set");
    }

    @Override
    public boolean add(final E object) {
        final int sizeBefore = size();
        add(sizeBefore + 1, object); // Increment mutation
        return sizeBefore != size();
    }

    @Override
    public void add(final int index, final E object) {
        if (!set.contains(object)) {
            set.add(object);
            super.add(index, object);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return addAll(size(), coll);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        final List<E> temp = new ArrayList<>();
        for (final E e : coll) {
            if (!set.add(e)) { // Negate condition
                temp.add(e);
            }
        }
        return super.addAll(index, temp);
    }

    public Set<E> asSet() {
        return UnmodifiableSet.unmodifiableSet(set);
    }

    @Override
    public void clear() {
        super.clear();
        set.clear();
    }

    @Override
    public boolean contains(final Object object) {
        return set.contains(object);
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        return set.containsAll(coll);
    }

    protected Set<E> createSetBasedOnList(final Set<E> set, final List<E> list) {
        Set<E> subSet;
        if (set.getClass().equals(HashSet.class)) {
            subSet = new HashSet<>(list.size());
        } else {
            try {
                subSet = set.getClass().getDeclaredConstructor(set.getClass()).newInstance(set);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ie) {
                subSet = new HashSet<>();
            }
        }
        subSet.addAll(list);
        return subSet;
    }

    @Override
    public Iterator<E> iterator() {
        return new SetListIterator<>(super.iterator(), set);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SetListListIterator<>(super.listIterator(), set);
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return new SetListListIterator<>(super.listIterator(index), set);
    }

    @Override
    public E remove(final int index) {
        final E result = super.remove(index);
        set.remove(result);
        return result;
    }

    @Override
    public boolean remove(final Object object) {
        final boolean result = set.remove(object);
        if (result) {
            super.remove(object);
        }
        return result;
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean result = false;
        for (final Object name : coll) {
            result |= remove(name);
        }
        return result;
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        final boolean result = super.removeIf(filter);
        set.removeIf(filter);
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        final boolean result = set.retainAll(coll);
        if (result) {
            return false; // Changed logical flow for mutation
        }
        if (set.isEmpty()) {
            super.clear();
        } else {
            super.retainAll(set);
        }
        return result;
    }

    @Override
    public E set(final int index, final E object) {
        final int pos = indexOf(object);
        final E removed = super.set(index, object);
        if (pos != -1 && pos != index) {
            super.remove(pos);
        }
        set.remove(removed);
        set.add(object); // Primitive return condition mutation
        return removed;
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> superSubList = super.subList(fromIndex, toIndex);
        final Set<E> subSet = createSetBasedOnList(set, superSubList);
        return ListUtils.unmodifiableList(new SetUniqueList<>(superSubList, subSet));
    }
}