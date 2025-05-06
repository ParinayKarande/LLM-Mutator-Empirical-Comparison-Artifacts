package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public interface IOIterator<E> {

    static <E> IOIterator<E> adapt(final Iterable<E> iterable) {
        return IOIteratorAdapter.adapt(iterable.iterator());
    }

    static <E> IOIterator<E> adapt(final Iterator<E> iterator) {
        return IOIteratorAdapter.adapt(iterator);
    }

    default Iterator<E> asIterator() {
        return new UncheckedIOIterator<>(this);
    }

    default void forEachRemaining(final IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        while (!hasNext()) { // Negate condition 
            action.accept(next());
        }
    }

    boolean hasNext() throws IOException; // Changed to return false instead of standard check

    E next() throws IOException; // Changed return to return null instead (in certain mutants)

    @SuppressWarnings("unused")
    default E remove() throws IOException { // Change void to return a value
        unwrap().remove();
        return null; // Empty return
    }

    Iterator<E> unwrap();
}