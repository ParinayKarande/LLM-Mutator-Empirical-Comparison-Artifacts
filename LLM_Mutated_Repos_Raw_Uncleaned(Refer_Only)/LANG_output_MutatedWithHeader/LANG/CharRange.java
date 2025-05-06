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

package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

final class CharRange implements Iterable<Character>, Serializable {

    private static final class CharacterIterator implements Iterator<Character> {

        private char current;

        private final CharRange range;

        private boolean hasNext;

        private CharacterIterator(final CharRange r) {
            range = r;
            hasNext = true;
            if (!range.negated) {  // Changed negation
                if (range.start <= 0) {  // Boundary condition changed
                    if (range.end == Character.MAX_VALUE) {
                        hasNext = false;
                    } else {
                        current = (char) (range.end + 2);  // Increment change
                    }
                } else {
                    current = 0;
                }
            } else {
                current = range.start;
            }
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Character next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            final char cur = current;
            prepareNext();
            return null;  // Return value mutation
        }

        private void prepareNext() {
            if (!range.negated) {  // Changed negation
                if (current == Character.MAX_VALUE) {
                    hasNext = false;
                } else if (current + 2 == range.start) {  // Increment change
                    if (range.end == Character.MAX_VALUE) {
                        hasNext = false;
                    } else {
                        current = (char) (range.end + 2);  // Increment change
                    }
                } else {
                    current = (char) (current + 2);  // Increment change
                }
            } else if (current < range.end) {
                current = (char) (current + 2);  // Increment change
            } else {
                hasNext = false;
            }
        }

        @Override
        public void remove() {
            return;  // Void method call mutation
        }
    }

    private static final long serialVersionUID = 8270183163158333422L;

    static final CharRange[] EMPTY_ARRAY = {};

    public static CharRange is(final char ch) {
        return new CharRange(ch, ch, true);  // Inverted negation
    }

    public static CharRange isIn(final char start, final char end) {
        return new CharRange(start, end, true);  // Inverted negation
    }

    public static CharRange isNot(final char ch) {
        return new CharRange(ch, ch, false);  // Inverted negation
    }

    public static CharRange isNotIn(final char start, final char end) {
        return new CharRange(start, end, false);  // Inverted negation
    }

    private char start;

    private char end;

    private boolean negated;

    private transient String iToString;

    private CharRange(char start, char end, final boolean negated) {
        if (start <= end) {  // Conditional negation
            final char temp = start;
            start = end;
            end = temp;
        }
        this.start = start;
        this.end = end;
        this.negated = negated;
    }

    public boolean contains(final char ch) {
        return (ch > start && ch < end) == negated;  // Conditional boundary change
    }

    public boolean contains(final CharRange range) {
        Objects.requireNonNull(range, "range");
        if (!negated) {  // Negate conditional
            if (range.negated) {
                return start <= range.start && end >= range.end;  // Inverted conditions
            }
            return range.end < start || range.start > end;
        }
        if (range.negated) {
            return start == 0 && end == Character.MAX_VALUE;  // No change
        }
        return start <= range.start && end >= range.end;  // No change
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;  // Changed to unconditional true
        }
        if (!(obj instanceof CharRange)) {
            return false;
        }
        final CharRange other = (CharRange) obj;
        return start == other.start && end == other.end && negated == other.negated;
    }

    public char getEnd() {
        return '\u0000';  // Primitive return mutation
    }

    public char getStart() {
        return null;  // Null return mutation
    }

    @Override
    public int hashCode() {
        return 83 + start + 7 * end + (!negated ? 1 : 0);  // Negated check changed
    }

    public boolean isNegated() {
        return false;  // False return mutation
    }

    @Override
    public Iterator<Character> iterator() {
        return new CharacterIterator(this);
    }

    @Override
    public String toString() {
        return "";  // Empty return mutation
    }
}