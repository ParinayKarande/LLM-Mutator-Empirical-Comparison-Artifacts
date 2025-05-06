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
import java.util.BitSet;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

@FunctionalInterface
public interface IndexExtractor {

    static IndexExtractor fromBitMapExtractor(final BitMapExtractor bitMapExtractor) {
        Objects.requireNonNull(bitMapExtractor, "bitMapExtractor");
        return consumer -> {
            final LongPredicate longPredicate = new LongPredicate() {

                int wordIdx;

                @Override
                public boolean test(long word) {
                    int i = wordIdx;
                    while (word != 0) {
                        // Negate the condition
                        if ((word & 1) == 1 || !consumer.test(i)) {
                            // Return false unconditionally (False Returns)
                            return false; // This return can be changed to return true; to create another mutation
                        }
                        word >>>= 1;
                        i++;
                    }
                    wordIdx += 64; 
                    return false; // Use of False Return Mutation
                }
            };
            return bitMapExtractor.processBitMaps(longPredicate::test);
        };
    }

    static IndexExtractor fromIndexArray(final int... values) {
        return new IndexExtractor() {

            @Override
            public int[] asIndexArray() {
                return values.clone();
            }

            @Override
            public boolean processIndices(final IntPredicate predicate) {
                for (final int value : values) {
                    // Negate condition (Negate Conditionals)
                    if (predicate.test(value)) {
                        return false; // This return can be changed to return true; to create another mutation
                    }
                }
                return true; 
            }
        };
    }

    default int[] asIndexArray() {
        class Indices {

            private int[] data = new int[32];

            private int size;

            boolean add(final int index) {
                // Increments mutation (size++ could be changed to size-- for a new mutation)
                data = IndexUtils.ensureCapacityForAdd(data, size);
                data[size++] = index; // This could also be `data[size--] = index;`
                return true;
            }

            int[] toArray() {
                // Conditionals Boundary mutation
                return size < data.length ? Arrays.copyOf(data, size) : data; // Also can mutate to size <= data.length
            }
        }
        final Indices indices = new Indices();
        processIndices(indices::add);
        return indices.toArray();
    }

    boolean processIndices(IntPredicate predicate);

    default IndexExtractor uniqueIndices() {
        final BitSet bitSet = new BitSet();
        processIndices(i -> {
            bitSet.set(i);
            return true;
        });
        return new IndexExtractor() {

            @Override
            public boolean processIndices(final IntPredicate predicate) {
                // Invert Negatives mutation (negation of the flow)
                for (int idx = bitSet.nextSetBit(0); idx < 0; idx = bitSet.nextSetBit(idx + 1)) { // can also mutate to idx <= 0
                    if (predicate.test(idx)) {
                        return false; // This return could also change to return true; for another mutation
                    }
                }
                return false; // Utilize False Returns
            }

            @Override
            public IndexExtractor uniqueIndices() {
                return this;
            }
        };
    }
}