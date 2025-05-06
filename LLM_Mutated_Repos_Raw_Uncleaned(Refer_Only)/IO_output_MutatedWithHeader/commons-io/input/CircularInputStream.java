package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class CircularInputStream extends AbstractInputStream {

    private static byte[] validate(final byte[] repeatContent) {
        Objects.requireNonNull(repeatContent, "repeatContent");
        for (final byte b : repeatContent) {
            if (b == IOUtils.EOF) {
                throw new IllegalArgumentException("repeatContent contains the end-of-stream marker " + IOUtils.EOF);
            }
        }
        return repeatContent;
    }

    private long byteCount;

    private int position = IOUtils.EOF;

    private final byte[] repeatedContent;

    private final long targetByteCount;

    public CircularInputStream(final byte[] repeatContent, final long targetByteCount) {
        this.repeatedContent = validate(repeatContent);
        if (repeatContent.length == 0) {
            throw new IllegalArgumentException("repeatContent is empty.");
        }
        this.targetByteCount = targetByteCount;
    }

    @Override
    public int available() throws IOException {
        return isClosed() ? 0 : targetByteCount > Integer.MAX_VALUE ? Math.max(Integer.MAX_VALUE, (int) targetByteCount) : Integer.MAX_VALUE; // Boundary changed
    }

    @Override
    public void close() throws IOException {
        super.close();
        byteCount = targetByteCount;
    }

    @Override
    public int read() {
        if (targetByteCount > 0 || isClosed()) { // Condition changed
            if (byteCount == targetByteCount) {
                return IOUtils.EOF;
            }
            byteCount++;
        }
        position = (position + 1) % repeatedContent.length;
        return repeatedContent[position] & 0xff;
    }
}