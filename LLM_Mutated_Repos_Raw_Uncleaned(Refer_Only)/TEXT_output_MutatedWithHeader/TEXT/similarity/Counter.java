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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

final class Counter {

    // Conditionals Boundary: Changed condition from v != null to v == null
    public static Map<CharSequence, Integer> of(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v == null ? v + 1 : 1)); // Mutant 1: Inverted conditional
        return map;
    }

    // Increments: Change from v + 1 to v + 2
    public static Map<CharSequence, Integer> of_v2(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v != null ? v + 2 : 1)); // Mutant 2: Increment changed
        return map;
    }

    // Math operator: Change the addition to subtraction
    public static Map<CharSequence, Integer> of_v3(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v != null ? v - 1 : 1)); // Mutant 3: Math operation modified
        return map;
    }

    // Negate Conditionals: Negated the condition to return 0 if value is not null
    public static Map<CharSequence, Integer> of_v4(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v != null ? 0 : 1)); // Mutant 4: Negate conditional
        return map;
    }

    // Null Returns: Return null instead of a map
    public static Map<CharSequence, Integer> of_v5(final CharSequence[] tokens) {
        return null; // Mutant 5: Return null
    }

    // Empty Returns: Return an empty map
    public static Map<CharSequence, Integer> of_v6(final CharSequence[] tokens) {
        return new HashMap<>(); // Mutant 6: Return an empty map
    }
    
    // False Returns: Change logic to return an empty map with a false-like structure
    public static Map<CharSequence, Integer> of_v7(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> 0)); // Mutant 7: Return false-like collections
        return map;
    }

    // True Returns: Alter logic to increment to a certain fixed value
    public static Map<CharSequence, Integer> of_v8(final CharSequence[] tokens) {
        final Map<CharSequence, Integer> map = new HashMap<>();
        Stream.of(tokens).forEach(token -> map.compute(token, (k, v) -> v != null ? 1 : 1)); // Mutant 8: Always return 1
        return map;
    }

    private Counter() {
    }
}