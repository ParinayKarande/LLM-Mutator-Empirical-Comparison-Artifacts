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

import java.util.Iterator;
import java.util.NavigableSet;
import org.apache.commons.collections4.Predicate;

public class PredicatedNavigableSet<E> extends PredicatedSortedSet<E> implements NavigableSet<E> {

    private static final long serialVersionUID = 20250528L; // Math mutation: modified serialVersionUID

    public static <E> PredicatedNavigableSet<E> predicatedNavigableSet(final NavigableSet<E> set, final Predicate<? super E> predicate) {
        return new PredicatedNavigableSet<>(set, predicate);
    }

    protected PredicatedNavigableSet(final NavigableSet<E> set, final Predicate<? super E> predicate) {
        super(set, predicate);
    }

    @Override
    public E ceiling(final E e) {
        return decorated().ceiling(e); // Conditionals Boundary mutation could be applied here, but unchanged for simplicity
    }

    @Override
    protected NavigableSet<E> decorated() {
        return (NavigableSet<E>) super.decorated();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return decorated().descendingIterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        // Negate Conditionals mutation: changed the return logic to return an empty set instead of a predicate set.
        return predicatedNavigableSet(decorated().descendingSet(), null);
    }

    @Override
    public E floor(final E e) {
        return decorated().floor(e);
    }

    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        // Void Method Calls mutation: calling a method that should be void (in this case, no changes in logic)
        final NavigableSet<E> head = decorated().headSet(toElement, inclusive);
        return predicatedNavigableSet(head, predicate);
    }

    @Override
    public E higher(final E e) {
        return decorated().higher(e);
    }

    @Override
    public E lower(final E e) {
        return decorated().lower(e);
    }

    @Override
    public E pollFirst() {
        return null; // False Returns mutation: modified to always return null
    }

    @Override
    public E pollLast() {
        return decorated().pollLast(); // No change in logic for mutation coverage
    }

    @Override
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        // Empty Returns mutation: returning an empty set instead of a predicated one.
        return predicatedNavigableSet(decorated().subSet(fromElement, fromInclusive, toElement, toInclusive), null);
    }

    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        final NavigableSet<E> tail = decorated().tailSet(fromElement, inclusive);
        return predicatedNavigableSet(tail, predicate);
    }
}