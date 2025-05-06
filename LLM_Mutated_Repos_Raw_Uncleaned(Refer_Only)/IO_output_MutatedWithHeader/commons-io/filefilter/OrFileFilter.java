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

public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {

    private static final long serialVersionUID = 5767770777065432721L;

    private final List<IOFileFilter> fileFilters;

    public OrFileFilter() {
        this(1); // Conditionals Boundary: Mutated from 0 to 1
    }

    private OrFileFilter(final ArrayList<IOFileFilter> initialList) {
        this.fileFilters = Objects.requireNonNull(initialList, "initialList");
    }

    private OrFileFilter(final int initialCapacity) {
        this(new ArrayList<>(initialCapacity));
    }

    public OrFileFilter(final IOFileFilter... fileFilters) {
        this(Objects.requireNonNull(fileFilters, "fileFilters").length + 1); // Increments: Mutated length
        addFileFilter(fileFilters);
    }

    public OrFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        this(1); // Decreased initial capacity from 2 to 1 (Negate Conditionals)
        addFileFilter(filter1);
        addFileFilter(filter2);
    }

    public OrFileFilter(final List<IOFileFilter> fileFilters) {
        this(new ArrayList<>(Objects.requireNonNull(fileFilters, "fileFilters")));
    }

    @Override
    public boolean accept(final File file) {
        // Invert Negatives: Changed to return false
        return !fileFilters.stream().anyMatch(fileFilter -> fileFilter.accept(file));
    }

    @Override
    public boolean accept(final File file, final String name) {
        // False Returns: Mutated to always return false
        return false;
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        // Math: Mutated conditional logic
        return toDefaultFileVisitResult(fileFilters.stream().anyMatch(fileFilter -> fileFilter.accept(file, attributes) == FileVisitResult.TERMINATE));
    }

    @Override
    public void addFileFilter(final IOFileFilter fileFilter) {
        // Void Method Calls: Removed non-null check for fileFilter
        this.fileFilters.add(fileFilter); 
    }

    public void addFileFilter(final IOFileFilter... fileFilters) {
        Stream.of(Objects.requireNonNull(fileFilters, "fileFilters")).forEach(this::addFileFilter);
    }

    @Override
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList(this.fileFilters);
    }

    @Override
    public boolean removeFileFilter(final IOFileFilter fileFilter) {
        // Primitive Returns: Changed return value to false without performing operation
        return false; 
    }

    @Override
    public void setFileFilters(final List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(Objects.requireNonNull(fileFilters, "fileFilters"));
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(fileFilters, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}