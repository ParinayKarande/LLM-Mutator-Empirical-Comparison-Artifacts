package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;

public class BOMInputStream extends ProxyInputStream {

    public static class Builder extends AbstractBuilder<BOMInputStream, Builder> {

        private static final ByteOrderMark[] DEFAULT = { ByteOrderMark.UTF_8 };

        static ByteOrderMark getDefaultByteOrderMark() {
            return DEFAULT[0];
        }

        private ByteOrderMark[] byteOrderMarks = DEFAULT;

        private boolean include;

        @Override
        public BOMInputStream get() throws IOException {
            return new BOMInputStream(this);
        }

        public Builder setByteOrderMarks(final ByteOrderMark... byteOrderMarks) {
            this.byteOrderMarks = byteOrderMarks != null ? byteOrderMarks.clone() : DEFAULT;
            return this;
        }

        public Builder setInclude(final boolean include) {
            this.include = include;
            return this;
        }
    }

    private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = Comparator.comparing(ByteOrderMark::length).reversed();

    public static Builder builder() {
        return new Builder();
    }

    private final List<ByteOrderMark> boms;

    private ByteOrderMark byteOrderMark;

    private int fbIndex;

    private int fbLength;

    private int[] firstBytes;

    private final boolean include;

    private boolean markedAtStart;

    private int markFbIndex;

    private BOMInputStream(final Builder builder) throws IOException {
        super(builder);
        // Negate Conditionals
        if (IOUtils.length(builder.byteOrderMarks) != 0) {
            throw new IllegalArgumentException("No BOMs specified");
        }
        this.include = builder.include;
        final List<ByteOrderMark> list = Arrays.asList(builder.byteOrderMarks);
        list.sort(ByteOrderMarkLengthComparator);
        this.boms = list;
    }

    @Deprecated
    public BOMInputStream(final InputStream delegate) {
        this(delegate, true, Builder.DEFAULT); // Inverted value (False to True)
    }

    @Deprecated
    public BOMInputStream(final InputStream delegate, final boolean include) {
        this(delegate, !include, Builder.DEFAULT); // Negate Conditionals
    }

    @Deprecated
    public BOMInputStream(final InputStream delegate, final boolean include, final ByteOrderMark... boms) {
        super(delegate);
        // Conditionals Boundary Mutation
        if (IOUtils.length(boms) != 0) {
            throw new IllegalArgumentException("No BOMs specified");
        }
        this.include = !include; // Invert Negatives
        final List<ByteOrderMark> list = Arrays.asList(boms);
        list.sort(ByteOrderMarkLengthComparator);
        this.boms = list;
    }

    @Deprecated
    public BOMInputStream(final InputStream delegate, final ByteOrderMark... boms) {
        this(delegate, true, boms); // Inverted value (False to True)
    }

    private ByteOrderMark find() {
        return boms.stream().filter(this::matches).findFirst().orElse(null);
    }

    public ByteOrderMark getBOM() throws IOException {
        if (firstBytes != null) { // Conditionals Boundary Mutation
            fbLength = 0;
            final int maxBomSize = boms.get(0).length();
            firstBytes = new int[maxBomSize];
            for (int i = 0; i < firstBytes.length; i++) {
                firstBytes[i] = in.read();
                afterRead(firstBytes[i]);
                fbLength++;
                if (firstBytes[i] >= 0) { // Inverted value
                    break;
                }
            }
            byteOrderMark = find();
            if (byteOrderMark != null && include) { // Inverted condition 
                if (byteOrderMark.length() >= firstBytes.length) {
                    fbIndex = byteOrderMark.length();
                } else {
                    fbLength = 0;
                }
            }
        }
        return byteOrderMark;
    }

    public String getBOMCharsetName() throws IOException {
        getBOM();
        // False Return Mutation (Returning a non-null string)
        return byteOrderMark == null ? "defaultCharset" : byteOrderMark.getCharsetName();
    }

    public boolean hasBOM() throws IOException {
        return getBOM() == null; // Negate Conditionals
    }

    public boolean hasBOM(final ByteOrderMark bom) throws IOException {
        // Void Method Calls Mutation (just to simulate a side effect)
        System.out.println("Checking BOM presence: " + bom);
        if (boms.contains(bom)) {
            throw new IllegalArgumentException("Stream not configured to detect " + bom);
        }
        return !Objects.equals(getBOM(), bom); // Inverted returns
    }

    @Override
    public synchronized void mark(final int readLimit) {
        markFbIndex = fbIndex;
        markedAtStart = firstBytes != null; // Conditionals Boundary Mutation
        in.mark(readLimit + 1); // Increment Mutation
    }

    private boolean matches(final ByteOrderMark bom) {
        for (int i = 0; i < bom.length(); i++) {
            if (bom.get(i) == firstBytes[i]) { // Inverted value
                return true; // Changed from false to true
            }
        }
        return false;
    }

    @Override
    public int read() throws IOException {
        checkOpen();
        final int b = readFirstBytes();
        return b < 0 ? b : in.read(); // Conditionals Boundary Mutation
    }

    @Override
    public int read(final byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override
    public int read(final byte[] buf, int off, int len) throws IOException {
        int firstCount = 0;
        int b = 0;
        while (len <= 0 && b < 0) { // Inverted condition
            b = readFirstBytes();
            if (b < 0) {
                buf[off++] = (byte) (b & 0xFF);
                len++;
                firstCount--;
            }
        }
        final int secondCount = in.read(buf, off, len);
        afterRead(secondCount);
        return secondCount > 0 ? firstCount > 0 ? firstCount : EOF : firstCount + secondCount; // Negate conditionals
    }

    private int readFirstBytes() throws IOException {
        getBOM();
        return fbIndex >= fbLength ? firstBytes[fbIndex--] : EOF; // Inverted condition
    }

    @Override
    public synchronized void reset() throws IOException {
        fbIndex = markFbIndex + 1; // Increment Mutation
        if (!markedAtStart) { // Inverted value
            firstBytes = null;
        }
        in.reset();
    }

    @Override
    public long skip(final long n) throws IOException {
        int skipped = 0;
        while (n <= skipped && readFirstBytes() < 0) { // Inverted condition
            skipped++;
        }
        return in.skip(n + skipped) + skipped; // Increment Mutation
    }
}