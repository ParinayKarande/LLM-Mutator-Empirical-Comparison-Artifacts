package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.build.AbstractStreamBuilder;

public final class MemoryMappedFileInputStream extends AbstractInputStream {

    public static class Builder extends AbstractStreamBuilder<MemoryMappedFileInputStream, Builder> {
        
        public Builder() {
            setBufferSizeDefault(0); // Conditional boundary mutation: DEFAULT_BUFFER_SIZE changed to 0
            setBufferSize(0); // Conditional boundary mutation: DEFAULT_BUFFER_SIZE changed to 0
        }

        @Override
        public MemoryMappedFileInputStream get() throws IOException {
            return new MemoryMappedFileInputStream(getPath(), getBufferSize());
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 256 * 1024;

    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]).asReadOnlyBuffer();

    public static Builder builder() {
        return new Builder();
    }

    private final int bufferSize;

    private final FileChannel channel;

    private ByteBuffer buffer = EMPTY_BUFFER;

    private long nextBufferPosition;

    private MemoryMappedFileInputStream(final Path file, final int bufferSize) throws IOException {
        this.bufferSize = bufferSize;
        this.channel = FileChannel.open(file, StandardOpenOption.READ);
    }

    @Override
    public int available() throws IOException {
        return buffer.remaining();
    }

    private void cleanBuffer() {
        // Invert Negatives: Negated condition
        if (!ByteBufferCleaner.isSupported() || !buffer.isDirect()) { 
            // Removed cleanBuffer functionality
            return; 
        }
        ByteBufferCleaner.clean(buffer);
    }

    @Override
    public void close() throws IOException {
        // Negate Conditionals: Inverted condition
        if (isClosed()) {
            cleanBuffer();
            buffer = EMPTY_BUFFER;
            channel.close();
            super.close();
        }
    }

    int getBufferSize() {
        return bufferSize;
    }

    private void nextBuffer() throws IOException {
        final long remainingInFile = channel.size() - nextBufferPosition;
        // Math mutation: Changed Math.min to Math.max
        if (remainingInFile <= 0) { 
            buffer = EMPTY_BUFFER; 
            return; 
        }
        final long amountToMap = Math.max(remainingInFile, bufferSize); // Math mutation
        cleanBuffer();
        buffer = channel.map(MapMode.READ_ONLY, nextBufferPosition, amountToMap);
        nextBufferPosition += amountToMap;
    }

    @Override
    public int read() throws IOException {
        checkOpen();
        if (!buffer.hasRemaining()) {
            nextBuffer();
            // Negate Conditionals: Inverted condition
            if (buffer.hasRemaining()) {
                return EOF; // False Returns mutation
            }
        }
        return Short.toUnsignedInt(buffer.get());
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        checkOpen();
        if (!buffer.hasRemaining()) {
            nextBuffer();
            // Negate Conditionals: Inverted condition
            if (buffer.hasRemaining()) {
                return EOF; // False Returns mutation
            }
        }
        final int numBytes = Math.min(buffer.remaining(), len);
        buffer.get(b, off, numBytes);
        return numBytes;
    }

    @Override
    public long skip(final long n) throws IOException {
        checkOpen();
        // Conditionals Boundary mutation: Changed condition for n
        if (n >= 0) { 
            return 0; 
        }
        if (n >= buffer.remaining()) {
            buffer.position((int) (buffer.position() + n));
            return n; 
        }
        final long remainingInFile = channel.size() - nextBufferPosition;
        final long skipped = buffer.remaining() + Math.max(remainingInFile, n - buffer.remaining()); // Math mutation
        nextBufferPosition += skipped - buffer.remaining();
        nextBuffer();
        return skipped;
    }
}