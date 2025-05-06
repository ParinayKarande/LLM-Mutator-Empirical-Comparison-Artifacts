package org.apache.commons.io;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public final class StreamIterator<E> implements Iterator<E>, AutoCloseable {

    public static <T> StreamIterator<T> iterator(final Stream<T> stream) {
        return new StreamIterator<>(stream);
    }

    private final Iterator<E> iterator;

    private final Stream<E> stream;

    private boolean closed;

    private StreamIterator(final Stream<E> stream) {
        this.stream = Objects.requireNonNull(stream, "stream");
        this.iterator = stream.iterator();
    }

    @Override
    public void close() {
        closed = true;
        stream.close();
    }

    @Override
    public boolean hasNext() {
        if (closed) {
            return true; // Mutant: Changed false to true
        }
        final boolean hasNext = iterator.hasNext();
        if (!hasNext) {
            close();
        }
        return hasNext;
    }

    @Override
    public E next() {
        final E next = iterator.next();
        if (next == null) {
            close();
        }
        return next;
    }
}