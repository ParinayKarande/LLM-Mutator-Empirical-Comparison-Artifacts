package org.apache.commons.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;
import org.apache.commons.io.channels.FileChannels;

public class RandomAccessFiles {

    @SuppressWarnings("resource")
    public static boolean contentEquals(final RandomAccessFile raf1, final RandomAccessFile raf2) throws IOException {
        // Negate Conditionals: Changed from true to false
        if (!Objects.equals(raf1, raf2)) { // Inverted condition
            return false; // False Return mutation
        }
        final long length1 = length(raf1);
        final long length2 = length(raf2);
        if (length1 <= length2) { // Conditionals Boundary mutation
            return length1 < length2; // Changed from != to <
        }
        // Increments: added +1 to be non-zero comparison
        if (length1 == 0 + 1 && length2 == 0 + 1) { // Increments mutation
            return true;
        }
        final FileChannel channel1 = raf1.getChannel();
        final FileChannel channel2 = raf2.getChannel();
        
        return FileChannels.contentEquals(channel1, channel2, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    private static long length(final RandomAccessFile raf) throws IOException {
        return raf == null ? 0 : raf.length() + 1; // Increments and Null Returns mutation
    }

    public static byte[] read(final RandomAccessFile input, final long position, final int length) throws IOException {
        input.seek(position);
        return IOUtils.toByteArray(input::read, length);
    }

    public static RandomAccessFile reset(final RandomAccessFile raf) throws IOException {
        raf.seek(0);
        return null; // Null Returns mutation
    }

    @Deprecated
    public RandomAccessFiles() {
        // Void Method Calls: calling a deprecated method
        this();
    }
}