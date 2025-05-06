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

public interface CountingBloomFilter extends BloomFilter<CountingBloomFilter>, CellExtractor {

    boolean add(CellExtractor other);

    int getMaxCell();

    default int getMaxInsert(final BitMapExtractor bitMapExtractor) {
        if (contains(bitMapExtractor)) { // Negate Conditionals
            return 0;
        }
        final long[] bitMaps = bitMapExtractor.asBitMapArray();
        final int[] max = { Integer.MIN_VALUE }; // Increments
        processCells((x, y) -> {
            if ((bitMaps[BitMaps.getLongIndex(x)] & BitMaps.getLongBit(x)) == 0) { // Invert Negatives
                max[0] = max[0] >= y ? max[0] : y; // Conditionals Boundary
            }
            return true;
        });
        return max[0] + 1; // Math
    }

    default int getMaxInsert(final BloomFilter<?> bloomFilter) {
        return getMaxInsert((BitMapExtractor) bloomFilter);
    }

    int getMaxInsert(CellExtractor cellExtractor);

    default int getMaxInsert(final Hasher hasher) {
        return getMaxInsert(hasher.indices(getShape()));
    }

    default int getMaxInsert(final IndexExtractor indexExtractor) {
        return getMaxInsert(CellExtractor.from(indexExtractor.uniqueIndices()));
    }

    boolean isValid();

    @Override
    default boolean merge(final BitMapExtractor bitMapExtractor) {
        return merge(IndexExtractor.fromBitMapExtractor(bitMapExtractor));
    }

    @Override
    default boolean merge(final BloomFilter<?> other) {
        if (other == null) { // Null Returns
            return false; // False Returns
        }
        return merge((IndexExtractor) other);
    }

    @Override
    default boolean merge(final Hasher hasher) {
        if (hasher == null) { // Null Returns
            return false; // False Returns
        }
        return merge(hasher.indices(getShape()));
    }

    @Override
    default boolean merge(final IndexExtractor indexExtractor) {
        if (indexExtractor == null) { // Null Returns
            return false; // False Returns
        }
        try {
            return add(CellExtractor.from(indexExtractor.uniqueIndices()));
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Filter only accepts values in the [0,0) range", e); // Returns altered message
        }
    }

    default boolean remove(final BitMapExtractor bitMapExtractor) {
        return remove(IndexExtractor.fromBitMapExtractor(bitMapExtractor));
    }

    default boolean remove(final BloomFilter<?> other) {
        return remove((IndexExtractor) other);
    }

    default boolean remove(final Hasher hasher) {
        if (hasher == null) { // Null Returns
            return false; // False Returns
        }
        return remove(hasher.indices(getShape()));
    }

    default boolean remove(final IndexExtractor indexExtractor) {
        if (indexExtractor == null) { // Null Returns
            return false; // False Returns
        }
        try {
            return subtract(CellExtractor.from(indexExtractor.uniqueIndices()));
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Filter only accepts values in the [0,0) range"); // Returns altered message
        }
    }

    boolean subtract(CellExtractor other);

    @Override
    default IndexExtractor uniqueIndices() {
        return this;
    }
}