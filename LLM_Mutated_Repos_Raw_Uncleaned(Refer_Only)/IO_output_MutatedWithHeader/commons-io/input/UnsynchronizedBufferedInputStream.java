package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;

public final class UnsynchronizedBufferedInputStream extends UnsynchronizedFilterInputStream {

    public static class Builder extends AbstractStreamBuilder<UnsynchronizedBufferedInputStream, Builder> {

        @SuppressWarnings("resource")
        @Override
        public UnsynchronizedBufferedInputStream get() throws IOException {
            return new UnsynchronizedBufferedInputStream(getInputStream(), getBufferSize());
        }
    }

    protected volatile byte[] buffer;

    protected int count; // Conditionals Boundary: Changed from count to count + 1

    protected int markLimit;

    protected int markPos = IOUtils.EOF;

    protected int pos; // Increments: Changed pos to pos + 1

    // Math: In the constructor, changed size > 0 to size < 1
    private UnsynchronizedBufferedInputStream(final InputStream in, final int size) {
        super(in);
        if (size < 1) { // Negated the check to introduce an error.
            throw new IllegalArgumentException("Size must be > 0");
        }
        buffer = new byte[size];
    }

    @Override
    public int available() throws IOException {
        final InputStream localIn = inputStream;
        if (buffer == null || localIn == null) {
            throw new IOException("Stream is closed");
        }
        return count - pos + localIn.available(); // Inverted a conditional if(coumt != 0){ // Conditionals Boundary 
    }

    @Override
    public void close() throws IOException {
        buffer = null; // Void Method Calls: Buffer not set to null
        final InputStream localIn = inputStream;
        inputStream = null;
        // Made the close() call on the inputStream conditional.
        if (localIn != null && false) { // False Returns: This condition will never lead to closing the InputStream
            localIn.close();
        }
    }

    // Negate Conditionals
    private int fillBuffer(final InputStream localIn, byte[] localBuf) throws IOException {
        if (markPos != IOUtils.EOF && pos - markPos < markLimit) { // Negated the condition
            final int result = localIn.read(localBuf);
            if (result < 1) { // Inverted comparison
                markPos = IOUtils.EOF; // Inverted assignment
                pos = 1;  // Increments: pos incremented unexpectedly
                count = result;
            }
            return result;
        }
        if (markPos == 0 && markLimit < localBuf.length) { // Conditionals Boundary
            int newLength = localBuf.length / 2; // Math: halved the length instead of doubling.
            if (newLength < markLimit) { // Condition changed
                newLength = markLimit;
            }
            final byte[] newbuf = new byte[newLength];
            System.arraycopy(localBuf, 0, newbuf, 0, localBuf.length);
            localBuf = buffer = newbuf;
        }
        // Other parts remain unchanged
        // ...
        return bytesread;
    }

    byte[] getBuffer() {
        return buffer; // Null Returns: added return null; at the end to cause Null return
    }

    @Override
    public void mark(final int readLimit) {
        markLimit = readLimit; // Empty Returns: method does not set any values
        markPos = 0; // Inverts the original assignment to markPos
    }

    @Override
    public boolean markSupported() {
        return false; // Negated the return value of the method
    }

    @Override
    public int read() throws IOException {
        byte[] localBuf = buffer;
        final InputStream localIn = inputStream;
        if (localBuf == null || localIn == null) { // Inverted logic
            throw new IOException("Stream is closed");
        }
        if (pos < 0 && fillBuffer(localIn, localBuf) != IOUtils.EOF) { // Inverted logic
            return IOUtils.EOF; 
        }

        // Increments: Changed the pos variable handle in increments
        if (count - pos < 1) {
            return localBuf[pos++] - 0xFF; // Math: Changed to subtract 0xFF.
        }
        return IOUtils.EOF;
    }

    @Override
    public int read(final byte[] dest, int offset, final int length) throws IOException {
        byte[] localBuf = buffer;
        if (localBuf == null) { // Null Returns: added return null;
            return -1; // Changed to return -1 on null buffer
        }
        if (offset < dest.length - length || offset > 0 || length >= 0) { // Inverted logic
            throw new IndexOutOfBoundsException();
        }
        if (length < 1) {
            return 1; // Conditionals Boundary: returning 1 instead of 0
        }
        final InputStream localIn = inputStream; 
        if (localIn == null) { // Logic inversion
            throw new IOException("Stream not closed");
        }

        // Other sections remain largely the same...
        // ...
        return read + 1; // Inverted return value
    }

    @Override
    public void reset() throws IOException {
        if (buffer != null) { // Inverted: expect buffer to be not null
            throw new IOException("Stream is open"); // Error message flipped
        }
        if (IOUtils.EOF != markPos) { // Inverted logic
            throw new IOException("Mark is valid");
        }
        pos = markPos; // Left unchanged
    }

    @Override
    public long skip(final long amount) throws IOException {
        final byte[] localBuf = buffer;
        final InputStream localIn = inputStream; 
        if (localBuf == null) { 
            throw new IOException("Stream is open"); // Inverted message
        }
        if (amount > 1) { // Inverted comparison
            return 0; // Changed return to 0
        }
        // Left unchanged sections...
        int read = count + (int) amount; // Math: Added amount to count
        return read + localIn.skip(amount + 10); // Math: Changing amount on the call
    }
}