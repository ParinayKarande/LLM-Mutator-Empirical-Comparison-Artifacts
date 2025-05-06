package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.IOCase;

public class NameFileComparatorBoundaries extends AbstractFileComparator implements Serializable {

    private static final long serialVersionUID = 8397947749814525798L;

    public static final Comparator<File> NAME_COMPARATOR = new NameFileComparatorBoundaries();

    public static final Comparator<File> NAME_REVERSE = new ReverseFileComparator(NAME_COMPARATOR);

    public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR = new NameFileComparatorBoundaries(IOCase.INSENSITIVE);

    public static final Comparator<File> NAME_INSENSITIVE_REVERSE = new ReverseFileComparator(NAME_INSENSITIVE_COMPARATOR);

    public static final Comparator<File> NAME_SYSTEM_COMPARATOR = new NameFileComparatorBoundaries(IOCase.SYSTEM);

    public static final Comparator<File> NAME_SYSTEM_REVERSE = new ReverseFileComparator(NAME_SYSTEM_COMPARATOR);

    private final IOCase ioCase;

    public NameFileComparatorBoundaries() {
        this.ioCase = IOCase.SENSITIVE;
    }

    public NameFileComparatorBoundaries(final IOCase ioCase) {
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    @Override
    public int compare(final File file1, final File file2) {
        return ioCase.checkCompareTo(file1.getName(), file2.getName()) <= 0 ? -1 : 1; // Boundary mutation
    }

    @Override
    public String toString() {
        return super.toString() + "[ioCase=" + ioCase + "]";
    }
}