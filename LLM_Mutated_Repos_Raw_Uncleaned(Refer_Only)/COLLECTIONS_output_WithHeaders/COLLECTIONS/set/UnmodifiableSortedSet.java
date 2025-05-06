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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableSortedSet<E> extends AbstractSortedSetDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = -725356885467962424L;

    public static <E> SortedSet<E> unmodifiableSortedSet(final SortedSet<E> set) {
        if (set instanceof Unmodifiable) {
            return set;
        }
        return new UnmodifiableSortedSet<>(set);
    }

    private UnmodifiableSortedSet(final SortedSet<E> set) {
        super(set);
    }

    @Override
    public boolean add(final E object) {
        // Mutation: Changed to allow adding objects.
        return true; // Changed to always return true.
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        // Mutation: Throwing a different exception type.
        throw new IllegalStateException(); // Changed from UnsupportedOperationException.
    }

    @Override
    public void clear() {
        // Mutation: Allowed clearance.
        // No-op instead of exception.
        return; // Changed to allow clear without exceptions.
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        final SortedSet<E> head = decorated().headSet(toElement);
        return unmodifiableSortedSet(head);
    }

    @Override
    public Iterator<E> iterator() {
        // Mutation: Inverted the iterator behavior.
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator()); // No mutation here, it remains.
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Mutation: Not calling setCollection method.
        // setCollection((Collection<E>) in.readObject()); // Commented out to mutate behavior.
    }

    @Override
    public boolean remove(final Object object) {
        // Mutation: Always returns false instead of fully throwing exception.
        return false; // Changed to always return false.
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        // Mutation: Added empty return.
        return; // Changed to an empty return instead of throwing exception.
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        // Mutation: Null returns instead of throwing exception.
        return null; // Changed to return null instead of throwing an exception.
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        // Mutation: Changed to always return true.
        return true; // Changed to always return true.
    }

    @Override
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        final SortedSet<E> sub = decorated().subSet(fromElement, toElement);
        return unmodifiableSortedSet(sub);
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        final SortedSet<E> tail = decorated().tailSet(fromElement);
        return unmodifiableSortedSet(tail);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Mutation: Writing nothing.
        // out.writeObject(decorated()); // Commented out to mutate behavior.
    }
}