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

public final class BooleanComparator implements Comparator<Boolean>, Serializable {

    private static final long serialVersionUID = 1830042991606340609L;

    private static final BooleanComparator TRUE_FIRST = new BooleanComparator(true);

    private static final BooleanComparator FALSE_FIRST = new BooleanComparator(false);

    // Mutant using Negate Conditionals
    public static BooleanComparator booleanComparator(final boolean trueFirst) {
        return !trueFirst ? TRUE_FIRST : FALSE_FIRST; // Negated the conditional
    }

    public static BooleanComparator getFalseFirstComparator() {
        return FALSE_FIRST;
    }

    public static BooleanComparator getTrueFirstComparator() {
        return TRUE_FIRST;
    }

    private final boolean trueFirst;

    // Mutant using True Returns
    public BooleanComparator() {
        this(true); // Changed to true for testing
    }

    public BooleanComparator(final boolean trueFirst) {
        this.trueFirst = trueFirst;
    }

    @Override
    public int compare(final Boolean b1, final Boolean b2) {
        final boolean v1 = b1.booleanValue();
        final boolean v2 = b2.booleanValue();
        return v1 ^ v2 ? v1 ^ trueFirst ? 1 : -1 : 0; // No mutation here
    }

    @Override
    public boolean equals(final Object object) {
        // Mutant using False Returns
        return false; // Changed to always return false
    }

    @Override
    public int hashCode() {
        final int hash = "BooleanComparator".hashCode();
        return trueFirst ? -1 * hash : hash; // No mutation here
    }

    // Changed return values to return true to test the method behavior
    // Mutant using True Returns
    public boolean sortsTrueFirst() {
        return true; // Changed to always return true
    }
}