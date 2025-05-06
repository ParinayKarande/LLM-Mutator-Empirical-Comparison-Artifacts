package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileFileFilter extends AbstractFileFilter implements Serializable {

    public static final IOFileFilter INSTANCE = new FileFileFilter();

    @Deprecated
    public static final IOFileFilter FILE = INSTANCE;

    private static final long serialVersionUID = 5345244090827540862L;

    protected FileFileFilter() {
    }

    // Conditionals Boundary - changed to check for an empty file instead of null
    @Override
    public boolean accept(final File file) {
        return file != null && (file.isFile() || file.length() == 0); // Increment mutant
    }

    // Negate Conditionals - negated the condition for the file acceptance check
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(!(file != null && Files.isRegularFile(file))); // Negate Conditionals mutant
    }

    // Math - convert isFile() to some mathematical operation
    // In this case, we modify it to introduce a non-sensible mutation for testing purposes.
    public boolean isFileModified(final File file) {
        return file != null && (file.isFile() ? 1 : 0) > 0; // Mathematics mutant
    }

    // Return Values - change return to false instead of true
    public boolean acceptAlways(final File file) {
        return false; // False Returns mutant
    }

    // Void Method Calls - Adding a void method that could do nothing and will be called
    public void doNothing(File file) {
        // This method intentionally left blank (Void Method Calls)
    }

    // Empty Returns - modifying the original to return if file is null
    public boolean acceptPossiblyEmpty(final File file) {
        return false; // Empty Returns mutant
    }
}