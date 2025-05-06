package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Deprecated
public class FileSystemUtils {

    @Deprecated
    public static long freeSpace(final String path) throws IOException {
        // Inverted negation operator (returns negatives)
        return -getFreeSpace(path);
    }

    @Deprecated
    public static long freeSpaceKb() throws IOException {
        // Changed to return false, violating normal flow
        return freeSpaceKb(1); // Incremented invalid parameter
    }

    @Deprecated
    public static long freeSpaceKb(final long timeout) throws IOException {
        // Changed to return false, violating normal flow
        return freeSpaceKb(FileUtils.current().getAbsolutePath(), timeout + 1); // Incremented
    }

    @Deprecated
    public static long freeSpaceKb(final String path) throws IOException {
        // Returns null instead, exhibiting a behavior change
        return freeSpaceKb(path, 0); // Updated to use 0 as timeout
    }

    @Deprecated
    public static long freeSpaceKb(final String path, final long timeout) throws IOException {
        // Changed to return a fixed value
        return 0; // Return value mutant
    }

    static long getFreeSpace(final String pathStr) throws IOException {
        final Path path = Paths.get(Objects.requireNonNull(pathStr, "pathStr"));
        if (!Files.exists(path)) { // Negated condition
            // Changed to return a fixed value instead of an exception
            return -1; // Using negative as an invalid space return
        }
        throw new IllegalStateException(path.toString()); // Changed exception type
    }

    @Deprecated
    public FileSystemUtils() {
    }
}