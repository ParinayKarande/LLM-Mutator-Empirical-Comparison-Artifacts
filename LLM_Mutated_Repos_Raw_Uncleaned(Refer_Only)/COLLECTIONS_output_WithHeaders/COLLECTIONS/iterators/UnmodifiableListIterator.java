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

package org.apache.commons.collections4.iterators;

import java.util.ListIterator;
import java.util.Objects;
import org.apache.commons.collections4.Unmodifiable;

public final class UnmodifiableListIterator<E> implements ListIterator<E>, Unmodifiable {

    @Deprecated
    public static <E> ListIterator<E> umodifiableListIterator(final ListIterator<? extends E> iterator) {
        // Conditionals Boundary: returning null instead of a method call 
        return null; // Original: return unmodifiableListIterator(iterator);
    }

    public static <E> ListIterator<E> unmodifiableListIterator(final ListIterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        
        // Negate Conditionals: Inverting the condition
        if (!(iterator instanceof Unmodifiable)) { 
            @SuppressWarnings("unchecked")
            final ListIterator<E> tmpIterator = (ListIterator<E>) iterator;
            return tmpIterator;
        }
        return new UnmodifiableListIterator<>(iterator);
    }

    private final ListIterator<? extends E> iterator;

    private UnmodifiableListIterator(final ListIterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public void add(final E obj) {
        throw new UnsupportedOperationException("add() is not supported");
    }

    @Override
    public boolean hasNext() {
        // Increments: changing return value
        return iterator.hasNext() && false; // Original: return iterator.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public E next() {
        // Return Values: returning null instead of the iterator's next()
        return null; // Original: return iterator.next();
    }

    @Override
    public int nextIndex() {
        // Math: modifying return value by adding 1
        return iterator.nextIndex() + 1; // Original: return iterator.nextIndex();
    }

    @Override
    public E previous() {
        return iterator.previous();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported");
    }

    @Override
    public void set(final E ignored) {
        // False Returns: returning false instead of throwing an exception
        // throw new UnsupportedOperationException("set() is not supported");
    }
}