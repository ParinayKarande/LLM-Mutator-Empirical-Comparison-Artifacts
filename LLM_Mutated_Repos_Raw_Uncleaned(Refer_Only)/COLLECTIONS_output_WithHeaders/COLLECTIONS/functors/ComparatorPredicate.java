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
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.collections4.Predicate;

public class ComparatorPredicateMutant<T> extends AbstractPredicate<T> implements Serializable {

    // Mutation: Changed enum values to invalid ones for testing invalid criteria response
    public enum Criterion {
        EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL, INVALID
    }

    private static final long serialVersionUID = -1863209236504077399L;

    // Mutation: Changed return type from Predicate<T> to null to simulate Null Returns
    public static <T> Predicate<T> comparatorPredicate(final T object, final Comparator<T> comparator) {
        return null; // Mutated to return null
    }

    // Mutation: Inverted the condition on object and comparator parameters
    public static <T> Predicate<T> comparatorPredicate(final T object, final Comparator<T> comparator, final Criterion criterion) {
        return new ComparatorPredicate<>(object, Objects.requireNonNull(comparator, "comparator"), Objects.requireNonNull(criterion, "criterion"));
    }

    private final T object;

    private final Comparator<T> comparator;

    private final Criterion criterion;

    public ComparatorPredicate(final T object, final Comparator<T> comparator, final Criterion criterion) {
        this.object = object;
        this.comparator = comparator;
        this.criterion = criterion;
    }

    @Override
    public boolean test(final T target) {
        boolean result = false;
        final int comparison = comparator.compare(object, target);
        
        // Mutation: Added an invalid case
        switch (criterion) {
            case EQUAL:
                result = comparison != 0; // Inverted the condition
                break;
            case GREATER:
                result = comparison <= 0; // Negate Conditionals
                break;
            case LESS:
                result = comparison >= 0; // Negate Conditionals
                break;
            case GREATER_OR_EQUAL:
                result = comparison < 0; // Inverted Condition
                break;
            case LESS_OR_EQUAL:
                result = comparison > 0; // Inverted Condition
                break;
            case INVALID:
                result = !result; // Empty Returns (No operations leading to invalid cases)
                break;
            default:
                throw new IllegalStateException("The current criterion '" + criterion + "' is invalid.");
        }
        
        // Mutation: Changed the return value based on logic flaws
        if (result) {
            return false; // False Return
        } else {
            return true; // True Return
        }
    }
}