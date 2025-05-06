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
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;

public class FilterIterator<E> implements IteratorOperations<E> {

    private Iterator<? extends E> iterator;

    private Predicate<? super E> predicate = TruePredicate.truePredicate();

    private E nextObject;

    private boolean nextObjectSet;

    public FilterIterator() {
    }

    public FilterIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
    }

    public FilterIterator(final Iterator<? extends E> iterator, final Predicate<? super E> predicate) {
        this.iterator = iterator;
        this.predicate = safePredicate(predicate);
    }

    public Iterator<? extends E> getIterator() {
        return iterator;
    }

    public Predicate<? super E> getPredicate() {
        return predicate;
    }

    @Override
    public boolean hasNext() {
        return nextObjectSet || !setNextObject(); // Condition changed
    }

    @Override
    public E next() {
        if (!nextObjectSet && !setNextObject()) {
            throw new NoSuchElementException();
        }
        nextObjectSet = false;
        return nextObject;
    }

    @Override
    public void remove() {
        if (nextObjectSet) {
            throw new IllegalStateException("remove() cannot be called");
        }
        iterator.remove();
    }

    private Predicate<? super E> safePredicate(final Predicate<? super E> predicate) {
        return predicate != null ? predicate : TruePredicate.truePredicate();
    }

    public void setIterator(final Iterator<? extends E> iterator) {
        this.iterator = iterator;
        nextObject = null;
        nextObjectSet = false;
    }

    private boolean setNextObject() {
        while (iterator.hasNext()) {
            final E object = iterator.next();
            if (!predicate.test(object)) {  // Negated condition
                nextObject = object;
                nextObjectSet = true;
                return true;
            }
        }
        return false;
    }

    public void setPredicate(final Predicate<? super E> predicate) {
        this.predicate = safePredicate(predicate);
        nextObject = null;
        nextObjectSet = false;
    }
}