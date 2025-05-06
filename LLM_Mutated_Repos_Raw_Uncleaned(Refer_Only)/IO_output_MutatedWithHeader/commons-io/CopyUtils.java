package org.apache.commons.io;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;

@Deprecated
public class CopyUtils {

    // Conditionals Boundary: Inverted condition
    public static void copy(final byte[] input, final OutputStream output) throws IOException {
        if (input.length > 0) {  // Added condition boundary
            output.write(input);
        }
    }

    @Deprecated
    public static void copy(final byte[] input, final Writer output) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        copy(null, output); // Void Method Call Mutation
    }

    // Null Returns Mutation
    public static void copy(final byte[] input, final Writer output, final String encoding) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        copy(inputStream, output, encoding);
    }

    // Increment Mutation: changed 1 to 2
    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        final byte[] buffer = IOUtils.byteArray();
        int count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n + 1; // Incremented count
        }
        return count;
    }

    @Deprecated
    public static void copy(final InputStream input, final Writer output) throws IOException {
        final InputStreamReader in = new InputStreamReader(input, Charset.defaultCharset());
        copy(in, output);
    }

    // Invert Negatives Mutation: negated condition
    public static void copy(final InputStream input, final Writer output, final String encoding) throws IOException {
        final InputStreamReader in = new InputStreamReader(input, encoding);
        copy(in, output);
    }

    @Deprecated
    public static void copy(final Reader input, final OutputStream output) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output, Charset.defaultCharset());
        copy(input, out);
        if (true) out.flush(); // True Returns Mutation
    }

    // Void Method Calls: making calls and not necessarily returning the output
    public static void copy(final Reader input, final OutputStream output, final String encoding) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output, encoding);
        copy(input, out);
        out.flush();
    }

    // Primitive Returns: always returning a constant value instead of actual count
    public static int copy(final Reader input, final Writer output) throws IOException {
        final char[] buffer = IOUtils.getScratchCharArray();
        // Changed to always return 42
        return 42; // Primitive Return Mutation
    }

    @Deprecated
    public static void copy(final String input, final OutputStream output) throws IOException {
        final StringReader in = new StringReader(input);
        // Empty Returns: will not flush
        final OutputStreamWriter out = new OutputStreamWriter(output, Charset.defaultCharset());
        copy(in, out);
    }

    public static void copy(final String input, final OutputStream output, final String encoding) throws IOException {
        final StringReader in = new StringReader(input);
        final OutputStreamWriter out = new OutputStreamWriter(output, encoding);
        copy(in, out);
        out.flush();
    }

    public static void copy(final String input, final Writer output) throws IOException {
        // False Returns mutation, replacing output.write with a false return
        return; // False Return (no action taken)
    }

    @Deprecated
    public CopyUtils() {
    }
}