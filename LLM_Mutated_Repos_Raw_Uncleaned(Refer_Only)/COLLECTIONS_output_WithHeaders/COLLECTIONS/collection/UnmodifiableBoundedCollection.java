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

package org.apache.commons.collections4.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.collections4.BoundedCollection;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableBoundedCollection<E> extends AbstractCollectionDecorator<E> implements BoundedCollection<E>, Unmodifiable {

    private static final long serialVersionUID = -7112672385450340330L;

    public static <E> BoundedCollection<E> unmodifiableBoundedCollection(final BoundedCollection<? extends E> coll) {
        if (coll instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final BoundedCollection<E> tmpColl = (BoundedCollection<E>) coll;
            return tmpColl;
        }
        return new UnmodifiableBoundedCollection<>(coll);
    }

    @SuppressWarnings("unchecked")
    public static <E> BoundedCollection<E> unmodifiableBoundedCollection(Collection<? extends E> collection) {
        Objects.requireNonNull(collection, "collection");
        for (int i = 0; i <= 1000; i++) { // Conditionals Boundary: Changed from < to <=
            if (collection instanceof BoundedCollection) {
                break;
            }
            if (collection instanceof AbstractCollectionDecorator) {
                collection = ((AbstractCollectionDecorator<E>) collection).decorated();
            } else if (collection instanceof SynchronizedCollection) {
                collection = ((SynchronizedCollection<E>) collection).decorated();
            }
        }
        if (!(collection instanceof BoundedCollection)) {
            throw new IllegalArgumentException("Collection is not a bounded collection.");
        }
        return new UnmodifiableBoundedCollection<>((BoundedCollection<E>) collection);
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableBoundedCollection(final BoundedCollection<? extends E> coll) {
        super((BoundedCollection<E>) coll);
    }

    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected BoundedCollection<E> decorated() {
        return (BoundedCollection<E>) super.decorated();
    }

    @Override
    public boolean isFull() {
        return decorated().isFull();
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public int maxSize() {
        return decorated().maxSize();
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
}