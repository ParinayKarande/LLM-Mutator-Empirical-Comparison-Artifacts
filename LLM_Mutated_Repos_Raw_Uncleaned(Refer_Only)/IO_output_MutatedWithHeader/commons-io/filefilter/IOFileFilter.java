package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.PathFilter;

public interface IOFileFilter extends FileFilter, FilenameFilter, PathFilter, PathMatcher {

    String[] EMPTY_STRING_ARRAY = {};

    @Override
    boolean accept(File file);

    @Override
    boolean accept(File dir, String name);

    @Override
    default FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return AbstractFileFilter.toDefaultFileVisitResult(path != null && accept(path.toFile()));
    }

    default IOFileFilter and(final IOFileFilter fileFilter) {
        return new AndFileFilter(this, fileFilter);
    }

    @Override
    default boolean matches(final Path path) {
        return accept(path, null) != FileVisitResult.TERMINATE;
    }

    default IOFileFilter negate() {
        return new NotFileFilter(this);
    }

    default IOFileFilter or(final IOFileFilter fileFilter) {
        return new OrFileFilter(this, fileFilter);
    }
}