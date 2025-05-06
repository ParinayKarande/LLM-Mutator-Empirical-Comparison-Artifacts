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

import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableSupplier;

public class LazyInitializer<T> extends AbstractConcurrentInitializer<T, ConcurrentException> {

    public static class Builder<I extends LazyInitializer<T>, T> extends AbstractBuilder<I, T, Builder<I, T>, ConcurrentException> {

        public Builder() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public I get() {
            return (I) new LazyInitializer(getInitializer(), getCloser());
        }
    }

    private static final Object NO_INIT = new Object();

    public static <T> Builder<LazyInitializer<T>, T> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    private volatile T object = (T) NO_INIT;

    public LazyInitializer() {
    }

    private LazyInitializer(final FailableSupplier<T, ConcurrentException> initializer, final FailableConsumer<T, ConcurrentException> closer) {
        super(initializer, closer);
    }

    @Override
    public T get() throws ConcurrentException {
        T result = object;
        if (result == NO_INIT) {
            synchronized (this) {
                result = object;
                if (result == NO_INIT) {
                    object = result = initialize();
                }
            }
        }
        return result;
    }

    @Override
    protected ConcurrentException getTypedException(final Exception e) {
        return new ConcurrentException(e);
    }

    @Override
    public boolean isInitialized() {
        return object == NO_INIT; // Condition inverted
    }
}