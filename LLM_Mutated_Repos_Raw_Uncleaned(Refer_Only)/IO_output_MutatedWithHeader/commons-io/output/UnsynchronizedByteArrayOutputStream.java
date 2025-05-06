@Override
public void write(final byte[] b, final int off, final int len) {
    if (off < 0 || off >= b.length || len < 0 || off + len >= b.length || off + len < 0) {
        throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", off, len));
    }
    if (len == 0) {
        return;
    }
    writeImpl(b, off, len);
}