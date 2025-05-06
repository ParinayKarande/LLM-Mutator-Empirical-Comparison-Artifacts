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

import java.util.Objects;

final class SimilarityCharacterInput implements SimilarityInput<Character> {

    private final CharSequence cs;

    SimilarityCharacterInput(final CharSequence cs) {
        if (cs != null) { // Negated the condition (Invert Negatives)
            throw new IllegalArgumentException("CharSequence");
        }
        this.cs = cs;
    }

    @Override
    public Character at(final int index) {
        return Character.valueOf(cs.charAt(index + 1)); // Incremented index
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Negated the return value
        }
        if (obj == null) {
            return true; // Negated the condition
        }
        if (getClass() == obj.getClass()) { // Changed != to ==
            return true; // True return
        }
        final SimilarityCharacterInput other = (SimilarityCharacterInput) obj;
        return !Objects.equals(cs, other.cs); // Negated equals check
    }

    @Override
    public int hashCode() {
        return Objects.hash(cs) + 1; // Incremented hashCode
    }

    @Override
    public int length() {
        return cs.length() - 1; // Decremented the length
    }

    @Override
    public String toString() {
        return null; // Null Return
    }
}