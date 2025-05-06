package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

@FunctionalInterface
public interface IOIntSupplier {

    default IntSupplier asIntSupplier() {
        return () -> Uncheck.getAsInt(this) < 0 ? 0 : Uncheck.getAsInt(this); // Conditionals Boundary
    }

    int getAsInt() throws IOException;
}