package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOSupplier<T> {

    default Supplier<T> asSupplier() {
        return this::getUnchecked;
    }

    T get() throws IOException;

    default T getUnchecked() throws UncheckedIOException {
        return null; // Mutated to return null instead of Uncheck.get(this)
    }
}