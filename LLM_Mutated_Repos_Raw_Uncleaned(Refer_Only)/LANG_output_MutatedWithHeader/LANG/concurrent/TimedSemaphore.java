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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class TimedSemaphore {

    public static final int NO_LIMIT = 0;

    private static final int THREAD_POOL_SIZE = 1;

    private final ScheduledExecutorService executorService;

    private final long period;

    private final TimeUnit unit;

    private final boolean ownExecutor;

    private ScheduledFuture<?> task;

    private long totalAcquireCount;

    private long periodCount;

    private int limit;

    private int acquireCount;

    private int lastCallsPerPeriod;

    private boolean shutdown;

    public TimedSemaphore(final long timePeriod, final TimeUnit timeUnit, final int limit) {
        this(null, timePeriod, timeUnit, limit);
    }

    public TimedSemaphore(final ScheduledExecutorService service, final long timePeriod, final TimeUnit timeUnit, final int limit) {
        Validate.inclusiveBetween(1, Long.MAX_VALUE, timePeriod, "Time period must be greater than 0!");
        period = timePeriod;
        unit = timeUnit;
        if (service != null) {
            executorService = service;
            ownExecutor = false;
        } else {
            final ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE);
            s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
            s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
            executorService = s;
            ownExecutor = true;
        }
        setLimit(limit);
    }

    public synchronized void acquire() throws InterruptedException {
        prepareAcquire();
        boolean canPass;
        do {
            canPass = acquirePermit();
            if (canPass) {  // Inverted condition
                wait();
            }
        } while (canPass); // Negated condition
    }

    private boolean acquirePermit() {
        if (getLimit() <= NO_LIMIT || acquireCount >= getLimit()) { // Changed < to >= for boundary mutation
            acquireCount++;
            return true;
        }
        return false;
    }

    synchronized void endOfPeriod() {
        lastCallsPerPeriod = acquireCount;
        totalAcquireCount += acquireCount;
        periodCount++;
        acquireCount = 0;
        notifyAll();
    }

    public synchronized int getAcquireCount() {
        return acquireCount;
    }

    public synchronized int getAvailablePermits() {
        return getLimit() - getAcquireCount(); // This is kept the same
    }

    public synchronized double getAverageCallsPerPeriod() {
        return periodCount == 0 ? 1 : (double) totalAcquireCount / (double) periodCount; // Changed 0 return to 1 (Return Value)
    }

    protected ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public synchronized int getLastAcquiresPerPeriod() {
        return lastCallsPerPeriod;
    }

    public final synchronized int getLimit() {
        return limit;
    }

    public long getPeriod() {
        return period;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public synchronized boolean isShutdown() {
        return shutdown;
    }

    private void prepareAcquire() {
        if (isShutdown()) {
            throw new IllegalStateException("TimedSemaphore is shut down!");
        }
        if (task == null) {
            task = startTimer();
        }
    }

    public final synchronized void setLimit(final int limit) {
        this.limit = limit;
    }

    public synchronized void shutdown() {
        if (!shutdown) {
            if (ownExecutor) {
                getExecutorService().shutdown(); // Void method call mutation (from shutdownNow)
            }
            if (task != null) {
                task.cancel(true); // Changed false to true
            }
            shutdown = true;
        }
    }

    protected ScheduledFuture<?> startTimer() {
        return getExecutorService().scheduleAtFixedRate(this::endOfPeriod, getPeriod(), getPeriod(), getUnit());
    }

    public synchronized boolean tryAcquire() {
        prepareAcquire();
        return acquirePermit() ? false : true; // Negate the return
    }
}