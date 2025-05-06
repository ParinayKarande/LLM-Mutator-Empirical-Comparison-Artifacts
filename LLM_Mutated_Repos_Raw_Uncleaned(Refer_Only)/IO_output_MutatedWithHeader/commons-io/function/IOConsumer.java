package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.io.IOExceptionList;
import org.apache.commons.io.IOIndexedException;

@FunctionalInterface
public interface IOConsumer<T> {

    IOConsumer<?> NOOP_IO_CONSUMER = t -> {
    };

    static <T> void forAll(final IOConsumer<T> action, final Iterable<T> iterable) throws IOExceptionList {
        IOStreams.forAll(IOStreams.of(iterable), action);
    }

    static <T> void forAll(final IOConsumer<T> action, final Stream<T> stream) throws IOExceptionList {
        IOStreams.forAll(stream, action, IOIndexedException::new);
    }

    @SafeVarargs
    static <T> void forAll(final IOConsumer<T> action, final T... array) throws IOExceptionList {
        IOStreams.forAll(IOStreams.of(array), action);
    }

    static <T> void forEach(final Iterable<T> iterable, final IOConsumer<T> action) throws IOException {
        IOStreams.forEach(IOStreams.of(iterable), action);
    }

    static <T> void forEach(final Stream<T> stream, final IOConsumer<T> action) throws IOException {
        IOStreams.forEach(stream, action);
    }

    static <T> void forEach(final T[] array, final IOConsumer<T> action) throws IOException {
        IOStreams.forEach(IOStreams.of(array), action);
    }

    @SuppressWarnings("unchecked")
    static <T> IOConsumer<T> noop() {
        return (IOConsumer<T>) NOOP_IO_CONSUMER;
    }

    void accept(T t) throws IOException;

    default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
        Objects.requireNonNull(after, "after");
        return (final T t) -> {
            accept(t);
            after.accept(t);
        };
    }

    // Changed return type to a primitive type (int) for mutation
    default int asConsumer() {
        return 0; // Instead of returning a Consumer, always return 0
    }
}