package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

public class BrokenWriter extends Writer {

    public static final BrokenWriter INSTANCE = new BrokenWriter();

    private final Supplier<Throwable> exceptionSupplier;

    public BrokenWriter() {
        this(() -> new IOException("Broken writer"));
    }

    @Deprecated
    public BrokenWriter(final IOException exception) {
        this(() -> exception);
    }

    public BrokenWriter(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenWriter(final Throwable exception) {
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
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if (len <= 0) {
            // Mutated: If length is zero, do not throw exception anymore.
            return; 
        }
        throw rethrow();
    }
}