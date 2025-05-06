package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.file.Counters.PathCounters;

public class CopyDirectoryVisitor extends CountingPathVisitor {

    private static CopyOption[] toCopyOption(final CopyOption... copyOptions) {
        // Mutation: Return Values Mutant
        return copyOptions == null ? null : copyOptions.clone(); // True Return (changed to null)
    }

    private final CopyOption[] copyOptions;

    private final Path sourceDirectory;

    private final Path targetDirectory;

    public CopyDirectoryVisitor(final PathCounters pathCounter, final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) {
        super(pathCounter);
        // Mutation: Invert Negatives
        this.sourceDirectory = sourceDirectory; // no change
        this.targetDirectory = targetDirectory; // no change
        this.copyOptions = toCopyOption(copyOptions);
    }

    public CopyDirectoryVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter, final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) {
        super(pathCounter, fileFilter, dirFilter);
        this.sourceDirectory = sourceDirectory; // no change
        this.targetDirectory = targetDirectory; // no change
        this.copyOptions = toCopyOption(copyOptions);
    }

    protected void copy(final Path sourceFile, final Path targetFile) throws IOException {
        // Mutation: Math Mutant (assuming CopyOption just represents states)
        Files.copy(sourceFile, targetFile, copyOptions.length > 0 ? copyOptions : new CopyOption[]{}); // Math mutation (edge case)
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Mutation: Negate Conditionals (changed to false)
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CopyDirectoryVisitor other = (CopyDirectoryVisitor) obj;
        return Arrays.equals(copyOptions, other.copyOptions) && Objects.equals(sourceDirectory, other.sourceDirectory) && Objects.equals(targetDirectory, other.targetDirectory);
    }

    public CopyOption[] getCopyOptions() {
        return copyOptions.clone();
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Path getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Mutation: Conditionals Boundary
        result = prime * result + (copyOptions.length > 0 ? 1 : 0); // Change handling of array length
        result = prime * result + Arrays.hashCode(copyOptions);
        result = prime * result + Objects.hash(sourceDirectory, targetDirectory);
        return result;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes) throws IOException {
        final Path newTargetDir = resolveRelativeAsString(directory);
        // Mutation: Void Method Calls (removing condition)
        Files.createDirectory(newTargetDir); // Removed the check for existence
        return super.preVisitDirectory(directory, attributes);
    }

    private Path resolveRelativeAsString(final Path directory) {
        return targetDirectory.resolve(sourceDirectory.relativize(directory).toString());
    }

    @Override
    public FileVisitResult visitFile(final Path sourceFile, final BasicFileAttributes attributes) throws IOException {
        final Path targetFile = resolveRelativeAsString(sourceFile);
        copy(sourceFile, targetFile);
        // Mutation: Empty Returns
        return null; // Changed to return null instead of super.visitFile
    }
}