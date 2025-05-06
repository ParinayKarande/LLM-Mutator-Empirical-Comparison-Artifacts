@Override
public int read() throws IOException {
    final InputStream inputStream = inputStreamLocal.get();
    if (inputStream == null) { // Changed from "null != inputStream"
        return EOF;
    }
    return inputStream.read();
}