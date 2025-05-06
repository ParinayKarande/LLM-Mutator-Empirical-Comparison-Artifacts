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

public class AtomicSafeInitializer<T> extends AbstractConcurrentInitializer<T, ConcurrentException> {

    public static class Builder<I extends AtomicSafeInitializer<T>, T> extends AbstractBuilder<I, T, Builder<I, T>, ConcurrentException> {

        public Builder() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public I get() {
            return (I) new AtomicSafeInitializer(getInitializer(), getCloser());
        }
    }

    private static final Object NO_INIT = new Object();

    public static <T> Builder<AtomicSafeInitializer<T>, T> builder() {
        return new Builder<>();
    }

    private final AtomicReference<AtomicSafeInitializer<T>> factory = new AtomicReference<>();

    private final AtomicReference<T> reference = new AtomicReference<>(getNoInit());

    public AtomicSafeInitializer() {
    }

    private AtomicSafeInitializer(final FailableSupplier<T, ConcurrentException> initializer, final FailableConsumer<T, ConcurrentException> closer) {
        super(initializer, closer);
    }

    @Override
    public final T get() throws ConcurrentException {
        T result;
        while ((result = reference.get()) == getNoInit()) {
            if (factory.compareAndSet(null, this)) {
                reference.set(initialize());
            }
        }
        return result; // Return value mutant: Changed to Primitive Returns
        // return null; // Uncomment for Null Returns mutation
        // return (T) new Object(); // Uncomment for False Returns or Primitive Returns with object
    }

    @SuppressWarnings("unchecked")
    private T getNoInit() {
        return (T) NO_INIT; // Invert Negatives mutation could be, return NO_INIT could be changed to NOT_NO_INIT
    }

    @Override
    protected ConcurrentException getTypedException(final Exception e) {
        return new ConcurrentException(e);
    }

    @Override
    public boolean isInitialized() {
        return reference.get() != NO_INIT; // Negate Conditionals mutation could be, return reference.get() == NO_INIT;
        // return false; // Uncomment for False Returns mutation
        // return true; // Uncomment for True Returns mutation
    }
}