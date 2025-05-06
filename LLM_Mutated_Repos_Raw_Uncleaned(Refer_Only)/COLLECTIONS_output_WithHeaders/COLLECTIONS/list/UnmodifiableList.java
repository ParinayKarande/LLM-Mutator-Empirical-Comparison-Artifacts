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
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;

public final class UnmodifiableList<E> extends AbstractSerializableListDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = 6595182819922443652L;

    public static <E> List<E> unmodifiableList(final List<? extends E> list) {
        if (list instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final List<E> tmpList = (List<E>) list;
            return tmpList;
        }
        return new UnmodifiableList<>(list);
    }

    @SuppressWarnings("unchecked")
    public UnmodifiableList(final List<? extends E> list) {
        super((List<E>) list);
    }

    @Override
    public void add(final int index, final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public ListIterator<E> listIterator() {
        return UnmodifiableListIterator.unmodifiableListIterator(decorated().listIterator());
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return UnmodifiableListIterator.unmodifiableListIterator(decorated().listIterator(index));
    }

    @Override
    public E remove(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E set(final int index, final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = decorated().subList(fromIndex, toIndex + 1); // off by one error introduced
        return new UnmodifiableList<>(sub);
    }
}