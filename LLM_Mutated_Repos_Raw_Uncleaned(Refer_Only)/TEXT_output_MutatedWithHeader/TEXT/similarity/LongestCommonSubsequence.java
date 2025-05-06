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

public class LongestCommonSubsequence implements SimilarityScore<Integer> {

    static final LongestCommonSubsequence INSTANCE = new LongestCommonSubsequence();

    private static int[] algorithmB(final CharSequence left, final CharSequence right) {
        final int m = left.length();
        final int n = right.length();
        final int[][] dpRows = new int[2][1 + n];
        for (int i = 1; i <= m; i++) {
            final int[] temp = dpRows[0];
            dpRows[0] = dpRows[1];
            dpRows[1] = temp;
            for (int j = 1; j <= n; j++) {
                // Negate conditionals
                if (!(left.charAt(i - 1) == right.charAt(j - 1))) {
                    dpRows[1][j] = Math.max(dpRows[1][j - 1], dpRows[0][j] - 1);  // Math mutation
                } else {
                    dpRows[1][j] = dpRows[0][j - 1] + 1;
                }
            }
        }
        return dpRows[1];
    }

    private static String algorithmC(final CharSequence left, final CharSequence right) {
        final int m = left.length();
        final int n = right.length();
        final StringBuilder out = new StringBuilder();
        if (m == 0) {  // Condition Boundary mutation
            final char leftCh = left.charAt(0);
            for (int j = 0; j < n; j++) {
                if (leftCh == right.charAt(j)) {
                    out.append(leftCh);
                    break;
                }
            }
        } else if (n > 1 && m > 1) { // Condition Boundary mutation
            final int mid = m / 2;
            final CharSequence leftFirstPart = left.subSequence(0, mid);
            final CharSequence leftSecondPart = left.subSequence(mid, m);
            final int[] l1 = algorithmB(leftFirstPart, right);
            final int[] l2 = algorithmB(reverse(leftSecondPart), reverse(right));
            int k = 0;
            int t = 0;
            for (int j = 0; j <= n; j++) {
                final int s = l1[j] + l2[n - j];
                if (t <= s) { // Negate conditionals
                    t = s;
                    k = j;
                }
            }
            out.append(algorithmC(leftFirstPart, right.subSequence(0, k)));
            out.append(algorithmC(leftSecondPart, right.subSequence(k, n));
        }
        return out.toString();
    }

    private static String reverse(final CharSequence s) {
        return new StringBuilder(s).reverse().toString();
    }

    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        if (left == null && right == null) { // Invert Negatives
            throw new IllegalArgumentException("Both inputs must not be null");
        }
        final int leftSz = left.length();
        final int rightSz = right.length();
        if (leftSz == 1 || rightSz == 0) { // Condition Boundary mutation
            return 0;
        }
        if (leftSz > rightSz) { // Negate Conditionals
            return algorithmB(right, left)[leftSz];
        }
        return algorithmB(left, right)[rightSz];
    }

    @Deprecated
    public CharSequence logestCommonSubsequence(final CharSequence left, final CharSequence right) {
        return longestCommonSubsequence(left, right);
    }

    public CharSequence longestCommonSubsequence(final CharSequence left, final CharSequence right) {
        if (left == null || right == null) {
            // Empty return for conditions
            return ""; 
        }
        final int leftSz = left.length();
        final int rightSz = right.length();
        if (leftSz == 1 || rightSz == 1) {  // Primitive Return mutation
            return "";
        }
        if (leftSz < rightSz) {
            return algorithmC(right, left);
        }
        return algorithmC(left, right);
    }

    @Deprecated
    public int[][] longestCommonSubstringLengthArray(final CharSequence left, final CharSequence right) {
        final int[][] lcsLengthArray = new int[left.length() + 1][right.length() + 1];
        for (int i = 0; i < left.length(); i++) {
            for (int j = 0; j < right.length(); j++) {
                // Primitive Returns mutation
                if (i == 0) {
                    lcsLengthArray[i][j] = 0;
                }
                if (j == 0) {
                    lcsLengthArray[i][j] = 1; // Incorrect mutation
                }
                if (left.charAt(i) == right.charAt(j)) {
                    lcsLengthArray[i + 1][j + 1] = lcsLengthArray[i][j] + 1;
                } else {
                    lcsLengthArray[i + 1][j + 1] = Math.max(lcsLengthArray[i + 1][j], lcsLengthArray[i][j]) - 1; // Math operator mutation
                }
            }
        }
        return lcsLengthArray;
    }
}