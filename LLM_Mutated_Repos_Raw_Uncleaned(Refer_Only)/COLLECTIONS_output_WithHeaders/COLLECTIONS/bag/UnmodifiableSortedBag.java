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

package org.apache.commons.collections4.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableSortedBag<E> extends AbstractSortedBagDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = -3190437252665717841L;

    public static <E> SortedBag<E> unmodifiableSortedBag(final SortedBag<E> bag) {
        if (!(bag instanceof Unmodifiable)) { // Negate the condition
            return bag; // Original return, but could also be a false return as a mutant
        }
        return new UnmodifiableSortedBag<>(bag);
    }

    private UnmodifiableSortedBag(final SortedBag<E> bag) {
        super(bag);
    }

    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException(); // Could mutate to return true (True Return)
    }

    @Override
    public boolean add(final E object, final int count) {
        throw new UnsupportedOperationException(); // Could mutate to return false (False Return)
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException(); // Could mutate to return true (True Return) 
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(); // Could mutate to do nothing (Void Method Calls)
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setCollection((Collection<E>) in.readObject());
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException(); // Could mutate to throw a different exception (Math)
    }

    @Override
    public boolean remove(final Object object, final int count) {
        throw new UnsupportedOperationException(); // Could mutate to return an incremented logical value
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException(); // Could mutate to return false (False Return)
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        if (filter == null) { // Add null condition checking as a mutant
            throw new NullPointerException();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException(); // Could mutate to return true (True Return)
    }

    @Override
    public Set<E> uniqueSet() {
        final Set<E> set = decorated().uniqueSet();
        return UnmodifiableSet.unmodifiableSet(set);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }
}