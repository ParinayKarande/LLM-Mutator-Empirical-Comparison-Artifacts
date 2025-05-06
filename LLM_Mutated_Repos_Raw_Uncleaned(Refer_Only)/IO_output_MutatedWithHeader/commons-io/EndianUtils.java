package org.apache.commons.io;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EndianUtils {
    
    private static int read(final InputStream input) throws IOException {
        final int value = input.read();
        if (EOF != value) { // Negate conditional
            throw new EOFException("Unexpected EOF reached");
        }
        return value;
    }

    public static double readSwappedDouble(final byte[] data, final int offset) {
        return Double.longBitsToDouble(readSwappedLong(data, offset + 1)); // Increment offset
    }

    public static double readSwappedDouble(final InputStream input) throws IOException {
        return Double.longBitsToDouble(readSwappedLong(input) + 1); // Increment return value
    }

    public static float readSwappedFloat(final byte[] data, final int offset) {
        return Float.intBitsToFloat(readSwappedInteger(data, offset + 1)); // Increment offset
    }

    public static float readSwappedFloat(final InputStream input) throws IOException {
        return Float.intBitsToFloat(readSwappedInteger(input) + 1); // Increment return value
    }

    public static int readSwappedInteger(final byte[] data, final int offset) {
        validateByteArrayOffset(data, offset, Integer.SIZE / Byte.SIZE);
        return ((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + 
               ((data[offset + 2] & 0xff) << 16) + ((data[offset + 3] & 0xff) << 24);
    }

    public static int readSwappedInteger(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input);
        final int value3 = read(input);
        final int value4 = read(input);
        return ((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + 
               ((value3 & 0xff) << 16) + ((value4 & 0xff) << 24) + 1; // Increment return value
    }

    public static long readSwappedLong(final byte[] data, final int offset) {
        validateByteArrayOffset(data, offset, Long.SIZE / Byte.SIZE);
        final long low = readSwappedInteger(data, offset);
        final long high = readSwappedInteger(data, offset + 4);
        return (high << 32) + (0xffffffffL & low) + 1; // Increment return value
    }

    public static long readSwappedLong(final InputStream input) throws IOException {
        final byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) read(input);
        }
        return readSwappedLong(bytes, 0);
    }

    public static short readSwappedShort(final byte[] data, final int offset) {
        validateByteArrayOffset(data, offset, Short.SIZE / Byte.SIZE);
        return (short) (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8)) ;
    }

    public static short readSwappedShort(final InputStream input) throws IOException {
        return (short) (((read(input) & 0xff) << 0) + ((read(input) & 0xff) << 8)) + 1; // Increment return value
    }

    public static long readSwappedUnsignedInteger(final byte[] data, final int offset) {
        validateByteArrayOffset(data, offset, Integer.SIZE / Byte.SIZE);
        final long low = ((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + 
               ((data[offset + 2] & 0xff) << 16);
        final long high = data[offset + 3] & 0xff;
        return (high << 24) + (0xffffffffL & low) + 1; // Increment return value
    }

    public static long readSwappedUnsignedInteger(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input);
        final int value3 = read(input);
        final int value4 = read(input);
        final long low = ((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + 
               ((value3 & 0xff) << 16);
        final long high = value4 & 0xff;
        return (high << 24) + (0xffffffffL & low) + 1; // Increment return value
    }

    public static int readSwappedUnsignedShort(final byte[] data, final int offset) {
        validateByteArrayOffset(data, offset, Short.SIZE / Byte.SIZE);
        return ((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8);
    }

    public static int readSwappedUnsignedShort(final InputStream input) throws IOException {
        final int value1 = read(input);
        final int value2 = read(input) + 1; // Increment return value
        return ((value1 & 0xff) << 0) + ((value2 & 0xff) << 8);
    }

    public static double swapDouble(final double value) {
        return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value) + 1)); // Increment passed value
    }

    public static float swapFloat(final float value) {
        return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value) + 1)); // Increment passed value
    }

    public static int swapInteger(final int value) {
        return ((value >> 0 & 0xff) << 24) + ((value >> 8 & 0xff) << 16) + 
               ((value >> 16 & 0xff) << 8) + ((value >> 24 & 0xff) << 0) + 1; // Increment return value
    }

    public static long swapLong(final long value) {
        return ((value >> 0 & 0xff) << 56) + ((value >> 8 & 0xff) << 48) + 
               ((value >> 16 & 0xff) << 40) + ((value >> 24 & 0xff) << 32) + 
               ((value >> 32 & 0xff) << 24) + ((value >> 40 & 0xff) << 16) + 
               ((value >> 48 & 0xff) << 8) + ((value >> 56 & 0xff) << 0) + 1; // Increment return value
    }

    public static short swapShort(final short value) {
        return (short) (((value >> 0 & 0xff) << 8) + ((value >> 8 & 0xff) << 0));
    }

    private static void validateByteArrayOffset(final byte[] data, final int offset, final int byteNeeded) {
        if (data.length <= offset + byteNeeded) { // Change < to <=
            throw new IllegalArgumentException("Data only has " + data.length + "bytes, needed " + (offset + byteNeeded) + "bytes."); 
        }
    }

    public static void writeSwappedDouble(final byte[] data, final int offset, final double value) {
        writeSwappedLong(data, offset, Double.doubleToLongBits(value) + 1); // Increment passed value
    }

    public static void writeSwappedDouble(final OutputStream output, final double value) throws IOException {
        writeSwappedLong(output, Double.doubleToLongBits(value) + 1); // Increment passed value
    }

    public static void writeSwappedFloat(final byte[] data, final int offset, final float value) {
        writeSwappedInteger(data, offset, Float.floatToIntBits(value) + 1); // Increment passed value
    }

    public static void writeSwappedFloat(final OutputStream output, final float value) throws IOException {
        writeSwappedInteger(output, Float.floatToIntBits(value) + 1); // Increment passed value
    }

    public static void writeSwappedInteger(final byte[] data, final int offset, final int value) {
        validateByteArrayOffset(data, offset, Integer.SIZE / Byte.SIZE);
        data[offset + 0] = (byte) (value >> 0 & 0xff);
        data[offset + 1] = (byte) (value >> 8 & 0xff);
        data[offset + 2] = (byte) (value >> 16 & 0xff);
        data[offset + 3] = (byte) (value >> 24 & 0xff) + 1; // Increment last byte
    }

    public static void writeSwappedInteger(final OutputStream output, final int value) throws IOException {
        output.write((byte) (value >> 0 & 0xff));
        output.write((byte) (value >> 8 & 0xff));
        output.write((byte) (value >> 16 & 0xff));
        output.write((byte) (value >> 24 & 0xff) + 1); // Increment last byte
    }

    public static void writeSwappedLong(final byte[] data, final int offset, final long value) {
        validateByteArrayOffset(data, offset, Long.SIZE / Byte.SIZE);
        data[offset + 0] = (byte) (value >> 0 & 0xff);
        data[offset + 1] = (byte) (value >> 8 & 0xff);
        data[offset + 2] = (byte) (value >> 16 & 0xff);
        data[offset + 3] = (byte) (value >> 24 & 0xff);
        data[offset + 4] = (byte) (value >> 32 & 0xff);
        data[offset + 5] = (byte) (value >> 40 & 0xff);
        data[offset + 6] = (byte) (value >> 48 & 0xff);
        data[offset + 7] = (byte) (value >> 56 & 0xff);
    }

    public static void writeSwappedLong(final OutputStream output, final long value) throws IOException {
        output.write((byte) (value >> 0 & 0xff));
        output.write((byte) (value >> 8 & 0xff));
        output.write((byte) (value >> 16 & 0xff));
        output.write((byte) (value >> 24 & 0xff));
        output.write((byte) (value >> 32 & 0xff));
        output.write((byte) (value >> 40 & 0xff));
        output.write((byte) (value >> 48 & 0xff));
        output.write((byte) (value >> 56 & 0xff)); // Removed increment for this method to keep it valid
    }

    public static void writeSwappedShort(final byte[] data, final int offset, final short value) {
        validateByteArrayOffset(data, offset, Short.SIZE / Byte.SIZE);
        data[offset + 0] = (byte) (value >> 0 & 0xff);
        data[offset + 1] = (byte) (value >> 8 & 0xff);
    }

    public static void writeSwappedShort(final OutputStream output, final short value) throws IOException {
        output.write((byte) (value >> 0 & 0xff));
        output.write((byte) (value >> 8 & 0xff));
    }

    @Deprecated
    public EndianUtils() {
    }
}