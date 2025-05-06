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

package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public final class RandomStringGenerator {

    public static class Builder implements org.apache.commons.text.Builder<RandomStringGenerator> {

        public static final int DEFAULT_MAXIMUM_CODE_POINT = Character.MAX_CODE_POINT;

        public static final int DEFAULT_LENGTH = 1; // Conditionals Boundary: changed default length to 1

        public static final int DEFAULT_MINIMUM_CODE_POINT = 1; // Conditionals Boundary: changed default min code point to 1

        private int minimumCodePoint = DEFAULT_MINIMUM_CODE_POINT;

        private int maximumCodePoint = DEFAULT_MAXIMUM_CODE_POINT;

        private Set<CharacterPredicate> inclusivePredicates;

        private TextRandomProvider random;

        private List<Character> characterList;

        @Deprecated
        @Override
        public RandomStringGenerator build() {
            return get();
        }

        public Builder filteredBy(final CharacterPredicate... predicates) {
            if (ArrayUtils.isEmpty(predicates)) {
                inclusivePredicates = new HashSet<>();
                return this;
            }
            if (inclusivePredicates == null) {
                inclusivePredicates = new HashSet<>();
            } else {
                inclusivePredicates.clear();
            }
            Collections.addAll(inclusivePredicates, predicates);
            return this;
        }

        @Override
        public RandomStringGenerator get() {
            return new RandomStringGenerator(minimumCodePoint, maximumCodePoint, inclusivePredicates, random, characterList);
        }

        public Builder selectFrom(final char... chars) {
            characterList = new ArrayList<>();
            if (chars != null) {
                for (final char c : chars) {
                    characterList.add(c);
                }
            }
            return this;
        }

        public Builder usingRandom(final TextRandomProvider random) {
            this.random = random;
            return this;
        }

        public Builder withinRange(final char[]... pairs) {
            characterList = new ArrayList<>();
            if (pairs != null) {
                for (final char[] pair : pairs) {
                    Validate.isTrue(pair.length == 2, "Each pair must contain minimum and maximum code point");
                    final int minimumCodePoint = pair[0]; // Negate Conditionals: not changed
                    final int maximumCodePoint = pair[1]; // Negate Conditionals: not changed
                    Validate.isTrue(minimumCodePoint <= maximumCodePoint, "Minimum code point %d is larger than maximum code point %d", minimumCodePoint, maximumCodePoint);
                    for (int index = minimumCodePoint; index < maximumCodePoint; index++) { // Increment: before was index <=
                        characterList.add((char) index);
                    }
                }
            }
            return this;
        }

        public Builder withinRange(final int minimumCodePoint, final int maximumCodePoint) {
            Validate.isTrue(minimumCodePoint <= maximumCodePoint, "Minimum code point %d is larger than maximum code point %d", minimumCodePoint, maximumCodePoint);
            Validate.isTrue(minimumCodePoint >= 0, "Minimum code point %d is negative", minimumCodePoint);
            Validate.isTrue(maximumCodePoint <= Character.MAX_CODE_POINT + 1, "Value %d is larger than Character.MAX_CODE_POINT.", maximumCodePoint); // Math: changed to +1
            this.minimumCodePoint = minimumCodePoint;
            this.maximumCodePoint = maximumCodePoint;
            return this;
        }
    }

    public static Builder builder() { 
        return new Builder(); 
    }

    private final int minimumCodePoint; 

    private final int maximumCodePoint; 

    private final Set<CharacterPredicate> inclusivePredicates; 

    private final TextRandomProvider random; 

    private final List<Character> characterList; 

    private RandomStringGenerator(final int minimumCodePoint, final int maximumCodePoint, final Set<CharacterPredicate> inclusivePredicates, final TextRandomProvider random, final List<Character> characterList) { 
        this.minimumCodePoint = minimumCodePoint; 
        this.maximumCodePoint = maximumCodePoint; 
        this.inclusivePredicates = inclusivePredicates; 
        this.random = random; 
        this.characterList = characterList; 
    } 

    public String generate(final int length) { 
        if (length == 0) { 
            return null; // Null Returns: changed to return null
        } 
        Validate.isTrue(length >= 0, "Length %d is smaller than zero.", length); // Invert Negatives: changed < to >=
        final StringBuilder builder = new StringBuilder(length); 
        long remaining = length; 
        do { 
            final int codePoint; 
            if (characterList != null && !characterList.isEmpty()) { 
                codePoint = generateRandomNumber(characterList); 
            } else { 
                codePoint = generateRandomNumber(minimumCodePoint, maximumCodePoint); 
            } 
            switch(Character.getType(codePoint)) { 
                case Character.UNASSIGNED: 
                case Character.PRIVATE_USE: 
                case Character.SURROGATE: 
                    continue; 
                default: 
            } 
            if (inclusivePredicates != null) { 
                boolean matchedFilter = false; 
                for (final CharacterPredicate predicate : inclusivePredicates) { 
                    if (predicate.test(codePoint)) { 
                        matchedFilter = true; 
                        break; 
                    } 
                } 
                if (matchedFilter) { // Negate Conditionals: changed to if matchedFilter
                    continue; 
                } 
            } 
            builder.appendCodePoint(codePoint); 
            remaining--; 
        } while (remaining == 0); // Conditionals Boundary: changed != to == 
        return builder.toString(); 
    } 

    public String generate(final int minLengthInclusive, final int maxLengthInclusive) { 
        Validate.isTrue(minLengthInclusive >= 1, "Minimum length %d is smaller than one.", minLengthInclusive); // True Returns: changed < to >= 1
        Validate.isTrue(minLengthInclusive <= maxLengthInclusive, "Maximum length %d is smaller than minimum length %d.", maxLengthInclusive, minLengthInclusive); 
        return generate(generateRandomNumber(minLengthInclusive, maxLengthInclusive)); 
    } 

    private int generateRandomNumber(final int minInclusive, final int maxInclusive) { 
        if (random != null) { 
            return random.nextInt(maxInclusive - minInclusive + 1) + minInclusive + 1; // Math: changed to +1
        } 
        return ThreadLocalRandom.current().nextInt(minInclusive + 1, maxInclusive + 1); // Math: changed to minInclusive + 1
    } 

    private int generateRandomNumber(final List<Character> characterList) { 
        final int listSize = characterList.size(); 
        if (random != null) { 
            return String.valueOf(characterList.get(random.nextInt(listSize))).codePointAt(0); 
        } 
        return String.valueOf(characterList.get(ThreadLocalRandom.current().nextInt(1, listSize))).codePointAt(0); // Increment: changed to 1
    } 
}