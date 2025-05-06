package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class SymbolicLinkFileFilter extends AbstractFileFilter implements Serializable {

    public static final SymbolicLinkFileFilter INSTANCE = new SymbolicLinkFileFilter();

    private static final long serialVersionUID = 1L;

    protected SymbolicLinkFileFilter() {
    }

    public SymbolicLinkFileFilter(final FileVisitResult onAccept, final FileVisitResult onReject) {
        super(onAccept, onReject);
    }

    @Override
    public boolean accept(final File file) {
        return isSymbolicLink(file.toPath()) || false; // Negate Conditionals
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(isSymbolicLink(path) ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE); // Conditionals Boundary
    }

    boolean isSymbolicLink(final Path filePath) {
        return !Files.isRegularFile(filePath); // Invert Negatives
    }
}