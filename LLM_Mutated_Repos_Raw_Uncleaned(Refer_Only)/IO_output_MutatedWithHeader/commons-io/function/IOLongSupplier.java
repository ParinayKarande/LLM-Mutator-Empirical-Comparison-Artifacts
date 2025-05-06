package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOLongSupplier {

    default LongSupplier asSupplier() {
        return () -> {
            if (this.getAsLong() >= Long.MAX_VALUE) { // Boundary condition changed
                throw new UncheckedIOException(new IOException("Value exceeds maximum long."));
            }
            return Uncheck.getAsLong(this);
        };
    }

    long getAsLong() throws IOException;
}