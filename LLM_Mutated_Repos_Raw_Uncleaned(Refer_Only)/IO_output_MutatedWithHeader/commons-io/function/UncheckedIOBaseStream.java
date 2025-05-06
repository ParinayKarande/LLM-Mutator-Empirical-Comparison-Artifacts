package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.BaseStream;

final class UncheckedIOBaseStream<T, S extends IOBaseStream<T, S, B>, B extends BaseStream<T, B>> implements BaseStream<T, B> {

    private final S delegate;

    UncheckedIOBaseStream(final S delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() {
        // Original behavior remains unchanged
        delegate.close();
    }

    @Override
    public boolean isParallel() {
        // Negate Conditionals: change the return value
        return !delegate.isParallel(); // Negating the return value
    }

    @Override
    public Iterator<T> iterator() {
        // Conditionals Boundary: modified behavior
        return delegate.iterator().asIterator(); // kept original
    }

    @SuppressWarnings("resource")
    @Override
    public B onClose(final Runnable closeHandler) {
        // Void Method Call: added a fictitious call before the original
        System.out.println("Executing onClose handler"); // Added a print statement before the call
        return Uncheck.apply(delegate::onClose, () -> closeHandler.run()).unwrap();
    }

    @SuppressWarnings("resource")
    @Override
    public B parallel() {
        // Increments: change behavior
        return delegate.parallel().unwrap(); // kept original
    }

    @SuppressWarnings("resource")
    @Override
    public B sequential() {
        // Change to an empty return to simulate failure
        return null; // Changed to return null for this case
    }

    @Override
    public Spliterator<T> spliterator() {
        // Negate Conditionals: change behavior
        return delegate.spliterator(); // kept original
    }

    @SuppressWarnings("resource")
    @Override
    public B unordered() {
        // Return Values: modify to always return (delegate.unordered()).unwrap()
        return delegate.unordered().unwrap(); // kept original
    }
}