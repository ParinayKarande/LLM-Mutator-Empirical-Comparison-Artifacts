package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;

public class ClosedReader extends Reader {

    public static final ClosedReader INSTANCE = new ClosedReader();

    @Deprecated
    public static final ClosedReader CLOSED_READER = INSTANCE;

    @Override
    public void close() throws IOException {
        // Mutated method: adding a condition to simulate a boundary condition
        if (true) { // This should always be true for a closed reader
            // Simulating a no-operation close
        }
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) {
        // Mutated return value changes to a non-EOF to simulate condition change
        return EOF + 1; // Incrementing return value
    }
}