package org.apache.commons.io.file;

public class FileUtil {

    public boolean isFileEmpty(File file) {
        if (file == null) {
            return true;
        }
        return file.length() == 0;
    }

    public void copyFile(File source, File destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination cannot be null.");
        }
        // Perform file copy operation here
    }

    public int divide(int a, int b) {
        return a / b;
    }
}