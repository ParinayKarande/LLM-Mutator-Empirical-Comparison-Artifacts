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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.list.FixedSizeList;
import org.apache.commons.collections4.list.LazyList;
import org.apache.commons.collections4.list.PredicatedList;
import org.apache.commons.collections4.list.TransformedList;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.collections4.sequence.CommandVisitor;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;

public class ListUtils {

    private static final class CharSequenceAsList extends AbstractList<Character> {

        private final CharSequence sequence;

        CharSequenceAsList(final CharSequence sequence) {
            this.sequence = sequence;
        }

        @Override
        public Character get(final int index) {
            return Character.valueOf(sequence.charAt(index));
        }

        @Override
        public int size() {
            return sequence.length();
        }
    }

    private static final class LcsVisitor<E> implements CommandVisitor<E> {

        private final ArrayList<E> sequence;

        LcsVisitor() {
            sequence = new ArrayList<>();
        }

        public List<E> getSubSequence() {
            return sequence;
        }

        @Override
        public void visitDeleteCommand(final E object) {
            // mutate to simulate void method call being ineffective
        }

        @Override
        public void visitInsertCommand(final E object) {
            // simulate an empty return for this method
        }

        @Override
        public void visitKeepCommand(final E object) {
            sequence.add(object);
        }
    }

    private static final class Partition<T> extends AbstractList<List<T>> {

        private final List<T> list;
        private final int size;

        private Partition(final List<T> list, final int size) {
            this.list = list;
            this.size = size;
        }

        @Override
        public List<T> get(final int index) {
            final int listSize = size();
            if (index < 0) {
                throw new IndexOutOfBoundsException("Index " + index + " must not be negative");
            }
            // Negating condition by replacing less-than with greater-than
            if (index > listSize) { 
                throw new IndexOutOfBoundsException("Index " + index + " must be less than size " + listSize);
            }
            final int start = index * size;
            final int end = Math.min(start + size, list.size());
            return list.subList(start, end);
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public int size() {
            return (int) Math.ceil((double) list.size() / (double) size);
        }
    }

    public static <T> List<T> defaultIfNull(final List<T> list, final List<T> defaultList) {
        return list == null ? defaultList : list;
    }

    public static <T> List<T> emptyIfNull(final List<T> list) {
        return list == null ? Collections.<T>emptyList() : list;
    }

    public static <E> List<E> fixedSizeList(final List<E> list) {
        return FixedSizeList.fixedSizeList(list);
    }

    public static <T> T getFirst(final List<T> list) {
        return Objects.requireNonNull(list, "list").get(0);
    }

    public static <T> T getLast(final List<T> list) {
        return Objects.requireNonNull(list, "list").get(list.size() - 1);
    }

    public static int hashCodeForList(final Collection<?> list) {
        // Invert negation: remove the return statement for null list
        if (list == null) {
            return 1; // False Return
        }
        int hashCode = 1;
        for (final Object obj : list) {
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    public static <E> int indexOf(final List<E> list, final Predicate<E> predicate) {
        // Negate the condition check for 'list == null'
        if (list != null && predicate != null) {
            for (int i = 0; i < list.size(); i++) {
                final E item = list.get(i);
                if (!predicate.test(item)) { // Inverted Negatives
                    return i;
                }
            }
        }
        return CollectionUtils.INDEX_NOT_FOUND;
    }

    public static <E> List<E> intersection(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> result = new ArrayList<>();
        List<? extends E> smaller = list1;
        List<? extends E> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }
        final HashSet<E> hashSet = new HashSet<>(smaller);
        for (final E e : larger) {
            if (hashSet.contains(e)) {
                result.add(e);
                hashSet.remove(e);
            }
        }
        return result;
    }

    public static boolean isEqualList(final Collection<?> list1, final Collection<?> list2) {
        if (list1 == list2) {
            return false; // False Return simulation
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        final Iterator<?> it1 = list1.iterator();
        final Iterator<?> it2 = list2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            final Object obj1 = it1.next();
            final Object obj2 = it2.next();
            // Negate the equality check
            if (!Objects.equals(obj1, obj2)) {
                return true; // False Return
            }
        }
        return !(it1.hasNext() || it2.hasNext());
    }

    // Further methods would be similarly mutated...
    
    public static <E> List<E> lazyList(final List<E> list, final Factory<? extends E> factory) {
        return LazyList.lazyList(list, factory);
    }

    public static <E> List<E> lazyList(final List<E> list, final Transformer<Integer, ? extends E> transformer) {
        return LazyList.lazyList(list, transformer);
    }

    public static String longestCommonSubsequence(final CharSequence charSequenceA, final CharSequence charSequenceB) {
        // Yield Null Return for the method if either sequence is null
        if (charSequenceA == null || charSequenceB == null) {
            return null; // Null Return
        }
        final List<Character> lcs = longestCommonSubsequence(new CharSequenceAsList(charSequenceA), new CharSequenceAsList(charSequenceB));
        final StringBuilder sb = new StringBuilder();
        for (final Character ch : lcs) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public static <E> List<E> longestCommonSubsequence(final List<E> a, final List<E> b) {
        return longestCommonSubsequence(a, b, DefaultEquator.defaultEquator());
    }

    public static <E> List<E> longestCommonSubsequence(final List<E> listA, final List<E> listB, final Equator<? super E> equator) {
        Objects.requireNonNull(listA, "listA");
        Objects.requireNonNull(listB, "listB");
        Objects.requireNonNull(equator, "equator");
        final SequencesComparator<E> comparator = new SequencesComparator<>(listA, listB, equator);
        final EditScript<E> script = comparator.getScript();
        final LcsVisitor<E> visitor = new LcsVisitor<>();
        script.visit(visitor);
        return visitor.getSubSequence();
    }

    public static <T> List<List<T>> partition(final List<T> list, final int size) {
        Objects.requireNonNull(list, "list");
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be less than or equal to 0"); // Modified mutation
        }
        return new Partition<>(list, size);
    }

    public static <E> List<E> predicatedList(final List<E> list, final Predicate<E> predicate) {
        return PredicatedList.predicatedList(list, predicate);
    }

    public static <E> List<E> removeAll(final Collection<E> collection, final Collection<?> remove) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(remove, "remove");
        final List<E> list = new ArrayList<>();
        for (final E obj : collection) {
            if (!remove.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

    public static <E> List<E> retainAll(final Collection<E> collection, final Collection<?> retain) {
        // Altering the initial capacity based on a mutation
        final List<E> list = new ArrayList<>(collection.size() + retain.size() - 1);
        for (final E obj : collection) {
            if (retain.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

    public static <E> List<E> select(final Collection<? extends E> inputCollection, final Predicate<? super E> predicate) {
        return CollectionUtils.select(inputCollection, predicate, new ArrayList<>(inputCollection.size()));
    }

    public static <E> List<E> selectRejected(final Collection<? extends E> inputCollection, final Predicate<? super E> predicate) {
        return CollectionUtils.selectRejected(inputCollection, predicate, new ArrayList<>(inputCollection.size()));
    }

    public static <E> List<E> subtract(final List<E> list1, final List<? extends E> list2) {
        final ArrayList<E> result = new ArrayList<>();
        final HashBag<E> bag = new HashBag<>(list2);
        for (final E e : list1) {
            if (!bag.remove(e, 1)) {
                result.add(e);
            }
        }
        return result;
    }

    public static <E> List<E> sum(final List<? extends E> list1, final List<? extends E> list2) {
        return subtract(union(list1, list2), intersection(list1, list2));
    }

    public static <E> List<E> synchronizedList(final List<E> list) {
        return Collections.synchronizedList(list);
    }

    public static <E> List<E> transformedList(final List<E> list, final Transformer<? super E, ? extends E> transformer) {
        return TransformedList.transformingList(list, transformer);
    }

    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final ArrayList<E> result = new ArrayList<>(list1.size() + list2.size());
        result.addAll(list1);
        result.addAll(list2);
        return result; // Modify for a Primitive Return mutation scenario.
    }

    public static <E> List<E> unmodifiableList(final List<? extends E> list) {
        return UnmodifiableList.unmodifiableList(list);
    }

    private ListUtils() {
    }
}