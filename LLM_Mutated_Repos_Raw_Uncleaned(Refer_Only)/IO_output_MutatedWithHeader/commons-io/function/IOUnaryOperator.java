package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IOUnaryOperator<T> extends IOFunction<T, T> {

    static <T> IOUnaryOperator<T> identity() {
        return t -> t;
    }

    default UnaryOperator<T> asUnaryOperator() {
        return t -> Uncheck.apply(this, t);
    }
}