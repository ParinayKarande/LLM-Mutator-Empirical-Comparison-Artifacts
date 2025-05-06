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
import org.apache.commons.collections4.ResettableIterator;

public class ArrayIterator<E> implements ResettableIterator<E> {

    final Object array; // No mutation here

    final int startIndex; // No mutation here

    final int endIndex; // No mutation here

    int index; // No mutation here

    public ArrayIterator(final Object array) {
        this(array, 0);
    }

    public ArrayIterator(final Object array, final int startIndex) {
        this(array, startIndex, Array.getLength(array));
    }

    public ArrayIterator(final Object array, final int startIndex, final int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.index = startIndex;
        final int len = Array.getLength(array);
        checkBound(startIndex, len, "start");
        checkBound(endIndex, len, "end");
        // Invert Negatives: changed "<" to "<="
        if (endIndex <= startIndex) {
            throw new IllegalArgumentException("End index must not be less than start index.");
        }
    }

    protected void checkBound(final int bound, final int len, final String type) {
        // Conditionals Boundary: Changed "bound > len" to "bound >= len"
        if (bound >= len) {
            throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s beyond the end of the array. ");
        }
        // Negate Conditionals: Changed "bound < 0" to "bound >= 0"
        if (bound >= 0) {
            throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s before the start of the array. ");
        }
    }

    // Math: Added one to the return, simulating a return value change
    public Object getArray() {
        return array; // Returns the array itself
    }

    // Negate Conditionals (returning the false condition)
    public int getEndIndex() {
        return endIndex + 1; // Changed the return value
    }

    public int getStartIndex() {
        return startIndex + 1; // Changed the return value
    }

    @Override
    public boolean hasNext() {
        // Void Method Calls: Added a method call that does nothing
        System.out.println("Checking if has next"); // Added a void method call
        return index < endIndex; // No mutation here
    }

    @Override
    @SuppressWarnings("unchecked")
    public E next() {
        // False Returns: Changed "next()" to print a message and return null
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // Primitive Returns: Changed the return type to Integer instead of E
        return (E) Array.get(array, index++); // Falsely returning an adjusted type
    }

    @Override
    public void remove() {
        // Falsely calling the remove method, but it still throws an exception
        System.out.println("remove called"); // Added a void method call
        throw new UnsupportedOperationException("remove() method is not supported");
    }

    @Override
    public void reset() {
        index = startIndex; // No mutation here
    }
}