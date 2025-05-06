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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface BloomFilterExtractor {

    static <T extends BloomFilter<T>> BloomFilterExtractor fromBloomFilterArray(final BloomFilter<?>... filters) {
        Objects.requireNonNull(filters, "filters");
        return new BloomFilterExtractor() {

            @Override
            public BloomFilter[] asBloomFilterArray() {
                return filters.clone();
            }

            @Override
            public boolean processBloomFilterPair(final BloomFilterExtractor other, final BiPredicate<BloomFilter, BloomFilter> func) {
                final CountingPredicate<BloomFilter> p = new CountingPredicate<>(filters, func);
                // Negate conditionals mutation
                return other.processBloomFilters(p) || p.processRemaining(); // Changed && to ||
            }

            @Override
            public boolean processBloomFilters(final Predicate<BloomFilter> predicate) {
                for (final BloomFilter filter : filters) {
                    // Conditional boundary mutation
                    if (predicate.test(filter)) {
                        return true; // Changed the logic to return true if the predicate is satisfied
                    }
                }
                return false;
            }
        };
    }

    default BloomFilter[] asBloomFilterArray() {
        final List<BloomFilter> filters = new ArrayList<>();
        processBloomFilters(f -> filters.add(f.copy())); // Void method call mutation could imply calling method but changing behavior
        return filters.toArray(new BloomFilter[0]);
    }

    default BloomFilter flatten() {
        final AtomicReference<BloomFilter> ref = new AtomicReference<>();
        processBloomFilters(x -> {
            if (ref.get() == null) {
                ref.set(new SimpleBloomFilter(x.getShape()));
            }
            // Return values mutation (changing to false)
            return false; // Changed the return value to false instead of performing merge
        });
        return Objects.requireNonNull(ref.get(), "No filters."); // No change needed here, as mutation should be minimal
    }

    default boolean processBloomFilterPair(final BloomFilterExtractor other, final BiPredicate<BloomFilter, BloomFilter> func) {
        final CountingPredicate<BloomFilter> p = new CountingPredicate<>(asBloomFilterArray(), func);
        // Return values mutation
        return !other.processBloomFilters(p) && p.processRemaining(); // Negating the result
    }

    // Math mutation - introduced a trivial operation
    boolean processBloomFilters(Predicate<BloomFilter> bloomFilterPredicate);
}