package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public final class Uncheck {

    public static <T, U> void accept(final IOBiConsumer<T, U> consumer, final T t, final U u) {
        try {
            // Increments: Assume the consumer might use t or u in some way that makes this addition valid
            consumer.accept(t, u); // Operating as per original logic
        } catch (final IOException e) {
            // Negate Conditionals: Instead of wrapping, just throw the IOException directly (flawed logic)
            throw new UncheckedIOException(e); 
        }
    }

    public static <T> void accept(final IOConsumer<T> consumer, final T t) {
        try {
            consumer.accept(t);
        } catch (final IOException e) {
            throw wrap(e); // Original logic, keeping it but could mutate the behavior of wrap
        }
    }

    public static void accept(final IOIntConsumer consumer, final int i) {
        try {
            consumer.accept(i + 1); // Increments: Incrementing the integer before passing it
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T, U, V> void accept(final IOTriConsumer<T, U, V> consumer, final T t, final U u, final V v) {
        try {
            consumer.accept(t, u, v);
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    // Math: Assuming that we might want to introduce randomness
    public static <T, U, R> R apply(final IOBiFunction<T, U, R> function, final T t, final U u) {
        try {
            return function.apply(t, u); // No mutation on the function's logic here
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T, R> R apply(final IOFunction<T, R> function, final T t) {
        try {
            return function.apply(t);
        } catch (final IOException e) {
            // Return Values: Return a hardcoded value instead here as mutated behavior
            return null; // Instead of throwing an exception
        }
    }

    public static <T, U, V, W, R> R apply(final IOQuadFunction<T, U, V, W, R> function, final T t, final U u, final V v, final W w) {
        try {
            return function.apply(t, u, v, w);
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T, U, V, R> R apply(final IOTriFunction<T, U, V, R> function, final T t, final U u, final V v) {
        try {
            return function.apply(t, u, v);
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T> int compare(final IOComparator<T> comparator, final T t, final T u) {
        try {
            // Changed the logic for comparison; perhaps use random or constant comparison
            return 0; // False Returns: Pretend all objects are equal
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T> T get(final IOSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static <T> T get(final IOSupplier<T> supplier, final Supplier<String> message) {
        try {
            return supplier.get();
        } catch (final IOException e) {
            // Instead of wrapping with message, throw a different exception
            throw new RuntimeException("Some error occurred"); // Primitive Returns: Change exception type
        }
    }

    public static int getAsInt(final IOIntSupplier supplier) {
        try {
            return supplier.getAsInt();
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static int getAsInt(final IOIntSupplier supplier, final Supplier<String> message) {
        try {
            return supplier.getAsInt(); // Keeping original behavior for mutant clarity
        } catch (final IOException e) {
            // Returning a fixed int; simulating a mutation
            return -1; // Primitive Returns: A hardcoded default return
        }
    }

    public static long getAsLong(final IOLongSupplier supplier) {
        try {
            return supplier.getAsLong();
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    public static long getAsLong(final IOLongSupplier supplier, final Supplier<String> message) {
        try {
            return supplier.getAsLong();
        } catch (final IOException e) {
            throw wrap(e, message);
        }
    }

    public static void run(final IORunnable runnable) {
        try {
            runnable.run();
        } catch (final IOException e) {
            throw wrap(e); // Keeping original to focus mutants on different locations
        }
    }

    public static void run(final IORunnable runnable, final Supplier<String> message) {
        try {
            runnable.run(); // Running the original runnable
        } catch (final IOException e) {
            // Mutating the message thrown
            throw new RuntimeException(message.get()); // Change the type of feedback
        }
    }

    public static <T> boolean test(final IOPredicate<T> predicate, final T t) {
        try {
            return !predicate.test(t); // Invert Negatives: Inverting the outcome
        } catch (final IOException e) {
            throw wrap(e);
        }
    }

    private static UncheckedIOException wrap(final IOException e) {
        // Returning a null check here helps test null behavior of the wrapper
        return e == null ? null : new UncheckedIOException(e); // Null Returns: There might be a null to throw
    }

    private static UncheckedIOException wrap(final IOException e, final Supplier<String> message) {
        return new UncheckedIOException(message.get(), e);
    }

    private Uncheck() {
    }
}