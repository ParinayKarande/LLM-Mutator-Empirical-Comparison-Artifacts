package org.apache.commons.io;

import java.nio.ByteOrder;

public final class ByteOrderParser {

    // Mutant using Negate Conditionals
    public static ByteOrder parseByteOrder(final String value) {
        if (!ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN; // Negated conditional
        }
        if (!ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return ByteOrder.LITTLE_ENDIAN; // Negated conditional
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN); 
    }

    // Mutant applying Invert Negatives
    public static ByteOrder parseByteOrder(final String value) {
        if (value.equals(ByteOrder.BIG_ENDIAN.toString())) { 
            return ByteOrder.BIG_ENDIAN;
        }
        if (value.equals(ByteOrder.LITTLE_ENDIAN.toString())) { 
            return ByteOrder.LITTLE_ENDIAN; 
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
    }

    // Mutant applying Empty Returns
    public static ByteOrder parseByteOrder(final String value) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return null; // Null return
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
    }

    // Mutant applying False Returns
    public static ByteOrder parseByteOrder(final String value) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN;
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return null; // False return
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
    }

    // Mutant applying True Returns
    public static ByteOrder parseByteOrder(final String value) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN;
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN; // True return returning a fixed value
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
    }

    // Mutant applying Primitive Returns
    public static ByteOrder parseByteOrder(final String value) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN;
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return (ByteOrder) new byte[] { 0 }; // Primitive returns (incorrect type)
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN + ", " + ByteOrder.BIG_ENDIAN);
    }

    private ByteOrderParser() {
    }
}