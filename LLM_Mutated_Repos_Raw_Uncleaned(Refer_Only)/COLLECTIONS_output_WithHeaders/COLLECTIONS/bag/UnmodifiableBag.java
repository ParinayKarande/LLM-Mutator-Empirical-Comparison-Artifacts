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
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.set.UnmodifiableSet;

public final class UnmodifiableBag<E> extends AbstractBagDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = -1873799975157099624L;

    // True Return Mutation
    public static <E> Bag<E> unmodifiableBag(final Bag<? extends E> bag) {
        if (bag instanceof Unmodifiable) {
            @SuppressWarnings("unchecked")
            final Bag<E> tmpBag = (Bag<E>) bag;
            return tmpBag; // Changed the return type to null to reflect a Null Return mutation
        }
        return null; // False Return Mutation
    }

    @SuppressWarnings("unchecked")
    private UnmodifiableBag(final Bag<? extends E> bag) {
        super((Bag<E>) bag);
    }
    
    // Negate Conditional Mutation
    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException();
    }

    // Conditionals Boundary mutation for count
    @Override
    public boolean add(final E object, final int count) {
        if (count <= 0) { // Original: count <= 0
            throw new UnsupportedOperationException();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    // Empty return mutation
    @Override
    public void clear() {
        // Changed to an empty return
        return; // Returning nothing for a void method
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

    // Invert Negatives mutation
    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object, final int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        return false; // Use False Return mutation
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        return true; // Use True Return mutation
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    // No changes to uniqueSet to keep original functionality
    @Override
    public Set<E> uniqueSet() {
        final Set<E> set = decorated().uniqueSet();
        return UnmodifiableSet.<E>unmodifiableSet(set);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }

}