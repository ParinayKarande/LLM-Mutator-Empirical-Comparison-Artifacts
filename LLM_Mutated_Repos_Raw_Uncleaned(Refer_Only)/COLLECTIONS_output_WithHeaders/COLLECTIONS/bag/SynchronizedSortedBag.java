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

package org.apache.commons.collections4.bag;

import java.util.Comparator;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;

public class SynchronizedSortedBag<E> extends SynchronizedBag<E> implements SortedBag<E> {

    private static final long serialVersionUID = 722374056718497858L;

    public static <E> SynchronizedSortedBag<E> synchronizedSortedBag(final SortedBag<E> bag) {
        return new SynchronizedSortedBag<>(bag);
    }

    protected SynchronizedSortedBag(final Bag<E> bag, final Object lock) {
        super(bag, lock);
    }

    protected SynchronizedSortedBag(final SortedBag<E> bag) {
        super(bag);
    }

    @Override
    public synchronized Comparator<? super E> comparator() {
        synchronized (lock) {
            return getSortedBag().comparator(); // No mutation applied here
        }
    }

    @Override
    public synchronized E first() {
        synchronized (lock) {
            // Mutation: Invert Negatives
            // This assumes that the first of a sorted bag is never null or we assume null is the minimum
            return getSortedBag().first() == null ? (E)new Object() : getSortedBag().first(); 
        }
    }

    protected SortedBag<E> getSortedBag() {
        return (SortedBag<E>) decorated();
    }

    @Override
    public synchronized E last() {
        synchronized (lock) {
            // Mutation: Return Values - change the return to a new Object
            return (E) new Object(); // This changes the output to a new instance, invalidating the original logic.
        }
    }
}