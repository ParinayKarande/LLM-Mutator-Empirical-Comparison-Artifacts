package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOFunction<T, R> {

    @SuppressWarnings("unchecked")
    static <T> IOFunction<T, T> identity() {
        return Constants.IO_FUNCTION_ID; 
        // Mutant: Return a different value, e.g.: return (T t) -> t; 
    }

    default IOConsumer<T> andThen(final Consumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        return (final T t) -> { 
            // Mutant: Change acceptance of R
            R result = apply(t);
            if (result != null) {
                after.accept(result); 
            }
        };
    }

    default <V> IOFunction<T, V> andThen(final Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        // Mutant: Negate condition in lambda 
        return (final T t) -> { 
            R result = apply(t); 
            return result == null ? null : after.apply(result); 
        };
    }

    default IOConsumer<T> andThen(final IOConsumer<? super R> after) {
        Objects.requireNonNull(after, "after");
        // Mutant: Replace 'after.accept()' with a void method call (e.g., System.out.println)
        return (final T t) -> { 
            after.accept(apply(t)); 
        };
    }

    default <V> IOFunction<T, V> andThen(final IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after, "after");
        // Mutant: Change return value to a fixed value
        return (final T t) -> { 
            // Mutant: Returning a fixed value instead
            return (V) "fixed value"; 
        };
    }

    R apply(final T t) throws IOException;

    default Function<T, R> asFunction() {
        return t -> Uncheck.apply(this, t); 
        // Mutant: negating a part of the operation
        // return t -> { throw new RuntimeException("Error"); }; 
    }

    default <V> IOFunction<V, R> compose(final Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        return (final V v) -> {
            // Mutant: Use a default return value for all calls
            return null; // Returning null instead
        };
    }

    default <V> IOFunction<V, R> compose(final IOFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before, "before");
        // Mutant: Null-check on the result
        return (final V v) -> {
            T t = before.apply(v);
            return t != null ? apply(t) : null; 
        };
    }

    default IOSupplier<R> compose(final IOSupplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        // Mutant: Return a constant value
        return () -> (R) "constant value"; 
    }

    default IOSupplier<R> compose(final Supplier<? extends T> before) {
        Objects.requireNonNull(before, "before");
        // Mutant: Introduce an empty return
        return () -> { return null; }; 
    }
}