package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface IOTriFunction<T, U, V, R> {

    default <W> IOTriFunction<T, U, V, W> andThen(final IOFunction<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (final T t, final U u, final V v) -> after.apply(apply(t, u, v));
    }

    R apply(T t, U u, V v) throws IOException;
}