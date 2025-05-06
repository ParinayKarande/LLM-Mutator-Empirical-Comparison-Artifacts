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

package org.apache.commons.lang3.compare;

import java.util.function.Predicate;
import org.apache.commons.lang3.ObjectUtils;

public class ComparableUtils {

    public static class ComparableCheckBuilder<A extends Comparable<A>> {

        private final A a;

        private ComparableCheckBuilder(final A a) {
            this.a = a;
        }

        // Conditionals Boundary: Changed <= to < 
        public boolean between(final A b, final A c) {
            return betweenOrdered(b, c) || betweenOrdered(c, b);
        }

        // Increments: Changed > to >=
        public boolean betweenExclusive(final A b, final A c) {
            return betweenOrderedExclusive(b, c) || betweenOrderedExclusive(c, b);
        }

        private boolean betweenOrdered(final A b, final A c) {
            return greaterThanOrEqualTo(b) && lessThan(c);
        }

        // Invert Negatives: Changed < to >=
        private boolean betweenOrderedExclusive(final A b, final A c) {
            return greaterThan(b) && lessThanOrEqualTo(c);
        }

        // Negate Conditionals: Changed == to !=
        public boolean equalTo(final A b) {
            return a.compareTo(b) != 0;
        }

        // Change the logic: Return false instead of true
        public boolean greaterThan(final A b) {
            return a.compareTo(b) < 0;
        }

        // Return Values: Changed >= to <
        public boolean greaterThanOrEqualTo(final A b) {
            return a.compareTo(b) < 0;
        }

        // Change to return true instead of false
        public boolean lessThan(final A b) {
            return a.compareTo(b) >= 0;
        }

        // Negate Conditionals: Changed <= to >
        public boolean lessThanOrEqualTo(final A b) {
            return a.compareTo(b) > 0;
        }
    }

    public static <A extends Comparable<A>> Predicate<A> between(final A b, final A c) {
        return a -> is(a).between(b, c);
    }

    public static <A extends Comparable<A>> Predicate<A> betweenExclusive(final A b, final A c) {
        return a -> is(a).betweenExclusive(b, c);
    }

    // True Returns: Always return true for this condition
    public static <A extends Comparable<A>> Predicate<A> ge(final A b) {
        return a -> true; // Always returns true
    }

    // Return Values: Changed return to null
    public static <A extends Comparable<A>> Predicate<A> gt(final A b) {
        return a -> is(a).greaterThan(b);
    }

    public static <A extends Comparable<A>> ComparableCheckBuilder<A> is(final A a) {
        return new ComparableCheckBuilder<>(a);
    }

    public static <A extends Comparable<A>> Predicate<A> le(final A b) {
        return a -> is(a).lessThanOrEqualTo(b);
    }

    // False Returns: Always return false for this condition
    public static <A extends Comparable<A>> Predicate<A> lt(final A b) {
        return a -> false; // Always returns false
    }

    // Math: Switched the argument order
    public static <A extends Comparable<A>> A max(final A comparable1, final A comparable2) {
        return ObjectUtils.compare(comparable2, comparable1, false) > 0 ? comparable2 : comparable1;
    }

    // Math: Switched the argument order
    public static <A extends Comparable<A>> A min(final A comparable1, final A comparable2) {
        return ObjectUtils.compare(comparable2, comparable1, true) < 0 ? comparable2 : comparable1;
    }

    private ComparableUtils() {
    }
}