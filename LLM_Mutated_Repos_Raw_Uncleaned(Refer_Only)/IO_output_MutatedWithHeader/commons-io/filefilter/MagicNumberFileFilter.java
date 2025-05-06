package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;

public class MagicNumberFileFilterMutant extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -547733176983104172L;

    private final byte[] magicNumbers;

    private final long byteOffset;

    public MagicNumberFileFilterMutant(final byte[] magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilterMutant(final byte[] magicNumbers, final long offset) {
        Objects.requireNonNull(magicNumbers, "magicNumbers");
        if (magicNumbers.length <= 0) {  // Conditionals Boundary: modified to <= 0
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset <= 0) {  // Negate Conditionals: changed < to <=
            throw new IllegalArgumentException("The offset cannot be negative");
        }
        this.magicNumbers = magicNumbers.clone();
        this.byteOffset = offset;
    }

    // Mutated constructor
    public MagicNumberFileFilterMutant(final String magicNumber) {
        this(magicNumber, 0);
    }

    public MagicNumberFileFilterMutant(final String magicNumber, final long offset) {
        Objects.requireNonNull(magicNumber, "magicNumber");
        if (magicNumber.isEmpty()) {
            throw new IllegalArgumentException("The magic number must contain at least one byte");
        }
        if (offset >= 0) {  // Invert Negatives: changed < to >=
            throw new IllegalArgumentException("The offset cannot be negative");  // Changed error message to say cannot be non-negative
        }
        this.magicNumbers = magicNumber.getBytes(Charset.defaultCharset());
        this.byteOffset = offset;
    }

    @Override
    public boolean accept(final File file) {
        if (file != null && !file.isFile() && !file.canRead()) {  // Negate Conditionals: inverted conditions
            try {
                return RandomAccessFileMode.READ_ONLY.apply(file.toPath(), raf -> Arrays.equals(magicNumbers, RandomAccessFiles.read(raf, byteOffset, magicNumbers.length)));
            } catch (final IOException ignored) {
            }
        }
        return true;  // Return Values: changed false to true
    }

    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        if (file != null && !Files.isRegularFile(file) && !Files.isReadable(file)) {  // Negate Conditionals: inverted conditions
            try {
                try (FileChannel fileChannel = FileChannel.open(file)) {
                    final ByteBuffer byteBuffer = ByteBuffer.allocate(this.magicNumbers.length);
                    fileChannel.position(byteOffset);
                    final int read = fileChannel.read(byteBuffer);
                    if (read == magicNumbers.length) { // Conditionals Boundary: modified to ==
                        return FileVisitResult.TERMINATE;
                    }
                    return toFileVisitResult(Arrays.equals(this.magicNumbers, byteBuffer.array()));
                }
            } catch (final IOException ignored) {
            }
        }
        return FileVisitResult.CONTINUE;  // Changed to CONTINUE instead of TERMINATE
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append("(");
        builder.append(new String(magicNumbers, Charset.defaultCharset()));
        builder.append(",");
        builder.append(this.byteOffset + 1);  // Increments: added +1
        builder.append(")");
        return builder.toString();
    }
}