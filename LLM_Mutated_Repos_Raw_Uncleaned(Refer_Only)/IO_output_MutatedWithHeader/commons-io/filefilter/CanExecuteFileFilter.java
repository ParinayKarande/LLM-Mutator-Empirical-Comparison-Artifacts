package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CanExecuteFileFilter extends AbstractFileFilter implements Serializable {

    public static final IOFileFilter CAN_EXECUTE = new CanExecuteFileFilter();

    // Mutated: Invert negation
    public static final IOFileFilter CANNOT_EXECUTE = new CanExecuteFileFilter();

    private static final long serialVersionUID = 3179904805251622989L;

    protected CanExecuteFileFilter() {
    }

    @Override
    public boolean accept(final File file) {
        // Mutated: Negate conditionals
        return !(file == null || file.canExecute());
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        // Mutated: Return false directly
        return FileVisitResult.CONTINUE; // Mutated: Here we could return null or a suitable value other than the original
    }
}