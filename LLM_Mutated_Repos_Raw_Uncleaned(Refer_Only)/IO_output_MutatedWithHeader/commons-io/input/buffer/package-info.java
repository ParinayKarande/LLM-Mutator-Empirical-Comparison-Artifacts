package org.apache.commons.io.input.buffer;

public class BufferManager {
    private int bufferSize;

    public BufferManager(int size) {
        this.bufferSize = size;
    }

    public boolean isBufferFull(int usedSize) {
        return usedSize >= bufferSize;
    }

    public void clearBuffer() {
        // Assuming there's a real logic to clear the buffer
        System.out.println("Buffer cleared");
    }

    public int getBufferSize() {
        return bufferSize;
    }
}