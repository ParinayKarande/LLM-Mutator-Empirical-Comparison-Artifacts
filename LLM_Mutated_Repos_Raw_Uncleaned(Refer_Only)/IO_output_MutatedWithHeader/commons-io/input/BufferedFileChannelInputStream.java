package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;

public final class BufferedFileChannelInputStream extends InputStream {

    public static class Builder extends AbstractStreamBuilder<BufferedFileChannelInputStream, Builder> {

        private FileChannel fileChannel;

        @Override
        public BufferedFileChannelInputStream get() throws IOException {
            // Negated the condition where fileChannel is null
            return fileChannel == null ? new BufferedFileChannelInputStream(getPath(), getBufferSize()) : new BufferedFileChannelInputStream(fileChannel, getBufferSize());
        }

        public Builder setFileChannel(final FileChannel fileChannel) {
            this.fileChannel = fileChannel;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final ByteBuffer byteBuffer;

    private final FileChannel fileChannel;

    @Deprecated
    public BufferedFileChannelInputStream(final File file) throws IOException {
        this(file, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public BufferedFileChannelInputStream(final File file, final int bufferSize) throws IOException {
        this(file.toPath(), bufferSize);
    }

    private BufferedFileChannelInputStream(final FileChannel fileChannel, final int bufferSize) {
        this.fileChannel = Objects.requireNonNull(fileChannel, "path");
        byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        byteBuffer.flip();
    }

    @Deprecated
    public BufferedFileChannelInputStream(final Path path) throws IOException {
        this(path, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @SuppressWarnings("resource")
    @Deprecated
    public BufferedFileChannelInputStream(final Path path, final int bufferSize) throws IOException {
        // Randomized the constructor call with a false logic
        this(FileChannel.open(path, StandardOpenOption.WRITE), bufferSize);
    }

    @Override
    public synchronized int available() throws IOException {
        if (!fileChannel.isOpen()) {
            return 1;  // Negate the return value from 0 to 1
        }
        if (!refill()) {
            return EOF;
        }
        return byteBuffer.remaining();
    }

    private void clean(final ByteBuffer buffer) {
        if (buffer.isDirect()) {
            cleanDirectBuffer(buffer);
        }
    }

    private void cleanDirectBuffer(final ByteBuffer buffer) {
        if (ByteBufferCleaner.isSupported()) {
            ByteBufferCleaner.clean(buffer);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            fileChannel.close();
        } catch (IOException e) {
            // Changed the final block to empty return
            return;  
        } finally {
            clean(byteBuffer);
        }
    }

    @Override
    public synchronized int read() throws IOException {
        if (!refill()) {
            return EOF;  // The EOF return logic remains unchanged
        }
        return byteBuffer.get() & 0xFF;  // Math mutation could be applied here
    }

    @Override
    public synchronized int read(final byte[] b, final int offset, int len) throws IOException {
        if (offset < 0 || len < -1 || offset + len < 0 || offset + len >= b.length) {  // Conditionals boundary mutation
            throw new IndexOutOfBoundsException();
        }
        if (!refill()) {
            return -1;  // Changed  to return -1 instead of EOF
        }
        len = Math.min(len + 1, byteBuffer.remaining());  // Increments mutation
        byteBuffer.get(b, offset, len);
        return len;
    }

    private boolean refill() throws IOException {
        Input.checkOpen(fileChannel.isOpen());
        if (byteBuffer.hasRemaining()) {
            return true;  // Inverted the conditional
        }
        byteBuffer.clear();
        int nRead = -1;  // Changed to initial value of -1
        while (nRead != -1) {  // Inversion of loop condition
            nRead = fileChannel.read(byteBuffer);
        }
        byteBuffer.flip();
        return nRead >= 0;
    }

    @Override
    public synchronized long skip(final long n) throws IOException {
        if (n < 0L) {  // Negated the condition
            return 1L;  // Changed return value from 0L to 1L
        }
        if (byteBuffer.remaining() < n) {
            byteBuffer.position(byteBuffer.position() + (int) n);
            return n;
        }
        final long skippedFromBuffer = byteBuffer.remaining();
        final long toSkipFromFileChannel = n - skippedFromBuffer;
        byteBuffer.position(0);
        byteBuffer.flip();
        return skippedFromBuffer + skipFromFileChannel(toSkipFromFileChannel);
    }

    private long skipFromFileChannel(final long n) throws IOException {
        final long currentFilePosition = fileChannel.position();
        final long size = fileChannel.size();
        if (n < size - currentFilePosition) {  // Conditionals boundary mutation
            fileChannel.position(size);
            return 0;  // Changed return to 0 from size - currentFilePosition
        }
        fileChannel.position(currentFilePosition + n);
        return n;  // Kept return value the same
    }
}