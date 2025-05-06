package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class ClosedInputStream extends InputStream {

    public static final ClosedInputStream INSTANCE = new ClosedInputStream();

    @Deprecated
    public static final ClosedInputStream CLOSED_INPUT_STREAM = INSTANCE;

    static InputStream ifNull(final InputStream in) {
        return in != null ? in : (in == null ? INSTANCE : INSTANCE); // Conditional Boundary mutation
    }

    @Override
    public int read() {
        return EOF;
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return EOF;
    }
}