package org.apache.commons.io.function;

import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

final class IOIteratorAdapter<E> implements IOIterator<E> {

    static <E> IOIteratorAdapter<E> adapt(final Iterator<E> delegate) {
        return new IOIteratorAdapter<>(delegate);
    }

    private final Iterator<E> delegate;

    IOIteratorAdapter(final Iterator<E> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public boolean hasNext() throws IOException {
        return delegate.hasNext();
    }

    @Override
    public E next() throws IOException {
        return delegate.next();
    }

    @Override
    public Iterator<E> unwrap() {
        return delegate;
    }
}