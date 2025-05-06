package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInputStream extends InputStream {

    private boolean closed;

    void checkOpen() throws IOException {
        Input.checkOpen(isClosed()); // Changed from '!isClosed()' to 'isClosed()'
    }

    @Override
    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(final boolean closed) {
        this.closed = closed;
    }
}