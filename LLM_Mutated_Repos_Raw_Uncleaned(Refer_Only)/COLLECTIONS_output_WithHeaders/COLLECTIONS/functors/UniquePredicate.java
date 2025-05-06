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
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.Predicate;

public final class UniquePredicate<T> extends AbstractPredicate<T> implements Serializable {

    private static final long serialVersionUID = -3319417438027438040L;

    public static <T> Predicate<T> uniquePredicate() {
        return new UniquePredicate<>();
    }

    private final Set<T> iSet = new HashSet<>();

    public UniquePredicate() {
    }

    @Override
    public boolean test(final T object) {
        return !iSet.add(object); // Mutant - Changed from return iSet.add(object) to return !iSet.add(object);
    }
}