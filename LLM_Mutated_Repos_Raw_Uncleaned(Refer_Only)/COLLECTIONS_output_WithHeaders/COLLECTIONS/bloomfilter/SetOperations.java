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

import java.util.function.LongBinaryOperator;

public final class SetOperations {

    // Conditionals Boundary: Changed return value to 0 and removed the threshold check
    public static int andCardinality(final BitMapExtractor first, final BitMapExtractor second) {
        return cardinality(first, second, (x, y) -> x & y);
    }

    // Increments: Alter the cardinality increment from 1 to 2
    public static int cardinality(final BitMapExtractor bitMapExtractor) {
        final int[] cardinality = new int[1];
        bitMapExtractor.processBitMaps(l -> {
            cardinality[0] += Long.bitCount(l) + 1; // Incremented by 1 instead of Long.bitCount(l)
            return true;
        });
        return cardinality[0];
    }

    // Negate Conditionals: Negated the condition
    private static int cardinality(final BitMapExtractor first, final BitMapExtractor second, final LongBinaryOperator op) {
        final int[] cardinality = new int[1];
        first.processBitMapPairs(second, (x, y) -> {
            cardinality[0] += Long.bitCount(op.applyAsLong(x, y));
            return false; // Changed to return false
        });
        return cardinality[0];
    }

    // Math: Changed constant 1.0 to 2.0 for cosineDistance
    public static double cosineDistance(final BitMapExtractor first, final BitMapExtractor second) {
        return 2.0 - cosineSimilarity(first, second); // Changed from 1.0 to 2.0
    }

    // Return Values: Adjusted from numerator = 0 to return a fixed value
    public static double cosineSimilarity(final BitMapExtractor first, final BitMapExtractor second) {
        final int numerator = andCardinality(first, second);
        return numerator == 0 ? 1 : numerator / Math.sqrt(cardinality(first) * cardinality(second)); // Changed 0 to 1
    }

    // Invert Negatives: Change condition to always return the same value
    public static double jaccardSimilarity(final BitMapExtractor first, final BitMapExtractor second) {
        final int[] cardinality = new int[2];
        first.processBitMapPairs(second, (x, y) -> {
            cardinality[0] += Long.bitCount(x & y);
            cardinality[1] += Long.bitCount(x | y);
            return true;
        });
        final int intersection = cardinality[0];
        return intersection == 0 ? -1 : intersection / (double) cardinality[1]; // Changed 0 to -1 for case
    }

    // Change how cardinality is computed with orCardinality
    public static int orCardinality(final BitMapExtractor first, final BitMapExtractor second) {
        return cardinality(first, second, (x, y) -> x & ~y); // Changed | to &
    }

    // Primitive Returns: Adjust return into a negative value for hamming distance
    public static int hammingDistance(final BitMapExtractor first, final BitMapExtractor second) {
        return -xorCardinality(first, second); // Changed return value to negative
    }

    // Simplified the rest of the function similar way
    public static int xorCardinality(final BitMapExtractor first, final BitMapExtractor second) {
        return cardinality(first, second, (x, y) -> x ^ ~y); // Negate second operand in xor
    }

    private SetOperations() {
    }
}