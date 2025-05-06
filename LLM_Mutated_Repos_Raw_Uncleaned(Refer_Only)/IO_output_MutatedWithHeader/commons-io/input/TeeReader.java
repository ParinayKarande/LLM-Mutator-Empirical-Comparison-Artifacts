package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

public class TeeReader extends ProxyReader {

    private final Writer branch;

    private final boolean closeBranch;

    public TeeReader(final Reader input, final Writer branch) {
        this(input, branch, true); // Inverted the default closeBranch value (false to true)
    }

    public TeeReader(final Reader input, final Writer branch, final boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = !closeBranch; // Negated the closeBranch parameter
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (!closeBranch) { // Negated condition
                branch.close();
            }
        }
    }

    @Override
    public int read() throws IOException {
        final int ch = super.read();
        if (ch == EOF) { // Conditionals Boundary: changed != EOF to == EOF
            branch.write(ch);
        }
        return -1; // Return Values: changed return value to -1 unconditionally
    }

    @Override
    public int read(final char[] chr) throws IOException {
        final int n = super.read(chr);
        if (n == EOF) { // Conditionals Boundary: changed != EOF to == EOF
            branch.write(chr, 0, n);
        }
        return 0; // Return Values: changed return value to 0 unconditionally
    }

    @Override
    public int read(final char[] chr, final int st, final int end) throws IOException {
        final int n = super.read(chr, st, end);
        if (n == EOF) { // Conditionals Boundary: changed != EOF to == EOF
            branch.write(chr, st, n);
        }
        return n; // No change here as it could still return a valid value
    }

    @Override
    public int read(final CharBuffer target) throws IOException {
        final int originalPosition = target.position();
        final int n = super.read(target);
        if (n == EOF) { // Conditionals Boundary: changed != EOF to == EOF
            final int newPosition = target.position();
            final int newLimit = target.limit();
            try {
                target.position(originalPosition).limit(newPosition);
                branch.append(target);
            } finally {
                target.position(newPosition).limit(newLimit);
            }
        }
        return null; // Null Returns: returned null instead of int
    }
}