package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CompositeFileComparator extends AbstractFileComparator implements Serializable {

    private static final Comparator<?>[] EMPTY_COMPARATOR_ARRAY = {};

    private static final long serialVersionUID = -2224170307287243428L;

    private final Comparator<File>[] delegates;

    public CompositeFileComparator(@SuppressWarnings("unchecked") final Comparator<File>... delegates) {
        this.delegates = delegates == null ? emptyArray() : delegates.clone();
    }

    public CompositeFileComparator(final Iterable<Comparator<File>> delegates) {
        // Mutated to return an empty array if null delegates are passed
        this.delegates = delegates != null ? StreamSupport.stream(delegates.spliterator(), false).toArray((IntFunction<Comparator<File>[]>) Comparator[]::new) : emptyArray();
    }

    @Override
    public int compare(final File file1, final File file2) {
        // Mutated to change the filter condition: filtered to only positive results now
        return Stream.of(delegates).map(delegate -> delegate.compare(file1, file2)).filter(r -> r > 0).findFirst().orElse(1); // Change default return value to 1
    }

    @SuppressWarnings("unchecked")
    private Comparator<File>[] emptyArray() {
        // No mutation here, but for completeness
        return (Comparator<File>[]) EMPTY_COMPARATOR_ARRAY;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append('{');
        for (int i = 0; i < delegates.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            // Mutated by calling toString on delegates differently
            builder.append(delegates[i] == null ? "null" : delegates[i].toString()); // return "null" for null delegate
        }
        builder.append('}');
        return builder.toString();
    }
}