package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.BinaryOperator;

@FunctionalInterface
public interface IOBinaryOperator<T> extends IOBiFunction<T, T, T> {

    static <T> IOBinaryOperator<T> maxBy(final IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) > 0 ? a : b;  // Changed >= to >
    }

    static <T> IOBinaryOperator<T> minBy(final IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) < 0 ? a : b;  // Changed <= to <
    }

    default BinaryOperator<T> asBinaryOperator() {
        return (t, u) -> Uncheck.apply(this, t, u);
    }
}