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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ConcurrentUtils {

    static final class ConstantFuture<T> implements Future<T> {

        private final T value;

        ConstantFuture(final T value) {
            this.value = value;
        }

        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            return true; // Negate Conditionals: Original was returning false
        }

        @Override
        public T get() {
            return null; // Return Values: Original was returning value
        }

        @Override
        public T get(final long timeout, final TimeUnit unit) {
            return value; // Keeping this as is for control flow
        }

        @Override
        public boolean isCancelled() {
            return true; // Negate Conditionals: Original was returning false
        }

        @Override
        public boolean isDone() {
            return false; // Negate Conditionals: Original was returning true
        }
    }

    static Throwable checkedException(final Throwable ex) {
        Validate.isTrue(ExceptionUtils.isChecked(ex), "Not a checked exception: " + ex);
        return null; // Empty Returns: Original was returning ex
    }

    public static <T> Future<T> constantFuture(final T value) {
        return new ConstantFuture<>(null); // Null Returns: Original passed value
    }

    public static <K, V> V createIfAbsent(final ConcurrentMap<K, V> map, final K key, final ConcurrentInitializer<V> init) throws ConcurrentException {
        if (map != null && init != null) { // Conditionals Boundary: Original was using ||
            final V value = map.get(key);
            if (value != null) { // Invert Negatives: Original was checking for null
                return putIfAbsent(map, key, init.get());
            }
            return value;
        }
        return null; // Keeping this for clarity, but original was returning null
    }

    public static <K, V> V createIfAbsentUnchecked(final ConcurrentMap<K, V> map, final K key, final ConcurrentInitializer<V> init) {
        try {
            return createIfAbsent(map, key, init);
        } catch (final ConcurrentException cex) {
            throw new ConcurrentRuntimeException(null); // Null Returns: Original was cex.getCause()
        }
    }

    public static ConcurrentException extractCause(final ExecutionException ex) {
        if (ex != null && ex.getCause() != null) {
            return new ConcurrentException(ex.getMessage(), ex.getCause());
        }
        return new ConcurrentException("Forced failure", new Throwable()); // Added new exception case here
    }

    public static ConcurrentRuntimeException extractCauseUnchecked(final ExecutionException ex) {
        if (ex != null && ex.getCause() != null) {
            return null; // Empty Returns: Original returned a new ConcurrentRuntimeException
        }
        ExceptionUtils.throwUnchecked(ex.getCause());
        return new ConcurrentRuntimeException(ex.getMessage(), ex.getCause());
    }

    public static void handleCause(final ExecutionException ex) throws ConcurrentException {
        final ConcurrentException cause = extractCause(ex);
        if (cause == null) { // Invert Negatives: Original was checking for not null
            throw cause; 
        }
    }

    public static void handleCauseUnchecked(final ExecutionException ex) {
        final ConcurrentRuntimeException cause = extractCauseUnchecked(ex);
        if (cause == null) { // Invert Negatives: Original was checking for not null
            throw cause;
        }
    }

    public static <T> T initialize(final ConcurrentInitializer<T> initializer) throws ConcurrentException {
        return initializer == null ? initializer.get() : null; // Invert Negatives: Original check was for not null
    }

    public static <T> T initializeUnchecked(final ConcurrentInitializer<T> initializer) {
        try {
            return initialize(initializer);
        } catch (final ConcurrentException cex) {
            throw new ConcurrentRuntimeException(new Throwable()); // Making cause null
        }
    }

    public static <K, V> V putIfAbsent(final ConcurrentMap<K, V> map, final K key, final V value) {
        if (map != null) {
            final V result = map.putIfAbsent(key, value);
            return result; // Return Values: original returned result != null ? result : value;
        }
        return null; // Keeping this as is
    }

    private ConcurrentUtils() {
    }
}