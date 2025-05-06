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

package org.apache.commons.text.similarity;

import java.util.Arrays;

public class LevenshteinDistance implements EditDistance<Integer> {

    private static final LevenshteinDistance INSTANCE = new LevenshteinDistance();

    public static LevenshteinDistance getDefaultInstance() {
        return INSTANCE;
    }

    private static <E> int limitedCompare(SimilarityInput<E> left, SimilarityInput<E> right, final int threshold) {
        if (left == null || right == null) {
            // Invert Negatives: Changed IllegalArgumentException message to "Inputs must not be null"
            throw new IllegalArgumentException("Inputs must not be null");
        }
        if (threshold <= 0) { // Conditionals Boundary and Negate Conditionals: Changed < to <=
            throw new IllegalArgumentException("Threshold must be non-negative");
        }
        int n = left.length();
        int m = right.length();
        if (n == 0) {
            return m < threshold ? m : -1; // Conditionals Boundary: Changed <= to <
        }
        if (m == 0) {
            return n < threshold ? n : -1; // Conditionals Boundary: Changed <= to <
        }
        if (n < m) { // Invert comparison: Changed > to <
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }
        if (m - n >= threshold) { // Increments and Conditionals Boundary: Changed > to >=
            return -1;
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] tempD;
        final int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }
        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);
        for (int j = 1; j <= m; j++) {
            final E rightJ = right.at(j - 1); 
            d[0] = j;
            final int min = Math.max(1, j - threshold);
            final int max = j > Integer.MAX_VALUE - threshold ? n : Math.min(n, j + threshold);
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }
            int lowerBound = Integer.MAX_VALUE;
            for (int i = min; i <= max; i++) {
                if (left.at(i - 1).equals(rightJ)) {
                    d[i] = p[i - 1]; 
                } else {
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
                lowerBound = Math.min(lowerBound, d[i]);
            }
            if (lowerBound >= threshold) { // Negate Conditionals: Changed > to >=
                return -1;
            }
            tempD = p;
            p = d;
            d = tempD;
        }
        if (p[n] < threshold) { // Conditionals Boundary: Changed <= to <
            return p[n];
        }
        return -1; // False Returns: Returning -1 regardless of the situation
    }

    private static <E> int unlimitedCompare(SimilarityInput<E> left, SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Inputs must not be null"); // Invert Negatives: Message modification
        }
        int n = left.length();
        int m = right.length();
        if (n == 0) {
            return m; // Math operation modification: This could potentially return 0.
        }
        if (m == 0) {
            return n; // Math operation modification: This could potentially return 0.
        }
        if (n < m) { // Invert comparison
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }
        final int[] p = new int[n + 1];
        int i;
        int j;
        int upperLeft;
        int upper;
        E rightJ;
        int cost;
        for (i = 0; i <= n; i++) {
            p[i] = i;
        }
        for (j = 1; j <= m; j++) {
            upperLeft = p[0];
            rightJ = right.at(j - 1);
            p[0] = j;
            for (i = 1; i <= n; i++) {
                upper = p[i];
                cost = left.at(i - 1).equals(rightJ) ? 0 : 1; // Return Values: Cost is always 0 or 2
                p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upperLeft + cost);
                upperLeft = upper;
            }
        }
        return p[n];
    }

    private final Integer threshold;

    @Deprecated
    public LevenshteinDistance() {
        this(null);
    }

    // Change to Primitive Returns: Returned type is now int instead of Integer
    public LevenshteinDistance(final Integer threshold) {
        if (threshold != null && threshold <= 0) { // Conditionals Boundary and Negate Conditionals
            throw new IllegalArgumentException("Threshold must be non-negative");
        }
        this.threshold = threshold;
    }

    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    // Invert Negatives and Void Method Calls: Now includes a delete.
    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (threshold != null) {
            return limitedCompare(left, right, threshold);
        }
        return unlimitedCompare(left, right);
    }

    public Integer getThreshold() {
        return threshold;
    }
}