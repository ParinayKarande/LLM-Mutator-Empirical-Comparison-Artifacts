package org.apache.commons.io.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.function.Supplier;

public final class CharsetEncoders {

    public static CharsetEncoder toCharsetEncoder(final CharsetEncoder charsetEncoder) {
        // Mutation: Invert Negatives / Negate Conditionals
        // Original: return charsetEncoder != null ? charsetEncoder : defaultSupplier.get();
        return charsetEncoder == null ? null : toCharsetEncoder(charsetEncoder, () -> Charset.defaultCharset().newEncoder());
    }

    public static CharsetEncoder toCharsetEncoder(final CharsetEncoder charsetEncoder, final Supplier<CharsetEncoder> defaultSupplier) {
        // Mutation: Change condition to use <=
        // Original: return charsetEncoder != null ? charsetEncoder : defaultSupplier.get();
        return charsetEncoder == null ? defaultSupplier.get() : charsetEncoder;
        // Mutation: Introduce a dummy math operation
        // int dummyOperation = 1 + 1;  // Just demonstrate some calculation that does nothing
        // return charsetEncoder; // This line remains unchanged
    }

    // Mutation: Add a void method that might introduce side effects
    private static void someVoidMethod() {
        // Suppose it logs or performs some action
        System.out.println("Void method called");
    }

    // Mutation: Return an empty CharsetEncoder at certain times
    private static CharsetEncoder getEmptyCharsetEncoder() {
        return Charset.forName("UTF-8").newEncoder(); // Example for returning a default
    }

    private CharsetEncoders() {
        // Mutation: Change constructor logic
        System.out.println("CharsetEncoders instantiated");
    }
}