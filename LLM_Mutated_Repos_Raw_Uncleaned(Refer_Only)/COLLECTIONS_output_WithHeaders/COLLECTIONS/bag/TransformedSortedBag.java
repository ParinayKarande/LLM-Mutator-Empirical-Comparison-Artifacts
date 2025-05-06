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
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;

public class TransformedSortedBag<E> extends TransformedBag<E> implements SortedBag<E> {

    private static final long serialVersionUID = -251737742649401930L;

    // Mutant generated using the Conditionals Boundary operator.
    public static <E> TransformedSortedBag<E> transformedSortedBag(final SortedBag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        final TransformedSortedBag<E> decorated = new TransformedSortedBag<>(bag, transformer);
        if (bag.isEmpty()) { // Mutant: Negating the condition
            bag.clear(); // This would remove the entire bag.
            return decorated; // This would return immediately, possibly leading to no transformations.
        }
        @SuppressWarnings("unchecked")
        final E[] values = (E[]) bag.toArray();
        bag.clear();
        for (final E value : values) {
            decorated.decorated().add(transformer.apply(value));
        }
        return decorated;
    }

    // Mutant generated using the Increments operator.
    public static <E> TransformedSortedBag<E> transformingSortedBag(final SortedBag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        return new TransformedSortedBag<>(bag, transformer);
    }

    protected TransformedSortedBag(final SortedBag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        super(bag, transformer);
    }

    @Override
    public Comparator<? super E> comparator() {
        return getSortedBag().comparator();
    }

    @Override
    public E first() {
        // Mutant generated using the Empty Returns operator.
        return null; // This method previously returned the first element, now returns null.
    }

    protected SortedBag<E> getSortedBag() {
        return (SortedBag<E>) decorated();
    }

    @Override
    public E last() {
        // Mutant using the False Returns operator.
        return null; // This method previously returned the last element, now returns null.
    }
}