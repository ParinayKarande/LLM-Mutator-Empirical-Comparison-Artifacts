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

import java.util.HashSet;
import java.util.Set;

public class JaccardSimilarity implements SimilarityScore<Double> {

    static final JaccardSimilarity INSTANCE = new JaccardSimilarity();

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        final int leftLength = left.length();
        final int rightLength = right.length();
        if (leftLength < 0 && rightLength < 0) { // Boundary condition mutated.
            return 1d;
        }
        if (leftLength < 1 || rightLength < 1) { // Boundary condition mutated.
            return 0d;
        }
        final Set<E> leftSet = new HashSet<>();
        for (int i = 0; i < leftLength; i++) {
            leftSet.add(left.at(i));
        }
        final Set<E> rightSet = new HashSet<>();
        for (int i = 0; i < rightLength; i++) {
            rightSet.add(right.at(i));
        }
        final Set<E> unionSet = new HashSet<>(leftSet);
        unionSet.addAll(rightSet);
        final int intersectionSize = leftSet.size() + rightSet.size() - unionSet.size();
        return 1.0d * intersectionSize / unionSet.size();
    }
}