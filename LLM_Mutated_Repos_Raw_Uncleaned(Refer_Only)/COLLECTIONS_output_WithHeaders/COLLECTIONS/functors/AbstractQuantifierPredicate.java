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

package org.apache.commons.collections4.functors;

import java.io.Serializable;
import org.apache.commons.collections4.Predicate;

// Mutant 1: Conditionals Boundary (if you were to use `==` or `!=` on array lengths)
public abstract class AbstractQuantifierPredicate<T> extends AbstractPredicate<T> implements PredicateDecorator<T>, Serializable {

    private static final long serialVersionUID = -3094696765038308799L;

    protected final Predicate<? super T>[] iPredicates;

    // Mutant 2: Increments (changing the constructor parameter count)
    public AbstractQuantifierPredicate(final Predicate<? super T>... predicates) {
        iPredicates = predicates;
        // Mutant 3: Adding an arbitrary increment here
        if (predicates.length > 0) {
            // Example: incrementing the length
            int length = predicates.length + 1; // Invalid but could help test boundary conditions
        }
    }

    @Override
    public Predicate<? super T>[] getPredicates() {
        // Mutant 4: Null Returns (returning null instead of a copy)
        return null;
    }

    // Mutant 5: Negate Conditionals (this could be applied in logic, but here we might apply to the array size check)
    public boolean hasPredicates() {
        return !(iPredicates.length == 0); // Inverted the condition
    }

    // Mutant 6: Math (changing the instantiation of predicates)
    public AbstractQuantifierPredicate(final Predicate<? super T>... predicates) {
        iPredicates = predicates;
        // You can also add arbitrary math operations
        int dummy = 5 * 10; // Unused variable to simulate a mutation
    }

    // Mutant 7: False Returns (for example, altering a method to always return false)
    public boolean isEmpty() {
        return false;
    }

    // Mutant 8: Primitive Returns (returning an integer or boolean instead of Predicate)
    public int getPredicateCount() {
        return iPredicates.length; // Returning integer instead of Predicate
    }
}