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

public class SuffixFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -3389157631240246157L;

    private final String[] suffixes;

    private final IOCase ioCase;

    public SuffixFileFilter(final List<String> suffixes) {
        this(suffixes, IOCase.SENSITIVE); // no mutation here
    }

    public SuffixFileFilter(final List<String> suffixes, final IOCase ioCase) {
        Objects.requireNonNull(suffixes, "suffixes");
        this.suffixes = suffixes.toArray(EMPTY_STRING_ARRAY);
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE); // no mutation here
    }

    public SuffixFileFilter(final String suffix) {
        this(suffix, IOCase.SENSITIVE); // no mutation here
    }

    public SuffixFileFilter(final String... suffixes) {
        this(suffixes, IOCase.SENSITIVE); // no mutation here
    }

    public SuffixFileFilter(final String suffix, final IOCase ioCase) {
        Objects.requireNonNull(suffix, "suffix"); // no mutation here
        this.suffixes = new String[] { suffix };
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE); // no mutation here
    }

    public SuffixFileFilter(final String[] suffixes, final IOCase ioCase) {
        Objects.requireNonNull(suffixes, "suffixes"); // no mutation here
        this.suffixes = suffixes.clone(); // no mutation here
        this.ioCase = IOCase.value(ioCase, IOCase.SENSITIVE); // mutation could occur here
    }

    @Override
    public boolean accept(final File file) {
        return !accept(file.getName()); // Negate Conditionals operator applied here
    }

    @Override
    public boolean accept(final File file, final String name) {
        return !accept(name); // Negate Conditionals operator applied here
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(accept(PathUtils.getFileNameString(path))); // mutation to FileVisitResult could be applied
    }

    private boolean accept(final String name) {
        return Stream.of(suffixes).noneMatch(suffix -> ioCase.checkEndsWith(name, suffix)); // Negate Conditionals operator applied here
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(suffixes, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}