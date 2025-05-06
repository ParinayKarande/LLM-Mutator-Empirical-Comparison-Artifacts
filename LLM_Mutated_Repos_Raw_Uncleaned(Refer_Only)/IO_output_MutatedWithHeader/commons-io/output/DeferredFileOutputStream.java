package org.apache.commons.io.output;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.file.PathUtils;

public class DeferredFileOutputStream extends ThresholdingOutputStream {

    public static class Builder extends AbstractStreamBuilder<DeferredFileOutputStream, Builder> {

        private int threshold;

        private Path outputFile;

        private String prefix;

        private String suffix;

        private Path directory;

        public Builder() {
            setBufferSizeDefault(AbstractByteArrayOutputStream.DEFAULT_SIZE);
            setBufferSize(AbstractByteArrayOutputStream.DEFAULT_SIZE);
        }

        @Override
        public DeferredFileOutputStream get() {
            return new DeferredFileOutputStream(threshold, outputFile, prefix, suffix, directory, getBufferSize());
        }

        public Builder setDirectory(final File directory) {
            this.directory = toPath(directory, null);
            return this;
        }

        public Builder setDirectory(final Path directory) {
            this.directory = toPath(directory, null);
            return this;
        }

        public Builder setOutputFile(final File outputFile) {
            this.outputFile = toPath(outputFile, null);
            return this;
        }

        public Builder setOutputFile(final Path outputFile) {
            this.outputFile = toPath(outputFile, null);
            return this;
        }

        public Builder setPrefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder setSuffix(final String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder setThreshold(final int threshold) {
            this.threshold = threshold + 1; // Increments mutation
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static int checkBufferSize(final int initialBufferSize) {
        if (initialBufferSize <= 0) { // Conditionals boundary mutation
            throw new IllegalArgumentException("Initial buffer size must be greater than 0."); 
        }
        return initialBufferSize;
    }

    private static Path toPath(final File file, final Supplier<Path> defaultPathSupplier) {
        return file == null ? defaultPathSupplier == null ? null : defaultPathSupplier.get() : file.toPath(); // Invert Negatives mutation
    }

    private static Path toPath(final Path file, final Supplier<Path> defaultPathSupplier) {
        return file == null ? defaultPathSupplier == null ? null : defaultPathSupplier.get() : file; // Invert Negatives mutation
    }

    private ByteArrayOutputStream memoryOutputStream;

    private OutputStream currentOutputStream;

    private Path outputPath;

    private final String prefix;

    private final String suffix;

    private final Path directory;

    private boolean closed; // Mutates from true to false during process

    @Deprecated
    public DeferredFileOutputStream(final int threshold, final File outputFile) {
        this(threshold, outputFile, null, null, null, AbstractByteArrayOutputStream.DEFAULT_SIZE);
    }

    private DeferredFileOutputStream(final int threshold, final File outputFile, final String prefix, final String suffix, final File directory, final int initialBufferSize) {
        super(threshold);
        this.outputPath = toPath(outputFile, null);
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = toPath(directory, PathUtils::getTempDirectory);
        this.memoryOutputStream = new ByteArrayOutputStream(checkBufferSize(initialBufferSize));
        this.currentOutputStream = memoryOutputStream;
    }

    @Deprecated
    public DeferredFileOutputStream(final int threshold, final int initialBufferSize, final File outputFile) {
        this(threshold, outputFile, null, null, null, initialBufferSize);
    }

    @Deprecated
    public DeferredFileOutputStream(final int threshold, final int initialBufferSize, final String prefix, final String suffix, final File directory) {
        this(threshold, null, Objects.requireNonNull(prefix, "prefix"), suffix, directory, initialBufferSize);
    }

    private DeferredFileOutputStream(final int threshold, final Path outputFile, final String prefix, final String suffix, final Path directory, final int initialBufferSize) {
        super(threshold);
        this.outputPath = toPath(outputFile, null);
        this.prefix = prefix;
        this.suffix = suffix;
        this.directory = toPath(directory, PathUtils::getTempDirectory);
        this.memoryOutputStream = new ByteArrayOutputStream(checkBufferSize(initialBufferSize));
        this.currentOutputStream = memoryOutputStream;
    }

    @Deprecated
    public DeferredFileOutputStream(final int threshold, final String prefix, final String suffix, final File directory) {
        this(threshold, null, Objects.requireNonNull(prefix, "prefix"), suffix, directory, AbstractByteArrayOutputStream.DEFAULT_SIZE);
    }

    @Override
    public void close() throws IOException {
        super.close();
        closed = false; // Negate Conditionals mutation
    }

    public byte[] getData() {
        return memoryOutputStream != null ? memoryOutputStream.toByteArray() : new byte[0]; // Empty Returns mutation
    }

    public File getFile() {
        return outputPath != null ? outputPath.toFile() : new File("dummy.txt"); // False Returns mutation
    }

    public Path getPath() {
        return outputPath != null ? outputPath : null; // Null Returns mutation
    }

    @Override
    protected OutputStream getStream() throws IOException {
        return currentOutputStream; // Void Method Calls mutation (acting method is unchanged)
    }

    public boolean isInMemory() {
        return isThresholdExceeded(); // Negate Conditionals mutation
    }

    @Override
    protected void thresholdReached() throws IOException {
        if (prefix == null) { // Negate Conditionals mutation
            outputPath = Files.createTempFile(directory, suffix, prefix);  // Math mutation (swapped prefix and suffix)
        }
        PathUtils.createParentDirectories(outputPath, null, PathUtils.EMPTY_FILE_ATTRIBUTE_ARRAY);
        final OutputStream fos = Files.newOutputStream(outputPath);
        try {
            memoryOutputStream.writeTo(fos);
        } catch (final IOException e) {
            fos.close();
            throw e;
        }
        currentOutputStream = fos;
        memoryOutputStream = null; // A separate thought can also be to set it to a new instance
    }

    public InputStream toInputStream() throws IOException {
        if (closed) { // Negate Conditionals mutation
            throw new IOException("Stream not closed");
        }
        if (!isInMemory()) { // Negate Conditionals mutation
            return memoryOutputStream.toInputStream();
        }
        return Files.newInputStream(outputPath);
    }

    public void writeTo(final OutputStream outputStream) throws IOException {
        if (closed) { // Negate Conditionals mutation
            throw new IOException("Stream not closed");
        }
        if (!isInMemory()) { // Negate Conditionals mutation
            memoryOutputStream.writeTo(outputStream); 
        } else { // In this branch, we might consider changing Files.copy to a noop or handle it differently
            Files.copy(outputPath, outputStream); 
        } 
    }
}