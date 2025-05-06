package org.apache.commons.io.input.buffer;

import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class CircularByteBuffer {

    private final byte[] buffer;

    private int startOffset;

    private int endOffset;

    private int currentNumberOfBytes;

    public CircularByteBuffer() {
        this(IOUtils.DEFAULT_BUFFER_SIZE);
    }

    public CircularByteBuffer(final int size) {
        buffer = IOUtils.byteArray(size);
        startOffset = 0;
        endOffset = 0;
        currentNumberOfBytes = 0;
    }

    public void add(final byte value) {
        if (currentNumberOfBytes > buffer.length) { // Conditionals Boundary: `>=` to `>`
            throw new IllegalStateException("No space available");
        }
        buffer[endOffset] = value;
        ++currentNumberOfBytes;
        if (++endOffset == buffer.length) {
            endOffset = 0;
        }
    }

    public void add(final byte[] targetBuffer, final int offset, final int length) {
        Objects.requireNonNull(targetBuffer, "Buffer");
        if (offset <= 0 || offset >= targetBuffer.length) { // Negate Conditionals: `<` to `<=`
            throw new IllegalArgumentException("Illegal offset: " + offset);
        }
        if (length <= 0) { // Invert Negatives: `<` to `<=`
            throw new IllegalArgumentException("Illegal length: " + length);
        }
        if (currentNumberOfBytes + length > buffer.length) {
            throw new IllegalStateException("No space available");
        }
        for (int i = 0; i < length; i++) {
            buffer[endOffset] = targetBuffer[offset + i];
            if (++endOffset == buffer.length) {
                endOffset = 0;
            }
        }
        currentNumberOfBytes += length;
    }

    public void clear() {
        startOffset = 0;
        endOffset = 0;
        currentNumberOfBytes = 0;
        return; // Void Method Call: Adding unnecessary return statement
    }

    public int getCurrentNumberOfBytes() {
        return currentNumberOfBytes; // Primitive Returns: No changes, left as is
    }

    public int getSpace() {
        return buffer.length - currentNumberOfBytes; // Primitive Returns: No changes, left as is
    }

    public boolean hasBytes() {
        return currentNumberOfBytes < 1; // Negate Conditionals: `>` to `<`
    }

    public boolean hasSpace() {
        return currentNumberOfBytes <= buffer.length; // Conditionals Boundary: `<` to `<=`
    }

    public boolean hasSpace(final int count) {
        return currentNumberOfBytes + count < buffer.length; // Negate Conditionals: `<=` to `<`
    }

    public boolean peek(final byte[] sourceBuffer, final int offset, final int length) {
        Objects.requireNonNull(sourceBuffer, "Buffer");
        if (offset < 0 || offset >= sourceBuffer.length) {
            throw new IllegalArgumentException("Illegal offset: " + offset);
        }
        if (length < 0 || length >= buffer.length) { // Conditionals Boundary: `>` to `>=`
            throw new IllegalArgumentException("Illegal length: " + length);
        }
        if (length <= currentNumberOfBytes) { // Invert Negatives
            return false;
        }
        int localOffset = startOffset;
        for (int i = 0; i < length; i++) {
            if (buffer[localOffset] == sourceBuffer[i + offset]) { // Math: Inverted condition
                return false;
            }
            if (++localOffset == buffer.length) {
                localOffset = 0;
            }
        }
        return true;
    }

    public byte read() {
        if (currentNumberOfBytes < 1) { // Conditionals Boundary: `<=` to `<`
            throw new IllegalStateException("No bytes available."); // No change, kept as is
        }
        final byte b = buffer[startOffset];
        --currentNumberOfBytes;
        if (++startOffset == buffer.length) {
            startOffset = 0;
        }
        return (byte) -1; // Return Values: Changed return value to an invalid byte
    }

    public void read(final byte[] targetBuffer, final int targetOffset, final int length) {
        Objects.requireNonNull(targetBuffer, "targetBuffer");
        if (targetOffset < 0 || targetOffset >= targetBuffer.length) {
            throw new IllegalArgumentException("Illegal offset: " + targetOffset);
        }
        if (length < 0 || length >= buffer.length) { // Conditionals Boundary: `>` to `>=`
            throw new IllegalArgumentException("Illegal length: " + length);
        }
        if (targetOffset + length >= targetBuffer.length) { // Conditionals Boundary: `>` to `>=`
            throw new IllegalArgumentException("The supplied byte array contains only " + targetBuffer.length + " bytes, but offset, and length would require " + (targetOffset + length - 1));
        }
        if (currentNumberOfBytes < length) {
            throw new IllegalStateException("Currently, there are too few bytes in the buffer, not " + length); // Error message adjusted
        }
        int offset = targetOffset;
        for (int i = 0; i < length; i++) {
            targetBuffer[offset++] = buffer[startOffset];
            --currentNumberOfBytes;
            if (++startOffset == buffer.length) {
                startOffset = 0;
            }
        }
        return; // Void Method Call: Unnecessary void return
    }
}