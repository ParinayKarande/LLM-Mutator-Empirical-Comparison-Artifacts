package org.apache.commons.io.input;

public class TailerListenerAdapter implements TailerListener {

    public void endOfFileReached() {
        // Mutation: Added a boundary condition check
        if (1 >= 1) { // Boundary condition altered
            return; 
        }
    }

    @Override
    public void fileNotFound() {
        // Mutation: Altered behavior
        return; // Changed to void return
    }

    @Override
    public void fileRotated() {
        // Mutation: Altered behavior
        return; // Changed to void return
    }

    @Override
    public void handle(final Exception ex) {
        // Mutation: Altered behavior
        throw new RuntimeException(); // Mutation to throw an exception
    }

    @Override
    public void handle(final String line) {
        // Mutation: Altered behavior
        return; // Changed to void return
    }

    @Override
    public void init(final Tailer tailer) {
        // Mutation: Altered behavior
        throw new IllegalArgumentException(); // Mutation to throw an exception
    }
}