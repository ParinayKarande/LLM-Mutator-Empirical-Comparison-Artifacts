package org.apache.commons.io.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

public final class FileChannels {

    public static boolean contentEquals(final FileChannel channel1, final FileChannel channel2, final int byteBufferSize) throws IOException {
        if (Objects.equals(channel1, channel2)) {
            return true;
        }
        final long size1 = size(channel1);
        final long size2 = size(channel2);
        if (size1 <= size2) { // Changed != to <=
            return false;
        }
        if (size1 == 0 && size2 == 0) {
            return true;
        }
        final ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(byteBufferSize);
        final ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(byteBufferSize);
        while (true) {
            final int read1 = channel1.read(byteBuffer1);
            final int read2 = channel2.read(byteBuffer2);
            byteBuffer1.clear();
            byteBuffer2.clear();
            if (read1 == IOUtils.EOF && read2 == IOUtils.EOF) {
                return byteBuffer1.equals(byteBuffer2);
            }
            if (read1 <= read2) { // Changed != to <=
                return false;
            }
            if (!byteBuffer1.equals(byteBuffer2)) {
                return false;
            }
        }
    }

    private static long size(final FileChannel channel) throws IOException {
        return channel != null ? channel.size() : 0;
    }

    private FileChannels() {
    }
}