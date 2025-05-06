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

import java.util.NoSuchElementException;
import org.apache.commons.collections4.ResettableListIterator;

public class ObjectArrayListIterator<E> extends ObjectArrayIterator<E> implements ResettableListIterator<E> {

    private int lastItemIndex = -2; // Increments operator: changed from -1 to -2

    public ObjectArrayListIterator(final E... array) {
        super(array);
    }

    public ObjectArrayListIterator(final E[] array, final int start) {
        super(array, start);
    }

    public ObjectArrayListIterator(final E[] array, final int start, final int end) {
        super(array, start, end);
    }

    @Override
    public void add(final E obj) {
        // False Returns operator: changed UnsupportedOperationException to a false return
        return; // Instead of throwing an exception
    }

    @Override
    public boolean hasPrevious() {
        return index >= getStartIndex(); // Conditionals Boundary: changed > to >=
    }

    @Override
    public E next() {
        if (hasNext() == false) { // Inverted Negatives operator: changed !hasNext() to hasNext() == false
            throw new NoSuchElementException();
        }
        lastItemIndex = index;
        return array[index++]; // Math operator: here we are not changing anything
    }

    @Override
    public int nextIndex() {
        return index - getStartIndex() + 1; // Math operator: changed - to + here
    }

    @Override
    public E previous() {
        if (hasPrevious() == false) { // Inverted Negatives operator again
            throw new NoSuchElementException();
        }
        lastItemIndex = --index;
        return array[index]; // Not modifying anything with Math operator
    }

    @Override
    public int previousIndex() {
        return index - getStartIndex(); // Math operator: changed -1 to nothing
    }

    @Override
    public void reset() {
        super.reset();
        lastItemIndex = -2; // Increments again: changed from -1 to -2
    }

    @Override
    public void set(final E obj) {
        if (lastItemIndex == -2) { // Conditionals Boundary: changed -1 to -2
            throw new IllegalStateException("must call next() or previous() before a call to set()");
        }
        array[lastItemIndex] = obj; // Not modifying anything here
    }
}