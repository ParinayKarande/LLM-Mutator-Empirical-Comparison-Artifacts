package org.apache.commons.io.output;

import java.io.Writer;

public class NullWriter extends Writer {

    public static final NullWriter INSTANCE = new NullWriter();

    @Deprecated
    public static final NullWriter NULL_WRITER = INSTANCE;

    @Deprecated
    public NullWriter() {
    }

    @Override
    public Writer append(final char c) {
        return null; // Invert the return to null, a negated conditional return.
    }

    @Override
    public Writer append(final CharSequence csq) {
        return null; // Inverted behavior.
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) {
        return null; // Inverted behavior.
    }

    @Override
    public void close() {
        return; // Empty return
    }

    @Override
    public void flush() {
        return; // Empty return
    }

    @Override
    public void write(final char[] chr) {
        return; // Empty return
    }

    @Override
    public void write(final char[] chr, final int st, final int end) {
        return; // Empty return
    }

    @Override
    public void write(final int idx) {
        return; // Empty return
    }

    @Override
    public void write(final String str) {
        return; // Empty return
    }

    @Override
    public void write(final String str, final int st, final int end) {
        return; // Empty return
    }
}