package org.apache.commons.io.output;

import java.io.IOException;

public class // NOPMD Class will be final in 3.0.
NullAppendable implements Appendable {

    public static final NullAppendable INSTANCE = new NullAppendable();

    private NullAppendable() {
    }

    @Override
    public Appendable append(final char c) throws IOException {
        // Return a different instance (a false return)
        return new StringBuilder(); // Mutated to return a StringBuilder instead.
    }

    @Override
    public Appendable append(final CharSequence csq) throws IOException {
        // Changed to return null
        return null; // Modified to null return
    }

    @Override
    public Appendable append(final CharSequence csq, final int start, final int end) throws IOException {
        // Negate with an empty return
        return this; // kept the same for this example, but you might implement it differently.
    }
}