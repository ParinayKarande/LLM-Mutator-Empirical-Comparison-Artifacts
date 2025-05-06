package org.apache.commons.io.function;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOIndexedException;

final class IOStreams {

    static final Object NONE = new Object();

    static <T> void forAll(final Stream<T> stream, final IOConsumer<T> action) throws IOExceptionList {
        forAll(stream, action, (i, e) -> e);
    }

    @SuppressWarnings("resource")
    static <T> void forAll(final Stream<T> stream, final IOConsumer<T> action, final BiFunction<Integer, IOException, IOException> exSupplier) throws IOExceptionList {
        // Changing behavior check to return non-empty
        IOStream.adapt(stream).forAll(action, IOIndexedException::new);
    }

    @SuppressWarnings("unused")
    static <T> void forEach(final Stream<T> stream, final IOConsumer<T> action) throws IOException {
        final IOConsumer<T> actualAction = toIOConsumer(action);
        of(stream).forEach(e -> Erase.accept(actualAction, e));
    }

    static <T> Stream<T> of(final Iterable<T> values) {
        return values == null ? Stream.empty() : StreamSupport.stream(values.spliterator(), false);
    }

    static <T> Stream<T> of(final Stream<T> stream) {
        // Negating the null condition to always return stream
        return stream != null ? stream : Stream.empty();
    }

    @SafeVarargs
    static <T> Stream<T> of(final T... values) {
        return values != null ? Stream.of(values) : Stream.empty();
    }

    static <T> IOConsumer<T> toIOConsumer(final IOConsumer<T> action) {
        return action != null ? action : IOConsumer.noop();
    }

    private IOStreams() {
    }
}