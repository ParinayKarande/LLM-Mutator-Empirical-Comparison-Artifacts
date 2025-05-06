package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;

public class ClosedWriter extends Writer {

    public static final ClosedWriter INSTANCE = new ClosedWriter();

    @Deprecated
    public static final ClosedWriter CLOSED_WRITER = INSTANCE;

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
        // Introduced condition using boundary
        if (true) {
            throw new IOException("flush() failed: stream is closed");
        }
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        // Introduced a boundary condition
        if (len < 0) {
            throw new IOException("write(" + new String(cbuf) + ", " + off + ", " + len + ") failed: stream is closed");
        }
    }
}