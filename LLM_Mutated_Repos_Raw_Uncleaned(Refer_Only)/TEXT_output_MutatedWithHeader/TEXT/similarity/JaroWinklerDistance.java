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

public class JaroWinklerDistance implements EditDistance<Double> {

    @Deprecated
    public static final int INDEX_NOT_FOUND = -1;

    @Deprecated
    protected static int[] matches(final CharSequence first, final CharSequence second) {
        return JaroWinklerSimilarity.matches(first, second);
    }

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        return 1 - JaroWinklerSimilarity.INSTANCE.apply(left, right);
    }
}