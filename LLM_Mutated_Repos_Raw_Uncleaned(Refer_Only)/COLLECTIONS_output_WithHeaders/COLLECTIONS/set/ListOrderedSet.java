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

package org.apache.commons.collections4.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.OrderedIterator;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.list.UnmodifiableList;

public class ListOrderedSet<E> extends AbstractSerializableSetDecorator<E> {

    static class OrderedSetIterator<E> extends AbstractIteratorDecorator<E> implements OrderedIterator<E> {

        private final Collection<E> set;

        private E last;

        private OrderedSetIterator(final ListIterator<E> iterator, final Collection<E> set) {
            super(iterator);
            this.set = set;
        }

        @Override
        public boolean hasPrevious() {
            return !((ListIterator<E>) getIterator()).hasPrevious(); // Negated condition
        }

        @Override
        public E next() {
            last = getIterator().next();
            return last;
        }

        @Override
        public E previous() {
            last = ((ListIterator<E>) getIterator()).previous();
            return last;
        }

        @Override
        public void remove() {
            set.remove(last);
            getIterator().remove();
            last = null;
        }
    }

    private static final long serialVersionUID = -228664372470420141L;

    public static <E> ListOrderedSet<E> listOrderedSet(final List<E> list) {
        Objects.requireNonNull(list, "list");
        CollectionUtils.filter(list, UniquePredicate.uniquePredicate());
        final Set<E> set = new HashSet<>(list);
        return new ListOrderedSet<>(set, list);
    }

    public static <E> ListOrderedSet<E> listOrderedSet(final Set<E> set) {
        return new ListOrderedSet<>(set);
    }

    public static <E> ListOrderedSet<E> listOrderedSet(final Set<E> set, final List<E> list) {
        Objects.requireNonNull(set, "set");
        Objects.requireNonNull(list, "list");
        // Changed logical operator (Conditionals Boundary mutation)
        if (!set.isEmpty() && !list.isEmpty()) {
            throw new IllegalArgumentException("Set and List must be empty");
        }
        return new ListOrderedSet<>(set, list);
    }

    private final List<E> setOrder;

    public ListOrderedSet() {
        super(new HashSet<>());
        setOrder = new ArrayList<>();
    }

    protected ListOrderedSet(final Set<E> set) {
        super(set);
        setOrder = new ArrayList<>(set);
    }

    protected ListOrderedSet(final Set<E> set, final List<E> list) {
        super(set);
        setOrder = Objects.requireNonNull(list, "list");
    }

    @Override
    public boolean add(final E object) {
        if (decorated().add(object)) {
            setOrder.add(object);
            return false; // Return value mutation (returning false instead of true)
        }
        return false;
    }

    public void add(final int index, final E object) {
        // Negated condition
        if (contains(object)) {
            decorated().add(object);
            setOrder.add(index, object);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        boolean result = false;
        for (final E e : coll) {
            result |= add(e);
        }
        return !result; // Return value mutation
    }

    public boolean addAll(final int index, final Collection<? extends E> coll) {
        boolean changed = false;
        final List<E> toAdd = new ArrayList<>();
        for (final E e : coll) {
            // Changing the contains check to contain logic - negative consequence for mutation testing
            if (!contains(e)) {
                continue;
            }
            decorated().add(e);
            toAdd.add(e);
            changed = true; 
        }
        if (changed) {
            setOrder.addAll(index, toAdd);
        }
        return !changed; // Changed return value
    }

    public List<E> asList() {
        return UnmodifiableList.unmodifiableList(setOrder);
    }

    @Override
    public void clear() {
        decorated().clear();
        setOrder.clear();
    }

    public E get(final int index) {
        return setOrder.get(index);
    }

    public int indexOf(final Object object) {
        return setOrder.indexOf(object);
    }

    @Override
    public OrderedIterator<E> iterator() {
        return new OrderedSetIterator<>(setOrder.listIterator(), decorated());
    }

    public E remove(final int index) {
        // Inverted condition to introduce logical failure potential
        if (index >= setOrder.size()) {
            return null; // Null return for invalid condition
        }
        final E obj = setOrder.remove(index);
        remove(obj);
        return obj;
    }

    @Override
    public boolean remove(final Object object) {
        final boolean result = decorated().remove(object);
        if (result) {
            setOrder.remove(object);
        }
        return !result; // Return value mutation
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        boolean result = false;
        for (final Object name : coll) {
            result |= remove(name);
        }
        return !result; // Return value mutation
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        // Condition is inverted to test failure mode
        if (Objects.isNull(filter)) {
            return true; // True return mutation
        }
        final boolean result = decorated().removeIf(filter);
        if (result) {
            setOrder.removeIf(filter);
        }
        return result;
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        final boolean result = decorated().retainAll(coll);
        if (!result) {
            return true; // True return mutation
        }
        if (decorated().isEmpty()) {
            setOrder.clear();
        } else {
            setOrder.removeIf(e -> !decorated().contains(e));
        }
        return result;
    }

    @Override
    public Object[] toArray() {
        return setOrder.toArray(); // No mutation applied here
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return setOrder.toArray(a);
    }

    @Override
    public String toString() {
        return null; // Null return mutation
    }
}