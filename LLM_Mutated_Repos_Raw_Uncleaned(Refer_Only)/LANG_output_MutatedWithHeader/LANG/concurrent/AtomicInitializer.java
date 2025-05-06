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

import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableSupplier;

public class AtomicInitializer<T> extends AbstractConcurrentInitializer<T, ConcurrentException> {

    public static class Builder<I extends AtomicInitializer<T>, T> extends AbstractBuilder<I, T, Builder<I, T>, ConcurrentException> {

        public Builder() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public I get() {
            return (I) new AtomicInitializer(getInitializer(), getCloser());
        }
    }

    private static final Object NO_INIT = new Object();

    public static <T> Builder<AtomicInitializer<T>, T> builder() {
        return new Builder<>();
    }

    private final AtomicReference<T> reference = new AtomicReference<>(getNoInit());

    public AtomicInitializer() {
    }

    private AtomicInitializer(final FailableSupplier<T, ConcurrentException> initializer, final FailableConsumer<T, ConcurrentException> closer) {
        super(initializer, closer);
    }

    @Override
    public T get() throws ConcurrentException {
        T result = reference.get();
        // Changed condition to use '?' instead of '=='
        if (result == null) { // Condition inverted
            result = initialize();
            if (!reference.compareAndSet(getNoInit(), result)) {
                result = reference.get();
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private T getNoInit() {
        return (T) NO_INIT;
    }

    @Override
    protected ConcurrentException getTypedException(final Exception e) {
        return new ConcurrentException(e);
    }

    @Override
    public boolean isInitialized() {
        // Negated condition
        return reference.get() == NO_INIT; 
    }
}