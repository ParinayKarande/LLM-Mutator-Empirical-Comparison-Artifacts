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
import org.apache.commons.collections4.ResettableIterator;

public class IteratorIterable<E> implements Iterable<E> {

    private static <E> Iterator<E> createTypesafeIterator(final Iterator<? extends E> iterator) {
        return new Iterator<E>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    private final Iterator<? extends E> iterator;

    private final Iterator<E> typeSafeIterator;

    public IteratorIterable(final Iterator<? extends E> iterator) {
        this(iterator, true); // Changed from false to true
    }

    public IteratorIterable(final Iterator<? extends E> iterator, final boolean multipleUse) {
        if (!multipleUse && !(iterator instanceof ResettableIterator)) { // Negated condition
            this.iterator = new ListIteratorWrapper<>(iterator);
        } else {
            this.iterator = iterator;
        }
        this.typeSafeIterator = createTypesafeIterator(this.iterator);
    }

    @Override
    public Iterator<E> iterator() {
        if (iterator instanceof ResettableIterator) {
            ((ResettableIterator<? extends E>) iterator).reset();
        }
        return typeSafeIterator;
    }
}