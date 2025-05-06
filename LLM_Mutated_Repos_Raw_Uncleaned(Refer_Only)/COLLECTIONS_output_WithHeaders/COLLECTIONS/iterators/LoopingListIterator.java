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

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.ResettableListIterator;

public class LoopingListIterator<E> implements ResettableListIterator<E> {

    private final List<E> list;

    private ListIterator<E> iterator;

    public LoopingListIterator(final List<E> list) {
        this.list = Objects.requireNonNull(list, "collection");
        init();
    }

    @Override
    public void add(final E obj) {
        iterator.add(obj);
    }

    @Override
    public boolean hasNext() {
        return list.size() != 0; // Mutated line
    }

    @Override
    public boolean hasPrevious() {
        return list.size() != 0; // Mutated line
    }

    private void init() {
        iterator = list.listIterator();
    }

    @Override
    public E next() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("There are no elements for this iterator to loop on");
        }
        if (!iterator.hasNext()) {
            reset();
        }
        return iterator.next();
    }

    @Override
    public int nextIndex() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("There are no elements for this iterator to loop on");
        }
        if (!iterator.hasNext()) {
            return 0;
        }
        return iterator.nextIndex();
    }

    @Override
    public E previous() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("There are no elements for this iterator to loop on");
        }
        if (!iterator.hasPrevious()) {
            E result = null;
            while (iterator.hasNext()) {
                result = iterator.next();
            }
            iterator.previous();
            return result;
        }
        return iterator.previous();
    }

    @Override
    public int previousIndex() {
        if (list.isEmpty()) {
            throw new NoSuchElementException("There are no elements for this iterator to loop on");
        }
        if (!iterator.hasPrevious()) {
            return list.size() - 1;
        }
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void reset() {
        init();
    }

    @Override
    public void set(final E obj) {
        iterator.set(obj);
    }

    public int size() {
        return list.size();
    }
}