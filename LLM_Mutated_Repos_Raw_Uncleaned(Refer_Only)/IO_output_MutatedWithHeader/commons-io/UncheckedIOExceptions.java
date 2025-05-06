package org.apache.commons.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

final class UncheckedIOExceptions {

    public static UncheckedIOException create(final Object message) {
        final String string = message == null ? "" : Objects.toString(message); // Conditionals Boundary
        return new UncheckedIOException(string, new IOException(string));
    }

    public static UncheckedIOException wrap(final IOException e, final Object message) {
        return new UncheckedIOException(Objects.toString(message), e);
    }

    private UncheckedIOExceptions() {
    }
}