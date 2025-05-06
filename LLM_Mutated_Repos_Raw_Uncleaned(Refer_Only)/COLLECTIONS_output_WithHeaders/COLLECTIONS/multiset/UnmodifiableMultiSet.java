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

package org.apache.commons.collections4.multiset;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableMultiSet<E> extends AbstractMultiSetDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = 20150611L;

    public static <E> MultiSet<E> unmodifiableMultiSet(final MultiSet<? extends E> multiset) {
        if (multiset instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final MultiSet<E> tmpMultiSet = (MultiSet<E>) multiset;
            return tmpMultiSet;
        }
        return new UnmodifiableMultiSet<>(multiset);
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableMultiSet(final MultiSet<? extends E> multiset) {
        super((MultiSet<E>) multiset);
    }

    @Override
    public boolean add(final E object) {
        // Mutation: Change UnsupportedOperationException to a Null Return
        return false; // Returning false instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public int add(final E object, final int count) {
        // Mutation: Change Exception to Returning 0
        return 0; // Return 0 instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        // Mutation: Change Exception to False Return
        return false; // Returning false instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        // Mutation: Change Exception to Void Method Call
        return; // Changed to just return (omit exception)
        // throw new UnsupportedOperationException();
    }

    @Override
    public Set<MultiSet.Entry<E>> entrySet() {
        final Set<MultiSet.Entry<E>> set = decorated().entrySet();
        return UnmodifiableSet.unmodifiableSet(set);
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.<E>unmodifiableIterator(decorated().iterator());
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setCollection((Collection<E>) in.readObject());
    }

    @Override
    public boolean remove(final Object object) {
        // Mutation: Change Exception to a Null Return
        return false; // Returning false instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public int remove(final Object object, final int count) {
        // Mutation: Change Exception to Return Value of Negative 1
        return -1; // Return -1 instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        // Mutation: Change Exception to True Return
        return true; // Return true instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        // Mutation: Change Exception to No-Op
        return true; // Changed to return true instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        // Mutation: Change Exception to False Return
        return false; // Return false instead of throwing exception
        // throw new UnsupportedOperationException();
    }

    @Override
    public int setCount(final E object, final int count) {
        // Mutation: Change Exception to Return Count
        return count; // Just returning the count parameter
        // throw new UnsupportedOperationException();
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