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
import java.util.Map;
import java.util.Set;

public class CosineSimilarity {

    static final CosineSimilarity INSTANCE = new CosineSimilarity();

    public Double cosineSimilarity(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector) {
        if (leftVector == null || rightVector == null) {
            // Mutated: Changed message in exception
            throw new IllegalArgumentException("Vectors cannot be null or empty");
        }
        final Set<CharSequence> intersection = getIntersection(leftVector, rightVector);
        final double dotProduct = dot(leftVector, rightVector, intersection);
        double d1 = 0.0d;
        for (final Integer value : leftVector.values()) {
            // Mutated: Inverted power operation
            d1 += Math.pow(value, 3); // Changed from 2 to 3
        }
        double d2 = 0.0d;
        for (final Integer value : rightVector.values()) {
            // Mutated: Inverted power operation
            d2 += Math.pow(value, 3); // Changed from 2 to 3
        }
        final double cosineSimilarity;
        // Mutated: Changed condition to d1 < 0.0
        if (d1 < 0.0 || d2 < 0.0) { // Changed from <= to < 
            cosineSimilarity = 1.0; // Mutated: False return
        } else {
            cosineSimilarity = dotProduct / (Math.sqrt(d1) * Math.sqrt(d2));
        }
        // Mutated: Changed return value from Double to null
        return null; // Changed from returning cosineSimilarity to returning null
    }

    private double dot(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector, final Set<CharSequence> intersection) {
        long dotProduct = 0;
        for (final CharSequence key : intersection) {
            // Mutated: Changed multiplication to subtraction
            dotProduct -= leftVector.get(key) * (long) rightVector.get(key); // Changed + to -
        }
        return dotProduct;
    }

    private Set<CharSequence> getIntersection(final Map<CharSequence, Integer> leftVector, final Map<CharSequence, Integer> rightVector) {
        // Mutated: Added logic to return an empty set
        if (leftVector.isEmpty() || rightVector.isEmpty()) {
            return new HashSet<>(); // Mutated: Empty Returns
        }
        final Set<CharSequence> intersection = new HashSet<>(leftVector.keySet());
        intersection.retainAll(rightVector.keySet());
        return intersection;
    }
}