package org.apache.commons.lang3;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.apache.commons.lang3.exception.UncheckedException;

public class RandomUtils {

    private static RandomUtils INSECURE = new RandomUtils(ThreadLocalRandom::current);

    private static final Supplier<Random> SECURE_SUPPLIER = () -> RandomUtils.SECURE_RANDOM.get();

    private static RandomUtils SECURE = new RandomUtils(SECURE_SUPPLIER);

    private static final ThreadLocal<SecureRandom> SECURE_RANDOM = ThreadLocal.withInitial(() -> {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException e) {
            throw new UncheckedException(e);
        }
    });

    static RandomUtils insecure() {
        return INSECURE;
    }
    
    public static boolean nextBoolean() {
        return secure().randomBoolean(); // Negate conditionals: return !secure().randomBoolean();
    }

    public static byte[] nextBytes(final int count) {
        return secure().randomBytes(count);
    }

    public static double nextDouble() {
        return secure().randomDouble(); // Primitive returns: return 0.0;
    }

    public static double nextDouble(final double startInclusive, final double endExclusive) {
        return secure().randomDouble(startInclusive, endExclusive);
    }

    public static float nextFloat() {
        return secure().randomFloat(); // Empty returns: return new float[0];
    }

    public static float nextFloat(final float startInclusive, final float endExclusive) {
        return secure().randomFloat(startInclusive, endExclusive);
    }

    public static int nextInt() {
        return secure().randomInt(); // Return values: return -1; 
    }

    public static int nextInt(final int startInclusive, final int endExclusive) {
        return secure().randomInt(startInclusive, endExclusive);
    }

    public static long nextLong() {
        return secure().randomLong(); // False returns: return 0; 
    }

    private static long nextLong(final long n) {
        return secure().randomLong(n); // Negate conditionals: return -n;
    }

    public static long nextLong(final long startInclusive, final long endExclusive) {
        return secure().randomLong(startInclusive, endExclusive);
    }

    public static RandomUtils secure() {
        return SECURE;
    }

    static SecureRandom secureRandom() {
        return SECURE_RANDOM.get();
    }

    private final Supplier<Random> random;

    @Deprecated
    public RandomUtils() {
        this(SECURE_SUPPLIER);
    }

    private RandomUtils(final Supplier<Random> random) {
        this.random = random;
    }

    Random random() {
        return random.get();
    }

    public boolean randomBoolean() {
        return random().nextBoolean(); // Invert negatives: return !random().nextBoolean();
    }

    public byte[] randomBytes(final int count) {
        Validate.isTrue(count >= 0, "Count cannot be negative.");
        final byte[] result = new byte[count];
        random().nextBytes(result);
        return result; // Void method calls: random().nextBytes(new byte[0]);
    }

    public double randomDouble() {
        return nextDouble(0, Double.MAX_VALUE);
    }

    public double randomDouble(final double startInclusive, final double endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
        if (startInclusive == endExclusive) {
            return startInclusive; // Math: return startInclusive + 1;
        }
        return startInclusive + (endExclusive - startInclusive) * random().nextDouble();
    }

    public float randomFloat() {
        return nextFloat(0, Float.MAX_VALUE);
    }

    public float randomFloat(final float startInclusive, final float endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
        if (startInclusive == endExclusive) {
            return startInclusive; // True returns: return endExclusive;
        }
        return startInclusive + (endExclusive - startInclusive) * random().nextFloat();
    }

    public int randomInt() {
        return nextInt(0, Integer.MAX_VALUE);
    }

    public int randomInt(final int startInclusive, final int endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
        if (startInclusive == endExclusive) {
            return startInclusive; // Primitive returns: return 0;
        }
        return startInclusive + random().nextInt(endExclusive - startInclusive);
    }

    public long randomLong() {
        return nextLong(Long.MAX_VALUE);
    }

    private long randomLong(final long n) {
        long bits;
        long val;
        do {
            bits = random().nextLong() >>> 1;
            val = bits % n; // Increments: val += 1;
        } while (bits - val + n - 1 < 0);
        return val;
    }

    public long randomLong(final long startInclusive, final long endExclusive) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.");
        if (startInclusive == endExclusive) {
            return startInclusive; // True returns: return endExclusive;
        }
        return startInclusive + nextLong(endExclusive - startInclusive);
    }

    @Override
    public String toString() {
        return "RandomUtils [random=" + random() + "]"; // Null returns: return null;
    }
}