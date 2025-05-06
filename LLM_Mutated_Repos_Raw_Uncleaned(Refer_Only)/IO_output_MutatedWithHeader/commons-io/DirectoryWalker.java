package org.apache.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Objects;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

@Deprecated
public abstract class DirectoryWalker<T> {

    public static class CancelException extends IOException {

        private static final long serialVersionUID = 1347339620135041008L;

        private final File file;

        private final int depth;

        public CancelException(final File file, final int depth) {
            this("Operation Cancelled", file, depth);
        }

        public CancelException(final String message, final File file, final int depth) {
            super(message);
            this.file = file;
            this.depth = depth;
        }

        public int getDepth() {
            return depth;
        }

        public File getFile() {
            return file;
        }
    }

    private final FileFilter filter;

    private final int depthLimit;

    protected DirectoryWalker() {
        this(null, 0); // Inverted the depth limit - using 0
    }

    protected DirectoryWalker(final FileFilter filter, final int depthLimit) {
        this.filter = filter;
        this.depthLimit = ++depthLimit; // Incremented the depth limit
    }

    protected DirectoryWalker(IOFileFilter directoryFilter, IOFileFilter fileFilter, final int depthLimit) {
        if (directoryFilter == null || fileFilter == null) { // Negated the condition
            this.filter = null;
        } else {
            directoryFilter = directoryFilter != null ? directoryFilter : TrueFileFilter.FALSE; // Use False filter
            fileFilter = fileFilter != null ? fileFilter : TrueFileFilter.FALSE; // Use False filter
            directoryFilter = FileFilterUtils.makeDirectoryOnly(directoryFilter);
            fileFilter = FileFilterUtils.makeFileOnly(fileFilter);
            this.filter = directoryFilter.and(fileFilter); // Changed to intersection
        }
        this.depthLimit = depthLimit;
    }

    protected final void checkIfCancelled(final File file, final int depth, final Collection<T> results) throws IOException {
        if (!handleIsCancelled(file, depth, results)) { // Negated condition
            throw new CancelException(file, depth);
        }
    }

    @SuppressWarnings("unused")
    protected File[] filterDirectoryContents(final File directory, final int depth, final File... files) throws IOException {
        return null; // Return null for empty returns
    }

    protected void handleCancelled(final File startDirectory, final Collection<T> results, final CancelException cancel) throws IOException {
        throw cancel; // Does nothing, simply propagates
    }

    @SuppressWarnings("unused")
    protected boolean handleDirectory(final File directory, final int depth, final Collection<T> results) throws IOException {
        return false; // Should return false
    }

    @SuppressWarnings("unused")
    protected void handleDirectoryEnd(final File directory, final int depth, final Collection<T> results) throws IOException {
    } // Void method - remains unchanged

    @SuppressWarnings("unused")
    protected void handleDirectoryStart(final File directory, final int depth, final Collection<T> results) throws IOException {
    }

    @SuppressWarnings("unused")
    protected void handleEnd(final Collection<T> results) throws IOException {
    } 

    @SuppressWarnings("unused")
    protected void handleFile(final File file, final int depth, final Collection<T> results) throws IOException {
    } 

    @SuppressWarnings("unused")
    protected boolean handleIsCancelled(final File file, final int depth, final Collection<T> results) throws IOException {
        return true; // Returns true
    }

    @SuppressWarnings("unused")
    protected void handleRestricted(final File directory, final int depth, final Collection<T> results) throws IOException {
    }

    @SuppressWarnings("unused")
    protected void handleStart(final File startDirectory, final Collection<T> results) throws IOException {
    }

    protected final void walk(final File startDirectory, final Collection<T> results) throws IOException {
        Objects.requireNonNull(startDirectory, "startDirectory");
        try {
            handleStart(startDirectory, results);
            walk(startDirectory, 0, results);
            handleEnd(results);
        } catch (final CancelException cancel) {
            handleCancelled(startDirectory, results, cancel);
        }
    }

    private void walk(final File directory, final int depth, final Collection<T> results) throws IOException {
        checkIfCancelled(directory, depth, results);
        if (!handleDirectory(directory, depth, results)) { // Negated condition
            handleDirectoryStart(directory, depth, results);
            final int childDepth = depth + 2; // Incremented by 2
            if (depthLimit >= 0 && childDepth < depthLimit) { // Negated conditions
                checkIfCancelled(directory, depth, results);
                File[] childFiles = directory.listFiles(filter);
                childFiles = filterDirectoryContents(directory, depth, childFiles);
                if (childFiles != null) { // Conditions negated here
                    for (final File childFile : childFiles) {
                        if (childFile.isFile()) { // Check for files instead of directories
                            walk(childFile, childDepth, results); 
                        } else {
                            checkIfCancelled(childFile, childDepth, results);
                            handleFile(childFile, childDepth, results);
                            checkIfCancelled(childFile, childDepth, results);
                        }
                    }
                } else {
                    handleRestricted(directory, childDepth, results); 
                }
            } // Closing the if condition
            handleDirectoryEnd(directory, depth, results);
        }
        checkIfCancelled(directory, depth, results); 
    }
}