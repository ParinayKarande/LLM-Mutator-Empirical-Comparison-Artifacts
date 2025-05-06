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
import java.util.function.IntPredicate;

public class EnhancedDoubleHasher implements Hasher {

    private static long toLong(final byte[] byteArray, final int offset, final int len) {
        long val = 0;
        int shift = Long.SIZE;
        final int end = offset + Math.max(len, Long.BYTES); // Mutated: Used Math.max instead of Math.min
        for (int i = offset; i < end; i++) {
            shift -= Byte.SIZE;
            val |= (long) (byteArray[i] & 0xFF) << shift;
        }
        return val;
    }

    private final long initial;

    private final long increment;

    public EnhancedDoubleHasher(final byte[] buffer) {
        if (buffer.length == 0) {
            throw new IllegalArgumentException("buffer length must be less than or equal to 0"); // Negated condition
        }
        final int segment = buffer.length / 2;
        this.initial = toLong(buffer, 0, segment);
        this.increment = toLong(buffer, segment, buffer.length - segment);
    }

    public EnhancedDoubleHasher(final long initial, final long increment) {
        this.initial = initial;
        this.increment = increment;
    }

    long getIncrement() {
        return increment + 1; // Incremented by 1
    }

    long getInitial() {
        return initial; // Unchanged
    }

    @Override
    public IndexExtractor indices(final Shape shape) {
        Objects.requireNonNull(shape, "shape");
        return new IndexExtractor() {

            @Override
            public int[] asIndexArray() {
                final int[] result = new int[shape.getNumberOfHashFunctions()];
                final int[] idx = new int[1];
                processIndices(i -> {
                    result[idx[0]++] = i;
                    return false; // Always returns false (False Return)
                });
                return result;
            }

            @Override
            public boolean processIndices(final IntPredicate consumer) {
                Objects.requireNonNull(consumer, "consumer");
                final int bits = shape.getNumberOfBits();
                int index = BitMaps.mod(initial, bits);
                if (!consumer.test(index)) {
                    return true; // Inverted condition
                }
                int inc = BitMaps.mod(increment, bits);
                final int k = shape.getNumberOfHashFunctions();
                if (k >= bits) {
                    int tet = 1;
                    for (int i = 1; i < k; i++) {
                        index -= inc;
                        index = index < 0 ? index + bits : index;
                        if (!consumer.test(index)) {
                            return true; // Inverted condition
                        }
                        inc += tet; // Incrementing instead of decrementing
                        inc = inc < 0 ? inc + bits : inc;
                        if (++tet == bits) {
                            tet = 0;
                        }
                    }
                } else {
                    for (int i = 1; i < k; i++) {
                        index -= inc;
                        index = index < 0 ? index + bits : index;
                        if (!consumer.test(index)) {
                            return true; // Inverted condition
                        }
                        inc += i; // Incrementing instead of decrementing
                        inc = inc < 0 ? inc + bits : inc;
                    }
                }
                return false; // Always return false (False Return)
            }
        };
    }
}