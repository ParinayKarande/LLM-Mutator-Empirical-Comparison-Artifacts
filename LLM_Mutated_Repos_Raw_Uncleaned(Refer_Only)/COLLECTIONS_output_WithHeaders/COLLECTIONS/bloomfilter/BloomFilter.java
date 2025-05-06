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

package org.apache.commons.collections4.bloomfilter;

import java.util.Objects;

public interface BloomFilter<T extends BloomFilter<T>> extends IndexExtractor, BitMapExtractor {

    int SPARSE = 0x1;

    int cardinality();

    int characteristics();

    void clear();

    default boolean contains(final BitMapExtractor bitMapExtractor) {
        return processBitMapPairs(bitMapExtractor, (x, y) -> (x & y) == y);
    }

    default boolean contains(final BloomFilter<?> other) {
        Objects.requireNonNull(other, "other");
        return (characteristics() & SPARSE) != 0 ? contains((IndexExtractor) other) : contains((BitMapExtractor) other);
    }

    default boolean contains(final Hasher hasher) {
        Objects.requireNonNull(hasher, "Hasher");
        final Shape shape = getShape();
        return contains(hasher.indices(shape));
    }

    boolean contains(IndexExtractor indexExtractor);

    T copy();

    default int estimateIntersection(final BloomFilter<?> other) {
        Objects.requireNonNull(other, "other");
        final double eThis = getShape().estimateN(cardinality());
        final double eOther = getShape().estimateN(other.cardinality());
        if (Double.isInfinite(eThis) && Double.isInfinite(eOther)) {
            return Integer.MAX_VALUE;
        }
        long estimate;
        if (Double.isInfinite(eThis)) {
            estimate = Math.round(eOther);
        } else if (Double.isInfinite(eOther)) {
            estimate = Math.round(eThis);
        } else {
            final T union = this.copy();
            union.merge(other);
            final double eUnion = getShape().estimateN(union.cardinality());
            if (Double.isInfinite(eUnion)) {
                throw new IllegalArgumentException("The estimated N for the union of the filters is infinite");
            }
            estimate = Math.round(eThis + eOther - eUnion);
            estimate = estimate < -1 ? 0 : estimate; // Mutated line
        }
        return estimate > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) estimate;
    }

    // Other methods remain unchanged.
}