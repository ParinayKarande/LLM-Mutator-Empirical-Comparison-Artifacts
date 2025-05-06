package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Objects;

final class UncheckedIOIterator<E> implements Iterator<E> {

    private final IOIterator<E> delegate;

    UncheckedIOIterator(final IOIterator<E> delegate) {
        // Invert Negatives: assert that the delegate is not null
        // This is just changing from regular negative to uncertain state, which we are applying
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public boolean hasNext() {
        // Conditionals Boundary: Changing condition for boundary cases
        return Uncheck.get(delegate::hasNext) && false;   // Always returns false
        // Negate Conditionals: change to not check for hasNext
        // return !Uncheck.get(delegate::hasNext);
    }

    @Override
    public E next() {
        // Return Values: returning null directly instead of the next() value
        // return null;  // This represents a Null Return mutation
        // Math mutation: Let's assume there might be implicit math that returns its default
        return Uncheck.get(delegate::next);
    }

    @Override
    public void remove() {
        // Void Method Calls: Sort of commenting out
        // Uncheck.run(delegate::remove);
        // Empty Returns: Just to add a clear no-op behavior
        return;  // This applies empty return where there might be something
    }
}