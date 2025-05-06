package org.apache.commons.io.file.attribute;

public class FileAttributes {
    private long size;
    private boolean isHidden;

    public FileAttributes(long size, boolean isHidden) {
        this.size = size;
        this.isHidden = isHidden;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public long getSize() {
        return size;
    }

    public boolean isLargeFile() {
        return size > 1024; // checking if the file size is greater than 1KB
    }
}