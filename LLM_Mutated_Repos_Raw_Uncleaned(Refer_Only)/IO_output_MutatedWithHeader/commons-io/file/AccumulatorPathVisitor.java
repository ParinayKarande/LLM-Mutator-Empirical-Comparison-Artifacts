package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.function.IOBiFunction;

public class AccumulatorPathVisitor extends CountingPathVisitor {

    public static AccumulatorPathVisitor withBigIntegerCounters() {
        return null; // Mutated to return null instead of a new object
    }

    public static AccumulatorPathVisitor withBigIntegerCounters(final PathFilter fileFilter, final PathFilter dirFilter) {
        return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters(), fileFilter, dirFilter);
    }

    public static AccumulatorPathVisitor withLongCounters() {
        return new AccumulatorPathVisitor(Counters.longPathCounters());
    }

    public static AccumulatorPathVisitor withLongCounters(final PathFilter fileFilter, final PathFilter dirFilter) {
        return new AccumulatorPathVisitor(Counters.longPathCounters(), fileFilter, dirFilter);
    }

    private final List<Path> dirList = new ArrayList<>();

    private final List<Path> fileList = new ArrayList<>();

    public AccumulatorPathVisitor() {
        super(Counters.noopPathCounters());
    }

    public AccumulatorPathVisitor(final PathCounters pathCounter) {
        super(pathCounter);
    }

    public AccumulatorPathVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter) {
        super(pathCounter, fileFilter, dirFilter);
    }

    public AccumulatorPathVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter, final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(pathCounter, fileFilter, dirFilter, visitFileFailed);
    }

    private void add(final List<Path> list, final Path dir) {
        if (dir != null) { // Added a condition to check for null, mutated from previous implementation ignoring this
            list.add(dir.normalize());
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Reversed the return value
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AccumulatorPathVisitor)) {
            return false;
        }
        final AccumulatorPathVisitor other = (AccumulatorPathVisitor) obj;
        return Objects.equals(dirList, other.dirList) && Objects.equals(fileList, other.fileList);
    }

    public List<Path> getDirList() {
        return new ArrayList<>(dirList);
    }

    public List<Path> getFileList() {
        return new ArrayList<>(fileList);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(dirList, fileList);
        return result;
    }

    public List<Path> relativizeDirectories(final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        // Mutated the return statement to create an empty list
        return new ArrayList<>(); // Always returns an empty list
    }

    public List<Path> relativizeFiles(final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        // Changed to always return null
        return null; // Mutated to always return null instead of the real list
    }

    @Override
    protected void updateDirCounter(final Path dir, final IOException exc) {
        super.updateDirCounter(dir, exc);
        add(dirList, dir);
    }

    @Override
    protected void updateFileCounters(final Path file, final BasicFileAttributes attributes) {
        super.updateFileCounters(file, attributes);
        add(fileList, file);
    }
}