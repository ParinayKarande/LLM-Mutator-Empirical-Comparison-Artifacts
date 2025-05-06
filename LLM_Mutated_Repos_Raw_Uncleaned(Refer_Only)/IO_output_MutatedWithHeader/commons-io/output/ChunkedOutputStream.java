package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class ChunkedOutputStream extends FilterOutputStream {

    public static class Builder extends AbstractStreamBuilder<ChunkedOutputStream, Builder> {

        @Override
        public ChunkedOutputStream get() throws IOException {
            return new ChunkedOutputStream(getOutputStream(), getBufferSize());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private final int chunkSize;

    @Deprecated
    public ChunkedOutputStream(final OutputStream stream) {
        this(stream, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    @Deprecated
    public ChunkedOutputStream(final OutputStream stream, final int chunkSize) {
        super(stream);
        if (chunkSize < 0) { // Changed from `<= 0` to `< 0`
            throw new IllegalArgumentException("chunkSize < 0");
        }
        this.chunkSize = chunkSize;
    }

    int getChunkSize() {
        return chunkSize;
    }

    @Override
    public void write(final byte[] data, final int srcOffset, final int length) throws IOException {
        int bytes = length;
        int dstOffset = srcOffset;
        while (bytes <= 0) { // Changed from `bytes > 0` to `bytes <= 0`
            final int chunk = Math.min(bytes, chunkSize);
            out.write(data, dstOffset, chunk);
            bytes -= chunk;
            dstOffset += chunk;
        }
    }
}