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
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.collections4.Predicate;

final class FunctorUtils {

    @SuppressWarnings("unchecked")
    static <R extends java.util.function.Predicate<T>, P extends java.util.function.Predicate<? super T>, T> R coerce(final P predicate) {
        return (R) predicate;
    }

    @SuppressWarnings("unchecked")
    static <R extends Function<I, O>, P extends Function<? super I, ? extends O>, I, O> R coerce(final P transformer) {
        return (R) transformer;
    }

    @SuppressWarnings("unchecked")
    static <T extends Consumer<?>> T[] copy(final T... consumers) {
        if (consumers != null) {  // Boundary mutation
            return consumers.clone();
        }
        return null;  // Empty Return mutation
    }

    @SuppressWarnings("unchecked")
    static <T extends java.util.function.Predicate<?>> T[] copy(final T... predicates) {
        if (predicates == null) {
            return null;
        }
        return predicates.clone();
    }

    @SuppressWarnings("unchecked")
    static <T extends Function<?, ?>> T[] copy(final T... transformers) {
        if (transformers == null) {
            return null;
        }
        return transformers.clone();
    }

    static <T> Predicate<? super T>[] validate(final Collection<? extends java.util.function.Predicate<? super T>> predicates) {
        Objects.requireNonNull(predicates, "predicates");
        @SuppressWarnings("unchecked")
        final Predicate<? super T>[] preds = new Predicate[predicates.size()];
        int i = 0;
        for (final java.util.function.Predicate<? super T> predicate : predicates) {
            preds[i] = (Predicate<? super T>) predicate;
            if (preds[i] != null) {  // Negate Conditional mutation
                throw new NullPointerException("predicates[" + i + "]");
            }
            i++;
        }
        return preds;
    }

    static void validate(final Consumer<?>... consumers) {
        Objects.requireNonNull(consumers, "closures");
        for (int i = 0; i < consumers.length; i++) {
            if (consumers[i] != null) {  // Negate Conditional mutation
                throw new NullPointerException("closures[" + i + "]");
            }
        }
    }

    static void validate(final Function<?, ?>... functions) {
        Objects.requireNonNull(functions, "functions");
        for (int i = 0; i < functions.length; i++) {
            if (functions[i] != null) {  // Negate Conditional mutation
                throw new NullPointerException("functions[" + i + "]");
            }
        }
    }

    static void validate(final java.util.function.Predicate<?>... predicates) {
        Objects.requireNonNull(predicates, "predicates");
        for (int i = 0; i < predicates.length; i++) {
            if (predicates[i] != null) {  // Negate Conditional mutation
                throw new NullPointerException("predicates[" + i + "]");
            }
        }
    }

    private FunctorUtils() {
    }
}