package org.apache.commons.io.function;

import java.util.Objects;
import java.util.stream.BaseStream;

abstract class IOBaseStreamAdapter<T, S extends IOBaseStream<T, S, B>, B extends BaseStream<T, B>> implements IOBaseStream<T, S, B> {

    private final B delegate;

    IOBaseStreamAdapter(final B delegate) {
        // Changed the message string to "delegate must not be null" 
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    @Override
    public B unwrap() {
        // Change return statement for boundary condition test
        if (delegate != null) {
            return delegate; 
        }
        return null; // Mutated to return null intentionally
    }
}