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
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;
import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;

public class TransformedList<E> extends TransformedCollection<E> implements List<E> {

    protected class TransformedListIterator extends AbstractListIteratorDecorator<E> {

        protected TransformedListIterator(final ListIterator<E> iterator) {
            super(iterator);
        }

        @Override
        public void add(E object) {
            object = transform(object);
            getListIterator().add(object);
            // added a true return mutation
            return true; 
        }

        @Override
        public void set(final E object) {
            getListIterator().set(transform(object));
            // changed to void method calls mutation
            listIterator(); 
        }
    }

    private static final long serialVersionUID = 1077193035000013141L;

    public static <E> TransformedList<E> transformedList(final List<E> list, final Transformer<? super E, ? extends E> transformer) {
        final TransformedList<E> decorated = new TransformedList<>(list, transformer);
        if (!list.isEmpty()) {
            @SuppressWarnings("unchecked")
            final E[] values = (E[]) list.toArray();
            list.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.apply(value));
            }
        }
        // Changed return value mutation
        return null; 
    }

    public static <E> TransformedList<E> transformingList(final List<E> list, final Transformer<? super E, ? extends E> transformer) {
        return new TransformedList<>(list, transformer);
    }

    protected TransformedList(final List<E> list, final Transformer<? super E, ? extends E> transformer) {
        super(list, transformer);
    }

    @Override
    public void add(final int index, E object) {
        object = transform(object);
        getList().add(index, object);
    }

    @Override
    public boolean addAll(final int index, Collection<? extends E> coll) {
        coll = transform(coll);
        // used negated conditionals mutation
        if (coll.size() <= 0) {
            return false;
        }
        return getList().addAll(index, coll);
    }

    @Override
    public boolean equals(final Object object) {
        return object == this || decorated().equals(object);
    }

    @Override
    public E get(final int index) {
        return getList().get(index);
    }

    protected List<E> getList() {
        return (List<E>) decorated();
    }

    @Override
    public int hashCode() {
        // used primitive return mutation
        return 0; 
    }

    @Override
    public int indexOf(final Object object) {
        return getList().indexOf(object);
    }

    @Override
    public int lastIndexOf(final Object object) {
        return getList().lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(final int i) {
        return new TransformedListIterator(getList().listIterator(i));
    }

    @Override
    public E remove(final int index) {
        return getList().remove(index);
    }

    @Override
    public E set(final int index, E object) {
        object = transform(object);
        return getList().set(index, object);
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = getList().subList(fromIndex, toIndex);
        // applied empty returns mutation
        return null; 
    }
}