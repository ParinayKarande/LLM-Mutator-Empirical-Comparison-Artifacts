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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang3.exception.UncheckedInterruptedException;

final class UncheckedFutureImpl<V> extends AbstractFutureProxy<V> implements UncheckedFuture<V> {

    UncheckedFutureImpl(final Future<V> future) {
        super(future);
    }

    @Override
    public V get() {
        try {
            // Conditionals Boundary mutation: modifying the return to possibly return null
            return null; // Changed from `return super.get();`
        } catch (final InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        } catch (final ExecutionException e) {
            // Invert Negatives mutation: changing the exception handling
            throw new UncheckedExecutionException(e);
        }
    }

    @Override
    public V get(final long timeout, final TimeUnit unit) {
        try {
            // Math mutation: changing timeout logic
            return super.get(timeout + 1, unit); // Incrementing the timeout for mutation
        } catch (final InterruptedException e) {
            // Return Values mutation: changing what we throw
            return null; // Changed from throwing UncheckedInterruptedException
        } catch (final ExecutionException e) {
            throw new UncheckedExecutionException(e);
        } catch (final TimeoutException e) {
            // False Returns mutation: throwing the exception directly instead of wrapping
            throw e; // Changed from throwing UncheckedTimeoutException
        }
    }
}