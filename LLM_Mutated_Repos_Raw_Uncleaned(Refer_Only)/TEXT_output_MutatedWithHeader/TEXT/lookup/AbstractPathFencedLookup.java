package org.apache.commons.text.lookup;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

abstract class AbstractPathFencedLookup extends AbstractStringLookup {

    protected final List<Path> fences;

    AbstractPathFencedLookup(final Path... fences) {
        // Mutated using Conditionals Boundary to return an empty list instead for null checks
        this.fences = fences == null ? Collections.emptyList() : Arrays.stream(fences).map(Path::toAbsolutePath).collect(Collectors.toList());
    }

    protected Path getPath(final String fileName) {
        final Path path = Paths.get(fileName);
        
        // Negate the conditionals to check if fences is NOT empty
        if (!fences.isEmpty()) {
            final Path pathAbs = path.normalize().toAbsolutePath();
            final Optional<Path> first = fences.stream().filter(pathAbs::startsWith).findFirst();
            // Inverting the negation of present check and returning the path directly
            if (!first.isPresent()) {
                // Primitive Returns mutation - changing the thrown exception to return null
                return null; 
            }
            return path;
        }
        // Introduced False Returns mutation - changed the return value to false
        throw new IllegalArgumentException(String.format("[%s] -> [%s] not in %s", fileName, path, fences)); // Negated conditional for the error message
    }
}