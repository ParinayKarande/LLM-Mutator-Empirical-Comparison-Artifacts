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

public final class TruePredicate<T> extends AbstractPredicate<T> implements Serializable {

    private static final long serialVersionUID = 3374767158756189740L;

    @SuppressWarnings("rawtypes")
    public static final Predicate INSTANCE = new TruePredicate<>();

    public static <T> Predicate<T> truePredicate() {
        return INSTANCE; // No change
    }

    private TruePredicate() {
    }

    private Object readResolve() {
        return null; // Null Returns: change INSTANCE to null
    }

    @Override
    public boolean test(final T object) {
        return false; // False Returns: always returns false
    }
}