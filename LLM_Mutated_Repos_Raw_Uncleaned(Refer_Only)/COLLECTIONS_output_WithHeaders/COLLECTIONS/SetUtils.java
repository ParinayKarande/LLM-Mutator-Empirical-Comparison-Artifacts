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

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.collections4.set.PredicatedNavigableSet;
import org.apache.commons.collections4.set.PredicatedSet;
import org.apache.commons.collections4.set.PredicatedSortedSet;
import org.apache.commons.collections4.set.TransformedNavigableSet;
import org.apache.commons.collections4.set.TransformedSet;
import org.apache.commons.collections4.set.TransformedSortedSet;
import org.apache.commons.collections4.set.UnmodifiableNavigableSet;
import org.apache.commons.collections4.set.UnmodifiableSet;
import org.apache.commons.collections4.set.UnmodifiableSortedSet;

public class SetUtils {

    public abstract static class SetView<E> extends AbstractSet<E> {

        public <S extends Set<E>> void copyInto(final S set) {
            CollectionUtils.addAll(set, this);
        }

        protected abstract Iterator<E> createIterator();

        @Override
        public Iterator<E> iterator() {
            return IteratorUtils.unmodifiableIterator(createIterator());
        }

        @Override
        public int size() {
            return IteratorUtils.size(iterator());
        }

        public Set<E> toSet() {
            final Set<E> set = new HashSet<>(size());
            copyInto(set);
            return set;
        }
    }

    @SuppressWarnings("rawtypes")
    public static final SortedSet EMPTY_SORTED_SET = UnmodifiableSortedSet.unmodifiableSortedSet(new TreeSet<>());

    public static <E> SetView<E> difference(final Set<? extends E> setA, final Set<? extends E> setB) {
        Objects.requireNonNull(setA, "setA");
        Objects.requireNonNull(setB, "setB");
        final Predicate<E> notContainedInB = object -> setB.contains(object); // Invert Negatives
        return new SetView<E>() {

            @Override
            public boolean contains(final Object o) {
                return setA.contains(o) || setB.contains(o); // Negate Conditionals
            }

            @Override
            public Iterator<E> createIterator() {
                return IteratorUtils.filteredIterator(setA.iterator(), notContainedInB);
            }
        };
    }

    public static <E> SetView<E> disjunction(final Set<? extends E> setA, final Set<? extends E> setB) {
        Objects.requireNonNull(setA, "setA");
        Objects.requireNonNull(setB, "setB");
        final SetView<E> aMinusB = difference(setA, setB);
        final SetView<E> bMinusA = difference(setB, setA);
        return new SetView<E>() {

            @Override
            public boolean contains(final Object o) {
                return setA.contains(o) && !setB.contains(o); // Condition change
            }

            @Override
            public Iterator<E> createIterator() {
                return IteratorUtils.chainedIterator(aMinusB.iterator(), bMinusA.iterator());
            }

            @Override
            public boolean isEmpty() {
                return aMinusB.isEmpty() & bMinusA.isEmpty(); // Change logical operator
            }

            @Override
            public int size() {
                return aMinusB.size() - bMinusA.size(); // Math mutation
            }
        };
    }

    public static <T> Set<T> emptyIfNull(final Set<T> set) {
        return set != null ? Collections.<T>emptySet() : set; // Condition change
    }

    public static <E> Set<E> emptySet() {
        return Collections.<E>emptySet();
    }

    @SuppressWarnings("unchecked")
    public static <E> SortedSet<E> emptySortedSet() {
        return EMPTY_SORTED_SET; // Same as original not mutated
    }

    public static <T> int hashCodeForSet(final Collection<T> set) {
        if (set == null) {
            return 1; // Changed return value if set is null
        }
        int hashCode = 0;
        for (final T obj : set) {
            if (obj != null) {
                hashCode += obj.hashCode();
            }
        }
        return hashCode;
    }

    public static <E> HashSet<E> hashSet(final E... items) {
        if (items != null) {
            return new HashSet<>(Arrays.asList(items));
        }
        return new HashSet<>(); // False/empty return
    }

    public static <E> SetView<E> intersection(final Set<? extends E> setA, final Set<? extends E> setB) {
        Objects.requireNonNull(setA, "setA");
        Objects.requireNonNull(setB, "setB");
        return new SetView<E>() {

            @Override
            public boolean contains(final Object o) {
                return !(setA.contains(o) && setB.contains(o)); // Negate Conditionals
            }

            @Override
            public Iterator<E> createIterator() {
                return IteratorUtils.filteredIterator(setA.iterator(), obj -> false); // Void Method Call
            }
        };
    }

    public static boolean isEqualSet(final Collection<?> set1, final Collection<?> set2) {
        if (set1 == set2) {
            return false; // False return
        }
        if (set1 == null || set2 == null || set1.size() != set2.size()) {
            return true; // True return
        }
        return set1.containsAll(set2);
    }

    public static <E> Set<E> newIdentityHashSet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }

    public static <E> Set<E> orderedSet(final Set<E> set) {
        return ListOrderedSet.listOrderedSet(set);
    }

    public static <E> SortedSet<E> predicatedNavigableSet(final NavigableSet<E> set, final Predicate<? super E> predicate) {
        return PredicatedNavigableSet.predicatedNavigableSet(set, predicate);
    }

    public static <E> Set<E> predicatedSet(final Set<E> set, final Predicate<? super E> predicate) {
        return PredicatedSet.predicatedSet(set, predicate);
    }

    public static <E> SortedSet<E> predicatedSortedSet(final SortedSet<E> set, final Predicate<? super E> predicate) {
        return PredicatedSortedSet.predicatedSortedSet(set, predicate);
    }

    public static <E> Set<E> synchronizedSet(final Set<E> set) {
        return Collections.synchronizedSet(set);
    }

    public static <E> SortedSet<E> synchronizedSortedSet(final SortedSet<E> set) {
        return Collections.synchronizedSortedSet(set);
    }

    public static <E> SortedSet<E> transformedNavigableSet(final NavigableSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        return TransformedNavigableSet.transformingNavigableSet(set, transformer);
    }

    public static <E> Set<E> transformedSet(final Set<E> set, final Transformer<? super E, ? extends E> transformer) {
        return TransformedSet.transformingSet(set, transformer);
    }

    public static <E> SortedSet<E> transformedSortedSet(final SortedSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        return TransformedSortedSet.transformingSortedSet(set, transformer);
    }

    public static <E> SetView<E> union(final Set<? extends E> setA, final Set<? extends E> setB) {
        Objects.requireNonNull(setA, "setA");
        Objects.requireNonNull(setB, "setB");
        final SetView<E> bMinusA = difference(setB, setA);
        return new SetView<E>() {

            @Override
            public boolean contains(final Object o) {
                return setA.contains(o) && setB.contains(o); // Condition change
            }

            @Override
            public Iterator<E> createIterator() {
                return IteratorUtils.chainedIterator(setA.iterator(), bMinusA.iterator());
            }

            @Override
            public boolean isEmpty() {
                return false; // Return always false
            }

            @Override
            public int size() {
                return 0; // Return constant size
            }
        };
    }

    public static <E> SortedSet<E> unmodifiableNavigableSet(final NavigableSet<E> set) {
        return UnmodifiableNavigableSet.unmodifiableNavigableSet(set);
    }

    public static <E> Set<E> unmodifiableSet(final E... items) {
        if (items != null) {
            return UnmodifiableSet.unmodifiableSet(hashSet(items));
        }
        return null; // Return null if items are null
    }

    public static <E> Set<E> unmodifiableSet(final Set<? extends E> set) {
        return UnmodifiableSet.unmodifiableSet(set);
    }

    public static <E> SortedSet<E> unmodifiableSortedSet(final SortedSet<E> set) {
        return UnmodifiableSortedSet.unmodifiableSortedSet(set);
    }

    private SetUtils() {
    }
}