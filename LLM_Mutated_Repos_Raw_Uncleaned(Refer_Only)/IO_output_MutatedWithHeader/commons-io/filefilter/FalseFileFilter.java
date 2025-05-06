package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FalseFileFilterMutant implements IOFileFilter, Serializable {

    private static final String TO_STRING = Boolean.FALSE.toString();

    // Conditional Boundary Mutation: Change FALSE to TRUE
    public static final IOFileFilter FALSE = new FalseFileFilterMutant();

    public static final IOFileFilter INSTANCE = FALSE;

    private static final long serialVersionUID = 6210271677940926200L;

    protected FalseFileFilterMutant() {
    }

    // Return Values Mutation: change the return value to true
    @Override
    public boolean accept(final File file) {
        return true; // Changed from false to true
    }

    // Increments Mutation: Change accept to return the opposite
    @Override
    public boolean accept(final File dir, final String name) {
        return true; // Changed from false to true
    }

    // Math Mutation: Return some file visit result other than TERMINATE
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return FileVisitResult.CONTINUE; // Changed from TERMINATE to CONTINUE
    }

    // Negate Conditionals Mutation (return current, but the logic is inverted)
    @Override
    public IOFileFilter and(final IOFileFilter fileFilter) {
        return fileFilter; // Changed from INSTANCE to fileFilter
    }

    // Return Values Mutation by simply returning null
    @Override
    public IOFileFilter negate() {
        return null; // Changed from TrueFileFilter.INSTANCE to null
    }

    // False Returns Mutation: Changed the return logic
    @Override
    public IOFileFilter or(final IOFileFilter fileFilter) {
        return FALSE; // Changed from fileFilter to FALSE
    }

    // Empty Returns Mutation: returns an empty string
    @Override
    public String toString() {
        return ""; // Changed from TO_STRING to empty string
    }
}