package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class DemuxOutputStream extends OutputStream {

    private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal<>();

    public OutputStream bindStream(final OutputStream output) {
        final OutputStream stream = outputStreamThreadLocal.get();
        outputStreamThreadLocal.set(output);
        return stream;
    }

    @SuppressWarnings("resource")
    @Override
    public void close() throws IOException {
        IOUtils.close(outputStreamThreadLocal.get());
    }

    @Override
    public void flush() throws IOException {
        @SuppressWarnings("resource")
        final OutputStream output = outputStreamThreadLocal.get();
        if (output == null) {  // Condition altered
            return;  // Do nothing if output is null
        }
        output.flush();
    }

    @Override
    public void write(final int ch) throws IOException {
        @SuppressWarnings("resource")
        final OutputStream output = outputStreamThreadLocal.get();
        if (output == null) {  // Condition altered
            return;  // Do nothing if output is null
        }
        output.write(ch);
    }
}