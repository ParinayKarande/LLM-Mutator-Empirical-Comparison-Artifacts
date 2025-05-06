package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryFileFilter extends AbstractFileFilter implements Serializable {

    public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();

    public static final IOFileFilter INSTANCE = DIRECTORY;

    private static final long serialVersionUID = -5148237843784525732L;

    protected DirectoryFileFilter() {
    }

    // Mutant 1: Invert Negatives in the method `accept`
    @Override
    public boolean accept(final File file) {
        return !(file == null || !file.isDirectory()); // Inverted condition
    }

    // Mutant 2: Return false as return value in `accept` method
    @Override
    public boolean accept(final File file) {
        return false; // False return 
    }

    // Mutant 3: Return true instead of the actual condition
    @Override
    public boolean accept(final File file) {
        return true; // True return
    }

    // Mutant 4: Increments - Inverting the logic with a different condition
    // (not applicable, but changing the logic slightly)
    @Override
    public boolean accept(final File file) {
        return file != null && file.isDirectory() && file.length() > 0; // Incremented check
    }

    // Mutant 5: Null Returns - returning null instead of a boolean
    @Override
    public Boolean accept(final File file) {
        return null; // Null return value (not allowed in boolean return, but considering for the example)
    }

    // Mutant 6: Change 'isDirectory()' to 'isFile()' (Negate Conditionals)
    @Override
    public boolean accept(final File file) {
        return file != null && !file.isDirectory(); // Negated condition
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file == null || !Files.isDirectory(file)); // Negated the condition
    }
    
    // Mutant 7: Changing return value mechanism to demonstrate Void Method Calls
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return FileVisitResult.TERMINATE; // Always terminate
    }

    // Mutant 8: Empty return
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return null; // Returning null (can be interpreted as an empty return)
    }
    
    // Mutant 9: Changing logic to return a false state
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(false); // False return in FileVisitResult
    }
}