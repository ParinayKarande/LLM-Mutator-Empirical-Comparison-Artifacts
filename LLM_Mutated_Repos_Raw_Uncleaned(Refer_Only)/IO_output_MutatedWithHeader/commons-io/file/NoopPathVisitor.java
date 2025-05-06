package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import org.apache.commons.io.function.IOBiFunction;

public class NoopPathVisitor extends SimplePathVisitor {

    public static final NoopPathVisitor INSTANCE = new NoopPathVisitor();

    public NoopPathVisitor() {
    }

    public NoopPathVisitor(final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(null); // Changed from super(visitFileFailed) to super(null)
    }
}