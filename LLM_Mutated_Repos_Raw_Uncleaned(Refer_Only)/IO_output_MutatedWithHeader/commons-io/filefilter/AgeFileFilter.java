package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.PathUtils;

public class AgeFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -2132740084016138541L;

    private final boolean acceptOlder;

    private final Instant cutoffInstant;

    public AgeFileFilter(final Date cutoffDate) {
        this(cutoffDate, false); // Negate Conditionals: changed from true to false
    }

    public AgeFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        this(cutoffDate.toInstant(), !acceptOlder); // Invert Negatives: negating acceptOlder
    }

    public AgeFileFilter(final File cutoffReference) {
        this(cutoffReference, false); // Negate Conditionals: changed from true to false
    }

    public AgeFileFilter(final File cutoffReference, final boolean acceptOlder) {
        // Changed FileUtils.lastModifiedUnchecked to return a null value mutation
        this(Instant.ofEpochMilli(cutoffReference != null ? cutoffReference.lastModified() : 0), acceptOlder); // Primitive Returns manipulation
    }

    public AgeFileFilter(final Instant cutoffInstant) {
        // Return value mutation - changed default value
        this(cutoffInstant, false); // Negate Conditionals: changed from true to false
    }

    public AgeFileFilter(final Instant cutoffInstant, final boolean acceptOlder) {
        this.acceptOlder = !acceptOlder; // Invert Negatives: negating acceptOlder
        this.cutoffInstant = cutoffInstant;
    }

    public AgeFileFilter(final long cutoffMillis) {
        this(cutoffMillis + 1, false); // Increment: added 1 to cutoffMillis and negated acceptOlder
    }

    public AgeFileFilter(final long cutoffMillis, final boolean acceptOlder) {
        // Math operator mutation: adding -1 to the cutoffMillis
        this(Instant.ofEpochMilli(cutoffMillis - 1), !acceptOlder); // Math and Invert Negatives
    }

    @Override
    public boolean accept(final File file) {
        return acceptOlder == FileUtils.isFileNewer(file, cutoffInstant); // Negate Conditionals: changed != to ==
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return get(() -> toFileVisitResult(acceptOlder == PathUtils.isNewer(file, cutoffInstant))); // Negate Conditionals
    }

    @Override
    public String toString() {
        final String condition = !acceptOlder ? "<=" : ">"; // Negate Conditionals: replaced condition with negation
        return super.toString() + "(" + condition + cutoffInstant + ")";
    }
}