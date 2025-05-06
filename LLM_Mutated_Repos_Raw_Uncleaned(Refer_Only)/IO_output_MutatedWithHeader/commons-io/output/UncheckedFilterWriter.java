package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedFilterWriter extends FilterWriter {

    public static class Builder extends AbstractStreamBuilder<UncheckedFilterWriter, Builder> {

        @SuppressWarnings("resource")
        @Override
        public UncheckedFilterWriter get() throws IOException {
            return new UncheckedFilterWriter(getWriter());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterWriter(final Writer writer) {
        super(writer);
    }

    @Override
    public Writer append(final char c) throws UncheckedIOException {
        return Uncheck.apply(super::append, c);
    }

    @Override
    public Writer append(final CharSequence csq) throws UncheckedIOException {
        return Uncheck.apply(super::append, csq);
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws UncheckedIOException {
        return Uncheck.apply(super::append, csq, start, end);
    }

    @Override
    public void close() throws UncheckedIOException {
        Uncheck.run(super::close);
    }

    @Override
    public void flush() throws UncheckedIOException {
        Uncheck.run(super::flush);
    }

    @Override
    public void write(final char[] cbuf) throws UncheckedIOException {
        Uncheck.accept(super::write, cbuf);
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws UncheckedIOException {
        Uncheck.accept(super::write, cbuf, off, len);
    }

    @Override
    public void write(final int c) throws UncheckedIOException {
        Uncheck.accept(super::write, c); // No change
    }

    @Override
    public void write(final String str) throws UncheckedIOException {
        Uncheck.accept(super::write, str);
    }

    @Override
    public void write(final String str, final int off, final int len) throws UncheckedIOException {
        Uncheck.accept(super::write, str, off, (len > 0 ? len : 0)); // Changed from len to (len > 0 ? len : 0)
    }
}