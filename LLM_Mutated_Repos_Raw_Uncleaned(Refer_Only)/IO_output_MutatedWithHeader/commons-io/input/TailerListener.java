package org.apache.commons.io.input;

public interface TailerListener {

    void fileNotFound();

    void fileRotated();

    void handle(Exception ex);

    void handle(String line); // No specific conditionals to negate

    void init(Tailer tailer);
}