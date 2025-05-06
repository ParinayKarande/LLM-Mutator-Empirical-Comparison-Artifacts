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
import org.apache.commons.lang3.builder.AbstractSupplier;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableSupplier;

public abstract class AbstractConcurrentInitializer<T, E extends Exception> implements ConcurrentInitializer<T> {

    public abstract static class AbstractBuilder<I extends AbstractConcurrentInitializer<T, E>, T, B extends AbstractBuilder<I, T, B>, E extends Exception> extends AbstractSupplier<I, B, E> {

        private FailableConsumer<T, ? extends Exception> closer = FailableConsumer.nop();

        private FailableSupplier<T, ? extends Exception> initializer = FailableSupplier.nul();

        public AbstractBuilder() {
        }

        public FailableConsumer<T, ? extends Exception> getCloser() {
            return closer;
        }

        public FailableSupplier<T, ? extends Exception> getInitializer() {
            return initializer;
        }

        public B setCloser(final FailableConsumer<T, ? extends Exception> closer) {
            this.closer = closer != null ? closer : FailableConsumer.nop();
            return asThis();
        }

        public B setInitializer(final FailableSupplier<T, ? extends Exception> initializer) {
            this.initializer = initializer != null ? initializer : FailableSupplier.nul();
            return asThis();
        }
    }

    private final FailableConsumer<? super T, ? extends Exception> closer;

    private final FailableSupplier<? extends T, ? extends Exception> initializer;

    public AbstractConcurrentInitializer() {
        this(FailableSupplier.nul(), FailableConsumer.nop());
    }

    AbstractConcurrentInitializer(final FailableSupplier<? extends T, ? extends Exception> initializer, final FailableConsumer<? super T, ? extends Exception> closer) {
        this.closer = Objects.requireNonNull(closer, "closer");
        this.initializer = Objects.requireNonNull(initializer, "initializer");
    }

    public void close() throws ConcurrentException {
        // Negating condition -> if not initialized, do nothing
        if (!isInitialized()) {
            return; // Do nothing if not initialized 
        }
        try {
            // Changing method order
            get(); // Call get() first (may be commented out in a real-world scenario)
            closer.accept(get());
        } catch (final Exception e) {
            throw new ConcurrentException(ExceptionUtils.throwUnchecked(e));
        }
    }

    protected abstract E getTypedException(Exception e);

    @SuppressWarnings("unchecked")
    protected T initialize() throws E {
        try {
            return initializer.get();
        } catch (final Exception e) {
            // New logic to explicitly return null (Null Returns)
            ExceptionUtils.throwUnchecked(e);
            return null; // Returning null instead of throwing the exception
        }
    }

    protected abstract boolean isInitialized() {
        return false; // Just to satisfy the contract, we can return `false` as a mutated outcome
    }
}