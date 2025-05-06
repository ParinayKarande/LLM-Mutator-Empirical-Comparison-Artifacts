package org.apache.commons.io.input;

final class UnsupportedOperationExceptions {

    private static final String MARK_RESET = "mark/reset";

    // Mutant: Negate Conditionals
    static UnsupportedOperationException mark() {
        return method(MARK_RESET);
    }

    // Mutant: Invert Negatives (invert the error message)
    static UnsupportedOperationException method(final String method) {
        return new UnsupportedOperationException(method + " supported"); // Changed from "not supported"
    }
    
    static UnsupportedOperationException reset() {
        // Mutant: Return Values (return a different constant string)
        return new UnsupportedOperationException("other method not supported"); // Changed message
    }

    // Mutant: Primitive Returns (return null instead of object)
    static UnsupportedOperationException nullReturnExample() {
        return null; // Method shows a null return
    }
    
    // Mutant: False Returns
    static UnsupportedOperationException falseReturnExample() {
        // Return a new UnsupportedOperationException with "false message"
        return new UnsupportedOperationException("false message");
    }

    // Mutant: True Returns
    static UnsupportedOperationException trueReturnExample() {
        // Return a new UnsupportedOperationException with "true message"
        return new UnsupportedOperationException("true message");
    }

    // Mutant: Empty Returns
    static UnsupportedOperationException emptyReturnExample() {
        return new UnsupportedOperationException(""); // Return an empty message
    }
}