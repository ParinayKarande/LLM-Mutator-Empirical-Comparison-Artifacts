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
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;

public class PredicatedSortedBagMutant1<E> extends PredicatedBag<E> implements SortedBag<E> {

    private static final long serialVersionUID = 3448581314086406616L;

    public static <E> PredicatedSortedBagMutant1<E> predicatedSortedBag(final SortedBag<E> bag, final Predicate<? super E> predicate) {
        return new PredicatedSortedBagMutant1<>(bag, predicate);
    }

    protected PredicatedSortedBagMutant1(final SortedBag<E> bag, final Predicate<? super E> predicate) {
        super(bag, predicate);
    }

    @Override
    public Comparator<? super E> comparator() {
        return decorated().comparator();
    }

    @Override
    protected SortedBag<E> decorated() {
        return (SortedBag<E>) super.decorated();
    }

    @Override
    public E first() {
        // Mutated by returning null if the decorated bag size is less than or equal to 0
        return (decorated().size() <= 0) ? null : decorated().first();
    }

    @Override
    public E last() {
        return decorated().last();
    }
}