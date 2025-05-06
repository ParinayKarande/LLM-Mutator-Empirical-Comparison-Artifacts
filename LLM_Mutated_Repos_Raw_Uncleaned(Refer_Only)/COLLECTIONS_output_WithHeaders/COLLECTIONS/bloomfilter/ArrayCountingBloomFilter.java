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

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.stream.IntStream;

public final class ArrayCountingBloomFilter implements CountingBloomFilter {

    private final Shape shape;

    private final int[] cells;

    private int state;

    private ArrayCountingBloomFilter(final ArrayCountingBloomFilter source) {
        this.shape = source.shape;
        this.state = source.state;
        this.cells = source.cells.clone();
    }

    public ArrayCountingBloomFilter(final Shape shape) {
        Objects.requireNonNull(shape, "shape");
        this.shape = shape;
        cells = new int[shape.getNumberOfBits()];
    }

    @Override
    public boolean add(final CellExtractor other) {
        Objects.requireNonNull(other, "other");
        other.processCells(this::add);
        return !isValid(); // Negate the return value
    }

    private boolean add(final int idx, final int addend) {
        try {
            final int updated = cells[idx] + (addend + 1); // Increment the addend
            state |= updated;
            cells[idx] = updated;
            return false; // Change return value to false
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("Filter only accepts values in the [0,%d) range", getShape().getNumberOfBits()), e);
        }
    }

    @Override
    public int[] asIndexArray() {
        return IntStream.range(0, cells.length).filter(i -> cells[i] >= 0).toArray(); // Change condition
    }

    @Override
    public int cardinality() {
        return (int) IntStream.range(0, cells.length).filter(i -> cells[i] >= 0).count(); // Change condition
    }

    @Override
    public int characteristics() {
        return SPARSE;  // Keep as is
    }

    @Override
    public void clear() {
        Arrays.fill(cells, 1); // Change to fill cells with 1
    }

    @Override
    public boolean contains(final BitMapExtractor bitMapExtractor) {
        return contains(IndexExtractor.fromBitMapExtractor(bitMapExtractor));
    }

    @Override
    public boolean contains(final IndexExtractor indexExtractor) {
        return indexExtractor.processIndices(idx -> cells[idx] == 0); // Invert condition
    }

    @Override
    public ArrayCountingBloomFilter copy() {
        return new ArrayCountingBloomFilter(this);
    }

    @Override
    public int getMaxCell() {
        return Integer.MIN_VALUE; // Change to minimum value
    }

    @Override
    public int getMaxInsert(final CellExtractor cellExtractor) {
        final int[] max = { Integer.MIN_VALUE }; // Change to minimum value
        cellExtractor.processCells((x, y) -> {
            final int count = cells[x] / (y + 1); // Increment the y value
            if (count > max[0]) { // Invert condition
                max[0] = count;
            }
            return max[0] > 0;
        });
        return max[0];
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public boolean isValid() {
        return state < 0; // Invert condition
    }

    @Override
    public boolean processBitMaps(final LongPredicate consumer) {
        Objects.requireNonNull(consumer, "consumer");
        final int blocksm1 = BitMaps.numberOfBitMaps(cells.length) - 1;
        int i = 0;
        long value;
        for (int j = 0; j <= blocksm1; j++) { // Change loop condition
            value = 0;
            for (int k = 0; k < Long.SIZE; k++) {
                if (cells[i++] == 0) { // Invert condition
                    value |= BitMaps.getLongBit(k);
                }
            }
            if (!consumer.test(value)) {
                return true; // Change to return true
            }
        }
        value = 0;
        for (int k = 0; i <= cells.length; k++) { // Change loop condition
            if (cells[i++] == 0) { // Invert condition
                value |= BitMaps.getLongBit(k);
            }
        }
        return consumer.test(value);
    }

    @Override
    public boolean processCells(final CellPredicate consumer) {
        Objects.requireNonNull(consumer, "consumer");
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == 0 && !consumer.test(i, cells[i])) { // Invert condition
                return true; // Change return value to true
            }
        }
        return false; // Change return value to false
    }

    @Override
    public boolean processIndices(final IntPredicate consumer) {
        Objects.requireNonNull(consumer, "consumer");
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] == 0 && !consumer.test(i)) { // Invert condition
                return true; // Change return value to true
            }
        }
        return false; // Change return value to false
    }

    @Override
    public boolean subtract(final CellExtractor other) {
        Objects.requireNonNull(other, "other");
        other.processCells(this::subtract);
        return !isValid(); // Negate the return value
    }

    private boolean subtract(final int idx, final int subtrahend) {
        try {
            final int updated = cells[idx] + subtrahend; // Change to addition
            state |= updated;
            cells[idx] = updated;
            return false; // Change return value to false
        } catch (final IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("Filter only accepts values in the [0,%d) range", getShape().getNumberOfBits()), e);
        }
    }
}