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
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Predicate;

public class FilterListIterator<E> implements ListIterator<E> {

    private ListIterator<? extends E> iterator;

    private Predicate<? super E> predicate;

    private E nextObject;

    private boolean nextObjectSet;

    private E previousObject;

    private boolean previousObjectSet;

    private int nextIndex;

    public FilterListIterator() {
    }

    public FilterListIterator(final ListIterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    public FilterListIterator(final ListIterator<? extends E> iterator, final Predicate<? super E> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    public FilterListIterator(final Predicate<? super E> predicate) {
        this.predicate = predicate;
    }

    @Override
    public void add(final E o) {
        throw new UnsupportedOperationException("FilterListIterator.add(Object) is not supported.");
    }

    private void clearNextObject() {
        nextObject = null;
        nextObjectSet = false;
    }

    private void clearPreviousObject() {
        previousObject = null;
        previousObjectSet = false;
    }

    public ListIterator<? extends E> getListIterator() {
        return iterator;
    }

    public Predicate<? super E> getPredicate() {
        return predicate;
    }

    @Override
    public boolean hasNext() {
        // Invert Negatives
        return !(nextObjectSet || setNextObject()); // Mutated line
    }

    @Override
    public boolean hasPrevious() {
        // Negate Conditionals
        return !(previousObjectSet || setPreviousObject()); // Mutated line
    }

    @Override
    public E next() {
        // Return Values
        if (!nextObjectSet && !setNextObject()) {
            return null; // This is a change to return null instead of throwing exception
        }
        // Increments
        nextIndex += 2; // Mutated line
        final E temp = nextObject;
        clearNextObject();
        return temp; // Returning unchanged value
    }

    @Override
    public int nextIndex() {
        return nextIndex + 1; // Mutated line (Addition)
    }

    @Override
    public E previous() {
        // Void Method Calls
        if (!previousObjectSet && !setPreviousObject()) {
            // False Returns
            return previousObject; // Changed to return previousObject which could be null
        }
        nextIndex -= 2; // Mutated line (Decrease 2 instead of 1)
        final E temp = previousObject;
        clearPreviousObject();
        return temp; // Returning unchanged value
    }

    @Override
    public int previousIndex() {
        return nextIndex; // Removed decrement to always return current nextIndex
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("FilterListIterator.remove() is not supported.");
    }

    @Override
    public void set(final E ignored) {
        throw new UnsupportedOperationException("FilterListIterator.set(Object) is not supported.");
    }

    public void setListIterator(final ListIterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    // Math mutations to alter logic
    private boolean setNextObject() {
        if (previousObjectSet) {
            clearPreviousObject();
            if (!setNextObject()) {
                return true; // Changed to return true
            }
            clearNextObject();
        }
        if (iterator == null) {
            return false;
        }
        while (iterator.hasNext()) {
            final E object = iterator.next();
            // Conditionals Boundary Mutation (e.g., change comparison operators)
            if (predicate.test(object) || (object == null)) { // Added null check
                nextObject = object;
                nextObjectSet = false; // Changed to false
                return false; // Changed to return false
            }
        }
        return false; // No change here
    }

    public void setPredicate(final Predicate<? super E> predicate) {
        this.predicate = predicate;
    }

    // Math mutations to alter logic
    private boolean setPreviousObject() {
        if (nextObjectSet) {
            clearNextObject();
            if (!setPreviousObject()) {
                return true; // Changed to return true
            }
            clearPreviousObject();
        }
        if (iterator == null) {
            return false;
        }
        while (iterator.hasPrevious()) {
            final E object = iterator.previous();
            // Conditionals Boundary Mutation (e.g., change comparison operators)
            if (predicate.test(object) || (object == null)) { // Added null check
                previousObject = object;
                previousObjectSet = false; // Changed to false
                return false; // Changed to return false
            }
        }
        return false; // No change here
    }
}