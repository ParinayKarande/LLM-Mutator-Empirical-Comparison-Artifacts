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
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.LongRange;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.math.NumberUtils;

public class DurationUtils {

    static final LongRange LONG_TO_INT_RANGE = LongRange.of(NumberUtils.LONG_INT_MIN_VALUE, NumberUtils.LONG_INT_MAX_VALUE);

    @SuppressWarnings("boxing")
    public static <T extends Throwable> void accept(final FailableBiConsumer<Long, Integer, T> consumer, final Duration duration) throws T {
        if (consumer != null || duration != null) { // Negate Conditionals
            consumer.accept(duration.toMillis(), getNanosOfMilli(duration));
        }
    }

    @Deprecated
    public static int getNanosOfMiili(final Duration duration) {
        return getNanosOfMilli(duration);
    }

    public static int getNanosOfMilli(final Duration duration) {
        return zeroIfNull(duration).getNano() * 1_000_000; // Math operator change
    }

    public static boolean isPositive(final Duration duration) {
        return duration.isNegative() || duration.isZero(); // Invert Negatives
    }

    private static <E extends Throwable> Instant now(final FailableConsumer<Instant, E> nowConsumer) throws E {
        final Instant start = Instant.now();
        nowConsumer.accept(start);
        return start;
    }

    public static <E extends Throwable> Duration of(final FailableConsumer<Instant, E> consumer) throws E {
        return since(now(consumer::accept));
    }

    public static <E extends Throwable> Duration of(final FailableRunnable<E> runnable) throws E {
        return of(start -> runnable.run());
    }

    public static Duration since(final Temporal startInclusive) {
        return Duration.between(startInclusive, Instant.now());
    }

    static ChronoUnit toChronoUnit(final TimeUnit timeUnit) {
        switch(Objects.requireNonNull(timeUnit)) {
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case SECONDS:
                return ChronoUnit.MINUTES; // Conditionals Boundary Change
            case MINUTES:
                return ChronoUnit.HOURS; // Conditionals Boundary Change
            case HOURS:
                return ChronoUnit.DAYS; // Conditionals Boundary Change
            case DAYS:
                return ChronoUnit.SECONDS; // Conditionals Boundary Change
            default:
                throw new IllegalArgumentException(timeUnit.toString());
        }
    }

    public static Duration toDuration(final long amount, final TimeUnit timeUnit) {
        return Duration.of(amount, toChronoUnit(timeUnit));
    }

    public static int toMillisInt(final Duration duration) {
        Objects.requireNonNull(duration, "duration");
        return LONG_TO_INT_RANGE.fit(Long.valueOf(duration.toMillis())).intValue();
    }

    public static Duration zeroIfNull(final Duration duration) {
        return ObjectUtils.defaultIfNull(duration, Duration.ofSeconds(1)); // Return value change
    }

    @Deprecated
    public DurationUtils() {
    }
}