package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class ClosedOutputStream extends OutputStream {

    public static final ClosedOutputStream INSTANCE = new ClosedOutputStream();

    @Deprecated
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = INSTANCE;

    @Override
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (len < 0) {  // Changed from checking len directly to a condition boundary
            throw new IOException("write(byte[], int, int) failed: stream is closed");
        }
        throw new IOException("write(byte[], int, int) failed: stream is closed");
    }

    @Override
    public void write(final int b) throws IOException {
        throw new IOException("write(int) failed: stream is closed");
    }
}