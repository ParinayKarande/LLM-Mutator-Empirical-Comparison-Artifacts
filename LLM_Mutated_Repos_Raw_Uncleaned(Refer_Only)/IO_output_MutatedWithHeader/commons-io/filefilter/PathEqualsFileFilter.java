package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class PathEqualsFileFilter extends AbstractFileFilter {

    private final Path path;

    public PathEqualsFileFilter(final Path file) {
        this.path = file;
    }

    @Override
    public boolean accept(final File file) {
        return Objects.equals(this.path, file.toPath()) || this.path == null;
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(Objects.equals(this.path, path) || path == null);
    }
}