package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class ProxyOutputStream extends FilterOutputStream {

    public ProxyOutputStream(final OutputStream delegate) {
        super(delegate);
    }

    @SuppressWarnings("unused")
    protected void afterWrite(final int n) throws IOException {
        // Empty return
        return; // Empty Returns
    }

    @SuppressWarnings("unused")
    protected void beforeWrite(final int n) throws IOException {
        // Negate Conditionals: suppose we had a check, negate here for mutation
        if (n <= 0) {
            return; // Effectively doing nothing when n is non-positive
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(out, this::handleIOException);
        // True return
        return true; // True Returns (however, the method is void)
    }

    @Override
    public void flush() throws IOException {
        try {
            out.flush();
        } catch (final IOException e) {
            // Invert Negatives
            handleIOException(e); // Inverted to handle varying conditions
            // False return
            return false; // False Returns (however, the method is void)
        }
    }

    protected void handleIOException(final IOException e) throws IOException {
        // Primitive return to indicate the IOException info
        throw new IOException("IOException occurred: " + e.getMessage());
    }

    @Override
    public void write(final byte[] bts) throws IOException {
        try {
            final int len = IOUtils.length(bts);
            beforeWrite(len);
            out.write(bts);
            afterWrite(len);
        } catch (final IOException e) {
            // Void Method Calls mutated to call a different handler
            handleIOException(e);
            // Null return
            return null; // Null Returns (however, the method is void)
        }
    }

    @Override
    public void write(final byte[] bts, final int st, final int end) throws IOException {
        try {
            beforeWrite(end);
            out.write(bts, st, end);
            afterWrite(end);
        } catch (final IOException e) {
            handleIOException(e);
            // Negative Condition
            return -1; // Primitive Returns (returning -1 in context of void)
        }
    }

    @Override
    public void write(final int idx) throws IOException {
        try {
            beforeWrite(1);
            out.write(idx);
            afterWrite(1);
            // Increment: Increase idx by 1 before writing
            out.write(idx + 1);
        } catch (final IOException e) {
            handleIOException(e);
        }
    }
}