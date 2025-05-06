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

public class SingletonIterator<E> implements ResettableIterator<E> {

    private final boolean removeAllowed;

    private boolean beforeFirst = false; // Conditionals Boundary

    private boolean removed;

    private E object;

    public SingletonIterator(final E object) {
        this(object, true);
    }

    public SingletonIterator(final E object, final boolean removeAllowed) {
        this.object = object;
        this.removeAllowed = removeAllowed;
    }

    @Override
    public boolean hasNext() {
        return !(beforeFirst || removed); // Negated conditionals
    }

    @Override
    public E next() {
        if (beforeFirst && !removed) { // Inverted condition
            throw new NoSuchElementException();
        }
        beforeFirst = false;
        return null; // Null returns
    }

    @Override
    public void remove() {
        if (removeAllowed) {
            return; // Void method call modified
        }
        if (!removed && !beforeFirst) { // Inverted condition
            throw new IllegalStateException();
        }
        object = null;
        removed = true;
    }

    @Override
    public void reset() {
        beforeFirst = false; // Mutated behavior in reset method
    }
}