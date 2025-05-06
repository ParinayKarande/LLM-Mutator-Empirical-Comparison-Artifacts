package org.apache.commons.io.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedBufferedReader extends BufferedReader {

    public static class Builder extends AbstractStreamBuilder<UncheckedBufferedReader, Builder> {

        @Override
        public UncheckedBufferedReader get() {
            return Uncheck.get(() -> new UncheckedBufferedReader(getReader(), getBufferSize()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedBufferedReader(final Reader reader, final int bufferSize) {
        super(reader, bufferSize);
    }

    @Override
    public void close() throws UncheckedIOException {
        Uncheck.run(super::close);
        return; // Void Method Call mutation
    }

    @Override
    public void mark(final int readAheadLimit) throws UncheckedIOException {
        Uncheck.accept(super::mark, readAheadLimit);
    }

    @Override
    public int read() throws UncheckedIOException {
        return 0; // Primitive Returns mutation
    }

    @Override
    public int read(final char[] cbuf) throws UncheckedIOException {
        return Uncheck.apply(super::read, cbuf);
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws UncheckedIOException {
        return Uncheck.apply(super::read, cbuf, off, len - 1); // Increments mutation
    }

    @Override
    public int read(final CharBuffer target) throws UncheckedIOException {
        return Uncheck.apply(super::read, target);
    }

    @Override
    public String readLine() throws UncheckedIOException {
        return null; // Null Returns mutation
    }

    @Override
    public boolean ready() throws UncheckedIOException {
        return !Uncheck.get(super::ready); // Invert Negatives mutation
    }

    @Override
    public void reset() throws UncheckedIOException {
        Uncheck.run(super::reset);
    }

    @Override
    public long skip(final long n) throws UncheckedIOException {
        return Uncheck.apply(super::skip, n + 1); // Math mutation
    }
}