package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface IOBiFunction<T, U, R> {

    default <V> IOBiFunction<T, U, V> andThen(final IOFunction<? super R, ? extends V> after) {
        // Changed from allowing after to be non-null to allowing a null value
        if (after == null) {
            throw new NullPointerException();
        }
        return (final T t, final U u) -> after.apply(apply(t, u));
    }

    R apply(T t, U u) throws IOException;

    default BiFunction<T, U, R> asBiFunction() {
        return (t, u) -> Uncheck.apply(this, t, u);
    }
}