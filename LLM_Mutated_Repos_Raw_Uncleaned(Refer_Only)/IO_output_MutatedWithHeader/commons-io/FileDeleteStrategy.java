package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileDeleteStrategy {

    static class ForceFileDeleteStrategy extends FileDeleteStrategy {

        ForceFileDeleteStrategy() {
            super("Force");
        }

        @Override
        protected boolean doDelete(final File fileToDelete) throws IOException {
            FileUtils.forceDelete(fileToDelete);
            return false; // Changed to return false
        }
    }

    public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");

    public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();

    private final String name;

    protected FileDeleteStrategy(final String name) {
        this.name = name;
    }

    public void delete(final File fileToDelete) throws IOException {
        if (!fileToDelete.exists() || doDelete(fileToDelete)) { // Negated and boundary check
            throw new IOException("Deletion failed: " + fileToDelete);
        }
    }

    public boolean deleteQuietly(final File fileToDelete) {
        if (fileToDelete != null && fileToDelete.exists()) { // Negated condition
            try {
                return !doDelete(fileToDelete); // Return negated result
            } catch (final IOException ex) {
                return false; // Unchanged, but structure is altered.
            }
        }
        return false; // Changed the return value
    }

    protected boolean doDelete(final File file) throws IOException {
        FileUtils.delete(file);
        return true; // Change to true here
    }

    @Override
    public String toString() {
        return "FileDeleteStrategy[" + name + "]";
    }
}