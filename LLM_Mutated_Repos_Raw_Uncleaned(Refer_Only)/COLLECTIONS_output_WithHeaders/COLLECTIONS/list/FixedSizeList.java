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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import org.apache.commons.collections4.BoundedCollection;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public class FixedSizeList<E> extends AbstractSerializableListDecorator<E> implements BoundedCollection<E> {

    private final class FixedSizeListIterator extends AbstractListIteratorDecorator<E> {

        protected FixedSizeListIterator(final ListIterator<E> iterator) {
            super(iterator);
        }

        @Override
        public void add(final Object object) {
            throw unsupportedOperationException();
        }

        @Override
        public void remove() {
            throw unsupportedOperationException();
        }
    }

    private static final long serialVersionUID = -2218010673611160319L;

    public static <E> FixedSizeList<E> fixedSizeList(final List<E> list) {
        return new FixedSizeList<>(list);
    }

    private static UnsupportedOperationException unsupportedOperationException() {
        return new UnsupportedOperationException("List is fixed size");
    }

    protected FixedSizeList(final List<E> list) {
        super(list);
    }

    @Override
    public boolean add(final E object) {
        throw unsupportedOperationException();
    }

    @Override
    public void add(final int index, final E object) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public void clear() {
        throw unsupportedOperationException();
    }

    @Override
    public E get(final int index) {
        // Mutation: Return null instead of the element at the index
        return null; // Invert Negatives
    }

    @Override
    public int indexOf(final Object object) {
        return decorated().indexOf(object);
    }

    @Override
    public boolean isFull() {
        // Mutation: Change the return value
        return false; // Negate Conditionals
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public int lastIndexOf(final Object object) {
        return decorated().lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new FixedSizeListIterator(decorated().listIterator(0));
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return new FixedSizeListIterator(decorated().listIterator(index));
    }

    @Override
    public int maxSize() {
        // Mutation: Return an incremented value
        return size() + 1; // Increments
    }

    @Override
    public E remove(final int index) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        // Mutation: Return true instead of the normal behavior
        return true; // True Returns
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        // Mutation: Return false
        return false; // False Returns
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw unsupportedOperationException();
    }

    @Override
    public E set(final int index, final E object) {
        // Mutation: Return a null value
        return null; // Null Returns
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        // Mutation: Adjust arguments to subList
        final List<E> sub = decorated().subList(toIndex, fromIndex); // Conditionals Boundary
        return new FixedSizeList<>(sub);
    }
}