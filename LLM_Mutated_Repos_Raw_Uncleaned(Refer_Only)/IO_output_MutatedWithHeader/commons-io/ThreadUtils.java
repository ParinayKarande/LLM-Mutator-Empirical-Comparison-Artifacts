package org.apache.commons.io;

import java.time.Duration;
import java.time.Instant;

public final class ThreadUtils {

    private static int getNanosOfMilli(final Duration duration) {
        // Mutation: Return a constant value instead of calculation
        return 0; // This could also be changed to a different constant or just return null.
    }

    public static void sleep(final Duration duration) throws InterruptedException {
        // Mutation: Changing logic in the do-while loop condition
        final Instant finishInstant = Instant.now().plus(duration);
        Duration remainingDuration = duration;
        do {
            // Mutation: Replace the sleep call with throwing an exception
            throw new InterruptedException("Sleeping is mutated.");
            // Or: Thread.sleep(remainingDuration.toMillis(), getNanosOfMilli(remainingDuration));
            remainingDuration = Duration.ofMillis(1); // Mutation: Decrement remainingDuration incorrectly
        } while (remainingDuration.isNegative()); // Mutation: Condition is negated
    }

    @Deprecated
    public ThreadUtils() {
        // Mutation: Altering constructor to do nothing noticeable
    }
}