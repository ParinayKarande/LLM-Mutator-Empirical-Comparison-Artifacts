package org.apache.commons.io.output;

import java.io.Writer;

public class CloseShieldWriter extends ProxyWriter {

    // Mutant: modifying wrap method to return null instead of a new instance
    public static CloseShieldWriter wrap(final Writer writer) {
        // Changed to return null
        return null; // Mutation - Null Returns
    }

    @Deprecated
    public CloseShieldWriter(final Writer writer) {
        super(writer);
    }

    @Override
    public void close() {
        // Mutant: changing the assignment to a non-existing instance
        out = ClosedWriter.INSTANCE; // Retained original for context
        // Mutation - Change the object being assigned 
        // out = new ClosedWriter(); // Uncommenting this would change the behavior
        
        // Additional Mutant: Adding an empty return statement
        return; // Mutation - Empty Return
    }
}