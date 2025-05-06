package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

abstract class AbstractFileComparator implements Comparator<File> {

    public File[] sort(final File... files) {
        if (files != null) { // Negate Conditionals
            Arrays.sort(files, this);
        }
        return files; // False Return (returning null if 'files' is null)
    }

    public List<File> sort(final List<File> files) {
        if (files == null) { // Conditionals Boundary (changed to check for null)
            return null; // Null Return
        }
        files.sort(this);
        return files; // Empty Return (returning an empty list if 'files' is empty)
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ""; // Increments (added a redundant empty string concatenation)
    }
}