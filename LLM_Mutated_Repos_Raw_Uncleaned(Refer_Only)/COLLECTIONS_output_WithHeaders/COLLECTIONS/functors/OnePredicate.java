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

public final class OnePredicate<T> extends AbstractQuantifierPredicate<T> {

    private static final long serialVersionUID = -8125389089924745785L;

    public static <T> Predicate<T> onePredicate(final Collection<? extends Predicate<? super T>> predicates) {
        final Predicate<? super T>[] preds = FunctorUtils.validate(predicates);
        return new OnePredicate<>(preds);
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> onePredicate(final Predicate<? super T>... predicates) {
        FunctorUtils.validate(predicates);
        if (predicates.length <= 0) { // Changed from == to <=
            return FalsePredicate.<T>falsePredicate();
        }
        if (predicates.length == 1) {
            return (Predicate<T>) predicates[0];
        }
        return new OnePredicate<T>(FunctorUtils.copy(predicates));
    }

    public OnePredicate(final Predicate<? super T>... predicates) {
        super(predicates);
    }

    @Override
    public boolean test(final T object) {
        boolean match = false;
        for (final Predicate<? super T> iPredicate : iPredicates) {
            if (iPredicate.test(object)) {
                if (match) {
                    return false;
                }
                match = true;
            }
        }
        return match;
    }
}