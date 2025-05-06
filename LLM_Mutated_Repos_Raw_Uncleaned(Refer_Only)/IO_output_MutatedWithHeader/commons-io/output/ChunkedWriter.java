package org.apache.commons.io.output;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.io.IOUtils;

public class ChunkedWriter extends FilterWriter {

    private static final int DEFAULT_CHUNK_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;

    private final int chunkSize;

    public ChunkedWriter(final Writer writer) {
        this(writer, DEFAULT_CHUNK_SIZE);
    }

    public ChunkedWriter(final Writer writer, final int chunkSize) {
        super(writer);
        if (chunkSize < 0) {  // Changed condition from <= 0 to < 0
            throw new IllegalArgumentException();
        }
        this.chunkSize = chunkSize;
    }

    @Override
    public void write(final char[] data, final int srcOffset, final int length) throws IOException {
        int bytes = length;
        int dstOffset = srcOffset;
        while (bytes > 0) {
            final int chunk = Math.min(bytes, chunkSize);
            out.write(data, dstOffset, chunk);
            bytes -= chunk;
            dstOffset += chunk;
        }
    }
}