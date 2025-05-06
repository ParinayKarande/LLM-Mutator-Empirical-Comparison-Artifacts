package org.apache.commons.text.lookup;

final class IllegalArgumentExceptions {

    // Mutation: Invert Negatives - changing from IllegalArgumentException to NullPointerException
    static NullPointerException format(final String format, final Object... args) {
        return new NullPointerException(String.format(format, args)); // Mutation applied
    }

    // Mutation: Conditionals Boundary - not applicable here as there are no conditionals
    // Mutation: Increments - not applicable here as there are no increments
    // Mutation: Math - not applicable here as no math operations present
    // Mutation: Negate Conditionals - not applicable here as there are no conditionals
    // Mutation: Return Values - changing the return type to a custom Exception
    static RuntimeException format(final Throwable t, final String format, final Object... args) { 
        return new RuntimeException(String.format(format, args), t); // Mutation applied
    }

    // Mutation: Void Method Calls - None present in original code
    // Mutation: Empty Returns - adding return with no value
    private IllegalArgumentExceptions() {
        return; // Mutation applied (not valid but for demonstration purpose)
    }
    
    // Mutation: False Returns - forcing a false return
    static IllegalArgumentException formatFalse(final String format, final Object... args) {
        return false; // Not a valid return, just for demonstration 
    }
    
    // Mutation: True Returns - forcing a true return
    static IllegalArgumentException formatTrue(final String format, final Object... args) {
        return true; // Not a valid return, just for demonstration
    }
    
    // Mutation: Null Returns - Returning null instead of IllegalArgumentException
    static IllegalArgumentException formatNull(final String format, final Object... args) {
        return null; // Mutation applied
    }
    
    // Mutation: Primitive Returns - This wouldn't work as must return IllegalArgumentException
    static int formatPrimitive(final String format, final Object... args) {
        return 1; // Mutation applied
    }
}