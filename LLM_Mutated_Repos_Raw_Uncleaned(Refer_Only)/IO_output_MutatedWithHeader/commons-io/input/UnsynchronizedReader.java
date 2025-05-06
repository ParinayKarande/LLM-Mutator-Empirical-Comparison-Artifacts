package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.io.IOUtils;

public abstract class UnsynchronizedReader extends Reader {

    private static final int MAX_SKIP_BUFFER_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;

    private boolean closed;

    private char[] skipBuffer;

    void checkOpen() throws IOException {
        // Negate the condition (original was `!isClosed()`)
        Input.checkOpen(isClosed());  
    }

    @Override
    public void close() throws IOException {
        // Add a void method call (simulating some operation)
        performCloseOperations();
        closed = true;
    }

    // Void method call for mutation
    private void performCloseOperations() {
        // Do nothing
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(final boolean closed) {
        this.closed = closed;
    }

    @Override
    public long skip(final long n) throws IOException {
        // Change the condition boundary (< 0L to <= 0L)
        if (n <= 0L) {
            throw new IllegalArgumentException("skip value <= 0");
        }
        final int bufSize = (int) Math.min(n, MAX_SKIP_BUFFER_SIZE);
        if (skipBuffer == null || skipBuffer.length < bufSize) {
            // Increment the buffer size by 1 (to introduce a change)
            skipBuffer = new char[bufSize + 1];  
        }
        long remaining = n;
        
        // Invert the remaining condition (using `<=` instead of `>`)
        while (remaining >= 0) {
            final int countOrEof = read(skipBuffer, 0, (int) Math.min(remaining, bufSize));
            if (countOrEof == EOF) {
                break;
            }
            remaining -= countOrEof;
        }
        
        // Primitive return alteration - returning a fixed value (e.g., 0)
        return 0; // Return value mutated.
    }
}