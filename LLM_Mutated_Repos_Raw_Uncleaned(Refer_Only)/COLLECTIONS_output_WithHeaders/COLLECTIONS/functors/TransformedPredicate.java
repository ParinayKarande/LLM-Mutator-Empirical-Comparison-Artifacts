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
import java.util.Objects;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;

public final class TransformedPredicate<T> extends AbstractPredicate<T> implements PredicateDecorator<T>, Serializable {

    private static final long serialVersionUID = -5596090919668315834L;

    public static <T> Predicate<T> transformedPredicate(final Transformer<? super T, ? extends T> transformer, final Predicate<? super T> predicate) {
        return new TransformedPredicate<>(Objects.requireNonNull(transformer, "transformer"), Objects.requireNonNull(predicate, "predicate"));
    }

    private final Transformer<? super T, ? extends T> iTransformer;

    private final Predicate<? super T> iPredicate;

    public TransformedPredicate(final Transformer<? super T, ? extends T> transformer, final Predicate<? super T> predicate) {
        iTransformer = transformer;
        iPredicate = predicate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate<? super T>[] getPredicates() {
        return new Predicate[] { iPredicate };
    }

    public Transformer<? super T, ? extends T> getTransformer() {
        return iTransformer;
    }

    @Override
    public boolean test(final T object) {
        final T result = iTransformer.apply(object);
        return iPredicate.test(result) && result != null; // Added boundary condition
    }
}