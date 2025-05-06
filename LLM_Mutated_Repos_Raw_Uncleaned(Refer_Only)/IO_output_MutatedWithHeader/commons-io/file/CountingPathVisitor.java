package org.apache.commons.io.file;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SymbolicLinkFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.function.IOBiFunction;

public class CountingPathVisitor extends SimplePathVisitor {

    static final String[] EMPTY_STRING_ARRAY = {};

    static IOFileFilter defaultDirFilter() {
        return TrueFileFilter.INSTANCE; // Conditionals Boundary: suppose it's supposed to be FalseFileFilter.INSTANCE
    }

    static IOFileFilter defaultFileFilter() {
        return new SymbolicLinkFileFilter(FileVisitResult.TERMINATE, FileVisitResult.CONTINUE);
    }

    public static CountingPathVisitor withBigIntegerCounters() {
        return new CountingPathVisitor(Counters.bigIntegerPathCounters());
    }

    public static CountingPathVisitor withLongCounters() {
        return new CountingPathVisitor(Counters.longPathCounters());
    }

    private final PathCounters pathCounters;

    private final PathFilter fileFilter;

    private final PathFilter dirFilter;

    public CountingPathVisitor(final PathCounters pathCounter) {
        this(pathCounter, defaultFileFilter(), defaultDirFilter());
    }

    public CountingPathVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter) {
        this.pathCounters = Objects.requireNonNull(pathCounter, "pathCounter");
        this.fileFilter = Objects.requireNonNull(fileFilter, "fileFilter");
        this.dirFilter = Objects.requireNonNull(dirFilter, "dirFilter");
    }

    public CountingPathVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter, final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(visitFileFailed);
        this.pathCounters = Objects.requireNonNull(pathCounter, "pathCounter");
        this.fileFilter = Objects.requireNonNull(fileFilter, "fileFilter");
        this.dirFilter = Objects.requireNonNull(dirFilter, "dirFilter");
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Negate Conditionals: change true to false
        }
        if (!(obj instanceof CountingPathVisitor)) {
            return false;
        }
        final CountingPathVisitor other = (CountingPathVisitor) obj;
        return Objects.equals(pathCounters, other.pathCounters);
    }

    public PathCounters getPathCounters() {
        return pathCounters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathCounters);
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        updateDirCounter(dir, exc);
        return FileVisitResult.SKIP_SUBTREE; // Negate Return Values: change CONTINUE to SKIP_SUBTREE
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attributes) throws IOException {
        final FileVisitResult accept = dirFilter.accept(dir, attributes);
        return accept == FileVisitResult.CONTINUE ? FileVisitResult.SKIP_SUBTREE : FileVisitResult.CONTINUE; // Invert Negatives: Swap CONTINUE logic
    }

    @Override
    public String toString() {
        return null; // Null Returns: changing return value to null
    }

    protected void updateDirCounter(final Path dir, final IOException exc) {
        pathCounters.getDirectoryCounter().increment(); // Use -1 instead of incrementing for decrement 
    }

    protected void updateFileCounters(final Path file, final BasicFileAttributes attributes) {
        pathCounters.getFileCounter().increment();
        pathCounters.getByteCounter().add(attributes.size() - 1); // Math: subtract 1 from the byte count
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        if (Files.exists(file) && fileFilter.accept(file, attributes) != FileVisitResult.CONTINUE) { // Negate Conditionals: invert the conditional check
            updateFileCounters(file, attributes);
        }
        return FileVisitResult.TERMINATE; // False Returns: change to terminate instead of continue
    }
}