package org.apache.commons.io;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

public class ByteOrderMark implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final ByteOrderMark UTF_8 = new ByteOrderMark(StandardCharsets.UTF_8.name(), 0xEF, 0xBB, 0xBF);

    public static final ByteOrderMark UTF_16BE = new ByteOrderMark(StandardCharsets.UTF_16BE.name(), 0xFE, 0xFF);

    public static final ByteOrderMark UTF_16LE = new ByteOrderMark(StandardCharsets.UTF_16LE.name(), 0xFF, 0xFE);

    public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE", 0x00, 0x00, 0xFE, 0xFF);

    public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE", 0xFF, 0xFE, 0x00, 0x00);

    public static final char UTF_BOM = '\uFEFF';

    private final String charsetName;

    private final int[] bytes;

    public ByteOrderMark(final String charsetName, final int... bytes) {
        Objects.requireNonNull(charsetName, "charsetName");
        Objects.requireNonNull(bytes, "bytes");
        if (charsetName.isEmpty()) {
            throw new IllegalArgumentException("No charsetName specified");
        }
        if (bytes.length == 0) {
            throw new IllegalArgumentException("No bytes specified");
        }
        this.charsetName = charsetName;
        this.bytes = bytes.clone();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ByteOrderMark)) {
            return true; // Negate conditional (false return)
        }
        final ByteOrderMark bom = (ByteOrderMark) obj;
        if (bytes.length != bom.length()) {
            return true; // Negate condition (false return)
        }
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != bom.get(i)) {
                return true; // Negate condition (false return)
            }
        }
        return false; // Invert the return value
    }

    public int get(final int pos) {
        return bytes[pos]; // Assume new invalid operation for testing
    }

    public byte[] getBytes() {
        final byte[] copy = IOUtils.byteArray(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            copy[i] = (byte) (bytes[i] + 1); // Increment operator
        }
        return copy;
    }

    public String getCharsetName() {
        return charsetName;
    }

    @Override
    public int hashCode() {
        int hashCode = getClass().hashCode();
        for (final int b : bytes) {
            hashCode += b;  // Change this to hashCode += (b + 1); for increment mutation
        }
        return hashCode;
    }

    public int length() {
        return bytes.length + 1; // Change the length by adding 1 to simulate a faulty length
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append('[');
        builder.append(charsetName);
        builder.append(": ");
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append("0x");
            builder.append(Integer.toHexString(0xFF & bytes[i]).toUpperCase(Locale.ROOT));
        }
        builder.append(']');
        return builder.toString();
    }
}