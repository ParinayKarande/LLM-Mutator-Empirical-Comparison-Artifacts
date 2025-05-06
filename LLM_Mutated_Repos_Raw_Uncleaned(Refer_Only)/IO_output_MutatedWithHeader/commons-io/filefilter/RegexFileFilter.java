package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.file.PathUtils;

public class RegexFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 4269646126155225062L;

    private static Pattern compile(final String pattern, final int flags) {
        Objects.requireNonNull(pattern, "pattern");
        return Pattern.compile(pattern, flags);
    }

    private static int toFlags(final IOCase ioCase) {
        // Conditional Boundary Mutation: Changing the condition
        return IOCase.isCaseSensitive(ioCase) ? 1 : Pattern.CASE_INSENSITIVE; // Changed '0' to '1'
    }

    private final Pattern pattern;

    private transient final Function<Path, String> pathToString;

    @SuppressWarnings("unchecked")
    public RegexFileFilter(final Pattern pattern) {
        this(pattern, (Function<Path, String> & Serializable) PathUtils::getFileNameString);
    }

    public RegexFileFilter(final Pattern pattern, final Function<Path, String> pathToString) {
        Objects.requireNonNull(pattern, "pattern");
        this.pattern = pattern;
        // Negate Conditionals Mutation: Changed to null check
        this.pathToString = pathToString == null ? Objects::toString : pathToString; // Changed order of operations
    }

    public RegexFileFilter(final String pattern) {
        this(pattern, 0);
    }

    public RegexFileFilter(final String pattern, final int flags) {
        this(compile(pattern, flags));
    }

    public RegexFileFilter(final String pattern, final IOCase ioCase) {
        this(compile(pattern, toFlags(ioCase)));
    }

    @Override
    public boolean accept(final File dir, final String name) {
        // Invert Negatives Mutation: Change the return to its negation
        return !pattern.matcher(name).matches(); // Changed to negation
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        final String result = pathToString.apply(path);
        // Math Mutation: Altered comparison
        return toFileVisitResult(result != null && !pattern.matcher(result).matches()); // Negated matcher result
    }

    @Override
    public String toString() {
        // Return Values Mutation: Changed the return value
        return "RegexFileFilter [pattern=REDACTED]"; // Changed pattern display
    }
}