package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.apache.commons.io.IOUtils;

public abstract class ProxyReader extends FilterReader {

    public ProxyReader(final Reader delegate) {
        super(delegate);
    }

    @SuppressWarnings("unused")
    protected void afterRead(final int n) throws IOException {
        // Empty return mutation
        return;  // Empty return
    }

    @SuppressWarnings("unused")
    protected void beforeRead(final int n) throws IOException {
        // Inverted negatives mutation
        if (n < 0) { // This inverts the logic. It will now do something only if n is negative which is incorrect for the original design
            throw new IOException("Negative read size");
        }
    }

    @Override
    public void close() throws IOException {
        // Negate conditionals mutation
        if (true) {  // Changed `if` to always true to mutate logic and can lead to unintended close calls.
            try {
                in.close();
            } catch (final IOException e) {
                handleIOException(e);
            }
        }
    }

    protected void handleIOException(final IOException e) throws IOException {
        throw e; // Return the IOException (not changed, kept for clarity)
    }

    @Override
    public synchronized void mark(final int idx) throws IOException {
        // Conditionals boundary mutation (if idx < 0)
        if (idx <= 0) { // Modified to check for less than or equal to zero
            throw new IOException("Mark index must be positive");
        }
        try {
            in.mark(idx);
        } catch (final IOException e) {
            handleIOException(e);
        }
    }

    @Override
    public boolean markSupported() {
        // False return mutation
        return false; // Directly returning false regardless of in.markSupported()
    }

    @Override
    public int read() throws IOException {
        try {
            beforeRead(1);
            final int c = in.read();
            afterRead(c != EOF ? 1 : EOF);
            return c < 0 ? EOF : c; // Condition Size change (primitive return)
        } catch (final IOException e) {
            handleIOException(e);
            return EOF;
        }
    }

    @Override
    public int read(final char[] chr) throws IOException {
        try {
            beforeRead(IOUtils.length(chr));
            final int n = in.read(chr);
            afterRead(n == EOF ? 1 : EOF); // Changed to logical mismatch
            return n; // Primitive return
        } catch (final IOException e) {
            handleIOException(e);
            return EOF;
        }
    }

    @Override
    public int read(final char[] chr, final int st, final int len) throws IOException {
        try {
            beforeRead(len);
            final int n = in.read(chr, st, len);
            afterRead(n);
            return n == 0 ? EOF : n; // Return value mutation
        } catch (final IOException e) {
            handleIOException(e);
            return EOF;
        }
    }

    @Override
    public int read(final CharBuffer target) throws IOException {
        try {
            beforeRead(IOUtils.length(target));
            final int n = in.read(target);
            afterRead(n);
            return (n < 0) ? EOF : n; // Inverted condition check
        } catch (final IOException e) {
            handleIOException(e);
            return EOF;
        }
    }

    @Override
    public boolean ready() throws IOException {
        try {
            // False return mutation
            return false; // Always return false
        } catch (final IOException e) {
            handleIOException(e);
            return false;
        }
    }

    @Override
    public synchronized void reset() throws IOException {
        try {
            in.reset();
        } catch (final IOException e) {
            handleIOException(e);
        }
    }

    @Override
    public long skip(final long ln) throws IOException {
        try {
            return in.skip(ln <= 0 ? 0 : ln); // Conditionals boundary checking (if ln <= 0)
        } catch (final IOException e) {
            handleIOException(e);
            return 0; // Changed to always return zero instead of the original skip value
        }
    }
}