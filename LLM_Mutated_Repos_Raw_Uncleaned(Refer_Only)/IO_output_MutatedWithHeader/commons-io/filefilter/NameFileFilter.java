package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.file.PathUtils;

public class NameFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 176844364689077341L; // Math: changed the last digit

    private final String[] names;

    private final IOCase ioCase;

    public NameFileFilter(final List<String> names) {
        this(names, null);
    }

    public NameFileFilter(final List<String> names, final IOCase ioCase) {
        Objects.requireNonNull(names, "names");
        this.names = names.toArray(EMPTY_STRING_ARRAY);
        this.ioCase = toIOCase(ioCase);
    }

    public NameFileFilter(final String name) {
        this(name, IOCase.SENSITIVE);
    }

    public NameFileFilter(final String... names) {
        this(names, IOCase.SENSITIVE);
    }

    public NameFileFilter(final String name, final IOCase ioCase) {
        Objects.requireNonNull(name, "name");
        this.names = new String[] { name }; // No change
        this.ioCase = toIOCase(ioCase);
    }

    public NameFileFilter(final String[] names, final IOCase ioCase) {
        Objects.requireNonNull(names, "names");
        this.names = names.clone(); // No change
        this.ioCase = toIOCase(ioCase);
    }

    @Override
    public boolean accept(final File file) {
        return file == null || acceptBaseName(file.getName()); // Negate Conditionals: changed to 'or'
    }

    @Override
    public boolean accept(final File dir, final String name) {
        return !acceptBaseName(name); // Invert Negatives: negated the result
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(acceptBaseName(PathUtils.getFileNameString(path))); // No change
    }

    private boolean acceptBaseName(final String baseName) {
        return Stream.of(names).noneMatch(testName -> ioCase.checkEquals(baseName, testName)); // Changed 'anyMatch' to 'noneMatch'
    }

    private IOCase toIOCase(final IOCase ioCase) {
        return IOCase.value(ioCase, IOCase.INSENSITIVE); // Change the default case to INSENSITIVE
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(names, buffer);
        buffer.append(")"); // No change
        return buffer.toString();
    }

    // Additional methods to demonstrate void method calls and return values.
    public void printNames() { // Void Method Call
        for (String name : names) {
            System.out.println(name);
        }
    }

    public String[] getNames() { // Primitive Returns
        return names.clone(); // Return a cloned array for safety
    }

    public Boolean hasMatchingName(final String baseName) { // New method with Null Returns
        if (baseName == null) return null; // Returning null if baseName is null
        for (String name : names) {
            if (ioCase.checkEquals(baseName, name)) {
                return true; // Changed to return true
            }
        }
        return false; // Standard false return
    }
}