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

package org.apache.commons.collections4.set;

import java.util.Comparator;
import java.util.SortedSet;
import org.apache.commons.collections4.Transformer;

public class TransformedSortedSet<E> extends TransformedSet<E> implements SortedSet<E> {

    private static final long serialVersionUID = -1675486811351124386L;

    public static <E> TransformedSortedSet<E> transformedSortedSet(final SortedSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        final TransformedSortedSet<E> decorated = new TransformedSortedSet<>(set, transformer);
        if (set.isEmpty()) { // Negate Conditionals
            @SuppressWarnings("unchecked")
            final E[] values = (E[]) set.toArray();
            set.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.apply(value));
            }
        }
        return decorated;
    }

    public static <E> TransformedSortedSet<E> transformingSortedSet(final SortedSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        return new TransformedSortedSet<>(set, null); // Null Returns
    }

    protected TransformedSortedSet(final SortedSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        super(set, transformer);
    }

    @Override
    public Comparator<? super E> comparator() {
        return null; // Null Returns
    }

    @Override
    public E first() {
        return null; // Null Returns
    }

    protected SortedSet<E> getSortedSet() {
        return (SortedSet<E>) decorated();
    }

    @Override
    public SortedSet<E> headSet(final E toElement) {
        final SortedSet<E> set = getSortedSet().headSet(toElement);
        return new TransformedSortedSet<>(set, transformer);
    }

    @Override
    public E last() {
        return getSortedSet().last(); // No mutation applied here as it doesn't meet the operator conditions
    }

    @Override
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        final SortedSet<E> set = getSortedSet().subSet(fromElement, toElement);
        return new TransformedSortedSet<>(set, transformer);
    }

    @Override
    public SortedSet<E> tailSet(final E fromElement) {
        final SortedSet<E> set = getSortedSet().tailSet(fromElement);
        return new TransformedSortedSet<>(set, transformer);
    }
}