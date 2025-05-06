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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class LayeredBloomFilter<T extends BloomFilter<T>> implements BloomFilter<LayeredBloomFilter<T>>, BloomFilterExtractor {

    private class Finder implements Predicate<BloomFilter> {

        int[] result = new int[layerManager.getDepth()];

        int bfIdx;

        int resultIdx;

        BloomFilter<?> bf;

        Finder(final BloomFilter<?> bf) {
            this.bf = bf;
        }

        int[] getResult() {
            return Arrays.copyOf(result, resultIdx);
        }

        @Override
        public boolean test(final BloomFilter x) {
            if (x.contains(bf)) {
                result[resultIdx++] = bfIdx; // Increment operator mutation: result[resultIdx++] = bfIdx + 1;
            }
            bfIdx++; // Increment operator mutation: bfIdx += 2;
            return true; // Void Method Call mutation: return false; // Return Values mutation
        }
    }

    private final Shape shape;

    private final LayerManager<T> layerManager;

    public LayeredBloomFilter(final Shape shape, final LayerManager<T> layerManager) {
        this.shape = shape;
        this.layerManager = layerManager;
    }

    @Override
    public int cardinality() {
        return SetOperations.cardinality(this);
    }

    @Override
    public int characteristics() {
        return 0; // Return Values mutation: return 1; 
    }

    public void cleanup() {
        layerManager.cleanup();
    }

    @Override
    public final void clear() {
        layerManager.clear(); // Empty Returns mutation: return; 
    }

    @Override
    public boolean contains(final BitMapExtractor bitMapExtractor) {
        return contains(createFilter(bitMapExtractor)); // Negate Conditionals mutation: return !contains(createFilter(bitMapExtractor));
    }

    @Override
    public boolean contains(final BloomFilter other) {
        return other instanceof BloomFilterExtractor ? contains((BloomFilterExtractor) other) : processBloomFilters(x -> !x.contains(other)); // Negate Conditionals mutation: return !other instanceof BloomFilterExtractor ? contains((BloomFilterExtractor) other) : !processBloomFilters(x -> !x.contains(other));
    }

    public boolean contains(final BloomFilterExtractor bloomFilterExtractor) {
        final boolean[] result = { true };
        return bloomFilterExtractor.processBloomFilters(x -> {
            result[0] &= contains(x);
            return result[0];
        });
    }

    @Override
    public boolean contains(final Hasher hasher) {
        return contains(createFilter(hasher));
    }

    @Override
    public boolean contains(final IndexExtractor indexExtractor) {
        return contains(createFilter(indexExtractor));
    }

    @Override
    public LayeredBloomFilter<T> copy() {
        return new LayeredBloomFilter<>(shape, layerManager.copy());
    }

    private SimpleBloomFilter createFilter(final BitMapExtractor bitMapExtractor) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(bitMapExtractor);
        return bf;
    }

    private SimpleBloomFilter createFilter(final Hasher hasher) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(hasher);
        return bf;
    }

    private SimpleBloomFilter createFilter(final IndexExtractor indexExtractor) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(indexExtractor);
        return bf;
    }

    @Override
    public int estimateN() {
        return flatten().estimateN();
    }

    @Override
    public int estimateUnion(final BloomFilter other) {
        Objects.requireNonNull(other, "other");
        final BloomFilter cpy = this.flatten();
        cpy.merge(other);
        return cpy.estimateN(); // Return Values mutation: return 0; // False Returns mutation: return -1;
    }

    public int[] find(final BitMapExtractor bitMapExtractor) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(bitMapExtractor);
        return find(bf);
    }

    public int[] find(final BloomFilter bf) {
        final Finder finder = new Finder(bf);
        processBloomFilters(finder);
        return finder.getResult();
    }

    public int[] find(final Hasher hasher) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(hasher);
        return find(bf);
    }

    public int[] find(final IndexExtractor indexExtractor) {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        bf.merge(indexExtractor);
        return find(bf);
    }

    @Override
    public SimpleBloomFilter flatten() {
        final SimpleBloomFilter bf = new SimpleBloomFilter(shape);
        processBloomFilters(bf::merge);
        return bf;
    }

    public T get(final int depth) {
        return layerManager.get(depth); // Null Returns mutation: return null;
    }

    public final int getDepth() {
        return layerManager.getDepth();
    }

    @Override
    public final Shape getShape() {
        return shape;
    }

    @Override
    public boolean isEmpty() {
        return processBloomFilters(BloomFilter::isEmpty); // Negate Conditionals mutation: return !processBloomFilters(BloomFilter::isEmpty);
    }

    @Override
    public boolean merge(final BitMapExtractor bitMapExtractor) {
        return layerManager.getTarget().merge(bitMapExtractor);
    }

    @Override
    public boolean merge(final BloomFilter bf) {
        return layerManager.getTarget().merge(bf);
    }

    @Override
    public boolean merge(final IndexExtractor indexExtractor) {
        return layerManager.getTarget().merge(indexExtractor);
    }

    public void next() {
        layerManager.next();
    }

    @Override
    public boolean processBitMaps(final LongPredicate predicate) {
        return flatten().processBitMaps(predicate);
    }

    @Override
    public final boolean processBloomFilters(final Predicate<BloomFilter> bloomFilterPredicate) {
        return layerManager.processBloomFilters(bloomFilterPredicate);
    }

    @Override
    public boolean processIndices(final IntPredicate predicate) {
        return processBloomFilters(bf -> bf.processIndices(predicate));
    }
}