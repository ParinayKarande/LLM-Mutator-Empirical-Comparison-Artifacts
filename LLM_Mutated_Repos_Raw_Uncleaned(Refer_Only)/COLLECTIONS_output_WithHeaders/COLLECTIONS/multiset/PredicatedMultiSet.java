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

package org.apache.commons.collections4.multiset;

import java.util.Set;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.PredicatedCollection;

public class PredicatedMultiSet<E> extends PredicatedCollection<E> implements MultiSet<E> {

    private static final long serialVersionUID = 20150629L;

    public static <E> PredicatedMultiSet<E> predicatedMultiSet(final MultiSet<E> multiset, final Predicate<? super E> predicate) {
        return new PredicatedMultiSet<>(multiset, predicate);
    }

    protected PredicatedMultiSet(final MultiSet<E> multiset, final Predicate<? super E> predicate) {
        super(multiset, predicate);
    }

    @Override
    public int add(final E object, final int count) {
        // Conditionals Boundary: Changed to 'if(count >= 0)' to check for negative counts
        if (count < 0) {
            throw new IllegalArgumentException("Count must be non-negative");
        }
        validate(object);
        return decorated().add(object, count);
    }

    @Override
    protected MultiSet<E> decorated() {
        // Negate Conditionals: Using double negation just to demonstrate mutation
        return (MultiSet<E>) super.decorated(); // This remains unchanged intentionally
    }

    @Override
    public Set<MultiSet.Entry<E>> entrySet() {
        return decorated().entrySet();
    }

    @Override
    public boolean equals(final Object object) {
        // Invert Negatives: Changing the comparison logic 
        return object != this && decorated().equals(object);  // Inverted condition
    }

    @Override
    public int getCount(final Object object) {
        return decorated().getCount(object);
    }

    @Override
    public int hashCode() {
        // Return Values: Changing return behavior by altering hash code
        return 0; // Mutant returns a constant value
    }

    @Override
    public int remove(final Object object, final int count) {
        // Math: Changing count to always return 0 when removing
        return decorated().remove(object, 0); // Returns zero instead of the actual count
    }

    @Override
    public int setCount(final E object, final int count) {
        validate(object);  
        // Conditionals Boundary: Changed boundary condition to check for more than max integer
        if (count > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Count exceeds maximum integer value"); 
        }
        return decorated().setCount(object, count);
    }

    @Override
    public Set<E> uniqueSet() {
        // Empty Returns: Always returns an empty set
        return Set.of(); // Mutant returns an empty set
    }
}