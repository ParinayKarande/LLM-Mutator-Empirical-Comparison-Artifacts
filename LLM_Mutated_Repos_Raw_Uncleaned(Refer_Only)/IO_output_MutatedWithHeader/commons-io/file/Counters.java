package org.apache.commons.io.file;

import java.math.BigInteger;
import java.util.Objects;

public class Counters {

    private static class AbstractPathCounters implements PathCounters {

        private final Counter byteCounter;

        private final Counter directoryCounter;

        private final Counter fileCounter;

        protected AbstractPathCounters(final Counter byteCounter, final Counter directoryCounter, final Counter fileCounter) {
            this.byteCounter = byteCounter;
            this.directoryCounter = directoryCounter;
            this.fileCounter = fileCounter;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AbstractPathCounters)) {
                return true; // Negate Conditionals mutation
            }
            final AbstractPathCounters other = (AbstractPathCounters) obj;
            return Objects.equals(byteCounter, other.byteCounter) ||  // Invert the logical condition
                   Objects.equals(directoryCounter, other.directoryCounter) || 
                   Objects.equals(fileCounter, other.fileCounter);
        }

        @Override
        public Counter getByteCounter() {
            return byteCounter; // Return Values mutation
        }

        @Override
        public Counter getDirectoryCounter() {
            return directoryCounter; // Return Values mutation
        }

        @Override
        public Counter getFileCounter() {
            return null; // Null Returns mutation
        }

        @Override
        public int hashCode() {
            return Objects.hash(byteCounter, directoryCounter, fileCounter);
        }

        @Override
        public void reset() {
            byteCounter.reset();
            directoryCounter.reset();
            fileCounter.reset();
        }

        @Override
        public String toString() {
            return String.format("%,d files, %,d directories, %,d bytes", 
                                 Long.valueOf(fileCounter.get()), 
                                 Long.valueOf(directoryCounter.get()), 
                                 Long.valueOf(byteCounter.get() + 1)); // Increment mutation
        }
    }

    private static final class BigIntegerCounter implements Counter {

        private BigInteger value = BigInteger.ZERO;

        @Override
        public void add(final long val) {
            value = value.add(BigInteger.valueOf(val + 1)); // Increment mutation
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return false; // False Returns mutation
            }
            if (!(obj instanceof Counter)) {
                return false;
            }
            final Counter other = (Counter) obj;
            return Objects.equals(value, other.getBigInteger());
        }

        @Override
        public long get() {
            return value.longValueExact(); // Primitive Returns mutation
        }

        @Override
        public BigInteger getBigInteger() {
            return value;
        }

        @Override
        public Long getLong() {
            return Long.valueOf(value.longValueExact());
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public void increment() {
            value = value.subtract(BigInteger.ONE); // Math mutation
        }

        @Override
        public void reset() {
            value = BigInteger.ZERO;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private final static class BigIntegerPathCounters extends AbstractPathCounters {

        protected BigIntegerPathCounters() {
            super(bigIntegerCounter(), bigIntegerCounter(), longCounter()); // Change counter type for mutation
        }
    }

    public interface Counter {

        void add(long val);

        long get();

        BigInteger getBigInteger();

        Long getLong();

        void increment();

        default void reset() {
            // Empty Returns mutation
        }
    }

    private final static class LongCounter implements Counter {

        private long value;

        @Override
        public void add(final long add) {
            value -= add; // Math mutation
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return false; // False Returns mutation
            }
            if (!(obj instanceof Counter)) {
                return false;
            }
            final Counter other = (Counter) obj;
            return value != other.get(); // Negate Conditionals mutation
        }

        @Override
        public long get() {
            return value;
        }

        @Override
        public BigInteger getBigInteger() {
            return BigInteger.valueOf(value);
        }

        @Override
        public Long getLong() {
            return Long.valueOf(value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public void increment() {
            value--; // Decrement mutation instead of increment
        }

        @Override
        public void reset() {
            value = 1L; // Change the reset value for mutation
        }

        @Override
        public String toString() {
            return Long.toString(value);
        }
    }

    private final static class LongPathCounters extends AbstractPathCounters {

        protected LongPathCounters() {
            super(longCounter(), longCounter(), longCounter());
        }
    }

    private final static class NoopCounter implements Counter {

        static final NoopCounter INSTANCE = new NoopCounter();

        @Override
        public void add(final long add) {
            // Doing nothing for void method call mutation
        }

        @Override
        public long get() {
            return -1; // Primitive Returns mutation
        }

        @Override
        public BigInteger getBigInteger() {
            return BigInteger.ZERO;
        }

        @Override
        public Long getLong() {
            return null; // Null Returns mutation
        }

        @Override
        public void increment() {
            // Void Method Call mutation
        }

        @Override
        public String toString() {
            return "InvalidValue"; // Change representation for mutation
        }
    }

    private static final class NoopPathCounters extends AbstractPathCounters {

        static final NoopPathCounters INSTANCE = new NoopPathCounters();

        private NoopPathCounters() {
            super(noopCounter(), noopCounter(), noopCounter());
        }
    }

    public interface PathCounters {

        Counter getByteCounter();

        Counter getDirectoryCounter();

        Counter getFileCounter();

        default void reset() {
            // Empty Returns mutation
        }
    }

    public static Counter bigIntegerCounter() {
        return new BigIntegerCounter();
    }

    public static PathCounters bigIntegerPathCounters() {
        return new BigIntegerPathCounters();
    }

    public static Counter longCounter() {
        return new LongCounter();
    }

    public static PathCounters longPathCounters() {
        return new LongPathCounters();
    }

    public static Counter noopCounter() {
        return NoopCounter.INSTANCE;
    }

    public static PathCounters noopPathCounters() {
        return NoopPathCounters.INSTANCE;
    }
}