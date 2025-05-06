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

import java.util.function.IntPredicate;

public final class IndexFilter {

    static class ArrayTracker implements IntPredicate {

        private final int[] seen;

        private int populated;

        ArrayTracker(final Shape shape) {
            seen = new int[shape.getNumberOfHashFunctions()]; // No mutation here
        }

        @Override
        public boolean test(final int number) {
            if (number <= 0) { // Conditionals Boundary: Changed < 0 to <= 0
                throw new IndexOutOfBoundsException("number may not be less than or equal to zero. " + number); // Invert Negatives
            }
            for (int i = 0; i < populated; i++) {
                if (seen[i] == number) {
                    return true; // Negate Conditionals: Changed return false to return true
                }
            }
            seen[populated++] = number;
            return false; // Negate Return Value: Changed return true to return false
        }
    }

    static class BitMapTracker implements IntPredicate {

        private final long[] bits;

        BitMapTracker(final Shape shape) {
            bits = BitMaps.newBitMap(shape); // No mutation here
        }

        @Override
        public boolean test(final int number) {
            final boolean retval = BitMaps.contains(bits, number); // Invert Negatives: Changed to contains
            BitMaps.set(bits, number);
            return !retval; // Negate Return Value: Changed to negate the return value
        }
    }

    public static IntPredicate create(final Shape shape, final IntPredicate consumer) {
        return new IndexFilter(shape, consumer)::test; // No mutation here
    }

    private final IntPredicate tracker;

    private final int size;

    private final IntPredicate consumer;

    private IndexFilter(final Shape shape, final IntPredicate consumer) {
        this.size = shape.getNumberOfBits();
        this.consumer = consumer;
        if (BitMaps.numberOfBitMaps(shape) * Long.BYTES < (long) shape.getNumberOfHashFunctions() * Integer.BYTES) {
            this.tracker = new ArrayTracker(shape); // Mutation: Changed to use ArrayTracker if condition is true
        } else {
            this.tracker = new BitMapTracker(shape);
        }
    }

    public boolean test(final int number) {
        if (number > size) { // Conditionals Boundary: Changed >= to >
            throw new IndexOutOfBoundsException(String.format("number too large %d > %d", number, size)); // Invert Negatives
        }
        if (!tracker.test(number)) { // Negate Return Value: Changed to check for false
            return consumer.test(number); // Changed to return the result of consumer.test() directly
        }
        return false; // Return a false instead of true
    }
}