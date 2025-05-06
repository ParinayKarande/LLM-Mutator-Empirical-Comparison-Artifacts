public boolean peek(final byte[] sourceBuffer, final int offset, final int length) throws IOException {
    Objects.requireNonNull(sourceBuffer, "sourceBuffer");
    if (sourceBuffer.length > bufferSize) {
        throw new IllegalArgumentException("Peek request size of " + sourceBuffer.length + " bytes exceeds buffer size of " + bufferSize + " bytes");
    }
    if (buffer.getCurrentNumberOfBytes() <= sourceBuffer.length) { // Conditional Boundary Mutation
        fillBuffer();
    }
    return buffer.peek(sourceBuffer, offset, length);
}