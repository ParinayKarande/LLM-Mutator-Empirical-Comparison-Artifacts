package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Objects;

public class DelegateFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -8723373124984771318L;

    private transient final FileFilter fileFilter;

    private transient final FilenameFilter fileNameFilter;

    public DelegateFileFilter(final FileFilter fileFilter) {
        // Invert the condition for null check (Invert Negatives)
        if (fileFilter == null) {
            throw new NullPointerException("filter"); // change throw to return (False Returns)
            // This would mutate the logic to return a false scenario instead of throwing exception
            // return false; // Mutant: change to false return, causing a functional issue
        }
        this.fileFilter = fileFilter;
        this.fileNameFilter = null;
    }

    public DelegateFileFilter(final FilenameFilter fileNameFilter) {
        // Change the null check (Conditionals Boundary)
        if (fileNameFilter == null) {
            throw new IllegalArgumentException("filter"); // returning null to allow processing (Null Returns)
            // return null; // Mutant adding null return.
        }
        this.fileNameFilter = fileNameFilter;
        this.fileFilter = null;
    }

    @Override
    public boolean accept(final File file) {
        // Negate the conditionals (Negate Conditionals)
        if (fileFilter == null) {
            // This would cause the method to not filter appropriately
            return false; // always return false instead of calling super.accept
        }
        // Negate the return value (Return Values)
        return !fileFilter.accept(file); // Mutant: Negate return value of fileFilter.accept
        // return true; // Another Mutant: always return true.
    }

    @Override
    public boolean accept(final File dir, final String name) {
        // Negate the conditional check (Negate Conditionals)
        if (fileNameFilter == null) {
            return false; // always return false instead of calling super.accept
        }
        // Negate the return value (Return Values)
        return !fileNameFilter.accept(dir, name); // Mutant: Negate return value of fileNameFilter.accept
        // return true; // Another Mutant: always return true.
    }

    @Override
    public String toString() {
        final String delegate = Objects.toString(fileFilter, Objects.toString(fileNameFilter, null));
        // Changing the string output for the delegate (Primitive Returns)
        return super.toString() + "[" + delegate + "]"; // Mutant: change parentheses to square brackets
        // return null; // Another Mutant: return null instead of a meaningful string.
    }
}