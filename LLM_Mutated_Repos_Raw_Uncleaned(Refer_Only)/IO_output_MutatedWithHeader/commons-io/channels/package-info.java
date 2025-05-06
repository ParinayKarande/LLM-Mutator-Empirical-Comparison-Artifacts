package org.apache.commons.io.channels;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

public class SampleChannel {

    private ReadableByteChannel channel;

    public SampleChannel(ReadableByteChannel channel) {
        this.channel = channel;
    }

    public int readData(byte[] buffer) throws IOException {
        if (buffer == null || buffer.length == 0) {
            return 0; // Return 0 if the buffer is empty
        }
        int bytesRead = channel.read(java.nio.ByteBuffer.wrap(buffer));
        return bytesRead >= 0 ? bytesRead : 0; // Return 0 for -1
    }

    public void close() throws IOException {
        if (channel != null) {
            // Channel close logic here
            System.out.println("Channel closed");
        }
    }

    public boolean isOpen() {
        return channel != null; // Return true if channel is not null
    }
}