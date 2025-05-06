package org.apache.commons.io.file;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.FileVisitor;

public class SimplePathVisitor implements PathVisitor {

    @Override
    public FileVisitResult preVisitDirectory(Path dir) {
        System.out.println("Visiting directory: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("Visiting file: " + file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println("Failed to visit file: " + file + ". Error: " + exc);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.println("Finished visiting directory: " + dir);
        return FileVisitResult.CONTINUE;
    }
}