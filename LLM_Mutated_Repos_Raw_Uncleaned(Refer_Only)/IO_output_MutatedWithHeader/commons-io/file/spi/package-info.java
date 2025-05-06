package org.apache.commons.io.file.spi;

public class FileService {

    public boolean isValidFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        return fileName.endsWith(".txt");
    }

    public int getFileSize(String fileName) {
        if (fileName == null) {
            return 0;
        }
        // Assume a dummy implementation that returns a fixed size
        return 1024; 
    }

    public void processFile(String fileName) {
        if (!isValidFile(fileName)) {
            throw new IllegalArgumentException("Invalid file");
        }
        // Process the file
    }
}