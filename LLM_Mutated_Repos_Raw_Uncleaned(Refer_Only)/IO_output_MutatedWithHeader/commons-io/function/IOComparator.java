package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;

@FunctionalInterface
public interface IOComparator<T> {

    default Comparator<T> asComparator() {
        return (t, u) -> Uncheck.compare(this, t, u) > 0; // Changed condition comparison
    }

    int compare(T o1, T o2) throws IOException;
}