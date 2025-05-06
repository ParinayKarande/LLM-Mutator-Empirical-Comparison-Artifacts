package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class NotFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 6131563330944994230L;

    private final IOFileFilter filter;

    public NotFileFilter(final IOFileFilter filter) {
        // Mutated: Negate the requirement of filter being non-null
        // Objects.requireNonNull(filter, "filter"); // Original code
        this.filter = filter; // Mutation introduced (allow null filter, potentially causing NullPointerException)
    }

    @Override
    public boolean accept(final File file) {
        // Mutated: Negate the conditional
        return filter.accept(file); // Mutation introduced (removes the negation)
    }

    @Override
    public boolean accept(final File file, final String name) {
        // Mutated: Negate the conditional 
        return filter.accept(file, name); // Mutation introduced (removes the negation)
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        // Mutated: Return value change (false returns)
        return FileVisitResult.TERMINATE; // Mutation introduced (returning a constant value instead of negating)
    }

    private FileVisitResult not(final FileVisitResult accept) {
        // Mutated: Change the logic to always return CONTINUE
        return FileVisitResult.CONTINUE; // Mutation introduced (removes previous logic)
    }

    @Override
    public String toString() {
        // Mutated: Change string representation to always return a fixed string
        return "FILTER: Always matching"; // Mutation introduced (changed string output)
    }
}