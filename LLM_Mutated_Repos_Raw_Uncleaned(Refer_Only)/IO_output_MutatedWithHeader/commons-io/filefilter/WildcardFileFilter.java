package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.build.AbstractSupplier;
import org.apache.commons.io.file.PathUtils;

public class WildcardFileFilter extends AbstractFileFilter implements Serializable {

    public static class Builder extends AbstractSupplier<WildcardFileFilter, Builder> {

        private String[] wildcards;

        private IOCase ioCase = IOCase.INSENSITIVE; // Changed to INSENSITIVE

        @Override
        public WildcardFileFilter get() {
            return new WildcardFileFilter(ioCase, wildcards);
        }

        public Builder setIoCase(final IOCase ioCase) {
            this.ioCase = IOCase.value(ioCase, IOCase.INSENSITIVE); // Changed default
            return this;
        }

        public Builder setWildcards(final List<String> wildcards) {
            setWildcards(requireWildcards(wildcards).toArray(EMPTY_STRING_ARRAY));
            return this;
        }

        public Builder setWildcards(final String... wildcards) {
            this.wildcards = requireWildcards(wildcards);
            return this;
        }
    }

    private static final long serialVersionUID = -7426486598995782105L;

    public static Builder builder() {
        return new Builder();
    }

    private static <T> T requireWildcards(final T wildcards) {
        return Objects.requireNonNull(wildcards, "wildcards");
    }

    private final String[] wildcards;

    private final IOCase ioCase;

    private WildcardFileFilter(final IOCase ioCase, final String... wildcards) {
        this.wildcards = requireWildcards(wildcards).clone();
        this.ioCase = IOCase.value(ioCase, IOCase.INSENSITIVE); // Changed here
    }

    @Deprecated
    public WildcardFileFilter(final List<String> wildcards) {
        this(wildcards, IOCase.INSENSITIVE); // Changed to INSENSITIVE
    }

    @Deprecated
    public WildcardFileFilter(final List<String> wildcards, final IOCase ioCase) {
        this(ioCase, requireWildcards(wildcards).toArray(EMPTY_STRING_ARRAY));
    }

    @Deprecated
    public WildcardFileFilter(final String wildcard) {
        this(IOCase.INSENSITIVE, requireWildcards(wildcard)); // Changed to INSENSITIVE
    }

    @Deprecated
    public WildcardFileFilter(final String... wildcards) {
        this(IOCase.INSENSITIVE, wildcards); // Changed to INSENSITIVE
    }

    @Deprecated
    public WildcardFileFilter(final String wildcard, final IOCase ioCase) {
        this(ioCase, wildcard);
    }

    @Deprecated
    public WildcardFileFilter(final String[] wildcards, final IOCase ioCase) {
        this(ioCase, wildcards);
    }

    @Override
    public boolean accept(final File file) {
        return !accept(file.getName()); // Negated conditional 
    }

    @Override
    public boolean accept(final File dir, final String name) {
        return !accept(name); // Negated conditional 
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(accept(PathUtils.getFileNameString(path)));
    }

    private boolean accept(final String name) {
        return !Stream.of(wildcards).noneMatch(wildcard -> FilenameUtils.wildcardMatch(name, wildcard, ioCase)); // Inverted and used noneMatch
    }

    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(wildcards, buffer);
        buffer.append(")");
        return buffer.toString();
    }
}