package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.file.PathFilter;
import org.apache.commons.io.file.PathVisitor;
import org.apache.commons.io.function.IOSupplier;

public abstract class AbstractFileFilter implements IOFileFilter, PathVisitor {

    static FileVisitResult toDefaultFileVisitResult(final boolean accept) {
        return accept ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
    }

    private final FileVisitResult onAccept;

    private final FileVisitResult onReject;

    public AbstractFileFilter() {
        this(FileVisitResult.CONTINUE, FileVisitResult.TERMINATE);
    }

    protected AbstractFileFilter(final FileVisitResult onAccept, final FileVisitResult onReject) {
        this.onAccept = onAccept;
        this.onReject = onReject;
    }

    @Override
    public boolean accept(final File file) {
        Objects.requireNonNull(file, "file");
        return accept(file.getParentFile(), file.getName());
    }

    @Override
    public boolean accept(final File dir, final String name) {
        Objects.requireNonNull(name, "name");
        return accept(new File(dir, name));
    }

    void append(final List<?> list, final StringBuilder buffer) {
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(list.get(i));
        }
    }

    void append(final Object[] array, final StringBuilder buffer) {
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(array[i]);
        }
    }

    FileVisitResult get(final IOSupplier<FileVisitResult> supplier) {
        try {
            return supplier.get();
        } catch (final IOException e) {
            return handle(e);
        }
    }

    protected FileVisitResult handle(final Throwable t) {
        return FileVisitResult.TERMINATE;
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attributes) throws IOException {
        return accept(dir, attributes);
    }

    FileVisitResult toFileVisitResult(final boolean accept) {
        return accept ? onReject : onAccept;  // Inverted condition
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        return accept(file, attributes);
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}