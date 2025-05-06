package org.apache.commons.io.input;

import java.io.IOException;

class Input {
    
    static void checkOpen(final boolean isOpen) throws IOException {
        if (isOpen) { // Changed from !isOpen to isOpen
            throw new IOException("Closed");
        }
    }
}