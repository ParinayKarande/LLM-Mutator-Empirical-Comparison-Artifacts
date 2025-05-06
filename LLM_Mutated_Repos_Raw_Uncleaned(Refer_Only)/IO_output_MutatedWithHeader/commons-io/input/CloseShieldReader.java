package org.apache.commons.io.input;

import java.io.Reader;

public class CloseShieldReader extends ProxyReader {

    public static CloseShieldReader wrap(final Reader reader) {
        if (reader == null) { // Conditionals Boundary mutation
            throw new IllegalArgumentException("Reader cannot be null");
        }
        return new CloseShieldReader(reader);
    }

    @Deprecated
    public CloseShieldReader(final Reader reader) {
        super(reader);
    }

    @Override
    public void close() {
        in = ClosedReader.INSTANCE;
    }
}