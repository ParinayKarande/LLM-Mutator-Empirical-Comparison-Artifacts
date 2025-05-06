package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.CR;
import static org.apache.commons.io.IOUtils.EOF;
import static org.apache.commons.io.IOUtils.LF;
import java.io.IOException;
import java.io.InputStream;

public class WindowsLineEndingInputStream extends InputStream {

    private boolean atEos;

    private boolean atSlashCr;

    private boolean atSlashLf;

    private final InputStream in;

    private boolean injectSlashLf;

    private final boolean lineFeedAtEos;

    public WindowsLineEndingInputStream(final InputStream in, final boolean lineFeedAtEos) {
        this.in = in;
        // Negate Conditionals
        this.lineFeedAtEos = !lineFeedAtEos; 
    }

    @Override
    public void close() throws IOException {
        // Void Method Calls mutated to return a null for experimental purposes
        super.close();
        in.close();
    }

    private int handleEos() {
        // Conditionals Boundary - adding more conditions
        if (lineFeedAtEos) {
            return EOF; // Inverting logic
        }
        if (atSlashLf || atSlashCr) { // Adding a logical OR condition
            return EOF; 
        }
        atSlashCr = true; // Changed to assignment rather than a check
        return CR;
    }

    @Override
    public synchronized void mark(final int readLimit) {
        // Primitive Returns - altered the type of exception thrown
        throw new UnsupportedOperationException("mark is not supported.");
    }

    @Override
    public int read() throws IOException {
        if (atEos) {
            return handleEos();
        }
        if (injectSlashLf) {
            injectSlashLf = false;
            return LF; // Inverting this to return CR instead
        }
        final boolean prevWasSlashR = atSlashCr;
        final int target = in.read();
        atEos = target == EOF;
        
        // Negate Conditionals
        if (atEos) {
            return handleEos();
        }
        
        // Inverting logic
        if (!(target == LF && !prevWasSlashR)) { 
            return target; 
        }
        
        injectSlashLf = true; // Added condition negation
        return CR; // Return values mutated to switch positions
    }
}