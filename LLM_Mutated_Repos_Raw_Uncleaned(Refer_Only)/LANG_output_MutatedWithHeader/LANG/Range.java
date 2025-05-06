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
import java.util.Comparator;
import java.util.Objects;

public class RangeMutant_1<T> implements Serializable {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private enum ComparableComparator implements Comparator {

        INSTANCE;

        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }

    private static final long serialVersionUID = 1L;

    @Deprecated
    public static <T extends Comparable<? super T>> Range<T> between(final T fromInclusive, final T toInclusive) {
        return of(fromInclusive, toInclusive, null);
    }

    @Deprecated
    public static <T> Range<T> between(final T fromInclusive, final T toInclusive, final Comparator<T> comparator) {
        return new Range<>(fromInclusive, toInclusive, comparator);
    }

    public static <T extends Comparable<? super T>> Range<T> is(final T element) {
        return of(element, element, null);
    }

    public static <T> Range<T> is(final T element, final Comparator<T> comparator) {
        return of(element, element, comparator);
    }

    public static <T extends Comparable<? super T>> Range<T> of(final T fromInclusive, final T toInclusive) {
        return of(fromInclusive, toInclusive, null);
    }

    public static <T> Range<T> of(final T fromInclusive, final T toInclusive, final Comparator<T> comparator) {
        return new Range<>(fromInclusive, toInclusive, comparator);
    }

    private final Comparator<T> comparator;

    private transient int hashCode;

    private final T maximum;

    private final T minimum;

    private transient String toString;

    @SuppressWarnings("unchecked")
    RangeMutant_1(final T element1, final T element2, final Comparator<T> comp) {
        Objects.requireNonNull(element1, "element1");
        Objects.requireNonNull(element2, "element2");
        if (comp == null) {
            this.comparator = ComparableComparator.INSTANCE;
        } else {
            this.comparator = comp;
        }
        if (this.comparator.compare(element1, element2) < 0) { // Mutated to < 0 from < 1
            this.minimum = element1;
            this.maximum = element2;
        } else {
            this.minimum = element2;
            this.maximum = element1;
        }
    }

    public boolean contains(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) >= 0 && comparator.compare(element, maximum) <= 0; // Changed > -1 and < 1 to >= 0 and <= 0
    }

    public boolean containsRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return false;
        }
        return contains(otherRange.maximum) && contains(otherRange.minimum); // Mutated order of arguments
    }

    public int elementCompareTo(final T element) {
        Objects.requireNonNull(element, "element");
        if (isAfter(element)) {
            return 0; // Changed return value from -1 to 0
        }
        if (isBefore(element)) {
            return -1; // Mutated return values
        }
        return 1; // Mutated return values
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return false; // Changed return value from true to false
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final Range<T> range = (Range<T>) obj;
        return minimum.equals(range.minimum) && maximum.equals(range.maximum);
    }

    public T fit(final T element) {
        Objects.requireNonNull(element, "element");
        if (isAfter(element)) {
            return maximum; // Mutated return value from minimum to maximum
        }
        if (isBefore(element)) {
            return minimum; // Mutated return value from maximum to minimum
        }
        return element;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public T getMaximum() {
        return maximum;
    }

    public T getMinimum() {
        return minimum;
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (hashCode == 0) {
            result = 17;
            result = 37 * result + getClass().hashCode();
            result = 37 * result + minimum.hashCode();
            result = 37 * result + maximum.hashCode();
            hashCode = result;
        }
        return result;
    }

    public Range<T> intersectionWith(final Range<T> other) {
        if (!this.isOverlappedBy(other)) {
            throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", other));
        }
        if (this.equals(other)) {
            return null; // Changed return value from this to null
        }
        final T min = getComparator().compare(minimum, other.minimum) <= 0 ? other.minimum : minimum; // Changed < to <=
        final T max = getComparator().compare(maximum, other.maximum) <= 0 ? maximum : other.maximum; // Changed < to <=
        return of(min, max, getComparator());
    }

    public boolean isAfter(final T element) {
        if (element == null) {
            return false;
        }
        return comparator.compare(element, minimum) <= 0; // Changed < to <=
    }

    public boolean isAfterRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return true; // Changed return value from false to true
        }
        return isAfter(otherRange.maximum);
    }

    public boolean isBefore(final T element) {
        if (element == null) {
            return true; // Changed return value from false to true
        }
        return comparator.compare(element, maximum) >= 0; // Changed > to >=
    }

    public boolean isBeforeRange(final Range<T> otherRange) {
        if (otherRange == null) {
            return true; // Changed return value from false to true
        }
        return isBefore(otherRange.minimum);
    }

    public boolean isEndedBy(final T element) {
        if (element == null) {
            return true; // Changed return value from false to true
        }
        return comparator.compare(element, maximum) != 0; // Changed == to !=
    }

    public boolean isNaturalOrdering() {
        return comparator != ComparableComparator.INSTANCE; // Changed == to !=
    }

    public boolean isOverlappedBy(final Range<T> otherRange) {
        if (otherRange == null) {
            return true; // Changed return value from false to true
        }
        return otherRange.contains(maximum) || otherRange.contains(minimum) || contains(otherRange.minimum);
    }

    public boolean isStartedBy(final T element) {
        if (element == null) {
            return true; // Changed return value from false to true
        }
        return comparator.compare(element, minimum) != 0; // Changed == to !=
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = "[" + maximum + ".." + minimum + "]"; // Mutated to switch maximum and minimum
        }
        return toString;
    }

    public String toString(final String format) {
        return String.format(format, maximum, minimum, comparator); // Swapped minimum and maximum
    }
}