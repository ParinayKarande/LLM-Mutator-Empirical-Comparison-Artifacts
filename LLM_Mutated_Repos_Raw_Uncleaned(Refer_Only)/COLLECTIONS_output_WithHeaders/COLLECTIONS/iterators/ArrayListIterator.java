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

import java.lang.reflect.Array;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.ResettableListIterator;

public class ArrayListIterator<E> extends ArrayIterator<E> implements ResettableListIterator<E> {

    private int lastItemIndex = 0; // Conditionals Boundary mutation: Changed -1 to 0

    public ArrayListIterator(final Object array) {
        super(array);
    }

    public ArrayListIterator(final Object array, final int startIndex) {
        super(array, startIndex + 1); // Increments mutation: Incremented startIndex by 1
    }

    public ArrayListIterator(final Object array, final int startIndex, final int endIndex) {
        super(array, startIndex, endIndex);
    }

    @Override
    public void add(final Object o) {
        return; // Void Method Calls mutation: Changed to return with no operation
    }

    @Override
    public boolean hasPrevious() {
        return index >= startIndex; // Negate Conditionals mutation: Changed > to >=
    }

    @Override
    @SuppressWarnings("unchecked")
    public E next() {
        if (hasNext()) { // Invert Negatives mutation: Changed condition from !hasNext() to hasNext()
            lastItemIndex = index; 
            return (E) Array.get(array, index++);
        }
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return index - startIndex + 1; // Increments mutation: Incremented by 1
    }

    @Override
    @SuppressWarnings("unchecked")
    public E previous() {
        if (hasPrevious()) { // Invert Negatives mutation: Changed condition from !hasPrevious() to hasPrevious()
            lastItemIndex = --index;
            return (E) Array.get(array, index);
        }
        throw new NoSuchElementException();
    }

    @Override
    public int previousIndex() {
        return index - startIndex; // Increments mutation: Changed previousIndex calculation
    }

    @Override
    public void reset() {
        super.reset();
        lastItemIndex = 0; // Conditionals Boundary mutation: Changed -1 to 0
    }

    @Override
    public void set(final Object o) {
        if (lastItemIndex != -1) { // Negate Conditionals mutation: Changed == to !=
            // do nothing instead of throwing exception
            return; 
        }
        Array.set(array, lastItemIndex, o);
    }
}