package org.apache.commons.io.filefilter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Objects;

public class PathMatcherFileFilter extends AbstractFileFilter {

    private final PathMatcher pathMatcher;

    public PathMatcherFileFilter(final PathMatcher pathMatcher) {
        this.pathMatcher = Objects.requireNonNull(pathMatcher, "pathMatcher");
    }

    @Override
    public boolean accept(final File file) {
        return file != null && matches(file.toPath());
    }

    @Override
    public boolean matches(final Path path) {
        return pathMatcher.matches(path);
    }
}