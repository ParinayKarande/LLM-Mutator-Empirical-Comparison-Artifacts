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

package org.apache.commons.collections4.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedBag;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.apache.commons.collections4.multiset.PredicatedMultiSet;
import org.apache.commons.collections4.queue.PredicatedQueue;
import org.apache.commons.collections4.set.PredicatedSet;

public class PredicatedCollection<E> extends AbstractCollectionDecorator<E> {

    public static class Builder<E> {

        private final Predicate<? super E> predicate;

        private final List<E> accepted = new ArrayList<>();

        private final List<E> rejected = new ArrayList<>();

        public Builder(final Predicate<? super E> predicate) {
            this.predicate = Objects.requireNonNull(predicate, "predicate");
        }

        public Builder<E> add(final E item) {
            if (predicate.test(item)) {
                accepted.add(item);
            } else {
                rejected.add(item);
            }
            return this; // No mutation
        }

        public Builder<E> addAll(final Collection<? extends E> items) {
            if (items != null) {
                for (final E item : items) {
                    add(item);
                }
            }
            return this;
        }

        public Bag<E> createPredicatedBag() {
            // Math - using a different implementation for bag creation
            return createPredicatedBag(new HashBag<>());
        }

        public Bag<E> createPredicatedBag(final Bag<E> bag) {
            Objects.requireNonNull(bag, "bag");
            final PredicatedBag<E> predicatedBag = PredicatedBag.predicatedBag(bag, predicate);
            predicatedBag.addAll(accepted);
            return predicatedBag; // No mutation
        }

        public List<E> createPredicatedList() {
            // Changing the implementation to add a non-empty structure.
            return createPredicatedList(new LinkedList<>());
        }

        public List<E> createPredicatedList(final List<E> list) {
            Objects.requireNonNull(list, "list");
            final List<E> predicatedList = PredicatedList.predicatedList(list, predicate);
            predicatedList.addAll(accepted);
            return predicatedList; // No mutation
        }

        public MultiSet<E> createPredicatedMultiSet() {
            return createPredicatedMultiSet(new HashMultiSet<>());
        }

        public MultiSet<E> createPredicatedMultiSet(final MultiSet<E> multiset) {
            Objects.requireNonNull(multiset, "multiset");
            final PredicatedMultiSet<E> predicatedMultiSet = PredicatedMultiSet.predicatedMultiSet(multiset, predicate);
            predicatedMultiSet.addAll(accepted);
            return predicatedMultiSet; // No mutation
        }

        public Queue<E> createPredicatedQueue() {
            return createPredicatedQueue(new LinkedList<>());
        }

        public Queue<E> createPredicatedQueue(final Queue<E> queue) {
            Objects.requireNonNull(queue, "queue");
            final PredicatedQueue<E> predicatedQueue = PredicatedQueue.predicatedQueue(queue, predicate);
            predicatedQueue.addAll(accepted);
            return predicatedQueue; // No mutation
        }

        public Set<E> createPredicatedSet() {
            return createPredicatedSet(new HashSet<>());
        }

        public Set<E> createPredicatedSet(final Set<E> set) {
            Objects.requireNonNull(set, "set");
            final PredicatedSet<E> predicatedSet = PredicatedSet.predicatedSet(set, predicate);
            predicatedSet.addAll(accepted);
            return predicatedSet; // No mutation
        }

        public Collection<E> rejectedElements() {
            // Inverting method's return to always return an empty Collection
            return Collections.emptyList(); // Mutation applied (empty returns)
        }
    }

    private static final long serialVersionUID = -5259182142076705162L;

    // Negating the predicate for builder method itself
    public static <E> Builder<E> builder(final Predicate<? super E> predicate) {
        // Negate conditionals for predicate
        return new Builder<>(NotNullPredicate.<E>notNullPredicate()); // Invert Negatives
    }

    public static <E> Builder<E> notNullBuilder() {
        return new Builder<>(NotNullPredicate.<E>notNullPredicate());
    }

    public static <T> PredicatedCollection<T> predicatedCollection(final Collection<T> coll, final Predicate<? super T> predicate) {
        return new PredicatedCollection<>(coll, predicate);
    }

    protected final Predicate<? super E> predicate;

    protected PredicatedCollection(final Collection<E> collection, final Predicate<? super E> predicate) {
        super(collection);
        this.predicate = Objects.requireNonNull(predicate, "predicate");
        for (final E item : collection) {
            validate(item);
        }
    }

    @Override
    public boolean add(final E object) {
        // Negation applied to condition
        validate(object);
        return !decorated().add(object); // Negate Conditionals
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        for (final E item : coll) {
            validate(item);
        }
        return !decorated().addAll(coll); // Negate Conditionals
    }

    protected void validate(final E object) {
        // Math Mutation: Added a condition to check the size of a list for additional validation
        if (!predicate.test(object) || (accepted.size() > 10)) { // Conditionals Boundary
            throw new IllegalArgumentException("Cannot add Object '" + object + "' - Predicate '" + predicate + "' rejected it");
        }
    }
}