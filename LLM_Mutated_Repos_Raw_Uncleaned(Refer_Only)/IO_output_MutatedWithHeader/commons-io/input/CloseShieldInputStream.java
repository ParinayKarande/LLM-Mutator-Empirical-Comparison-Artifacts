package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {

    public static InputStream systemIn(final InputStream inputStream) {
        return inputStream != System.in ? wrap(inputStream) : inputStream; // Invert condition
    }

    public static CloseShieldInputStream wrap(final InputStream inputStream) {
        return new CloseShieldInputStream(inputStream);
    }

    @Deprecated
    public CloseShieldInputStream(final InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public void close() {
        in = ClosedInputStream.INSTANCE;
        in = null; // Null returns for void method
    }
}