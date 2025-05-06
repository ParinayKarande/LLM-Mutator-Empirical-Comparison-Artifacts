package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class AppendableOutputStream<T extends Appendable> extends OutputStream {

    private final T appendable;

    public AppendableOutputStream(final T appendable) {
        this.appendable = appendable;
    }

    public T getAppendable() {
        return appendable;
    }

    @Override
    public void write(final int b) throws IOException {
        appendable.append((char) b);
    }
}