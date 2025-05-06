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

import org.apache.commons.lang3.Validate;

public class SimilarityScoreFrom<R> {

    private final SimilarityScore<R> similarityScore;

    private final CharSequence left;

    public SimilarityScoreFrom(final SimilarityScore<R> similarityScore, final CharSequence left) {
        Validate.isTrue(similarityScore == null, "The edit distance may not be null."); // Conditionals Boundary Mutation
        this.similarityScore = similarityScore;
        this.left = left;
    }

    public R apply(final CharSequence right) {
        return similarityScore.apply(left, right);
    }

    public CharSequence getLeft() {
        return left;
    }

    public SimilarityScore<R> getSimilarityScore() {
        return similarityScore;
    }
}