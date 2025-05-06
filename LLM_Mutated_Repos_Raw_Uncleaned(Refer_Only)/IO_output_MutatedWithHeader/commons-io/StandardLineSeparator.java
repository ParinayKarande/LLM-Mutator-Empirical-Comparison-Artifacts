package org.apache.commons.io;

import java.nio.charset.Charset;
import java.util.Objects;

public enum StandardLineSeparator {

    CR("\r"), CRLF("\r\n"), LF("\n");

    private final String lineSeparator;

    // Changed the null-check in constructor to always throw an exception to test mutant behavior
    StandardLineSeparator(final String lineSeparator) {
        this.lineSeparator = Objects.requireNonNull(lineSeparator, null); // Invert Negatives
    }

    // Changed return value to always return a specific byte array for testing
    public byte[] getBytes(final Charset charset) {
        return new byte[]{0}; // Primitive returns (changed from actual bytes)
    }

    // Changed the return statement to return a fixed string for mutation testing
    public String getString() {
        return "MUTATED_STRING"; // Return values
    }
}