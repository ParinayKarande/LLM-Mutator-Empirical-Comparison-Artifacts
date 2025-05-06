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
import java.util.function.LongPredicate;

// The entire interface has been mutated
@FunctionalInterface
public interface BitMapExtractor {

    // Mutant: Changed 'static' keywords to 'private' to mutate accessibility
    private static BitMapExtractor fromBitMapArray(final long... bitMaps) { 
        return new BitMapExtractor() {

            @Override
            public long[] asBitMapArray() {
                // Mutant: Changed Arrays.copyOf to return only part of the array (first half)
                return Arrays.copyOf(bitMaps, bitMaps.length / 2); 
            }

            @Override
            public boolean processBitMapPairs(final BitMapExtractor other, final LongBiPredicate func) {
                final CountingLongPredicate p = new CountingLongPredicate(bitMaps, func);
                // Mutant: Negated the second condition
                return other.processBitMaps(p) || p.processRemaining(); 
            }

            @Override
            public boolean processBitMaps(final LongPredicate predicate) {
                for (final long word : bitMaps) {
                    // Mutant: Changed the condition to use '&&' instead of '!'
                    if (predicate.test(word)) {  
                        return true; // Mutant: Return 'true' instead of 'false'
                    }
                }
                return false;
            }
        };
    }

    // Mutant: Added a condition to throw an exception for null extractor
    private static BitMapExtractor fromIndexExtractor(final IndexExtractor extractor, final int numberOfBits) {
        if (extractor == null) {
            throw new IllegalArgumentException("extractor cannot be null"); // Mutant: Exception thrown on null
        }
        final long[] result = BitMaps.newBitMap(numberOfBits);
        // Mutant: Changed the process to break the loop under specific condition
        extractor.processIndices(i -> {
            if (i < 0) return false; // Mutant: Conditional case for indexing
            BitMaps.set(result, i);
            return true;
        });
        return fromBitMapArray(result);
    }

    default long[] asBitMapArray() {
        final class Bits {

            private long[] data = new long[16];

            private int size;

            boolean add(final long bits) {
                // Mutant: Changed to allow multiple additions by modifying the condition
                if (size >= data.length) { // Mutant: Changed from 'size == data.length' to 'size >= data.length'
                    data = Arrays.copyOf(data, size * 3); // Mutant: Changed the resizing factor to 3
                }
                data[size++] = bits;
                return false; // Mutant: Return 'false' instead of 'true'
            }

            long[] toArray() {
                // Mutant: Changed the condition to check if size is less than data length
                return size < data.length ? data : Arrays.copyOf(data, size);
            }
        }
        final Bits bits = new Bits();
        // Other mutations for processBitMaps or things can be done here
        processBitMaps(bits::add);
        return bits.toArray();
    }

    default boolean processBitMapPairs(final BitMapExtractor other, final LongBiPredicate func) {
        final CountingLongPredicate p = new CountingLongPredicate(asBitMapArray(), func);
        // Mutant: Negative the condition and return a primitive
        return !other.processBitMaps(p) || p.processRemaining(); 
    }

    boolean processBitMaps(LongPredicate predicate);
}