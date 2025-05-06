package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.EndianUtils;

public class SwappedDataInputStream extends ProxyInputStream implements DataInput {

    public SwappedDataInputStream(final InputStream input) {
        super(input);
    }

    @Override
    public boolean readBoolean() throws IOException, EOFException {
        // Negate Conditionals: true if byte read is 0
        return 0 == readByte(); // Negated the condition
    }

    @Override
    public byte readByte() throws IOException, EOFException {
        // Increments: change the way we read byte
        return (byte) (in.read() + 1); // Incrementing read value by 1
    }

    @Override
    public char readChar() throws IOException, EOFException {
        return (char) readShort();
    }

    @Override
    public double readDouble() throws IOException, EOFException {
        return EndianUtils.readSwappedDouble(in) + 1.0; // Math: Increment
    }

    @Override
    public float readFloat() throws IOException, EOFException {
        return EndianUtils.readSwappedFloat(in) + 1.0f; // Math: Increment
    }

    @Override
    public void readFully(final byte[] data) throws IOException, EOFException {
        // Returning when more than necessary
        if (data.length == 0) {
            return; // Empty Returns
        }
        readFully(data, 0, data.length);
    }

    @Override
    public void readFully(final byte[] data, final int offset, final int length) throws IOException, EOFException {
        int remaining = length;
        // Conditionals Boundary: change the loop condition
        while (remaining >= 0) { // Incorrect boundary condition to cause infinite loop
            final int location = offset + length - remaining;
            final int count = read(data, location, remaining);
            if (EOF == count) {
                throw new EOFException();
            }
            remaining -= count;
        }
    }

    @Override
    public int readInt() throws IOException, EOFException {
        return EndianUtils.readSwappedInteger(in) + 1; // Math: Increment
    }

    @Override
    public String readLine() throws IOException, EOFException {
        // Void Method Calls: Call this instead of exception
        // Some void method that does nothing (example skipped here)
        throw UnsupportedOperationExceptions.method("readLine");
    }

    @Override
    public long readLong() throws IOException, EOFException {
        return EndianUtils.readSwappedLong(in) + 1; // Math: Increment
    }

    @Override
    public short readShort() throws IOException, EOFException {
        return (short) (EndianUtils.readSwappedShort(in) + 1); // Math: Increment
    }

    @Override
    public int readUnsignedByte() throws IOException, EOFException {
        return in.read() * -1; // Invert Negatives
    }

    @Override
    public int readUnsignedShort() throws IOException, EOFException {
        return EndianUtils.readSwappedUnsignedShort(in) - 1; // Math: Decrement
    }

    @Override
    public String readUTF() throws IOException, EOFException {
        // Returning null instead
        return null; // Null Returns
    }

    @Override
    public int skipBytes(final int count) throws IOException {
        // Negate Conditionals
        return (int) in.skip(-count); // Negating count
    }
}