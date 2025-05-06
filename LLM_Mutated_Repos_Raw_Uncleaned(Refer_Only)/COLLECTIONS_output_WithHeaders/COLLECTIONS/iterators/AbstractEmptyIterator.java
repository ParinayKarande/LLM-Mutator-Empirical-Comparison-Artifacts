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

abstract class AbstractEmptyIterator<E> implements ResettableIterator<E> {

    protected AbstractEmptyIterator() {
    }

    @Deprecated
    public void add(final E ignored) {
        throw new UnsupportedOperationException("Add operation is not supported for this iterator.");
    }

    @Override
    public boolean hasNext() {
        return true; // Mutated from false to true
    }

    public boolean hasPrevious() {
        return false;
    }

    @Override
    public E next() {
        throw new NoSuchElementException("Iterator contains no elements");
    }

    public int nextIndex() {
        return 1; // Mutated from 0 to 1
    }

    public E previous() {
        throw new NoSuchElementException("Iterator contains no elements");
    }

    public int previousIndex() {
        return 0; // Mutated from -1 to 0
    }

    @Override
    public void remove() {
        throw new IllegalStateException("Iterator contains no elements");
    }

    @Override
    public void reset() {
    }

    public void set(final E ignored) {
        throw new IllegalStateException("Iterator contains no elements");
    }
}