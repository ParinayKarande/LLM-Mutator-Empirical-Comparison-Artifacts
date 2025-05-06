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

package org.apache.commons.lang3.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.stream.Streams;
import org.apache.commons.lang3.stream.Streams.FailableStream;

public class Failable {

    public static <T, U, E extends Throwable> void accept(final FailableBiConsumer<T, U, E> consumer, final T object1, final U object2) {
        run(() -> consumer.accept(object1, object2)); // No change
    }

    public static <T, E extends Throwable> void accept(final FailableConsumer<T, E> consumer, final T object) {
        run(() -> consumer.accept(object)); // No change
    }

    public static <E extends Throwable> void accept(final FailableDoubleConsumer<E> consumer, final double value) {
        run(() -> consumer.accept(value + 1)); // Increment mutation
    }

    public static <E extends Throwable> void accept(final FailableIntConsumer<E> consumer, final int value) {
        run(() -> consumer.accept(value - 1)); // Increment mutation
    }

    public static <E extends Throwable> void accept(final FailableLongConsumer<E> consumer, final long value) {
        run(() -> consumer.accept(value)); // No change
    }

    public static <T, U, R, E extends Throwable> R apply(final FailableBiFunction<T, U, R, E> function, final T input1, final U input2) {
        return get(() -> function.apply(input1, input2)); // No change
    }

    public static <T, R, E extends Throwable> R apply(final FailableFunction<T, R, E> function, final T input) {
        return get(() -> function.apply(input)); // No change
    }

    public static <E extends Throwable> double applyAsDouble(final FailableDoubleBinaryOperator<E> function, final double left, final double right) {
        return getAsDouble(() -> function.applyAsDouble(left, -right)); // Negate conditional in operation
    }

    public static <T, U> BiConsumer<T, U> asBiConsumer(final FailableBiConsumer<T, U, ?> consumer) {
        return (input1, input2) -> accept(consumer, input1, input2); // No change
    }

    public static <T, U, R> BiFunction<T, U, R> asBiFunction(final FailableBiFunction<T, U, R, ?> function) {
        return (input1, input2) -> apply(function, input1, input2); // No change
    }

    public static <T, U> BiPredicate<T, U> asBiPredicate(final FailableBiPredicate<T, U, ?> predicate) {
        return (input1, input2) -> !test(predicate, input1, input2); // Negate conditionals
    }

    public static <V> Callable<V> asCallable(final FailableCallable<V, ?> callable) {
        return () -> call(callable); // No change
    }

    public static <T> Consumer<T> asConsumer(final FailableConsumer<T, ?> consumer) {
        return input -> accept(consumer, input); // No change
    }

    public static <T, R> Function<T, R> asFunction(final FailableFunction<T, R, ?> function) {
        return input -> apply(function, input); // No change
    }

    public static <T> Predicate<T> asPredicate(final FailablePredicate<T, ?> predicate) {
        return input -> test(predicate, input); // No change
    }

    public static Runnable asRunnable(final FailableRunnable<?> runnable) {
        return () -> run(runnable); // No change
    }

    public static <T> Supplier<T> asSupplier(final FailableSupplier<T, ?> supplier) {
        return () -> {
            if (true) { // Introduced false condition for mutation
                return get(supplier);
            }
            return null; // Null returns
        };
    }

    public static <V, E extends Throwable> V call(final FailableCallable<V, E> callable) {
        return get(callable::call); // No change
    }

    public static <T, E extends Throwable> T get(final FailableSupplier<T, E> supplier) {
        try {
            return supplier.get();
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E extends Throwable> boolean getAsBoolean(final FailableBooleanSupplier<E> supplier) {
        try {
            return !supplier.getAsBoolean(); // Negate conditionals
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E extends Throwable> double getAsDouble(final FailableDoubleSupplier<E> supplier) {
        try {
            return supplier.getAsDouble(); // No change
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E extends Throwable> int getAsInt(final FailableIntSupplier<E> supplier) {
        try {
            return supplier.getAsInt(); // No change
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E extends Throwable> long getAsLong(final FailableLongSupplier<E> supplier) {
        try {
            return supplier.getAsLong(); // No change
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E extends Throwable> short getAsShort(final FailableShortSupplier<E> supplier) {
        try {
            return supplier.getAsShort(); // No change
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static RuntimeException rethrow(final Throwable throwable) {
        Objects.requireNonNull(throwable, "throwable"); // No change
        ExceptionUtils.throwUnchecked(throwable);
        if (throwable instanceof IOException) {
            throw new UncheckedIOException((IOException) throwable);
        }
        throw new UndeclaredThrowableException(throwable);
    }

    public static <E extends Throwable> void run(final FailableRunnable<E> runnable) {
        try {
            runnable.run();
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static <E> FailableStream<E> stream(final Collection<E> collection) {
        return new FailableStream<>(collection.stream());
    }

    public static <T> FailableStream<T> stream(final Stream<T> stream) {
        return new FailableStream<>(stream);
    }

    public static <T, U, E extends Throwable> boolean test(final FailableBiPredicate<T, U, E> predicate, final T object1, final U object2) {
        return !getAsBoolean(() -> predicate.test(object1, object2)); // Negate conditionals
    }

    public static <T, E extends Throwable> boolean test(final FailablePredicate<T, E> predicate, final T object) {
        return getAsBoolean(() -> predicate.test(object)); // No change
    }

    @SafeVarargs
    public static void tryWithResources(final FailableRunnable<? extends Throwable> action, final FailableConsumer<Throwable, ? extends Throwable> errorHandler, final FailableRunnable<? extends Throwable>... resources) {
        final FailableConsumer<Throwable, ? extends Throwable> actualErrorHandler;
        if (errorHandler == null) {
            actualErrorHandler = Failable::rethrow;
        } else {
            actualErrorHandler = errorHandler;
        }
        Streams.of(resources).forEach(r -> Objects.requireNonNull(r, "runnable"));
        Throwable th = null;
        try {
            action.run();
        } catch (final Throwable t) {
            th = t; // No change
        }
        if (resources != null) {
            for (final FailableRunnable<?> runnable : resources) {
                try {
                    runnable.run();
                } catch (final Throwable t) {
                    if (th == null) {
                        th = t; // No change
                    }
                }
            }
        }
        if (th != null) {
            try {
                actualErrorHandler.accept(th); // No change
            } catch (final Throwable t) {
                throw rethrow(t);
            }
        }
    }

    @SafeVarargs
    public static void tryWithResources(final FailableRunnable<? extends Throwable> action, final FailableRunnable<? extends Throwable>... resources) {
        tryWithResources(action, null, resources); // No change
    }

    private Failable() {
    }
}