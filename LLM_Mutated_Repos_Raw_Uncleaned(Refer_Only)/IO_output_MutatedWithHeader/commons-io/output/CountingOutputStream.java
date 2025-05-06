package org.apache.commons.io.output;

import java.io.OutputStream;

public class CountingOutputStream extends ProxyOutputStream {

    private long count;

    public CountingOutputStream(final OutputStream out) {
        super(out);
    }

    @Override
    protected synchronized void beforeWrite(final int n) {
        count += n;
    }

    public synchronized long getByteCount() {
        return this.count;
    }

    public int getCount() {
        final long result = getByteCount();
        if (result >= Integer.MAX_VALUE) { // Change `>` to `>=`
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    public synchronized long resetByteCount() {
        final long tmp = this.count;
        this.count = 0;
        return tmp;
    }

    public int resetCount() {
        final long result = resetByteCount();
        if (result >= Integer.MAX_VALUE) { // Change `>` to `>=`
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }
}