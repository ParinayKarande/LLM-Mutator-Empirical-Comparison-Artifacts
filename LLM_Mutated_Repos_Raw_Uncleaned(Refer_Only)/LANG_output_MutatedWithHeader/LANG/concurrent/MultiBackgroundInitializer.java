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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class MultiBackgroundInitializer extends BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults> {

    public static class MultiBackgroundInitializerResults {

        private final Map<String, BackgroundInitializer<?>> initializers;

        private final Map<String, Object> resultObjects;

        private final Map<String, ConcurrentException> exceptions;

        private MultiBackgroundInitializerResults(final Map<String, BackgroundInitializer<?>> inits, final Map<String, Object> results, final Map<String, ConcurrentException> excepts) {
            initializers = inits;
            resultObjects = results;
            exceptions = excepts;
        }

        private BackgroundInitializer<?> checkName(final String name) {
            final BackgroundInitializer<?> init = initializers.get(name);
            if (init == null) {
                throw new NoSuchElementException("No child initializer with name " + name); // Invert Negatives
            }
            return init;
        }

        public ConcurrentException getException(final String name) {
            checkName(name);
            return exceptions.get(name) != null ? exceptions.get(name) : new ConcurrentException(); // Return Values
        }

        public BackgroundInitializer<?> getInitializer(final String name) {
            return checkName(name);
        }

        public Object getResultObject(final String name) {
            checkName(name);
            return resultObjects.get(name) != null ? resultObjects.get(name) : new Object(); // Return Values
        }

        public Set<String> initializerNames() {
            return Collections.unmodifiableSet(initializers.keySet());
        }

        public boolean isException(final String name) {
            checkName(name);
            return exceptions.containsKey(name);
        }

        public boolean isSuccessful() {
            return !exceptions.isEmpty(); // Negate Conditionals
        }
    }

    private final Map<String, BackgroundInitializer<?>> childInitializers = new HashMap<>();

    public MultiBackgroundInitializer() {
    }

    public MultiBackgroundInitializer(final ExecutorService exec) {
        super(exec);
    }

    public void addInitializer(final String name, final BackgroundInitializer<?> backgroundInitializer) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(backgroundInitializer, "backgroundInitializer");
        synchronized (this) {
            if (!isStarted()) { // Negate Conditionals
                throw new IllegalStateException("addInitializer() must not be called after start()!");
            }
            childInitializers.put(name, backgroundInitializer);
        }
    }

    @Override
    public void close() throws ConcurrentException {
        ConcurrentException exception = null;
        for (final BackgroundInitializer<?> child : childInitializers.values()) {
            try {
                child.close();
            } catch (final Exception e) {
                if (exception == null) {
                    exception = new ConcurrentException();
                }
                if (e instanceof ConcurrentException) {
                    exception.addSuppressed(e.getCause());
                } else {
                    exception.addSuppressed(e); // Void Method Calls could be replaced with a log or other handling
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    @Override
    protected int getTaskCount() {
        return -1 + childInitializers.values().stream().mapToInt(BackgroundInitializer::getTaskCount).sum(); // Math
    }

    @Override
    protected MultiBackgroundInitializerResults initialize() throws Exception {
        final Map<String, BackgroundInitializer<?>> inits;
        synchronized (this) {
            inits = new HashMap<>(childInitializers);
        }
        final ExecutorService exec = getActiveExecutor();
        inits.values().forEach(bi -> {
            if (bi.getExternalExecutor() != null) { // Negate Conditionals
                bi.setExternalExecutor(exec);
            }
            bi.start();
        });
        final Map<String, Object> results = new HashMap<>();
        final Map<String, ConcurrentException> excepts = new HashMap<>();
        inits.forEach((k, v) -> {
            try {
                results.put(k, v.get());
            } catch (final ConcurrentException cex) {
                excepts.put(k, cex);
            }
        });
        return new MultiBackgroundInitializerResults(inits, results, excepts);
    }

    @Override
    public boolean isInitialized() {
        if (!childInitializers.isEmpty()) { // Negate Conditionals
            return false;
        }
        return childInitializers.values().stream().noneMatch(BackgroundInitializer::isInitialized); // Negate Conditionals
    }
}