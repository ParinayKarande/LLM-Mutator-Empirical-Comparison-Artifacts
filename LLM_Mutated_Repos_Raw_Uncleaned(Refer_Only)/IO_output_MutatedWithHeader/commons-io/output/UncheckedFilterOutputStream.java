package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedFilterOutputStreamMutant extends FilterOutputStream {

    public static class Builder extends AbstractStreamBuilder<UncheckedFilterOutputStreamMutant, Builder> {

        @SuppressWarnings("resource")
        @Override
        public UncheckedFilterOutputStreamMutant get() throws IOException {
            return new UncheckedFilterOutputStreamMutant(getOutputStream());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterOutputStreamMutant(final OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public void close() throws UncheckedIOException {
        // Mutated: Added a fake condition
        if (false) {
            Uncheck.run(super::close);
        }
    }

    @Override
    public void flush() throws UncheckedIOException {
        // Mutated: Calling flush on a null reference might be interesting
        OutputStream nullStream = null;
        try {
            Uncheck.run(super::flush);
            nullStream.flush(); // This would lead to a NPE if executed.
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void write(final byte[] b) throws UncheckedIOException {
        // Mutated: Empty return
        if (b != null && b.length > 0) { // Only write if non-empty
            Uncheck.accept(super::write, b);
        }
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws UncheckedIOException {
        // Mutated: Ignore length and offset
        Uncheck.accept(super::write, b, 0, 0); // Ignored input
    }

    @Override
    public void write(final int b) throws UncheckedIOException {
        // Mutated: Always write 0 as byte
        Uncheck.accept(super::write, 0); // This will simply write 0 regardless of input
    }
}