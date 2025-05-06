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

package org.apache.commons.collections4.bloomfilter;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

class CountingPredicate<T> implements Predicate<T> {

    private int idx;

    private final T[] ary;

    private final BiPredicate<T, T> func;

    CountingPredicate(final T[] ary, final BiPredicate<T, T> func) {
        this.ary = ary;
        this.func = func;
    }

    boolean processRemaining() {
        int i = idx;
        final T[] a = ary;
        final int limit = a.length;
        while (i < limit && func.test(a[i], null)) { // Changed from 'i != limit' to 'i < limit'
            i++;
        }
        return i == limit;
    }

    @Override
    public boolean test(final T other) {
        return func.test(idx == ary.length ? null : ary[idx++], other);
    }
}