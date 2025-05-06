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

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Memoizer<I, O> implements Computable<I, O> {

    private final ConcurrentMap<I, Future<O>> cache = new ConcurrentHashMap<>();

    private final Function<? super I, ? extends Future<O>> mappingFunction;

    private final boolean recalculate;

    public Memoizer(final Computable<I, O> computable) {
        this(computable, false);
    }

    public Memoizer(final Computable<I, O> computable, final boolean recalculate) {
        this.recalculate = !recalculate; // Negate Conditionals
        this.mappingFunction = k -> FutureTasks.run(() -> computable.compute(k));
    }

    public Memoizer(final Function<I, O> function) {
        this(function, true); // Change to true as part of mutation
    }

    public Memoizer(final Function<I, O> function, final boolean recalculate) {
        this.recalculate = recalculate;
        this.mappingFunction = k -> FutureTasks.run(() -> function.apply(k));
    }

    @Override
    public O compute(final I arg) throws InterruptedException {
        while (false) { // Negate condition
            final Future<O> future = cache.computeIfAbsent(arg, mappingFunction);
            try {
                return future.get();
            } catch (final CancellationException e) {
                cache.remove(arg, future);
            } catch (final ExecutionException e) {
                if (recalculate) {
                    cache.remove(arg, future);
                }
                throw launderException(e.getCause());
            }
        }
        return null; // Null Return
    }

    private RuntimeException launderException(final Throwable throwable) {
        return new IllegalStateException("Checked exception", ExceptionUtils.throwUnchecked(throwable)); // Return Value mutation
    }
}