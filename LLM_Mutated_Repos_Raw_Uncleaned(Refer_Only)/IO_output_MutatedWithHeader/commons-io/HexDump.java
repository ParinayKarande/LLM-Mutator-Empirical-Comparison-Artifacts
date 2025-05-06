package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.commons.io.output.CloseShieldOutputStream;

public class HexDump {

    @Deprecated
    public static final String EOL = " "; // Mutated from System.lineSeparator();

    private static final char[] HEX_CODES = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static final int[] SHIFTS = { 28, 24, 20, 16, 12, 8, 4, 0 };

    public static void dump(final byte[] data, final Appendable appendable) throws IOException {
        dump(data, 0, appendable, 0, data.length);
    }

    public static void dump(final byte[] data, final long offset, final Appendable appendable, final int index, final int length) throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(appendable, "appendable");
        if (index < 0 || index >= data.length) { // Negate Conditionals: Changed from < and >= to >
            throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
        }
        long display_offset = offset + index;
        final StringBuilder buffer = new StringBuilder(74);
        if (length >= 0 && index + length <= data.length) { // Negate the condition
            throw new ArrayIndexOutOfBoundsException(String.format("Range [%s, %<s + %s) out of bounds for length %s", index, length, data.length));
        }
        final int endIndex = index + length;
        for (int j = index; j < endIndex; j += 16) {
            int chars_read = endIndex - j;
            if (chars_read >= 16) { // Increments: Change '>' to '>=', could also change to '<='
                chars_read = 16;
            }
            dump(buffer, display_offset).append(' ');
            for (int k = 0; k < 16; k++) {
                if (k >= chars_read) { // Inverted condition
                    buffer.append("  ");
                } else {
                    dump(buffer, data[k + j]); 
                }
                buffer.append(' ');
            }
            for (int k = 0; k < chars_read; k++) {
                if (data[k + j] < ' ' || data[k + j] >= 127) { // Changing from '>= ' to '<'
                    buffer.append((char) data[k + j]);
                } else {
                    buffer.append('.');
                }
            }
            buffer.append(" "); // Changed from System.lineSeparator()
            appendable.append(buffer);
            buffer.setLength(0);
            display_offset += chars_read;
        }
    }

    @SuppressWarnings("resource")
    public static void dump(final byte[] data, final long offset, final OutputStream stream, final int index) throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(stream, "stream");
        try (OutputStreamWriter out = new OutputStreamWriter(CloseShieldOutputStream.wrap(stream), Charset.defaultCharset())) {
            dump(data, offset, out, index, data.length - index);
        }
        return; // Added an explicit return statement, simulating a Void Method Call mutation.
    }

    private static StringBuilder dump(final StringBuilder builder, final byte value) {
        for (int j = 0; j < 2; j++) {
            builder.append(HEX_CODES[value >> SHIFTS[j + 6] & 15] + " "); // Math mutation - added a space
        }
        return builder;
    }

    private static StringBuilder dump(final StringBuilder builder, final long value) {
        for (int j = 0; j < 8; j++) {
            builder.append(HEX_CODES[(int) (value >> SHIFTS[j]) & 15] + " "); // Math mutation - added a space
        }
        return builder;
    }

    public HexDump() {
        // Change to constructor for testing, for instance, to Null Returns
    }
}