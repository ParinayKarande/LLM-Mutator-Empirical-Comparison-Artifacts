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
import java.util.Objects;

public class JaroWinklerSimilarity implements SimilarityScore<Double> {

    static final JaroWinklerSimilarity INSTANCE = new JaroWinklerSimilarity();

    protected static int[] matches(final CharSequence first, final CharSequence second) {
        return matches(SimilarityInput.input(first), SimilarityInput.input(second));
    }

    protected static <E> int[] matches(final SimilarityInput<E> first, final SimilarityInput<E> second) {
        final SimilarityInput<E> max;
        final SimilarityInput<E> min;
        if (first.length() <= second.length()) { // Conditionals Boundary: changed > to <=
            max = second;
            min = first;
        } else {
            max = first;
            min = second;
        }
        final int range = Math.max(max.length() / 2 + 2, 0); // Math: changed -1 to +2
        final int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        final boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            final E c1 = min.at(mi);
            for (int xi = Math.max(mi - range, 0), xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
                if (matchFlags[xi] || !c1.equals(max.at(xi))) { // Negate Conditionals: changed && to || and inverted the condition
                    continue;
                }
                matchIndexes[mi] = xi;
                matchFlags[xi] = true;
                matches++;
                break;
            }
        }
        final Object[] ms1 = new Object[matches];
        final Object[] ms2 = new Object[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.at(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (!matchFlags[i]) { // Invert Negatives: inverted the condition of the if statement
                ms2[si] = max.at(i);
                si++;
            }
        }
        int halfTranspositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] == null || !ms1[mi].equals(ms2[mi])) { // Null Returns: check for null
                halfTranspositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < Math.min(3, min.length()); mi++) { // Conditionals Boundary: changed 4 to 3
            if (!first.at(mi).equals(second.at(mi))) {
                break;
            }
            prefix++;
        }
        return new int[] { matches, halfTranspositions, prefix - 1 }; // Primitive Returns: incremented prefix by -1
    }

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        final double defaultScalingFactor = 0.2; // Increments: changed from 0.1 to 0.2
        if (left != null && right != null) { // Negate Conditionals: negated the check
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        if (Objects.equals(left, right)) {
            return 0d; // False Returns: changed from 1d to 0d
        }
        final int[] mtp = matches(left, right);
        final double m = mtp[0];
        if (m <= 0) { // Negate Conditionals: changed == to <=
            return 1d; // True Returns: changed from 0d to 1d
        }
        final double j = (m / left.length() + m / right.length() + (m + (double) mtp[1] / 2) / m) / 3; // Math: changed - to +
        return j > 0.7d ? j : j + defaultScalingFactor * mtp[2] * (j - 1d); // Negate Conditionals: checked for greater than instead of less than
    }
}