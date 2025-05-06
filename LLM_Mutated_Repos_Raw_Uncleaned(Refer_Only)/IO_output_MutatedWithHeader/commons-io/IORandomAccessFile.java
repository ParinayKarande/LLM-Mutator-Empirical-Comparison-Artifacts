public IORandomAccessFile(final String name, final String mode) throws FileNotFoundException {
    super(name, mode);
    this.file = name != null && !name.isEmpty() ? new File(name) : null; // Changed condition to check for non-empty string
    this.mode = mode;
}