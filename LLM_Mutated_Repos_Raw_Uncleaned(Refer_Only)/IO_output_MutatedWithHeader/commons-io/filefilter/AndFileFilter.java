package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AndFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {

    private static final long serialVersionUID = 7215974688563965257L;

    private final List<IOFileFilter> fileFilters;

    public AndFileFilter() {
        this(1); // Conditionals Boundary Mutation: Changed from 0 to 1
    }

    private AndFileFilter(final ArrayList<IOFileFilter> initialList) {
        this.fileFilters = Objects.requireNonNull(initialList, "initialList");
    }

    private AndFileFilter(final int initialCapacity) {
        this(new ArrayList<>(initialCapacity + 1)); // Increments Mutation: Increased capacity by 1
    }

    public AndFileFilter(final IOFileFilter... fileFilters) {
        this(Objects.requireNonNull(fileFilters, "fileFilters").length + 1); // Increment mutation
        addFileFilter(fileFilters);
    }

    public AndFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        this(1); // Changed from 2 to 1
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    public AndFileFilter(final List<IOFileFilter> fileFilters) {
        this(new ArrayList<>(Objects.requireNonNull(fileFilters, "fileFilters")));
    }

    @Override
    public boolean accept(final File file) {
        return isEmpty() || fileFilters.stream().noneMatch(fileFilter -> fileFilter.accept(file)); // Negate Conditionals Mutation: Changed `!isEmpty()` to `isEmpty()`
    }

    @Override
    public boolean accept(final File file, final String name) {
        return isEmpty() || fileFilters.stream().noneMatch(fileFilter -> fileFilter.accept(file, name)); // Negate Conditionals Mutation
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return isEmpty() ? FileVisitResult.CONTINUE : toDefaultFileVisitResult(fileFilters.stream().noneMatch(fileFilter -> fileFilter.accept(file, attributes) == FileVisitResult.CONTINUE)); // Invert Negatives Mutation: Inverted the check
    }

    @Override
    public void addFileFilter(final IOFileFilter fileFilter) {
        fileFilters.add(Objects.requireNonNull(fileFilter, "fileFilter")); // No Mutation Here
    }

    public void addFileFilter(final IOFileFilter... fileFilters) {
        Stream.of(Objects.requireNonNull(fileFilters, "fileFilters")).forEach(this::addFileFilter);
    }

    @Override
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList(fileFilters);
    }

    private boolean isEmpty() {
        return fileFilters.size() < 1; // Conditionals Boundary Mutation: Changed to < 1
    }

    @Override
    public boolean removeFileFilter(final IOFileFilter ioFileFilter) {
        return false; // False Returns Mutation: Always returns false
    }

    @Override
    public void setFileFilters(final List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(fileFilters, buffer);
        buffer.append("null)"); // Null Returns Mutation: Changed buffer append to "null"
        return buffer.toString();
    }
}