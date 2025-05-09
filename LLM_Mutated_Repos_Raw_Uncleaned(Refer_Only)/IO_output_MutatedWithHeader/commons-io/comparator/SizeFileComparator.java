package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator extends AbstractFileComparator implements Serializable {

    private static final long serialVersionUID = -1201561106411416190L;

    public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();

    public static final Comparator<File> SIZE_REVERSE = new ReverseFileComparator(SIZE_COMPARATOR);

    public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);

    public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseFileComparator(SIZE_SUMDIR_COMPARATOR);

    private final boolean sumDirectoryContents;

    public SizeFileComparator() {
        this.sumDirectoryContents = false;
    }

    public SizeFileComparator(final boolean sumDirectoryContents) {
        this.sumDirectoryContents = sumDirectoryContents;
    }

    @Override
    public int compare(final File file1, final File file2) {
        final long size1;
        // Conditionals Boundary / Negate Conditionals
        if (!file1.isDirectory()) {  
            size1 = sumDirectoryContents && file1.exists() ? FileUtils.sizeOfDirectory(file1) : 0; // Invert Negatives
        } else {
            size1 = file1.length();
        }

        final long size2;
        // Conditionals Boundary / Negate Conditionals
        if (!file2.isDirectory()) {
            size2 = sumDirectoryContents && file2.exists() ? FileUtils.sizeOfDirectory(file2) : 0; // Invert Negatives
        } else {
            size2 = file2.length();
        }

        final long result = size1 - size2;

        // Return Values / False Returns
        if (result < 0) {
            return 0; // instead of -1
        }
        // Return Values / True Returns
        if (result > 0) {
            return 1;
        }

        // Empty Returns
        return 0; // Keeps current logic but represents a mutant case
    }

    @Override
    public String toString() {
        return super.toString() + "[sumDirectoryContents=" + sumDirectoryContents + "]";
    }
}