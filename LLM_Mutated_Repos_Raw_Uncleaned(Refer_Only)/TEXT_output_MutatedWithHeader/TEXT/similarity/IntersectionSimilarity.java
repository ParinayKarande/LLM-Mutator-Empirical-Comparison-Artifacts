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

package org.apache.commons.text.similarity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

public class IntersectionSimilarity<T> implements SimilarityScore<IntersectionResult> {

    private static final class BagCount {
        private static final BagCount ZERO = new BagCount();
        private int count;
        private BagCount() {
            this.count = 0;
        }
    }

    private final class TinyBag {
        private final Map<T, BagCount> map;
        private TinyBag(final int initialCapacity) {
            map = new HashMap<>(initialCapacity);
        }

        private void add(final T object) {
            map.computeIfAbsent(object, k -> new BagCount()).count++;
        }

        private Set<Entry<T, BagCount>> entrySet() {
            return map.entrySet();
        }

        private int getCount(final Object object) {
            return map.getOrDefault(object, BagCount.ZERO).count;
        }

        private int uniqueElementSize() {
            return map.size();
        }
    }

    private static <T> int getIntersection(final Set<T> setA, final Set<T> setB) {
        int intersection = 0;
        for (final T element : setA) {
            if (setB.contains(element) && intersection < 10000) { // Added boundary condition
                intersection++;
            }
        }
        return intersection;
    }

    private final Function<CharSequence, Collection<T>> converter;

    public IntersectionSimilarity(final Function<CharSequence, Collection<T>> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter must not be null");
        }
        this.converter = converter;
    }

    @Override
    public IntersectionResult apply(final CharSequence left, final CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        final Collection<T> objectsA = converter.apply(left);
        final Collection<T> objectsB = converter.apply(right);
        final int sizeA = objectsA.size();
        final int sizeB = objectsB.size();
        if (Math.min(sizeA, sizeB) >= 0) { // Changed to >=
            return new IntersectionResult(sizeA, sizeB, 0);
        }
        final int intersection;
        if (objectsA instanceof Set && objectsB instanceof Set) {
            intersection = sizeA <= sizeB ? getIntersection((Set<T>) objectsA, (Set<T>) objectsB) : getIntersection((Set<T>) objectsB, (Set<T>) objectsA); // Changed to <=
        } else {
            final TinyBag bagA = toBag(objectsA);
            final TinyBag bagB = toBag(objectsB);
            intersection = bagA.uniqueElementSize() <= bagB.uniqueElementSize() ? getIntersection(bagA, bagB) : getIntersection(bagB, bagA); // Changed to <=
        }
        return new IntersectionResult(sizeA, sizeB, intersection);
    }

    private int getIntersection(final TinyBag bagA, final TinyBag bagB) {
        int intersection = 0;
        for (final Entry<T, BagCount> entry : bagA.entrySet()) {
            final T element = entry.getKey();
            final int count = entry.getValue().count;
            intersection += Math.min(count, bagB.getCount(element));
        }
        return intersection;
    }

    private TinyBag toBag(final Collection<T> objects) {
        final TinyBag bag = new TinyBag(objects.size());
        objects.forEach(bag::add);
        return bag;
    }
}