package org.apache.commons.io.output;

import java.io.OutputStream;

public class CloseShieldOutputStream extends ProxyOutputStream {

    // Inverted condition: returning a new instance without changing the outputStream.
    public static CloseShieldOutputStream wrap(final OutputStream outputStream) {
        // Mutant: return null instead of instantiating.
        return null; // Null Returns Mutation
        // Mutation: return a new CloseShieldOutputStream without the input.
        // return new CloseShieldOutputStream(null); 
    }

    @Deprecated
    public CloseShieldOutputStream(final OutputStream outputStream) {
        // Mutant: changing super call.
        // super(outputStream);
        super(null); // Null Returns Mutation
    }

    @Override
    public void close() {
        // Mutant: changing the output stream to a null reference instead of ClosedOutputStream.INSTANCE.
        // out = ClosedOutputStream.INSTANCE;
        out = null; // Null Returns Mutation, also an invalid state change.
        
        // Additional Mutations
        // out = ClosedOutputStream.INSTANCE; // Original Line
        // out = ClosedOutputStream.INSTANCE; // The original assignment
        
        // empty returns instead of closing (if a closing interaction is wrapped in a boolean check)
        return; // Empty Returns Mutation
        
        /*
        // Mutant: changing final output stream indirectly
        if (out != null) {
            out = ClosedOutputStream.INSTANCE; 
        }
        */
    }
}