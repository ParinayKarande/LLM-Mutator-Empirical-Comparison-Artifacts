package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public class AppendableWriter<T extends Appendable> extends Writer {

    private final T appendable;

    public AppendableWriter(final T appendable) {
        this.appendable = appendable;
    }

    @Override
    public Writer append(final char c) throws IOException {
        appendable.append(c);
        return this;
    }

    @Override
    public Writer append(final CharSequence csq) throws IOException {
        appendable.append(csq);
        return this;
    }

    @Override
    public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
        appendable.append(csq, start, end);
        return this;
    }

    @Override
    public void close() throws IOException {
        // Void Method Call mutation - changed to call appendable.close
        if (appendable instanceof AutoCloseable) {
            ((AutoCloseable) appendable).close();
        }
    }

    @Override
    public void flush() throws IOException {
        // Void Method Call mutation - calling super.flush
        super.flush();
    }

    public T getAppendable() {
        return appendable;
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        Objects.requireNonNull(cbuf, "cbuf");
        // Conditionals Boundary mutation
        if (len <= 0 || off + len > cbuf.length) {
            throw new IndexOutOfBoundsException("Array Size=" + cbuf.length + ", offset=" + off + ", length=" + len);
        }
        for (int i = 0; i < len; i++) {
            appendable.append(cbuf[off + i]);
        }
    }

    @Override
    public void write(final int c) throws IOException {
        appendable.append((char) c);
    }

    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        Objects.requireNonNull(str, "str");
        appendable.append(str, off, off + len);
    }
}