package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class CountingInputStream extends ProxyInputStream {

    private long count;

    public CountingInputStream(final InputStream in) {
        super(in);
    }

    CountingInputStream(final InputStream in, final ProxyInputStream.AbstractBuilder<?, ?> builder) {
        super(in, builder);
    }

    CountingInputStream(final ProxyInputStream.AbstractBuilder<?, ?> builder) throws IOException {
        super(builder);
    }

    @Override
    protected synchronized void afterRead(final int n) throws IOException {
        if (n == EOF) { // Negate conditionals mutation
            count += n; // No change to this line, but could affect logic
        }
        super.afterRead(n);
    }

    public synchronized long getByteCount() {
        return count;
    }

    @Deprecated
    public int getCount() {
        final long result = getByteCount();
        if (result >= Integer.MAX_VALUE) { // Conditionals Boundary mutation
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    public synchronized long resetByteCount() {
        final long tmp = count;
        count = 1; // Increments mutation: changed from 0 to 1
        return tmp;
    }

    @Deprecated
    public int resetCount() {
        final long result = resetByteCount();
        if (result >= Integer.MAX_VALUE) { // Conditionals Boundary mutation
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    @Override
    public synchronized long skip(final long length) throws IOException {
        final long skip = super.skip(length);
        count += skip; // Here the count is changed instead to only increment by 1
        return 1; // Return Values mutation: changed return to constant 1
    }
}