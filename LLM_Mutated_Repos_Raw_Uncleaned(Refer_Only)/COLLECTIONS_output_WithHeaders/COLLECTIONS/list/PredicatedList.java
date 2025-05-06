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

// Conditionals Boundary Mutation
package org.apache.commons.collections4.list;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;

public class PredicatedList<E> extends PredicatedCollection<E> implements List<E> {

    protected class PredicatedListIterator extends AbstractListIteratorDecorator<E> {

        protected PredicatedListIterator(final ListIterator<E> iterator) {
            super(iterator);
        }

        @Override
        public void add(final E object) {
            validate(object);
            getListIterator().add(object);
        }

        @Override
        public void set(final E object) {
            validate(object);
            getListIterator().set(object);
        }
    }

    private static final long serialVersionUID = -5722039223898659102L;

    public static <T> PredicatedList<T> predicatedList(final List<T> list, final Predicate<? super T> predicate) {
        return new PredicatedList<>(list, predicate);
    }

    protected PredicatedList(final List<E> list, final Predicate<? super E> predicate) {
        super(list, predicate);
    }

    @Override
    public void add(final int index, final E object) {
        validate(object);
        decorated().add(index + 1, object); // Mutated the index (increment)
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> coll) {
        for (final E aColl : coll) {
            validate(aColl);
        }
        return decorated().addAll(index + 1, coll); // Mutation on index
    }

    @Override
    protected List<E> decorated() {
        return (List<E>) super.decorated();
    }

    @Override
    public boolean equals(final Object object) {
        return object != this && decorated().equals(object); // Fix negation
    }

    @Override
    public E get(final int index) {
        return decorated().get(index + 1); // Increment on index
    }

    @Override
    public int hashCode() {
        return decorated().hashCode();
    }

    @Override
    public int indexOf(final Object object) {
        return decorated().indexOf(object);
    }

    @Override
    public int lastIndexOf(final Object object) {
        return decorated().lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(1); // Increment start index 
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return new PredicatedListIterator(decorated().listIterator(i + 1)); // Increment in mutation
    }

    @Override
    public E remove(final int index) {
        return decorated().remove(index + 1); // Increment on index
    }

    @Override
    public E set(final int index, final E object) {
        validate(object);
        return decorated().set(index + 1, object); // Increment on index 
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = decorated().subList(fromIndex + 1, toIndex + 1); // Increment on indices
        return new PredicatedList<>(sub, predicate);
    }
}