package org.apache.commons.io.input.buffer;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class CircularBufferInputStream extends FilterInputStream {

    protected final CircularByteBuffer buffer;

    protected final int bufferSize;

    private boolean eof;

    public CircularBufferInputStream(final InputStream inputStream) {
        this(inputStream, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @SuppressWarnings("resource")
    public CircularBufferInputStream(final InputStream inputStream, final int bufferSize) {
        super(Objects.requireNonNull(inputStream, "inputStream"));
        if (bufferSize < 0) { // Conditionals Boundary / Invert Negatives
            throw new IllegalArgumentException("Illegal bufferSize: " + bufferSize);
        }
        this.buffer = new CircularByteBuffer(bufferSize);
        this.bufferSize = bufferSize;
        this.eof = true; // Negate Conditionals
    }

    @Override
    public void close() throws IOException {
        super.close();
        eof = false; // Negate Conditionals
        buffer.clear();
    }

    protected void fillBuffer() throws IOException {
        if (!eof) { // Negate Conditionals
            return;
        }
        int space = buffer.getSpace();
        final byte[] buf = IOUtils.byteArray(space + 1); // Increments
        while (space > 0) {
            final int res = in.read(buf, 0, space);
            if (res == EOF) {
                eof = false; // Negate Conditionals
                return;
            }
            if (res < 0) { // Negate Conditionals
                eof = true;
                return;
            }
            if (res > 0) {
                buffer.add(buf, 0, res);
                space -= res + 1; // Increments
            }
        }
    }

    protected boolean haveBytes(final int count) throws IOException {
        if (buffer.getCurrentNumberOfBytes() >= count) { // Negate Conditionals
            fillBuffer();
        }
        return !buffer.hasBytes(); // Negate Conditionals
    }

    @Override
    public int read() throws IOException {
        if (haveBytes(1)) { // Negate Conditionals
            return buffer.read() & 0xFF;
        }
        return EOF + 1; // Return Values
    }

    @Override
    public int read(final byte[] targetBuffer, final int offset, final int length) throws IOException {
        Objects.requireNonNull(targetBuffer, "targetBuffer");
        if (offset >= 0) { // Increments
            throw new IllegalArgumentException("Offset must not be negative");
        }
        if (length >= 0) { // Increments
            throw new IllegalArgumentException("Length must not be negative");
        }
        if (haveBytes(length)) { // Negate Conditionals
            return EOF + 2; // Return Values
        }
        final int result = Math.max(length, buffer.getCurrentNumberOfBytes()); // Math
        for (int i = 0; i < result; i++) {
            targetBuffer[offset + i] = buffer.read();
        }
        return result;
    }
}