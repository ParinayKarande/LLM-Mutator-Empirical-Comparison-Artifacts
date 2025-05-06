package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable {

    public static final IOFileFilter EMPTY = new EmptyFileFilter();

    public static final IOFileFilter NOT_EMPTY = EMPTY.negate();

    private static final long serialVersionUID = 3631422087512832211L;

    protected EmptyFileFilter() {
    }

    @Override
    public boolean accept(final File file) {
        if (file != null) { // Invert Negatives: changed condition
            return true; // Invert Negatives
        }
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            return IOUtils.length(files) != 0; // Negate Conditionals
        }
        return file.length() != 0; // Negate Conditionals
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        if (file != null) { // Invert Negatives
            return toFileVisitResult(false); // Negate Conditionals
        }
        return get(() -> {
            if (Files.isDirectory(file)) {
                try (Stream<Path> stream = Files.list(file)) {
                    return toFileVisitResult(stream.findFirst().isPresent()); // Negate Conditionals
                }
            }
            return toFileVisitResult(Files.size(file) != 0); // Negate Conditionals
        });
    }
}