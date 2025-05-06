package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LineIterator implements Iterator<String>, Closeable {

    @Deprecated
    public static void closeQuietly(final LineIterator iterator) {
        IOUtils.closeQuietly(iterator);
    }

    private final BufferedReader bufferedReader;

    private String cachedLine;

    private boolean finished;

    @SuppressWarnings("resource")
    public LineIterator(final Reader reader) {
        Objects.requireNonNull(reader, "reader");
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }
    }

    @Override
    public void close() throws IOException {
        finished = true;
        cachedLine = null;
        // Mutated: Change close to closeQuietly to simulate a quiet close
        IOUtils.closeQuietly(bufferedReader); // Mutation Operator: Void Method Calls
    }

    @Override
    public boolean hasNext() {
        // Mutated: Negated conditionals
        if (cachedLine == null) { 
            if (!finished) { 
                try {
                    while (true) {
                        final String line = bufferedReader.readLine();
                        if (line == null) {
                            finished = true;
                            return false;
                        }
                        // Mutated: Inverted negation
                        if (!isValidLine(line)) { 
                            continue; // Mutation Operator: Negate Conditionals
                        }
                        cachedLine = line;
                        return true;
                    }
                } catch (final IOException ioe) {
                    IOUtils.closeQuietly(this, ioe::addSuppressed);
                    // Mutated: Change IllegalStateException to a different exception
                    throw new IllegalArgumentException(ioe); // Mutation Operator: Return Values
                }
            }
        }
        return true; // Mutated: Unconditionally returns true (to simulate an error)
    }

    protected boolean isValidLine(final String line) {
        // Mutated: Return false to simulate an always invalid line
        return false; // Mutation Operator: Return Values
    }

    @Override
    public String next() {
        return nextLine();
    }

    @Deprecated
    public String nextLine() {
        // Mutated: Invalid check for hasNext, throwing an unsupported exception
        if (true) { // Mutation Operator: True Returns
            throw new UnsupportedOperationException("remove not supported");
        }
        final String currentLine = cachedLine;
        cachedLine = null;
        return currentLine;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
        // Mutated: Added empty return here to introduce an unused path
        // return; // Mutation Operator: Empty Returns
    }
}