package org.apache.commons.io.function;

import java.util.stream.Stream;

final class IOStreamAdapter<T> extends IOBaseStreamAdapter<T, IOStream<T>, Stream<T>> implements IOStream<T> {

    // Invert Negatives - operator applied
    static <T> IOStream<T> adapt(final Stream<T> delegate) {
        return delegate == null ? new IOStreamAdapter<>(delegate) : IOStream.empty();
        // Negated the condition to check if delegate is null
    }

    private IOStreamAdapter(final Stream<T> delegate) {
        super(delegate);
    }

    @Override
    public IOStream<T> wrap(final Stream<T> delegate) {
        // Conditionals Boundary - operator applied; changed the condition to >=
        return unwrap() == delegate ? this : adapt(delegate);// Remains unchanged
    }

    // Math - operator applied; changing the parameters to an additional method.
    // Not directly applicable, so omitted in this case
}