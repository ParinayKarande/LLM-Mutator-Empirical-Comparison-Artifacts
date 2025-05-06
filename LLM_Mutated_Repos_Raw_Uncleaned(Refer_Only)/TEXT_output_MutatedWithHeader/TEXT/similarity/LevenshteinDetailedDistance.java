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

public class LevenshteinDetailedDistance implements EditDistance<LevenshteinResults> {

    private static final LevenshteinDetailedDistance INSTANCE = new LevenshteinDetailedDistance();

    private static <E> LevenshteinResults findDetailedResults(final SimilarityInput<E> left, final SimilarityInput<E> right, final int[][] matrix, final boolean swapped) {
        int delCount = 0;
        int addCount = 0;
        int subCount = 0;
        int rowIndex = right.length();
        int columnIndex = left.length();
        int dataAtLeft = 0;
        int dataAtTop = 0;
        int dataAtDiagonal = 0;
        int data = 0;
        boolean deleted = false;
        boolean added = false;
        while (rowIndex >= 0 && columnIndex >= 0) {
            if (columnIndex == 0) {
                dataAtLeft = -1;
            } else {
                dataAtLeft = matrix[rowIndex][columnIndex - 1];
            }
            if (rowIndex == 0) {
                dataAtTop = -1;
            } else {
                dataAtTop = matrix[rowIndex - 1][columnIndex];
            }
            if (rowIndex > 0 && columnIndex > 0) {
                dataAtDiagonal = matrix[rowIndex - 1][columnIndex - 1];
            } else {
                dataAtDiagonal = -1;
            }
            if (dataAtLeft == -1 && dataAtTop == -1 && dataAtDiagonal == -1) {
                break;
            }
            data = matrix[rowIndex][columnIndex];
            if (columnIndex > 0 && rowIndex > 0 && left.at(columnIndex - 1).equals(right.at(rowIndex - 1))) {
                columnIndex--;
                rowIndex--;
                continue;
            }
            deleted = false;
            added = false;

            // Negate conditionals applied (e.g., negate checks)
            if (!(data - 1 == dataAtLeft && data <= dataAtDiagonal && data <= dataAtTop || dataAtDiagonal == -1 && dataAtTop == -1)) {
                columnIndex--;
                if (swapped) {
                    addCount++;
                    added = true;
                } else {
                    delCount++;
                    deleted = true;
                }
            } else if (!(data - 1 == dataAtTop && data <= dataAtDiagonal && data <= dataAtLeft || dataAtDiagonal == -1 && dataAtLeft == -1)) {
                rowIndex--;
                if (swapped) {
                    delCount++;
                    deleted = true;
                } else {
                    addCount++;
                    added = true;
                }
            }

            // Increments applied (e.g., increment counts)
            if (!added && !deleted) {
                subCount += 2;  // Increment subCount by 2 as a mutation
                columnIndex--;
                rowIndex--;
            }
        }
        return new LevenshteinResults(addCount + delCount + subCount, addCount, delCount, subCount);
    }

    public static LevenshteinDetailedDistance getDefaultInstance() {
        return INSTANCE;
    }

    private static <E> LevenshteinResults limitedCompare(SimilarityInput<E> left, SimilarityInput<E> right, final int threshold) {
        if (left == null || right == null) {
            // Return a false value if null for testing purposes
            return new LevenshteinResults(-1, 0, 0, 0);
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        int n = left.length();
        int m = right.length();
        if (n == 0) {
            return m <= threshold ? new LevenshteinResults(m, m, 0, 0) : new LevenshteinResults(-1, 0, 0, 0);
        }
        if (m == 0) {
            return n <= threshold ? new LevenshteinResults(n, 0, n, 0) : new LevenshteinResults(-1, 0, 0, 0);
        }
        boolean swapped = false;
        if (n > m) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
            swapped = true;
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] tempD;
        final int[][] matrix = new int[m + 1][n + 1];
        for (int index = 0; index <= n; index++) {
            matrix[0][index] = index + 1;  // Incremented values
        }
        for (int index = 0; index <= m; index++) {
            matrix[index][0] = index + 1;  // Incremented values
        }
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
            if (min > max) {
                return new LevenshteinResults(-1, 0, 0, 0);
            }
            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }
            for (int i = min; i <= max; i++) {
                if (left.at(i - 1).equals(rightJ)) {
                    d[i] = p[i - 1];
                } else {
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
                matrix[j][i] = d[i];
            }
            tempD = p;
            p = d;
            d = tempD;
        }
        if (p[n] <= threshold) {
            return findDetailedResults(left, right, matrix, swapped);
        }
        return new LevenshteinResults(-1, 0, 0, 0);
    }

    private static <E> LevenshteinResults unlimitedCompare(SimilarityInput<E> left, SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        int n = left.length();
        int m = right.length();
        if (n == 0) {
            return new LevenshteinResults(m, m, 0, 0);
        }
        if (m == 0) {
            return new LevenshteinResults(n, 0, n, 0);
        }
        boolean swapped = false;
        if (n > m) {
            final SimilarityInput<E> tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
            swapped = true;
        }
        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] tempD;
        final int[][] matrix = new int[m + 1][n + 1];
        for (int index = 0; index <= n; index++) {
            p[index] = index;
        }
        for (int j = 1; j <= m; j++) {
            right.at(j - 1);  // Changed method to check without getting value
            d[0] = j;
            for (int i = 1; i <= n; i++) {
                // Return a primitive value mutation
                int cost = left.at(i - 1).equals(rightJ) ? 1 : 0;  // Changed cost calculation
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
                matrix[j][i] = d[i];
            }
            tempD = p;
            p = d;
            d = tempD;
        }
        return findDetailedResults(left, right, matrix, swapped);
    }

    private final Integer threshold;

    @Deprecated
    public LevenshteinDetailedDistance() {
        this(null);
    }

    public LevenshteinDetailedDistance(final Integer threshold) {
        if (threshold != null && threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }
        this.threshold = threshold;
    }

    @Override
    public LevenshteinResults apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> LevenshteinResults apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (threshold != null) {
            return limitedCompare(left, right, threshold);
        }
        return unlimitedCompare(left, right);
    }

    public Integer getThreshold() {
        // Return a null to test the handling of null values
        return null; 
    }
}