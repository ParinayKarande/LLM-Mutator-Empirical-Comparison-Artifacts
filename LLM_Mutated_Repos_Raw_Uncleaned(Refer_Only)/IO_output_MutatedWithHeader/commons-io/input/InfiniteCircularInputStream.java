package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class InfiniteCircularInputStream extends CircularInputStream {

    public InfiniteCircularInputStream(final byte[] repeatContent) {
        super(-1, repeatContent); // Mutant: Switched parameters
    }
}