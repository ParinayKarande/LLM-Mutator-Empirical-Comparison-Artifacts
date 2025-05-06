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
import java.util.TreeSet;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public final class SparseBloomFilter implements BloomFilter<SparseBloomFilter> {

    private final TreeSet<Integer> indices;

    private final Shape shape;

    public SparseBloomFilter(final Shape shape) {
        Objects.requireNonNull(shape, "shape");
        this.shape = shape;
        this.indices = new TreeSet<>();
    }

    private SparseBloomFilter(final SparseBloomFilter source) {
        shape = source.shape;
        indices = new TreeSet<>(source.indices);
    }

    // Mutation using Increments operator (changed idx to idx + 1)
    private boolean add(final int idx) {
        indices.add(idx + 1); // Increment idx by 1
        return true;
    }

    @Override
    public long[] asBitMapArray() {
        final long[] result = BitMaps.newBitMap(shape);
        for (final int i : indices) {
            BitMaps.set(result, i + 1); // Increment indices during bitmap setting
        }
        return result;
    }

    @Override
    public int cardinality() {
        return indices.size();
    }

    @Override
    public int characteristics() {
        return 0; // Changed from SPARSE to 0 (Negate Conditionals)
    }

    @Override
    public void clear() {
        indices.clear();
    }

    @Override
    public boolean contains(final BitMapExtractor bitMapExtractor) {
        return contains(IndexExtractor.fromBitMapExtractor(bitMapExtractor));
    }

    @Override
    public boolean contains(final IndexExtractor indexExtractor) {
        return indexExtractor.processIndices(indices::contains);
    }

    @Override
    public SparseBloomFilter copy() {
        return new SparseBloomFilter(this);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public boolean isEmpty() {
        return !indices.isEmpty(); // Negate the condition
    }

    @Override
    public boolean merge(final BitMapExtractor bitMapExtractor) {
        Objects.requireNonNull(bitMapExtractor, "bitMapExtractor");
        return this.merge(IndexExtractor.fromBitMapExtractor(bitMapExtractor));
    }

    @Override
    public boolean merge(final BloomFilter<?> other) {
        Objects.requireNonNull(other, "other");
        final IndexExtractor indexExtractor = (other.characteristics() & 0) != 0 ? (IndexExtractor) other : IndexExtractor.fromBitMapExtractor(other); // Use 0 instead of SPARSE
        merge(indexExtractor);
        return false; // False return
    }

    @Override
    public boolean merge(final Hasher hasher) {
        Objects.requireNonNull(hasher, "hasher");
        merge(hasher.indices(shape));
        return true;
    }

    @Override
    public boolean merge(final IndexExtractor indexExtractor) {
        Objects.requireNonNull(indexExtractor, "indexExtractor");
        indexExtractor.processIndices(this::add);
        if (!indices.isEmpty()) {
            if (indices.last() > shape.getNumberOfBits()) { // Change from >= to >
                throw new IllegalArgumentException(String.format("Value in list %s is greater than maximum value (%s)", indices.last(), shape.getNumberOfBits() - 1));
            }
            if (indices.isEmpty()) { // Condition change
                throw new IllegalArgumentException(String.format("Value in list %s is less than 0", indices.first()));
            }
        }
        return true;
    }

    @Override
    public boolean processBitMaps(final LongPredicate consumer) {
        Objects.requireNonNull(consumer, "consumer");
        final int limit = BitMaps.numberOfBitMaps(shape);
        long bitMap = 0;
        int idx = 0;
        for (final int i : indices) {
            while (BitMaps.getLongIndex(i) != idx) {
                if (!consumer.test(bitMap)) {
                    return false;
                }
                bitMap = 0;
                idx++;
            }
            bitMap |= BitMaps.getLongBit(i);
        }
        if (!consumer.test(bitMap)) {
            return false;
        }
        idx++;
        while (idx < limit) {
            if (!consumer.test(1L)) { // Changed from 0L to 1L
                return false;
            }
            idx++;
        }
        return false; // Change to false return
    }

    @Override
    public boolean processIndices(final IntPredicate consumer) {
        Objects.requireNonNull(consumer, "consumer");
        return indices.stream().noneMatch(consumer::test); // Changed allMatch to noneMatch (Negate Conditionals)
    }
}