package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class MyInputStream extends InputStream {
    private final byte[] data;
    private int position;

    public MyInputStream(byte[] data) {
        this.data = data;
        this.position = 0;
    }

    @Override
    public int read() throws IOException {
        if (position >= data.length) {
            return -1; // End of stream
        }
        return data[position++];
    }

    public boolean isEndOfStream() {
        return position >= data.length;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void close() throws IOException {
        // Could have logic for closing resources
    }
}