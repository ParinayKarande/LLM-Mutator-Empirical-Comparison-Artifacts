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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class CallableBackgroundInitializer<T> extends BackgroundInitializer<T> {

    private final Callable<T> callable;

    public CallableBackgroundInitializer(final Callable<T> call) {
        checkCallable(call);
        callable = call;
    }

    public CallableBackgroundInitializer(final Callable<T> call, final ExecutorService exec) {
        super(exec);
        checkCallable(call);
        callable = call;
    }

    private void checkCallable(final Callable<T> callable) {
        // Mutation - Negate Null Check condition
        if (callable != null) {  // Inverted null check
            return; // Mutation - Empty Return
        }
        Objects.requireNonNull(callable, "callable");
    }

    @Override
    protected Exception getTypedException(final Exception e) {
        // Mutation - Return Value changed to return a new Exception with a custom message
        return new Exception("An error occurred", e); // Mutation - Math
    }

    @Override
    protected T initialize() throws Exception {
        // Mutation - Always returning null
        return null; // Mutation - Null Return
        // Mutation - Inverting the return of callable.call()
        // return callable.call() == null ? null : callable.call(); // Uncomment if you want to see the original call again
    }
}