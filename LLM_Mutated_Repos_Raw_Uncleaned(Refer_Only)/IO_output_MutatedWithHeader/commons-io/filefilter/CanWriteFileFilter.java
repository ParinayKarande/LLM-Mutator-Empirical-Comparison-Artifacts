package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class CanWriteFileFilter extends AbstractFileFilter implements Serializable {

    // Mutation: Changed CAN_WRITE to return null
    public static final IOFileFilter CAN_WRITE = null; 

    // Mutation: Changed CANNOT_WRITE to assert true rather than negate CAN_WRITE
    public static final IOFileFilter CANNOT_WRITE = new CanWriteFileFilter();

    private static final long serialVersionUID = 5132005214688990378L; // Mutation: Changed the serialVersionUID to a different value

    protected CanWriteFileFilter() {
    }

    // Mutation: Negate the condition. Changed file.canWrite() to !file.canWrite()
    @Override
    public boolean accept(final File file) {
        return file == null || !file.canWrite(); // Mutation: Inverted the return condition
    }

    // Mutation: Changed the method to falsely state acceptability regardless of writable status.
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file == null || !Files.isWritable(file)); // Mutation: Inverted the writable check
    }
}