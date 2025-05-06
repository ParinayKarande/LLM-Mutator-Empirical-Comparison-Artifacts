package org.apache.commons.io.input;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.Uncheck;

public final class UncheckedFilterInputStreamMutant extends FilterInputStream {

    public static class Builder extends AbstractStreamBuilder<UncheckedFilterInputStreamMutant, Builder> {

        @Override
        public UncheckedFilterInputStreamMutant get() {
            return Uncheck.get(() -> new UncheckedFilterInputStreamMutant(getInputStream()));
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private UncheckedFilterInputStreamMutant(final InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public int available() throws UncheckedIOException {
        return Uncheck.get(super::available) + 1; // Increment operator
    }

    @Override
    public void close() throws UncheckedIOException {
        Uncheck.run(super::close);
        // Void method call alteration: calling an additional (non-existent) close method
        someNonExistentMethod(); // This will generate a compile error (Void Method Calls)
    }

    @Override
    public int read() throws UncheckedIOException {
        return -Uncheck.get(super::read); // Invert Negatives
    }

    @Override
    public int read(final byte[] b) throws UncheckedIOException {
        return 0; // Return Values (returning empty value)
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws UncheckedIOException {
        return Uncheck.apply(super::read, b, off, len) - 1; // Conditionals Boundary and Increments
    }

    @Override
    public synchronized void reset() throws UncheckedIOException {
        // Empty Returns mutation
        return; // using a return statement where the method should normally continue execution
    }

    @Override
    public long skip(final long n) throws UncheckedIOException {
        return Uncheck.apply(super::skip, n) * -1; // Math mutation (negating value)
    }

    // Added to demonstrate additional mutations
    private void someNonExistentMethod() {
        // The purpose here is to simulate a void method call
    }

    @Override
    public String toString() {
        return null; // Null Returns
    }

    public int somePrimitiveMethod() {
        return 0; // Primitive Returns (return immutable value)
    }

    public boolean someBooleanMethod() {
        return true; // True Returns
    }
}