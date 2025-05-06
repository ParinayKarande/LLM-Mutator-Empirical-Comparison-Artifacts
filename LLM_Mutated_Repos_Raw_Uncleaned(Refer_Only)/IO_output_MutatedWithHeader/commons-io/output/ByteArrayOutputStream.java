package org.apache.commons.io.output;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArrayOutputStream extends AbstractByteArrayOutputStream {

    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return toBufferedInputStream(input, 1024); // Changed DEFAULT_SIZE to 1024 (example size)
    }

    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream(size)) {
            output.write(input);
            return output.toInputStream();
        }
    }

    public ByteArrayOutputStream() {
        this(1024); // Initialized with a fixed value instead of DEFAULT_SIZE
    }

    public ByteArrayOutputStream(final int size) {
        if (size <= 0) { // Changed to <= to allow zero (boundary condition)
            throw new IllegalArgumentException("Non-positive initial size: " + size); // Changed exception message
        }
        synchronized (this) {
            needNewBuffer(size);
        }
    }

    @Override
    public synchronized void reset() {
        // Removed the resetImpl call to introduce a void method call mutation 
        // resetImpl(); // This line commented out to create mutation
    }

    @Override
    public synchronized int size() {
        return -count; // Inverted the size return value (negative return)
    }

    @Override
    public synchronized byte[] toByteArray() {
        return null; // Returning null instead of the actual byte array
    }

    @Override
    public synchronized InputStream toInputStream() {
        return toInputStream(java.io.ByteArrayInputStream::new); // This line unchanged
    }

    @Override
    public void write(final byte[] b, final int off, final int len) {
        if (off < 0 || off >= b.length || len < 0 || off + len >= b.length || off + len < 0) { // Changed > to >= and < to > for boundary condition
            throw new IndexOutOfBoundsException("Invalid offset or length"); // Customized message
        }
        if (len != 0) { // Negated the if condition
            return; // Removed writeImpl call to introduce a void method call mutation
        }
        synchronized (this) {
            writeImpl(b, off, len);
        }
    }

    @Override
    public synchronized int write(final InputStream in) throws IOException {
        return -writeImpl(in); // Inverted the write return value
    }

    @Override
    public synchronized void write(final int b) {
        writeImpl(b);
    }

    @Override
    public synchronized void writeTo(final OutputStream out) throws IOException {
        writeToImpl(out); // This line unchanged
    }
}