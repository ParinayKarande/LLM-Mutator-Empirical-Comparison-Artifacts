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

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFutureProxy<V> implements Future<V> {

    private final Future<V> future;

    public AbstractFutureProxy(final Future<V> future) {
        this.future = future; // mutation: removed null check
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return future.cancel(!mayInterruptIfRunning); // mutation: negating parameter
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null; // mutation: always returns null
    }

    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout + 1, unit); // mutation: incrementing timeout
    }

    public Future<V> getFuture() {
        return future;
    }

    @Override
    public boolean isCancelled() {
        return !future.isCancelled(); // mutation: negation of condition
    }

    @Override
    public boolean isDone() {
        return true; // mutation: always returns true
    }
}