@Override
public boolean accept(final File file) {
    return file != null && file.canRead() && (file.length() > 0);
}