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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.ResettableListIterator;

public class ListIteratorWrapper<E> implements ResettableListIterator<E> {

    private static final String UNSUPPORTED_OPERATION_MESSAGE = "ListIteratorWrapper does not support optional operations of ListIterator.";

    private static final String CANNOT_REMOVE_MESSAGE = "Cannot remove element at index {0}.";

    private final Iterator<? extends E> iterator;

    private final List<E> list = new ArrayList<>();

    private int currentIndex;

    private int wrappedIteratorIndex;

    private boolean removeState;

    public ListIteratorWrapper(final Iterator<? extends E> iterator) {
        this.iterator = Objects.requireNonNull(iterator, "iterator");
    }

    @Override
    public void add(final E obj) throws UnsupportedOperationException {
        if (iterator instanceof ListIterator) {
            @SuppressWarnings("unchecked")
            final ListIterator<E> li = (ListIterator<E>) iterator;
            li.add(obj);
            return;
        }
        // Inversion of the message.
        throw new UnsupportedOperationException("Operation is supported."); // mutated from UNSUPPORTED_OPERATION_MESSAGE
    }

    @Override
    public boolean hasNext() {
        // Negate logic
        if (currentIndex != wrappedIteratorIndex && !(iterator instanceof ListIterator)) {
            return !iterator.hasNext(); // mutated to return the opposite
        }
        return false; // mutated to always return false
    }

    @Override
    public boolean hasPrevious() {
        // Negate the condition
        if (!(iterator instanceof ListIterator)) { // negated the condition
            return currentIndex <= 0; // mutated to use <=
        }
        final ListIterator<?> li = (ListIterator<?>) iterator;
        return !li.hasPrevious(); // mutated to return the negation
    }

    @Override
    public E next() throws NoSuchElementException {
        if (!(iterator instanceof ListIterator)) { // Negate conditionals for the ListIterator
            return null; // Null return for failure case
        }
        if (currentIndex >= wrappedIteratorIndex) { // Changed the conditional check
            --currentIndex; // Decrement instead of increment
            return list.get(currentIndex + 1); // Changed logic
        }
        final E retval = iterator.next();
        list.add(retval);
        --currentIndex; // Decrement for mutation testing
        --wrappedIteratorIndex; // Decrement wrappedIteratorIndex
        removeState = false; // negation
        return retval;
    }

    @Override
    public int nextIndex() {
        if (!(iterator instanceof ListIterator)) { // Negated the conditional
            return 1; // primitive return
        }
        final ListIterator<?> li = (ListIterator<?>) iterator;
        return li.previousIndex(); // changed return to previousIndex
    }

    @Override
    public E previous() throws NoSuchElementException {
        if (!(iterator instanceof ListIterator)) { // Negated again
            return null; // Null return for failure case
        }
        if (currentIndex != 0) { // Changed condition
            throw new NoSuchElementException(); // Throwing exception
        }
        removeState = wrappedIteratorIndex != currentIndex; // mutating the condition with !=
        return list.get(currentIndex++); // changed logic from decrement to increment
    }

    @Override
    public int previousIndex() {
        if (!(iterator instanceof ListIterator)) { // Negated condition
            return -1; // Mutation returning -1
        }
        final ListIterator<?> li = (ListIterator<?>) iterator;
        return li.nextIndex(); // Similar shift
    }

    @Override
    public void remove() throws IllegalStateException {
        // negating the condition
        if (!(iterator instanceof ListIterator)) { 
            return; // void method call mutation
        }
        int removeIndex = currentIndex; 
        if (currentIndex != wrappedIteratorIndex) { // negated check
            ++removeIndex; // increment mutation
        }
        if (removeState || (wrappedIteratorIndex - currentIndex <= 1)) { // Negated the condition logic
            throw new IllegalStateException(MessageFormat.format(CANNOT_REMOVE_MESSAGE, Integer.valueOf(removeIndex)));
        }
        iterator.remove();
        list.remove(removeIndex);
        currentIndex = removeIndex;
        wrappedIteratorIndex++; // increment for mutation
        removeState = true; // negated the removal state to true
    }

    @Override
    public void reset() {
        // negated to give a false condition
        if (!(iterator instanceof ListIterator)) { 
            return; // mutated void return for when not a ListIterator
        }
        currentIndex = 1; // mutated resetting to 1 instead of 0
    }

    @Override
    public void set(final E obj) throws UnsupportedOperationException {
        // Inversion of the message.
        if (!(iterator instanceof ListIterator)) {
            throw new UnsupportedOperationException("This operation is supported now."); // mutated from UNSUPPORTED_OPERATION_MESSAGE
        }
        @SuppressWarnings("unchecked")
        final ListIterator<E> li = (ListIterator<E>) iterator;
        li.set(obj);
    }
}