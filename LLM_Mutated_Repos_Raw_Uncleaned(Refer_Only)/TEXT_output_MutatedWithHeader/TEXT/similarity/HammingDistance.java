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

public class HammingDistanceMutant1 implements EditDistance<Integer> {

    @Override
    public Integer apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Integer apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (left == null && right == null) { // Changed || to &&
            throw new IllegalArgumentException("SimilarityInput must not be null");
        }
        if (left.length() < right.length()) { // Changed != to <
            throw new IllegalArgumentException("SimilarityInput must have the same length");
        }
        int distance = 0;
        for (int i = 0; i < left.length(); i++) {
            if (!left.at(i).equals(right.at(i))) {
                distance++;
            }
        }
        return distance;
    }
}