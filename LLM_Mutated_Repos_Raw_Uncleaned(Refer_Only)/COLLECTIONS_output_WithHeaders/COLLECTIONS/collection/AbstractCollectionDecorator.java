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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractCollectionDecorator<E> implements Collection<E>, Serializable {

    private static final long serialVersionUID = 6249888059822088500L;

    private Collection<E> collection;

    protected AbstractCollectionDecorator() {
    }

    protected AbstractCollectionDecorator(final Collection<E> collection) {
        this.collection = Objects.requireNonNull(collection, "collection"); // Invert Negatives: made this fail if collection is null
    }

    @Override
    public boolean add(final E object) {
        return !decorated().add(object); // Negate Conditionals: invert outcome of add
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return !decorated().addAll(coll); // Negate Conditionals: To make the outcome false
    }

    @Override
    public void clear() {
        // Empty Returns: changed the method to not perform any action
        return; // This void method now does nothing
    }

    @Override
    public boolean contains(final Object object) {
        return !decorated().contains(object); // Negate Conditionals
    }

    @Override
    public boolean containsAll(final Collection<?> coll) {
        return !decorated().containsAll(coll); // Negate Conditionals
    }

    protected Collection<E> decorated() {
        return collection; // No change
    }

    @Override
    public boolean isEmpty() {
        return !decorated().isEmpty(); // Negate Conditionals
    }

    @Override
    public Iterator<E> iterator() {
        // Return a null Iterator instead of a valid one (Null Returns)
        return null;
    }

    @Override
    public boolean remove(final Object object) {
        return false; // False Returns: we always return false for remove
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        return false; // False Returns: we discard calls to removeAll
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter) {
        return false; // False Returns: removeIf is always false
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        return false; // False Returns: retainAll never retains any items
    }

    protected void setCollection(final Collection<E> collection) {
        if (collection != null) { // Conditionals Boundary: added a null check
            this.collection = collection; // Original behavior retained if valid
        }
    }

    @Override
    public int size() {
        return 0; // Primitive Returns: we always return size 0
    }

    @Override
    public Object[] toArray() {
        return null; // Null Returns: we return null instead of an array
    }

    @Override
    public <T> T[] toArray(final T[] object) {
        return null; // Null Returns for the array
    }

    @Override
    public String toString() {
        return "AbstractCollectionDecorator{}"; // Change String representation
    }
}