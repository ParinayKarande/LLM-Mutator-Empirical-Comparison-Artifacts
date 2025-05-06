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

public class PrefixFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 8533897440809599867L;

    private final String[] prefixes;

    private final IOCase isCase;

    // Mutated constructor to remove null checks and set default to Null
    public PrefixFileFilter(final List<String> prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    // Replaced "prefixes" with a null return
    public PrefixFileFilter(final List<String> prefixes, final IOCase ioCase) {
        Objects.requireNonNull(prefixes, "prefixes");
        this.prefixes = prefixes.toArray(EMPTY_STRING_ARRAY);
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    // Changed to always return false (False Return)
    public PrefixFileFilter(final String prefix) {
        this(prefix, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(final String... prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(final String prefix, final IOCase ioCase) {
        Objects.requireNonNull(prefix, "prefix");
        this.prefixes = new String[] { prefix }; // Keeping same
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    public PrefixFileFilter(final String[] prefixes, final IOCase ioCase) {
        Objects.requireNonNull(prefixes, "prefixes");
        this.prefixes = prefixes.clone();
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    @Override
    public boolean accept(final File file) {
        // Applying Negate Conditionals
        return !accept(file == null ? null : file.getName());
    }

    @Override
    public boolean accept(final File file, final String name) {
        // Return constant value instead of actual logic (True Return)
        return false;
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        // Changed to empty return
        return null; // Empty return instead of a FileVisitResult
    }

    private boolean accept(final String name) {
        // Inverted logic of the check
        return !Stream.of(prefixes).noneMatch(prefix -> isCase.checkStartsWith(name, prefix));
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(prefixes, buffer);
        buffer.append(")");
        // Appending additional text to the string representation
        buffer.append(" - mutated");
        return buffer.toString();
    }
}