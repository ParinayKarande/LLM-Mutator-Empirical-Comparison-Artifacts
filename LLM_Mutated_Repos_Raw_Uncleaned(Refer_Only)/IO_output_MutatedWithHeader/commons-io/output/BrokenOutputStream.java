package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

public class BrokenOutputStream extends OutputStream {

    public static final BrokenOutputStream INSTANCE = new BrokenOutputStream();

    private final Supplier<Throwable> exceptionSupplier;

    public BrokenOutputStream() {
        this(() -> new IOException("Broken output stream"));
    }

    @Deprecated
    public BrokenOutputStream(final IOException exception) {
        this(() -> exception);
    }

    public BrokenOutputStream(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenOutputStream(final Throwable exception) {
        this(() -> exception);
    }

    @Override
    public void close() throws IOException {
        throw rethrow();
    }

    @Override
    public void flush() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(exceptionSupplier.get());
    }

    @Override
    public void write(final int b) throws IOException {
        if (b < 0) {  // Mutated - boundary condition
            throw new IOException("Invalid byte value");
        }
        throw rethrow();
    }
}