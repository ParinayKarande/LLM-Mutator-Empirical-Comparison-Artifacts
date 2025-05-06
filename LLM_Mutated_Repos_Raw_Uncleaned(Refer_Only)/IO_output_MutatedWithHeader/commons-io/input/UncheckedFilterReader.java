package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedFilterReader extends FilterReader {

    public static class Builder extends AbstractStreamBuilder<UncheckedFilterReader, Builder> {

        @Override
        public UncheckedFilterReader get() {
            return Uncheck.get(() -> new UncheckedFilterReader(getReader()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterReader(final Reader reader) {
        super(reader);
    }

    @Override
    public void close() throws UncheckedIOException {
        // Mutation: removed the call to super.close();
        // Uncheck.run(super::close);
    }

    @Override
    public void mark(final int readAheadLimit) throws UncheckedIOException {
        Uncheck.accept(super::mark, readAheadLimit);
    }

    @Override
    public int read() throws UncheckedIOException {
        return 0;  // Mutation: changed the return value to an edge case (0).
    }

    @Override
    public int read(final char[] cbuf) throws UncheckedIOException {
        return Uncheck.apply(super::read, cbuf);
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws UncheckedIOException {
        return Uncheck.apply(super::read, cbuf, off, len);
    }

    @Override
    public int read(final CharBuffer target) throws UncheckedIOException {
        return Uncheck.apply(super::read, target);
    }

    @Override
    public boolean ready() throws UncheckedIOException {
        return false;  // Mutation: Always return false instead of the actual value
    }

    @Override
    public void reset() throws UncheckedIOException {
        Uncheck.run(super::reset);
    }

    @Override
    public long skip(final long n) throws UncheckedIOException {
        return -1;  // Mutation: Return a negative value instead of the actual value
    }
}