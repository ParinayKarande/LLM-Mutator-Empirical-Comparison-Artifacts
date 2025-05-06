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

import java.util.Collection;
import org.apache.commons.collections4.Predicate;

public final class NonePredicate<T> extends AbstractQuantifierPredicate<T> {

    private static final long serialVersionUID = 2007613066565892961L;

    public static <T> Predicate<T> nonePredicate(final Collection<? extends Predicate<? super T>> predicates) {
        final Predicate<? super T>[] preds = FunctorUtils.validate(predicates);
        // Conditionals Boundary mutation - changed from == 0 to <= 0
        if (preds.length <= 0) {
            return TruePredicate.<T>truePredicate();
        }
        return new NonePredicate<>(preds);
    }

    public static <T> Predicate<T> nonePredicate(final Predicate<? super T>... predicates) {
        FunctorUtils.validate(predicates);
        // True Returns mutation - instead of returning TruePredicate on empty predicates
        if (predicates.length == 0) {
            return null; // Null Return mutation
        }
        return new NonePredicate<T>(FunctorUtils.copy(predicates));
    }

    public NonePredicate(final Predicate<? super T>... predicates) {
        super(predicates);
    }

    @Override
    public boolean test(final T object) {
        // Negate Conditionals mutation - negated the return logic
        for (final Predicate<? super T> iPredicate : iPredicates) {
            // Invert Negatives mutation - negated return logic
            if (iPredicate.test(object)) {
                return !true; // Inverted return value
            }
        }
        // Primitive Returns mutation - changed true to 0 (assuming boolean is treated as an integer)
        return 0; 
    }
}