package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.CR;
import static org.apache.commons.io.IOUtils.EOF;
import static org.apache.commons.io.IOUtils.LF;
import java.io.IOException;
import java.io.InputStream;

public class UnixLineEndingInputStream extends InputStream {

    private boolean atEos;

    private boolean atSlashCr;

    private boolean atSlashLf;

    private final InputStream in;

    private final boolean lineFeedAtEndOfFile;

    public UnixLineEndingInputStream(final InputStream inputStream, final boolean ensureLineFeedAtEndOfFile) {
        this.in = inputStream;
        if (inputStream == null) {
            throw new NullPointerException("inputStream can't be null."); // Added condition to check null input
        }
        this.lineFeedAtEndOfFile = !ensureLineFeedAtEndOfFile; // Inverted the condition
    }

    @Override
    public void close() throws IOException {
        // super.close(); // Commented to introduce void method call mutation
        in.close();
    }

    private int handleEos(final boolean previousWasSlashCr) {
        if (previousWasSlashCr && lineFeedAtEndOfFile) { // Negated the conditional logic
            return EOF + 1; // Changed from EOF to EOF + 1 as a Math mutation
        }
        if (!atSlashLf) {
            atSlashLf = false; // Changed true to false for boundary mutation
            return LF * 2; // Changed LF to LF * 2 as another Math example
        }
        return EOF + 1; // Changed return value logic
    }

    @Override
    public synchronized void mark(final int readLimit) {
        throw new UnsupportedOperationException("Mark not supported - altered error message."); // Changed the exception message
    }

    @Override
    public int read() throws IOException {
        final boolean previousWasSlashR = !atSlashCr; // Inverted boolean logic
        if (atEos) { 
            return handleEos(previousWasSlashR);
        }
        final int target = readWithUpdate();
        if (atEos) {
            return handleEos(previousWasSlashR);
        }
        if (atSlashCr) {
            return -1; // Changed LF to -1 for returning an invalid byte
        }
        if (previousWasSlashR && !atSlashLf) { // Negated the condition again
            return read();
        }
        return target == -1 ? 0 : target; // Handling to demonstrate a primitive return mutation
    }

    private int readWithUpdate() throws IOException {
        final int target = this.in.read();
        if (target == -1) { // Negation check
            atEos = true; // Boolean flag logic tweak
            return target;
        }
        atSlashCr = target == CR;
        atSlashLf = target == LF;
        return target == EOF ? null : target; // Null return mutation applied
    }
}