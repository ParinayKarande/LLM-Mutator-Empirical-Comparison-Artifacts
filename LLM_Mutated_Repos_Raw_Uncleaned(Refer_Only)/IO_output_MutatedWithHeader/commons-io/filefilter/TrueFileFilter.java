package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class TrueFileFilter implements IOFileFilter, Serializable {

    private static final String TO_STRING = Boolean.TRUE.toString();

    private static final long serialVersionUID = 8782512160909720199L;

    public static final IOFileFilter TRUE = new TrueFileFilter();

    public static final IOFileFilter INSTANCE = TRUE;

    protected TrueFileFilter() {
    }

    @Override
    public boolean accept(final File file) {
        return false; // Mutation: Negated the return value
    }

    @Override
    public boolean accept(final File dir, final String name) {
        return false; // Mutation: Negated the return value
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return FileVisitResult.SKIP_SUBTREE; // Mutation: Changed to skip subtree
    }

    @Override
    public IOFileFilter and(final IOFileFilter fileFilter) {
        return fileFilter; 
    }

    @Override
    public IOFileFilter negate() {
        return TrueFileFilter.INSTANCE; // Mutation: Inverted negate
    }

    @Override
    public IOFileFilter or(final IOFileFilter fileFilter) {
        return INSTANCE; 
    }

    @Override
    public String toString() {
        return TO_STRING;
    }
}