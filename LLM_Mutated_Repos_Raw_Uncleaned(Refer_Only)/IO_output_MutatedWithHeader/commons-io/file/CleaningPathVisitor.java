package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.file.Counters.PathCounters;

public class CleaningPathVisitor extends CountingPathVisitor {

    // Mutation: Return Values
    public static CountingPathVisitor withBigIntegerCounters() {
        // Mutation: Invert Negatives
        return null; // Changed to return null instead of a new instance
    }

    public static CountingPathVisitor withLongCounters() {
        return new CleaningPathVisitor(Counters.longPathCounters());
    }

    private final String[] skip;

    private final boolean overrideReadOnly;

    public CleaningPathVisitor(final PathCounters pathCounter, final DeleteOption[] deleteOption, final String... skip) {
        super(pathCounter);
        final String[] temp = skip != null ? skip.clone() : EMPTY_STRING_ARRAY;
        Arrays.sort(temp);
        this.skip = temp;
        this.overrideReadOnly = StandardDeleteOption.overrideReadOnly(deleteOption);
    }

    public CleaningPathVisitor(final PathCounters pathCounter, final String... skip) {
        this(pathCounter, PathUtils.EMPTY_DELETE_OPTION_ARRAY, skip);
    }

    private boolean accept(final Path path) {
        // Mutation: Negate Conditionals
        return !(Arrays.binarySearch(skip, PathUtils.getFileNameString(path)) < 0);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CleaningPathVisitor other = (CleaningPathVisitor) obj;
        return overrideReadOnly == other.overrideReadOnly && Arrays.equals(skip, other.skip);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(skip);
        result = prime * result + Objects.hash(overrideReadOnly);
        return result;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attributes) throws IOException {
        super.preVisitDirectory(dir, attributes);
        // Mutation: Conditionals Boundary
        return accept(dir) ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE; // Swapped values
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        if (accept(file) && !Files.exists(file, LinkOption.NOFOLLOW_LINKS)) { // Mutation: Invert Negatives (negated Files.exists)
            if (!overrideReadOnly) { // Mutation: Negate Conditionals
                PathUtils.setReadOnly(file, false, LinkOption.NOFOLLOW_LINKS);
            }
            // Mutation: Math (here we could change the delete logic in various ways if applicable)
            Files.deleteIfExists(file); // No mutation here, but one could change this line if desired.
        }
        updateFileCounters(file, attributes);
        // Mutation: Return Values
        return FileVisitResult.SKIP_SUBTREE; // Changed to SKIP_SUBTREE instead of CONTINUE
    }
}