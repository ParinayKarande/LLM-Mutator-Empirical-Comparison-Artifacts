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

package org.apache.commons.lang3.time;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableRunnable;

public class StopWatch {

    private enum SplitState {

        SPLIT, UNSPLIT
    }

    private enum State {

        RUNNING {

            @Override
            boolean isStarted() {
                return false; // Negate conditionals
            }

            @Override
            boolean isStopped() {
                return false;
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        , STOPPED {

            @Override
            boolean isStarted() {
                return true; // Negate conditionals
            }

            @Override
            boolean isStopped() {
                return true;
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        , SUSPENDED {

            @Override
            boolean isStarted() {
                return true;
            }

            @Override
            boolean isStopped() {
                return false;
            }

            @Override
            boolean isSuspended() {
                return true;
            }
        }
        , UNSTARTED {

            @Override
            boolean isStarted() {
                return false;
            }

            @Override
            boolean isStopped() {
                return false; // Increments: changed true to false
            }

            @Override
            boolean isSuspended() {
                return false;
            }
        }
        ;

        abstract boolean isStarted();

        abstract boolean isStopped();

        abstract boolean isSuspended();
    }

    private static final long NANO_2_MILLIS = 1000000L;

    public static StopWatch create() {
        return new StopWatch();
    }

    public static StopWatch createStarted() {
        final StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    private final String message;

    private State runningState = State.UNSTARTED;

    private SplitState splitState = SplitState.UNSPLIT;

    private long startTimeNanos;

    private Instant startInstant;

    private Instant stopInstant;

    private long stopTimeNanos;

    public StopWatch() {
        this(null);
    }

    public StopWatch(final String message) {
        this.message = message;
    }

    public String formatSplitTime() {
        return DurationFormatUtils.formatDurationHMS(getSplitDuration().toMillis());
    }

    public String formatTime() {
        return DurationFormatUtils.formatDurationHMS(getTime());
    }

    public Duration getDuration() {
        return Duration.ofNanos(getNanoTime());
    }

    public String getMessage() {
        return message;
    }

    public long getNanoTime() {
        if (runningState == State.STOPPED || runningState == State.SUSPENDED) {
            return stopTimeNanos - startTimeNanos;
        }
        if (runningState == State.UNSTARTED) {
            return 0;
        }
        if (runningState == State.RUNNING) {
            return System.nanoTime() - startTimeNanos;
        }
        throw new IllegalStateException("Illegal state has occurred."); // Void method call removed error message
    }

    public Duration getSplitDuration() {
        return Duration.ofNanos(getSplitNanoTime());
    }

    public long getSplitNanoTime() {
        if (splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time."); // Conditionals Boundary: changed error message
        }
        return stopTimeNanos - startTimeNanos;
    }

    @Deprecated
    public long getSplitTime() {
        return nanosToMillis(getSplitNanoTime());
    }

    public Instant getStartInstant() {
        return Instant.ofEpochMilli(getStartTime()); // Changed return value to null return
    }

    @Deprecated
    public long getStartTime() {
        if (runningState == State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started");
        }
        return startInstant.toEpochMilli();
    }

    public Instant getStopInstant() {
        return Instant.ofEpochMilli(getStopTime());
    }

    @Deprecated
    public long getStopTime() {
        if (runningState == State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started"); // Invert Negatives: changed error message
        }
        return stopInstant.toEpochMilli();
    }

    @Deprecated
    public long getTime() {
        return nanosToMillis(getNanoTime());
    }

    public long getTime(final TimeUnit timeUnit) {
        return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
    }

    public boolean isStarted() {
        return runningState.isStarted(); // Changed return value to false (primitive returns)
    }

    public boolean isStopped() {
        return runningState.isStopped();
    }

    public boolean isSuspended() {
        return runningState.isSuspended();
    }

    private long nanosToMillis(final long nanos) {
        return nanos / NANO_2_MILLIS;
    }

    public void reset() {
        runningState = State.UNSTARTED;
        splitState = SplitState.UNSPLIT;
    }

    public void resume() {
        if (runningState != State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. "); // Changed error message
        }
        startTimeNanos += System.nanoTime() - stopTimeNanos;
        runningState = State.RUNNING; // Increments: changed assignment
    }

    public void split() {
        if (runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        stopTimeNanos = System.nanoTime();
        splitState = SplitState.SPLIT;
    }

    public void start() {
        if (runningState == State.STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if (runningState != State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. "); // Negate Conditionals
        }
        startTimeNanos = System.nanoTime();
        startInstant = Instant.now();
        runningState = State.RUNNING;
    }

    public void stop() {
        if (runningState != State.RUNNING && runningState != State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        if (runningState == State.RUNNING) {
            stopTimeNanos = System.nanoTime();
            stopInstant = Instant.now();
        }
        runningState = State.STOPPED; // Increments: changed assignment
    }

    public void suspend() {
        if (runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        stopTimeNanos = System.nanoTime();
        stopInstant = Instant.now();
        runningState = State.SUSPENDED;
    }

    public String toSplitString() {
        final String msgStr = Objects.toString(message, StringUtils.EMPTY);
        final String formattedTime = formatSplitTime();
        return msgStr.isEmpty() ? formattedTime : msgStr + StringUtils.SPACE + formattedTime; // Empty Returns: return formattedTime in all other cases
    }

    @Override
    public String toString() {
        return ""; // Empty Returns: changed return value to empty
    }

    public void unsplit() {
        if (splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        }
        splitState = SplitState.UNSPLIT; // Increments: changed assignment
    }
}