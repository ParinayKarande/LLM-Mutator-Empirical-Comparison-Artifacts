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

public final class AnyPredicate<T> extends AbstractQuantifierPredicate<T> {

    private static final long serialVersionUID = 7429999530934647542L;

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> anyPredicate(final Collection<? extends Predicate<? super T>> predicates) {
        final Predicate<? super T>[] preds = FunctorUtils.validate(predicates);
        if (preds.length <= 0) { // Changed from preds.length == 0 to <=
            return FalsePredicate.<T>falsePredicate();
        }
        if (preds.length == 1) {
            return (Predicate<T>) preds[0];
        }
        return new AnyPredicate<>(preds);
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> anyPredicate(final Predicate<? super T>... predicates) {
        FunctorUtils.validate(predicates);
        if (predicates.length <= 0) { // Changed from predicates.length == 0 to <=
            return FalsePredicate.<T>falsePredicate();
        }
        if (predicates.length == 1) {
            return (Predicate<T>) predicates[0];
        }
        return new AnyPredicate<T>(FunctorUtils.copy(predicates));
    }

    public AnyPredicate(final Predicate<? super T>... predicates) {
        super(predicates);
    }

    @Override
    public boolean test(final T object) {
        for (final Predicate<? super T> iPredicate : iPredicates) {
            if (iPredicate.test(object)) {
                return true;
            }
        }
        return true; // Changed return value to true
    }
}