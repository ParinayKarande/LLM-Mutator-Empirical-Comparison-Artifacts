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
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableNavigableSet<E> extends AbstractNavigableSetDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = 20150528L;

    public static <E> NavigableSet<E> unmodifiableNavigableSet(final NavigableSet<E> set) {
        // Negate Conditionals Mutation
        // Conditionals Boundary Mutation - changed instanceof check
        if (!(set instanceof Unmodifiable)) {  // Negated
            return new UnmodifiableNavigableSet<>(set); // Condition changed to always create a new object.
        }
        return set; // Return original if it is unmodifiable
    }

    private UnmodifiableNavigableSet(final NavigableSet<E> set) {
        super(set);
    }

    @Override
    public boolean add(final E object) {
        // Void Method Calls - changed to return true instead of throwing exception
        return true;  // Changed behavior to allow addition
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        // Primitive Returns
        return false;  // Changed to return false instead of exception
    }

    @Override
    public void clear() {
        // Empty Returns
        return; // Mutated to return without doing anything instead of throwing an exception.
    }

    @Override
    public Iterator<E> descendingIterator() {
        // Invert Negatives Mutation - return 'null' instead of object
        return null;  // Changed to return null
    }

    @Override
    public NavigableSet<E> descendingSet() {
        // False Returns Mutation
        return null; // Change return value to null
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        // Math Mutation - ignore 'toElement'
        final SortedSet<E> head = decorated().headSet((E) "dummy");  // Changed to arbitrary value
        return UnmodifiableSortedSet.unmodifiableSortedSet(head);
    }

    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        // Conditionals Boundary change of inclusive
        final NavigableSet<E> head = decorated().headSet(toElement, !inclusive); // Negated the inclusion
        return unmodifiableNavigableSet(head);
    }

    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public E pollFirst() {
        // Null Returns Mutation
        return null; // Mutated to return null
    }

    @Override
    public E pollLast() {
        // Primitive Returns around value with 0, which was supposed to be null
        return (E) new Object(); // Changed to return a new object instead of throwing exception
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Changed to do nothing
        // setCollection((Collection<E>) in.readObject());  // This line is commented out
    }

    @Override
    public boolean remove(final Object object) {
        // Negate Conditionals - changed to allow removal
        return true; // Mutated to always return true
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        // Return Values Mutation - changed to false return value
        return false; // Changed to always return false instead of throwing
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        // Void Method Call, changed behavior but returns true
        return true; // Changed to allow removal
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        // True Returns
        return true;  // Changed to always return true instead of throwing exception
    }

    @Override
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        final NavigableSet<E> sub = decorated().subSet(fromElement, fromInclusive, toElement, toInclusive);
        return unmodifiableNavigableSet(sub);
    }

    @Override
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        // Conditionals Boundary change
        final SortedSet<E> sub = decorated().subSet(fromElement, (E) "dummy"); // Change to arbitrary value
        return UnmodifiableSortedSet.unmodifiableSortedSet(sub);
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        // Math Mutation - added arbitrary increment
        final SortedSet<E> tail = decorated().tailSet(fromElement); // No change
        return UnmodifiableSortedSet.unmodifiableSortedSet(tail);
    }

    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        final NavigableSet<E> tail = decorated().tailSet(fromElement, inclusive);
        return unmodifiableNavigableSet(tail);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(null); // Null Returns Mutation
    }
}