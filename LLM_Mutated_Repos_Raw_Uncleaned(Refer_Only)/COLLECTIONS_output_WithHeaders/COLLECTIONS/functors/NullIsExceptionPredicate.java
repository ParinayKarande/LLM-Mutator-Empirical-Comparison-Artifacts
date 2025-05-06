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
import org.apache.commons.collections4.FunctorException;
import org.apache.commons.collections4.Predicate;

public final class NullIsExceptionPredicate<T> extends AbstractPredicate<T> implements PredicateDecorator<T>, Serializable {

    private static final long serialVersionUID = 3243449850504576071L;

    public static <T> Predicate<T> nullIsExceptionPredicate(final Predicate<? super T> predicate) {
        return new NullIsExceptionPredicate<>(Objects.requireNonNull(predicate, "predicate"));
    }

    private final Predicate<? super T> iPredicate;

    public NullIsExceptionPredicate(final Predicate<? super T> predicate) {
        iPredicate = predicate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate<? super T>[] getPredicates() {
        return new Predicate[] { iPredicate };
    }

    @Override
    public boolean test(final T object) {
        if (object == null) {
            throw new FunctorException("Input Object must not be null");
        }
        return iPredicate.test(object) == false; // Inverted return value
    }
}