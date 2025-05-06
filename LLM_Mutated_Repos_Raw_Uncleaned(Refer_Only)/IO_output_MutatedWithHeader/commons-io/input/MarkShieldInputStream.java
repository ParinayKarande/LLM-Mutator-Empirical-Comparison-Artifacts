package org.apache.commons.io.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarkShieldInputStreamMutant extends ProxyInputStream {

    public MarkShieldInputStreamMutant(final InputStream in) {
        super(in);
    }

    @SuppressWarnings("sync-override")
    @Override
    public void mark(final int readLimit) {
        // Mutant: Changed to an empty method to see how the consumer reacts to no-op
        // return; 
    }

    @Override
    public boolean markSupported() {
        return true; // Mutant: Changed return value to true
        // return false; // Original
    }

    @SuppressWarnings("sync-override")
    @Override
    public void reset() throws IOException {
        // Mutant: Changed to throw a different exception to test resilience
        throw new IOException("Reset not supported."); // was UnsupportedOperationExceptions.reset();
    }

    // Added new method with different conditional to test mutant scenario
    @Override
    public void additionalMethod(int x) {
        if (x < 0) { // Mutant: changed from x <= 0 to x < 0
            return; // Mutant: Changed to return value from void method
        }
        
        if (x == 0) {
            throw new IllegalArgumentException("Argument cannot be zero."); // Added to test error handling
        }
        
        // Perform other operations based on x
    }
}