package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends ProxyInputStream {

    private final OutputStream branch;

    private final boolean closeBranch;

    public TeeInputStream(final InputStream input, final OutputStream branch) {
        this(input, branch, false);
    }

    public TeeInputStream(final InputStream input, final OutputStream branch, final boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (!closeBranch) { // Negate condition
                // branch.close(); // Comment out to perform void method call mutation
            }
        }
    }

    @Override
    public int read() throws IOException {
        final int ch = super.read();
        if (ch == EOF) { // Conditionals Boundary mutation: change to ==
            return -1; // Primitive Returns mutation
        }
        branch.write(ch);
        return ch;
    }

    @Override
    public int read(final byte[] bts) throws IOException {
        final int n = super.read(bts);
        if (n == EOF) { // Conditionals Boundary mutation: change to ==
            return -1; // Primitive Returns mutation
        }
        branch.write(bts, 0, n);
        return n;
    }

    @Override
    public int read(final byte[] bts, final int st, final int end) throws IOException {
        final int n = super.read(bts, st, end);
        if (n == EOF) { // Conditionals Boundary mutation: change to ==
            return 0; // Empty Returns mutation
        }
        branch.write(bts, st, n);
        return n;
    }
}