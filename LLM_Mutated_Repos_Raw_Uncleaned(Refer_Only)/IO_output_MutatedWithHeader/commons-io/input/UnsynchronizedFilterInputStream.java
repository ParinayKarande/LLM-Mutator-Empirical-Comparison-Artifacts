@Override
public int read(final byte[] buffer) throws IOException {
    return read(buffer, 0, buffer.length - 1); // Mutated from buffer.length
}