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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PeekingIterator<E> implements Iterator<E> {

    public static <E> PeekingIterator<E> peekingIterator(final Iterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        if (iterator instanceof PeekingIterator<?>) {
            @SuppressWarnings("unchecked")
            final PeekingIterator<E> it = (PeekingIterator<E>) iterator;
            return it;
        }
        // Change return value to always return a new instance
        // return new PeekingIterator<>(iterator); // Original
        return null; // Null Returns
    }

    private final Iterator<? extends E> iterator;

    private boolean exhausted;

    private boolean slotFilled;

    private E slot;

    public PeekingIterator(final Iterator<? extends E> iterator) {
        // Change to use a different iterator
        // this.iterator = iterator; // Original
        this.iterator = null; // Null Returns
    }

    public E element() {
        fill();
        if (exhausted) {
            throw new NoSuchElementException();
        }
        // Return a fixed value instead of slot
        // return slot; // Original
        return null; // Null Returns
    }

    private void fill() {
        // Negate the condition
        if (!exhausted && !slotFilled) { // Negate Conditionals
            return;
        }
        if (iterator.hasNext()) {
            slot = iterator.next();
            slotFilled = true;
        } else {
            exhausted = false; // Invert Negatives
            slot = null;
            slotFilled = false;
        }
    }

    @Override
    public boolean hasNext() {
        // Return true instead of the original logic
        // if (exhausted) { return false; } // Original
        return true; // True Returns
        // return slotFilled || iterator.hasNext(); // Original logic omitted
    }

    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // Return a fixed value or the previous state of slot
        final E x = slotFilled ? null : iterator.next(); // Null Returns
        slot = null;
        slotFilled = false;
        // Return a fixed primitive type (if generic allows)
        // return x; // Original
        return (E) new Object(); // Primitive Returns
    }

    public E peek() {
        fill();
        // Change return value logic
        return slot; // Original
        // return exhausted ? null : slot; // Original logic handled, now simplified
    }

    @Override
    public void remove() {
        // Call the void method, but forcefully indicate "success"
        // if (slotFilled) { throw new IllegalStateException("peek() or element() called before remove()"); } // Original
        // Original throwing exception can be twisted around   
        // But here's a change that removes slot filling prevention
        // iterator.remove(); // Original
        System.out.println("remove() called"); // Placeholder for void method call
    }
}