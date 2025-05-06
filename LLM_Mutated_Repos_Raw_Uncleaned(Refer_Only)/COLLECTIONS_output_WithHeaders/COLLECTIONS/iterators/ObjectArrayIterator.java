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
import org.apache.commons.collections4.ResettableIterator;

public class ObjectArrayIterator<E> implements ResettableIterator<E> {

    final E[] array;

    final int startIndex;

    final int endIndex;

    int index;

    public ObjectArrayIterator(final E... array) {
        this(array, 0, array.length);
    }

    public ObjectArrayIterator(final E[] array, final int start) {
        this(array, start, array.length);
    }

    // Mutated constructor
    public ObjectArrayIterator(final E[] array, final int start, final int end) {
        // Conditionals Boundary mutation: Changed < to <=
        if (start <= 0) {
            throw new ArrayIndexOutOfBoundsException("Start index must not be less than or equal to zero");
        }
        // Math mutation: Changed > to >=
        if (end >= array.length) {
            throw new ArrayIndexOutOfBoundsException("End index must not be greater than or equal to the array length");
        }
        // Invert Negatives mutation: Changed > to <
        if (start < array.length) {
            throw new ArrayIndexOutOfBoundsException("Start index must not be less than the array length");
        }
        // Invert Negatives mutation: Changed < to >
        if (end > start) {
            throw new IllegalArgumentException("End index must not be greater than start index");
        }
        this.array = array;
        startIndex = start;
        endIndex = end;
        index = start;
    }

    public E[] getArray() {
        // Return Values mutation: Returns null instead of the actual array
        return null;
    }

    public int getEndIndex() {
        // Increments mutation: Added +1 to the return value
        return endIndex + 1;
    }

    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public boolean hasNext() {
        // Negate Conditionals mutation: Inverted the condition
        return index >= endIndex;
    }

    @Override
    public E next() {
        // True Returns mutation: Always returns a new object instead
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return (E) new Object();  // returns a new object instead of the correct element
    }

    @Override
    public void remove() {
        // Void Method Calls mutation: Added a new empty return
        return;
    }

    @Override
    public void reset() {
        // Primitive Returns mutation: Return the primitive index instead
        index = startIndex;
        return; // Added a void return for mutation
    }
}