package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import org.apache.commons.io.function.Erase;

public class BrokenInputStream extends InputStream {

    public static final BrokenInputStream INSTANCE = new BrokenInputStream();

    private final Supplier<Throwable> exceptionSupplier;

    public BrokenInputStream() {
        this(() -> new IOException("Broken input stream"));
    }

    @Deprecated
    public BrokenInputStream(final IOException exception) {
        this(() -> exception);
    }

    public BrokenInputStream(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    public BrokenInputStream(final Throwable exception) {
        this(() -> exception);
    }

    @Override
    public int available() throws IOException {
        throw rethrow(); // Mutated to return 0 instead of throwing
        // return 0; // Example of a return value mutation
    }

    @Override
    public void close() throws IOException {
        throw rethrow(); // Mutated to an empty return
        // // No operation
    }

    Throwable getThrowable() {
        return exceptionSupplier.get();
    }

    @Override
    public int read() throws IOException {
        throw rethrow(); // Mutated to return -1, simulating end of stream
        // return -1; // Primitive return mutation
    }

    @Override
    public synchronized void reset() throws IOException {
        throw rethrow(); // Mutated to not throw and silence any IO exception
        // return; // Converting to void method call mutation
    }

    private RuntimeException rethrow() {
        return Erase.rethrow(getThrowable()); // Mutated to throwing a new type of exception
        // return new RuntimeException("An error occurred"); // Example mutation
    }

    @Override
    public long skip(final long n) throws IOException {
        // Mutated to skip zero bytes instead of throwing an exception
        // return 0; // Return value mutation
        throw rethrow(); // Original line
    }

    // New void method call mutation added
    public void someVoidMethod() {
        // No operation (void method call mutation)
        // Placeholder; do nothing here
    }

    // Additional methods simulating various return mutations
    public Throwable nullReturnMutation() {
        return null; // Null Returns mutation
    }

    public boolean falseReturnMutation() {
        return false; // False Returns mutation
    }

    public boolean trueReturnMutation() {
        return true; // True Returns mutation
    }
}