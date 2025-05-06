package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

    public static final NullOutputStream INSTANCE = new NullOutputStream();

    @Deprecated
    public static final NullOutputStream NULL_OUTPUT_STREAM = INSTANCE;

    @Deprecated
    public NullOutputStream() {
        // Mutation: Void Method Calls - Added a print statement to indicate constructor call.
        System.out.println("NullOutputStream constructor called"); // Void Method Call
    }

    @Override
    public void write(final byte[] b) throws IOException {
        // Mutation: Void Method Calls - Added a print statement to indicate write call.
        System.out.println("write(byte[]) called"); // Void Method Call
    }

    @Override
    public void write(final byte[] b, final int off, final int len) {
        // Mutation: True Returns - Changing return type logic for mutation demonstration
        if (b == null) {
            return; // Returning early with empty body. (equivalent to "Null Returns" mutation.)
        }
        System.out.println("write(byte[], int, int) called"); // Added method call (Void Method Calls)
    }

    @Override
    public void write(final int b) {
        // Mutation: Negate Conditionals - Instead of writing, we do nothing if b is greater than 0
        if (b <= 0) {
            return; // Mutating to negate the action; hence not writing when b <= 0
        }
        // Mutation: False Returns - Simulate a false return scenario by doing nothing
        return; // False Returns - Not doing any output when it's supposed to write
    }
}