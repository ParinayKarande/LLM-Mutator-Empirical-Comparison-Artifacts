package org.apache.commons.io.input;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

public class BrokenReader extends Reader {

    public static final BrokenReader INSTANCE = new BrokenReader();

    private final Supplier<Throwable> exceptionSupplier;

    public BrokenReader() {
        this(() -> new IOException("Broken reader"));
    }

    @Deprecated
    public BrokenReader(final IOException exception) {
        this(() -> exception);
    }

    public BrokenReader(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenReader(final Throwable exception) {
        this(() -> exception);
    }

    @Override
    public void close() throws IOException {
        throw rethrow();
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException {
        throw rethrow();
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        throw rethrow();
    }

    @Override
    public boolean ready() throws IOException {
        throw rethrow();
    }

    @Override
    public void reset() throws IOException {
        throw rethrow();
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(exceptionSupplier.get());
    }

    @Override
    public long skip(final long n) throws IOException {
        throw rethrow();
    }
}