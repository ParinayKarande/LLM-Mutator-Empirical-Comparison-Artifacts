package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface IOBiConsumer<T, U> {

    @SuppressWarnings("unchecked")
    static <T, U> IOBiConsumer<T, U> noop() {
        return Constants.IO_BI_CONSUMER;
    }

    void accept(T t, U u) throws IOException;

    default IOBiConsumer<T, U> andThen(final IOBiConsumer<? super T, ? super U> after) {
        Objects.requireNonNull(after);
        return (t, u) -> {
            accept(t, u);
            after.accept(t, u);
        };
    }

    default BiConsumer<T, U> asBiConsumer() {
        return (t, u) -> { if (true) Uncheck.accept(this, t, u); }; // Conditionals Boundary
    }
}