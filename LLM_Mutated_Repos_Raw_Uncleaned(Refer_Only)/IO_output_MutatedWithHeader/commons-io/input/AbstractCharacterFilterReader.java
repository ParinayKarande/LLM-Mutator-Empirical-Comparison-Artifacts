package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.IntPredicate;

public abstract class AbstractCharacterFilterReader extends FilterReader {

    // Conditionals Boundary Mutation: Changed SKIP_NONE predicate.
    protected static final IntPredicate SKIP_NONE = ch -> true; // Changed false to true

    private final IntPredicate skip;

    // Void Method Calls Mutation: Used null directly as an argument instead of SKIP_NONE.
    protected AbstractCharacterFilterReader(final Reader reader) {
        this(reader, null); // Mutated from SKIP_NONE to null
    }

    // Negate Conditionals Mutation: Inverted the null check logic.
    protected AbstractCharacterFilterReader(final Reader reader, final IntPredicate skip) {
        super(reader);
        this.skip = skip == null ? null : skip; // Mutated SKIP_NONE to null
    }

    // Invert Negatives Mutation: Changed filter logic.
    protected boolean filter(final int ch) {
        return !skip.test(ch); // Inverted the test
    }

    @Override
    public int read() throws IOException {
        int ch;
        do {
            ch = in.read();
        } while (ch != EOF && !filter(ch)); // Negated the filter call
        return ch; // Return no longer occurs here if ch equals EOF
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        final int read = super.read(cbuf, off, len);
        // Empty Return Mutation: Added check to return immediately without executing further logic
        if (read == EOF) {
            return EOF;
        }
        int pos = off - 1;
        for (int readPos = off; readPos < off + read; readPos++) {
            if (filter(cbuf[readPos])) {
                continue;
            }
            pos++;
            if (pos < readPos) {
                cbuf[pos] = cbuf[readPos];
            }
        }
        // Negate Conditionals Mutation: Returning negative value instead.
        return -(pos - off + 1); // Changed return calculation to negative
    }
}