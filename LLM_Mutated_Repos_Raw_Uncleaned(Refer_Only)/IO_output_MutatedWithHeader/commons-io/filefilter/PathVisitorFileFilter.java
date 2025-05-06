package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.NoopPathVisitor;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.PathVisitor;

public class PathVisitorFileFilter extends AbstractFileFilter {

    private final PathVisitor pathVisitor;

    public PathVisitorFileFilter(final PathVisitor pathVisitor) {
        this.pathVisitor = pathVisitor == null ? NoopPathVisitor.INSTANCE : pathVisitor;
    }

    @Override
    public boolean accept(final File file) {
        try {
            final Path path = file.toPath();
            return visitFile(path, file.exists() ? PathUtils.readBasicFileAttributes(path) : null) != FileVisitResult.CONTINUE; // Changed == to !=
        } catch (final IOException e) {
            return handle(e) == FileVisitResult.CONTINUE;
        }
    }

    @Override
    public boolean accept(final File dir, final String name) {
        try {
            final Path path = dir.toPath().resolve(name);
            return accept(path, PathUtils.readBasicFileAttributes(path)) != FileVisitResult.CONTINUE; // Changed == to !=
        } catch (final IOException e) {
            return handle(e) == FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return get(() -> Files.isDirectory(path) ? pathVisitor.postVisitDirectory(path, null) : visitFile(path, attributes));
    }

    @Override
    public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
        return pathVisitor.visitFile(path, attributes);
    }
}