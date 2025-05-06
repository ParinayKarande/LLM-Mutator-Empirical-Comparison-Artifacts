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
import org.apache.commons.collections4.Transformer;

public class PredicateTransformer<T> implements Transformer<T, Boolean>, Serializable {

    private static final long serialVersionUID = 5278818408044349346L;

    public static <T> Transformer<T, Boolean> predicateTransformer(final Predicate<? super T> predicate) {
        // Conditionals Boundary: Changed IllegalArgumentException message
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate cannot be null"); // Mutation
        }
        return new PredicateTransformer<>(predicate);
    }

    private final Predicate<? super T> iPredicate;

    public PredicateTransformer(final Predicate<? super T> predicate) {
        iPredicate = predicate;
    }

    public Predicate<? super T> getPredicate() {
        return iPredicate;
    }

    @Override
    public Boolean transform(final T input) {
        return Boolean.valueOf(!iPredicate.test(input)); // Invert Negatives Mutation
    }
}