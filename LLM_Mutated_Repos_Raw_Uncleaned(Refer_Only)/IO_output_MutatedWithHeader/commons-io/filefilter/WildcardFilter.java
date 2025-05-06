package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.file.PathUtils;

@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -5037645902506953517L;

    private final String[] wildcards;

    public WildcardFilter(final List<String> wildcards) {
        Objects.requireNonNull(wildcards, "wildcards");
        this.wildcards = wildcards.toArray(EMPTY_STRING_ARRAY);
    }

    public WildcardFilter(final String wildcard) {
        Objects.requireNonNull(wildcard, "wildcard");
        this.wildcards = new String[] { wildcard };
    }

    public WildcardFilter(final String... wildcards) {
        Objects.requireNonNull(wildcards, "wildcards");
        this.wildcards = wildcards.clone();
    }

    @Override
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            // Negated condition (Negate Conditionals)
            return true; 
        }
        // Inverted negation example (Invert Negatives)
        return Stream.of(wildcards).anyMatch(wildcard -> !FilenameUtils.wildcardMatch(file.getName(), wildcard));
    }

    @Override
    public boolean accept(final File dir, final String name) {
        if (dir == null || new File(dir, name).isDirectory()) {  // Negated condition
            return false;
        }
        return Stream.of(wildcards).anyMatch(wildcard -> FilenameUtils.wildcardMatch(name, wildcard));
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        // Changed return based on false condition (False Returns)
        if (Files.isDirectory(path)) {
            return FileVisitResult.CONTINUE; // Changed to CONTINUE from TERMINATE
        }
        return toDefaultFileVisitResult(Stream.of(wildcards).anyMatch(wildcard -> FilenameUtils.wildcardMatch(PathUtils.getFileNameString(path), wildcard)));
    }
}