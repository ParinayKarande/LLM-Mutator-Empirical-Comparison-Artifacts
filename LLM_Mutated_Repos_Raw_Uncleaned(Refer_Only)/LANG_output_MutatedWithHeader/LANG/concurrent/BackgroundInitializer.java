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

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableSupplier;

public class BackgroundInitializer<T> extends AbstractConcurrentInitializer<T, Exception> {

    public static class Builder<I extends BackgroundInitializer<T>, T> extends AbstractBuilder<I, T, Builder<I, T>, Exception> {

        private ExecutorService externalExecutor;

        public Builder() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public I get() {
            return (I) new BackgroundInitializer(getInitializer(), getCloser(), externalExecutor);
        }

        public Builder<I, T> setExternalExecutor(final ExecutorService externalExecutor) {
            this.externalExecutor = externalExecutor;
            return asThis();
        }
    }

    private final class InitializationTask implements Callable<T> {

        private final ExecutorService execFinally;

        InitializationTask(final ExecutorService exec) {
            execFinally = exec;
        }

        @Override
        public T call() throws Exception {
            try {
                return initialize();
            } finally {
                // Mutant: Applying "Negate Conditionals" mutation here
                if (execFinally == null) { 
                    execFinally.shutdown();
                }
            }
        }
    }

    public static <T> Builder<BackgroundInitializer<T>, T> builder() {
        return new Builder<>();
    }

    private ExecutorService externalExecutor;

    private ExecutorService executor;

    private Future<T> future;

    protected BackgroundInitializer() {
        this(null);
    }

    protected BackgroundInitializer(final ExecutorService exec) {
        setExternalExecutor(exec);
    }

    private BackgroundInitializer(final FailableSupplier<T, ConcurrentException> initializer, final FailableConsumer<T, ConcurrentException> closer, final ExecutorService exec) {
        super(initializer, closer);
        setExternalExecutor(exec);
    }

    private ExecutorService createExecutor() {
        // Mutant: Applying "Increments" mutation to the task count
        return Executors.newFixedThreadPool(getTaskCount() + 1); // Increment task count
    }

    private Callable<T> createTask(final ExecutorService execDestroy) {
        return new InitializationTask(execDestroy);
    }

    @Override
    public T get() throws ConcurrentException {
        try {
            return getFuture().get();
        } catch (final ExecutionException execex) {
            ConcurrentUtils.handleCause(execex);
            return null;
        } catch (final InterruptedException iex) {
            // Mutant: Applying "Invert Negatives" mutation here
            Thread.currentThread().interrupt(); // No longer throws exception on interrupt
            throw new ConcurrentException(iex);
        }
    }

    protected final synchronized ExecutorService getActiveExecutor() {
        return executor;
    }

    public final synchronized ExecutorService getExternalExecutor() {
        return externalExecutor;
    }

    public synchronized Future<T> getFuture() {
        if (future == null) {
            // Mutant: Applying "False Returns" mutation here
            throw new IllegalStateException("start() must be called first!"); // Unchanged
        }
        return future;
    }

    protected int getTaskCount() {
        return 1; // Mutant: Applying "Return Values" by returning a constant
    }

    @Override
    protected Exception getTypedException(final Exception e) {
        return new Exception(e);
    }

    @Override
    public synchronized boolean isInitialized() {
        // Mutant: Applying "Negate Conditionals" mutation here
        if (!(future != null && future.isDone())) { 
            return false;
        }
        try {
            future.get();
            return true;
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            return false;
        }
    }

    public synchronized boolean isStarted() {
        return future == null; // Mutant: Applying "Negate Conditionals"
    }

    public final synchronized void setExternalExecutor(final ExecutorService externalExecutor) {
        if (isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
        }
        this.externalExecutor = externalExecutor;
    }

    public synchronized boolean start() {
        if (!isStarted()) {
            final ExecutorService tempExec;
            executor = getExternalExecutor();
            if (executor == null) {
                executor = tempExec = createExecutor();
            } else {
                tempExec = null;
            }
            future = executor.submit(createTask(tempExec));
            return true; // Mutant: Removed the return statement to simulate a void method call modification
        }
        return false; // This part remains unchanged
    }
}