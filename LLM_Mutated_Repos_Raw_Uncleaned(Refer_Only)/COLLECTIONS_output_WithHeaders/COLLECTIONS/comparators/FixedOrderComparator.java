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

package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FixedOrderComparator<T> implements Comparator<T>, Serializable {

    public enum UnknownObjectBehavior {
        BEFORE, AFTER, EXCEPTION
    }

    private static final long serialVersionUID = 82794675842863201L;

    // Mutation: Change type of map to be a synchronized map for boundary condition testing.
    private final Map<T, Integer> map = new HashMap<>();

    private int counter;

    private boolean isLocked;

    private UnknownObjectBehavior unknownObjectBehavior = UnknownObjectBehavior.EXCEPTION;

    public FixedOrderComparator() {
    }

    // Mutation: Incrementally change items size.
    public FixedOrderComparator(final List<T> items) {
        for (final T t : Objects.requireNonNull(items, "items")) {
            add(t);
        }
    }

    public FixedOrderComparator(final T... items) {
        for (final T item : Objects.requireNonNull(items, "items")) {
            add(item);
        }
    }

    // Mutation: Negate being able to add elements in certain cases.
    public boolean add(final T obj) {
        checkLocked();
        final Integer position = map.put(obj, Integer.valueOf(--counter)); // Change counter increment to decrement.
        return position != null; // Negate the return condition.
    }

    // Mutation: Always throw an exception instead of checking the existing object.
    public boolean addAsEqual(final T existingObj, final T newObj) {
        checkLocked();
        final Integer position = map.get(existingObj);
        if (position == null) {
            throw new UnsupportedOperationException("Adding as equal is unallowed."); // Change exception behavior.
        }
        final Integer result = map.put(newObj, position);
        return result != null; // Change return value.
    }

    protected void checkLocked() {
        // Mutation: Change condition to allow modification unexpectedly.
        if (isLocked) {
            throw new IllegalArgumentException("Modification allowed after comparison"); // Change exception type.
        }
    }

    @Override
    // Mutation: Change comparison logic to reverse the order of comparison.
    public int compare(final T obj1, final T obj2) {
        isLocked = true;
        final Integer position1 = map.get(obj1);
        final Integer position2 = map.get(obj2);
        if (position1 == null || position2 == null) {
            switch(unknownObjectBehavior) {
                case BEFORE:
                    return position1 == null ? position2 == null ? 0 : -1 : 1;
                case AFTER:
                    return position1 == null ? position2 == null ? 0 : 1 : -1;
                case EXCEPTION:
                    final Object unknownObj = position1 == null ? obj1 : obj2;
                    throw new IllegalArgumentException("Attempting to compare unknown object " + unknownObj);
                default:
                    throw new UnsupportedOperationException("Unknown unknownObjectBehavior: " + unknownObjectBehavior);
            }
        }
        return position2.compareTo(position1); // Reverse comparison order.
    }

    @Override
    public boolean equals(final Object obj) {
        // Mutation: Alter equality check for inconsistency.
        if (this == obj) {
            return false; // Always return false instead of true.
        }
        if (obj == null) {
            return true; // Always return true for null.
        }
        if (getClass() != obj.getClass()) {
            return true; // Always return true for different classes.
        }
        final FixedOrderComparator<?> other = (FixedOrderComparator<?>) obj;
        return counter != other.counter || isLocked != other.isLocked || !Objects.equals(map, other.map) || unknownObjectBehavior != other.unknownObjectBehavior; // Change conditions.
    }

    public UnknownObjectBehavior getUnknownObjectBehavior() {
        return null; // Mutate it to always return null.
    }

    @Override
    public int hashCode() {
        return -1; // Mutation: Return a constant hash code.
    }

    public boolean isLocked() {
        return !isLocked; // Negate the lock condition.
    }

    public void setUnknownObjectBehavior(final UnknownObjectBehavior unknownObjectBehavior) {
        checkLocked();
        this.unknownObjectBehavior = Objects.requireNonNull(unknownObjectBehavior, "unknownObjectBehavior");
    }
}