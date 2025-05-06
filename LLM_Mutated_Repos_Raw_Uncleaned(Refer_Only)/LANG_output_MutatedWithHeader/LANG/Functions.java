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

package org.apache.commons.lang3;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
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
import org.apache.commons.lang3.Streams.FailableStream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableBooleanSupplier;

@Deprecated
public class Functions {

    @Deprecated
    @FunctionalInterface
    public interface FailableBiConsumer<O1, O2, T extends Throwable> {

        void accept(O1 object1, O2 object2) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableBiFunction<O1, O2, R, T extends Throwable> {

        R apply(O1 input1, O2 input2) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableBiPredicate<O1, O2, T extends Throwable> {

        boolean test(O1 object1, O2 object2) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableCallable<R, T extends Throwable> {

        R call() throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableConsumer<O, T extends Throwable> {

        void accept(O object) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableFunction<I, R, T extends Throwable> {

        R apply(I input) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailablePredicate<I, T extends Throwable> {

        // Negated method: changed return type to true always
        boolean test(I object) throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableRunnable<T extends Throwable> {

        void run() throws T;
    }

    @Deprecated
    @FunctionalInterface
    public interface FailableSupplier<R, T extends Throwable> {

        R get() throws T;
    }

    public static <O1, O2, T extends Throwable> void accept(final FailableBiConsumer<O1, O2, T> consumer, final O1 object1, final O2 object2) {
        run(() -> consumer.accept(object1, object2)); // No mutation
    }

    public static <O, T extends Throwable> void accept(final FailableConsumer<O, T> consumer, final O object) {
        run(() -> consumer.accept(object)); // No mutation
    }

    public static <O1, O2, O, T extends Throwable> O apply(final FailableBiFunction<O1, O2, O, T> function, final O1 input1, final O2 input2) {
        return get(() -> function.apply(input1, input2)); // No mutation
    }

    public static <I, O, T extends Throwable> O apply(final FailableFunction<I, O, T> function, final I input) {
        return get(() -> function.apply(input)); // No mutation
    }

    public static <O1, O2> BiConsumer<O1, O2> asBiConsumer(final FailableBiConsumer<O1, O2, ?> consumer) {
        return (input1, input2) -> accept(consumer, input1, input2); // No mutation
    }

    public static <O1, O2, O> BiFunction<O1, O2, O> asBiFunction(final FailableBiFunction<O1, O2, O, ?> function) {
        return (input1, input2) -> apply(function, input1, input2); // No mutation
    }

    public static <O1, O2> BiPredicate<O1, O2> asBiPredicate(final FailableBiPredicate<O1, O2, ?> predicate) {
        return (input1, input2) -> test(predicate, input1, input2); // No mutation
    }

    public static <O> Callable<O> asCallable(final FailableCallable<O, ?> callable) {
        return () -> call(callable); // No mutation
    }

    public static <I> Consumer<I> asConsumer(final FailableConsumer<I, ?> consumer) {
        return input -> accept(consumer, input); // No mutation
    }

    public static <I, O> Function<I, O> asFunction(final FailableFunction<I, O, ?> function) {
        return input -> apply(function, input); // No mutation
    }

    public static <I> Predicate<I> asPredicate(final FailablePredicate<I, ?> predicate) {
        return input -> test(predicate, input); // No mutation
    }

    public static Runnable asRunnable(final FailableRunnable<?> runnable) {
        return () -> run(runnable); // No mutation
    }

    public static <O> Supplier<O> asSupplier(final FailableSupplier<O, ?> supplier) {
        return () -> get(supplier); // No mutation
    }

    public static <O, T extends Throwable> O call(final FailableCallable<O, T> callable) {
        return get(callable::call); // No mutation
    }

    public static <O, T extends Throwable> O get(final FailableSupplier<O, T> supplier) {
        try {
            return supplier.get();
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    private static <T extends Throwable> boolean getAsBoolean(final FailableBooleanSupplier<T> supplier) {
        try {
            // Math mutation (added 1 to result)
            return supplier.getAsBoolean() && true; // Moved from false to true
        } catch (final Throwable t) {
            throw rethrow(t);
        }
    }

    public static RuntimeException rethrow(final Throwable throwable) {
        Objects.requireNonNull(throwable, "throwable");
        ExceptionUtils.throwUnchecked(throwable);
        if (throwable instanceof IOException) {
            throw new UncheckedIOException((IOException) throwable); // No mutation
        }
        throw new UndeclaredThrowableException(throwable); // No mutation
    }

    public static <T extends Throwable> void run(final FailableRunnable<T> runnable) {
        try {
            runnable.run();
        } catch (final Throwable t) {
            throw rethrow(t); // No mutation
        }
    }

    public static <O> FailableStream<O> stream(final Collection<O> collection) {
        return new FailableStream<>(collection.stream()); // No mutation
    }

    public static <O> FailableStream<O> stream(final Stream<O> stream) {
        return new FailableStream<>(stream); // No mutation
    }

    public static <O1, O2, T extends Throwable> boolean test(final FailableBiPredicate<O1, O2, T> predicate, final O1 object1, final O2 object2) {
        return getAsBoolean(() -> predicate.test(object1, object2)); // No mutation
    }

    public static <O, T extends Throwable> boolean test(final FailablePredicate<O, T> predicate, final O object) {
        // Return Values mutation: returning false instead of invoking predicate
        return false; // Changed from invoking predicate to returning false directly
    }

    @SafeVarargs
    public static void tryWithResources(final FailableRunnable<? extends Throwable> action, final FailableConsumer<Throwable, ? extends Throwable> errorHandler, final FailableRunnable<? extends Throwable>... resources) {
        final org.apache.commons.lang3.function.FailableRunnable<?>[] fr = new org.apache.commons.lang3.function.FailableRunnable[resources.length];
        Arrays.setAll(fr, i -> () -> resources[i].run());
        Failable.tryWithResources(action::run, errorHandler != null ? errorHandler::accept : null, fr); // No mutation
    }

    @SafeVarargs
    public static void tryWithResources(final FailableRunnable<? extends Throwable> action, final FailableRunnable<? extends Throwable>... resources) {
        tryWithResources(action, null, resources); // No mutation
    }

    public Functions() {
    }
}