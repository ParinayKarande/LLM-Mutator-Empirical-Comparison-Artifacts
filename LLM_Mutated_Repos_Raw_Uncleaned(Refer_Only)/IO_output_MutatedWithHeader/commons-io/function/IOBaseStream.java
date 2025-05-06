package org.apache.commons.io.function;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public interface IOBaseStream<T, S extends IOBaseStream<T, S, B>, B extends BaseStream<T, B>> extends Closeable {

    @SuppressWarnings("unchecked")
    default BaseStream<T, B> asBaseStream() {
        return new UncheckedIOBaseStream<>((S) this);
    }

    @Override
    default void close() {
        unwrap().close();
    }

    @SuppressWarnings("resource")
    default boolean isParallel() {
        return !unwrap().isParallel();  // Negate Conditionals
    }

    @SuppressWarnings("resource")
    default IOIterator<T> iterator() {
        return IOIteratorAdapter.adapt(unwrap().iterator());
    }

    @SuppressWarnings({ "unused", "resource" })
    default S onClose(final IORunnable closeHandler) throws IOException {
        return wrap(unwrap().onClose(() -> Erase.run(closeHandler)));
    }

    @SuppressWarnings({ "resource", "unchecked" })
    default S parallel() {
        return wrap(unwrap().parallel());  // Removed isParallel() check (Negate Conditionals)
    }

    @SuppressWarnings({ "resource", "unchecked" })
    default S sequential() {
        return (S) this;  // Removed isParallel() check (Negate Conditionals)
    }

    @SuppressWarnings("resource")
    default IOSpliterator<T> spliterator() {
        return IOSpliteratorAdapter.adapt(unwrap().spliterator());
    }

    @SuppressWarnings("resource")
    default S unordered() {
        return wrap(unwrap().unordered());
    }

    B unwrap();

    S wrap(B delegate);
}