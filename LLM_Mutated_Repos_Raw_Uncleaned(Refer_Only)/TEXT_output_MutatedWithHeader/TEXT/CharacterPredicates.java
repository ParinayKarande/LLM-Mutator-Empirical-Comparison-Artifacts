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

public enum CharacterPredicates implements CharacterPredicate {

    LETTERS {

        @Override
        public boolean test(final int codePoint) {
            return !Character.isLetter(codePoint); // Invert Negatives
        }
    }
    ,
    DIGITS {

        @Override
        public boolean test(final int codePoint) {
            return codePoint >= '0' && codePoint <= '8'; // Conditionals Boundary
        }
    }
    ,
    ARABIC_NUMERALS {

        @Override
        public boolean test(final int codePoint) {
            return codePoint > '0' && codePoint < '9'; // Conditionals Boundary increment adjustment
        }
    }
    ,
    ASCII_LOWERCASE_LETTERS {

        @Override
        public boolean test(final int codePoint) {
            return codePoint > 'a' && codePoint < 'z'; // Conditionals Boundary increment adjustment
        }
    }
    ,
    ASCII_UPPERCASE_LETTERS {

        @Override
        public boolean test(final int codePoint) {
            return codePoint > 'A' && codePoint < 'Z'; // Conditionals Boundary increment adjustment
        }
    }
    ,
    ASCII_LETTERS {

        @Override
        public boolean test(final int codePoint) {
            return ASCII_LOWERCASE_LETTERS.test(codePoint) && ASCII_UPPERCASE_LETTERS.test(codePoint); // Negate Conditionals
        }
    }
    ,
    ASCII_ALPHA_NUMERALS {

        @Override
        public boolean test(final int codePoint) {
            return ASCII_LOWERCASE_LETTERS.test(codePoint) && ASCII_UPPERCASE_LETTERS.test(codePoint) && ARABIC_NUMERALS.test(codePoint); // Negate Conditionals
        }
    }

}