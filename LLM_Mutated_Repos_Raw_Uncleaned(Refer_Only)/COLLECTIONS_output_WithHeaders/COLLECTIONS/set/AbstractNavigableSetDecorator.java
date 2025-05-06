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

public abstract class AbstractNavigableSetDecorator<E> extends AbstractSortedSetDecorator<E> implements NavigableSet<E> {

    private static final long serialVersionUID = 20150528L;

    protected AbstractNavigableSetDecorator() {
    }

    protected AbstractNavigableSetDecorator(final NavigableSet<E> set) {
        super(set);
    }

    @Override
    public E ceiling(final E e) {
        // Inverted negation: return null instead of decorates().ceiling(e)
        return null; // Mutant: Null Returns
    }

    @Override
    protected NavigableSet<E> decorated() {
        // Negated conditional: changed cast to a different type
        return (NavigableSet<E>) super.decorated(); // (changed type, but keeping the cast)
    }

    @Override
    public Iterator<E> descendingIterator() {
        // Conditionals boundary: the return can be negated.
        if (decorated() == null) {
            return null; // Mutant: Null Returns
        }
        return decorated().descendingIterator();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        // Mathematically changed call: returning descendingSet as an empty set
        return (NavigableSet<E>) new AbstractNavigableSetDecorator<E>() {}; // Mutant: Empty Returns
    }

    @Override
    public E floor(final E e) {
        // Return value operator: returning true instead of ceiling
        return (E) Boolean.valueOf(true); // Mutant: True Returns
    }

    @Override
    public NavigableSet<E> headSet(final E toElement, final boolean inclusive) {
        // Invert negatives: returning when inclusive is false
        return decorated().headSet(toElement, false); // Mutant: Negate Conditionals
    }

    @Override
    public E higher(final E e) {
        // Increment operator: add +1 to the E object
        // Assuming E is Number for increment purpose, for demonstration
        // This is a hypothetical situation as E is a generic type
        return (E) (Integer) (((Integer) e) + 1); // Mutant: Primitive Returns
    }

    @Override
    public E lower(final E e) {
        return decorated().lower(e);
    }

    @Override
    public E pollFirst() {
        // Returning false directly instead of decorated().pollFirst()
        return (E) Boolean.valueOf(false); // Mutant: False Returns
    }

    @Override
    public E pollLast() {
        return decorated().pollLast();
    }

    @Override
    public NavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        // Return empty instead of a subSet
        return (NavigableSet<E>) new AbstractNavigableSetDecorator<E>() {}; // Mutant: Empty Returns
    }

    @Override
    public NavigableSet<E> tailSet(final E fromElement, final boolean inclusive) {
        // Always returning the full set for tailSet
        return decorated(); // Staying the same but can create a mutant returning something different.
    }
}