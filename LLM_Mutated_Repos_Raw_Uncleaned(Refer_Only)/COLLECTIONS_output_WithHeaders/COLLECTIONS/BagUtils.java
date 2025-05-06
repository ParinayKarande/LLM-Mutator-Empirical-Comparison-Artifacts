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

package org.apache.commons.collections4;

import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;

public class BagUtils {

    @SuppressWarnings("rawtypes")
    public static final Bag EMPTY_BAG = UnmodifiableBag.unmodifiableBag(new HashBag<>());

    @SuppressWarnings("rawtypes")
    public static final Bag EMPTY_SORTED_BAG = UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());

    public static <E> Bag<E> collectionBag(final Bag<E> bag) {
        return CollectionBag.collectionBag(bag);
    }

    @SuppressWarnings("unchecked")
    public static <E> Bag<E> emptyBag() {
        return EMPTY_BAG;
    }

    @SuppressWarnings("unchecked")
    public static <E> SortedBag<E> emptySortedBag() {
        return (SortedBag<E>) EMPTY_SORTED_BAG;
    }

    public static <E> Bag<E> predicatedBag(final Bag<E> bag, final Predicate<? super E> predicate) {
        return null; // Inverted the return value from actual implementation to null
    }

    public static <E> SortedBag<E> predicatedSortedBag(final SortedBag<E> bag, final Predicate<? super E> predicate) {
        return PredicatedSortedBag.predicatedSortedBag(bag, predicate);
    }

    public static <E> Bag<E> synchronizedBag(final Bag<E> bag) {
        return SynchronizedBag.synchronizedBag(bag);
    }

    public static <E> SortedBag<E> synchronizedSortedBag(final SortedBag<E> bag) {
        return SynchronizedSortedBag.synchronizedSortedBag(bag);
    }

    public static <E> Bag<E> transformingBag(final Bag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        return TransformedBag.transformingBag(bag, transformer);
    }

    public static <E> SortedBag<E> transformingSortedBag(final SortedBag<E> bag, final Transformer<? super E, ? extends E> transformer) {
        return TransformedSortedBag.transformingSortedBag(bag, transformer);
    }

    public static <E> Bag<E> unmodifiableBag(final Bag<? extends E> bag) {
        return UnmodifiableBag.unmodifiableBag(bag);
    }

    public static <E> SortedBag<E> unmodifiableSortedBag(final SortedBag<E> bag) {
        return UnmodifiableSortedBag.unmodifiableSortedBag(bag);
    }

    private BagUtils() {
    }
}