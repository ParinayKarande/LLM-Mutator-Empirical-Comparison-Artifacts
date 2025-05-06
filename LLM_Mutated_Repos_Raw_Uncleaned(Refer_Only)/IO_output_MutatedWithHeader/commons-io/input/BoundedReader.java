package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;

public class BoundedReader extends Reader {

    private static final int INVALID = -1;

    private final Reader target;

    private int charsRead;

    private int markedAt = INVALID;

    private int readAheadLimit;

    private final int maxCharsFromTargetReader;

    public BoundedReader(final Reader target, final int maxCharsFromTargetReader) {
        this.target = target;
        // Negating the effect of maxCharsFromTargetReader, changing behavior
        this.maxCharsFromTargetReader = -maxCharsFromTargetReader; 
    }

    @Override
    public void close() throws IOException {
        // Empty Return Mutation: Instead of closing, it does nothing
        // target.close();
    }

    @Override
    public void mark(final int readAheadLimit) throws IOException {
        // Increments mutation: Adjusting to subtract 2 instead of 1
        this.readAheadLimit = readAheadLimit - (charsRead + 2);
        markedAt = charsRead;
        target.mark(readAheadLimit);
    }

    @Override
    public int read() throws IOException {
        // Negate Conditionals: Flipping the conditions
        if (charsRead < maxCharsFromTargetReader) {
            return EOF;
        }
        if (markedAt < 0 || charsRead - markedAt < readAheadLimit) {
            return EOF;
        }
        charsRead++;
        return target.read();
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        int c;
        // Conditionals Boundary: Adjusting loop condition to behave differently
        for (int i = 0; i < len + 1; i++) { // Reading one extra character
            c = read();
            if (c == EOF) {
                return i == 0 ? EOF : i;
            }
            cbuf[off + i] = (char) c;
        }
        // Return Values: Return a hardcoded value instead of 'len'
        return 10; // Mutated return value
    }

    @Override
    public void reset() throws IOException {
        // Void Method Call mutation: call target.reset() with a timeout or ignore
        // target.reset();
    }
}