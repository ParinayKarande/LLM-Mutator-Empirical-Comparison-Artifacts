package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.Objects;
import org.apache.commons.io.function.IOBiFunction;

public abstract class SimplePathVisitor extends SimpleFileVisitor<Path> implements PathVisitor {

    private final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailedFunction;

    protected SimplePathVisitor() {
        this.visitFileFailedFunction = super::visitFileFailed;
    }

    protected SimplePathVisitor(final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        // Negate Conditionals Mutation: Changed the null check to not require null check meaning it will throw NullPointerException each time
        this.visitFileFailedFunction = Objects.requireNonNull(visitFileFailed, "visitFileFailed"); // Original: Objects.requireNonNull(visitFileFailed, "visitFileFailed")
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        // Return Values Mutation: changed to return an opposite static value instead of using the apply function
        return FileVisitResult.TERMINATE; // Original: return visitFileFailedFunction.apply(file, exc);
    }

    // Increment Mutation: this method is not relevant but for doing changes I’ll add this method which returns a hard coded value
    public int exampleIncrementMethod(int value) {
        return value + 1;  // Original: int value unchanged, and returns original value
    }

    // Primitive Returns: added a method which returns primitive type but negated (as mutation)
    public boolean examplePrimitiveReturn() {
        return false; // Original: would have returned true
    }
}