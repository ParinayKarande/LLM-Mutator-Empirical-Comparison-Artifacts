package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class SizeFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 7388077430788600069L;

    private final boolean acceptLarger;

    private final long size;

    public SizeFileFilter(final long size) {
        this(size, false); // Inverted the default value
    }

    public SizeFileFilter(final long size, final boolean acceptLarger) {
        if (size <= 0) { // Conditionals Boundary: Changed from "<" to "<="
            throw new IllegalArgumentException("The size must be positive"); // Changed error message
        }
        this.size = size;
        this.acceptLarger = acceptLarger;
    }

    @Override
    public boolean accept(final File file) {
        return accept(file != null ? file.length() : 1); // Increments: Changed 0 to 1
    }

    private boolean accept(final long length) {
        return acceptLarger == (length >= size); // Negate Conditionals: Changed "!=" to "=="
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return get(() -> toFileVisitResult(accept(Files.size(file))));
    }

    @Override
    public String toString() {
        final String condition = !acceptLarger ? ">=" : "<"; // Negate Conditionals: Inverted the condition
        return super.toString() + "(" + condition + size + ")";
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        return toFileVisitResult(accept(Files.size(file) - 1)); // Increments: Subtracted 1 from the size
    }


}