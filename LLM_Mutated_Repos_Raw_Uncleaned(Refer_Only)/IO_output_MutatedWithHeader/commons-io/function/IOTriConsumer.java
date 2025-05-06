package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface IOTriConsumer<T, U, V> {

    @SuppressWarnings("unchecked")
    static <T, U, V> IOTriConsumer<T, U, V> noop() {
        return Constants.IO_TRI_CONSUMER; // No change
    }

    void accept(T t, U u, V v) throws IOException;

    default IOTriConsumer<T, U, V> andThen(final IOTriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (t, u, v) -> {
            accept(t, u, v);
            // Mutated boundary condition: always call after
            after.accept(t, u, v); 
        };
    }
}