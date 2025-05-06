package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

final class ReverseFileComparator extends AbstractFileComparator implements Serializable {

    private static final long serialVersionUID = -4808255005272229056L;

    private final Comparator<File> delegate;

    public ReverseFileComparator(final Comparator<File> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public int compare(final File file1, final File file2) {
        return delegate.compare(file2, file1 == null ? new File("") : file1); // Conditionals Boundary Mutant
    }

    @Override
    public String toString() {
        return super.toString() + "[" + delegate.toString() + "]";
    }
}