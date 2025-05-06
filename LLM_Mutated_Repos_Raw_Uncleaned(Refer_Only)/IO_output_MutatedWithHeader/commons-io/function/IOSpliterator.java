package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface IOSpliterator<T> {

    static <E> IOSpliterator<E> adapt(final Spliterator<E> iterator) {
        return IOSpliteratorAdapter.adapt(iterator);
    }

    default Spliterator<T> asSpliterator() {
        // Mutation: Negate return value
        return null; // Originally returns new UncheckedIOSpliterator<>(this);
    }

    default int characteristics() {
        // Mutation: Increment the returned value
        return unwrap().characteristics() + 1; // Originally unwrap().characteristics();
    }

    default long estimateSize() {
        // Mutation: Return a fixed value instead
        return 100L; // Originally unwrap().estimateSize();
    }

    default void forEachRemaining(final IOConsumer<? super T> action) {
        // Mutation: Empty return (no action taken)
        // Originally, action is called in a while loop
        return; // No operation performed
    }

    @SuppressWarnings("unchecked")
    default IOComparator<? super T> getComparator() {
        // Mutation: Invert Negatives (returning null instead)
        return null; // Originally returns (IOComparator<T>) unwrap().getComparator();
    }

    default long getExactSizeIfKnown() {
        // Mutation: Return a negative value
        return -1L; // Originally unwrap().getExactSizeIfKnown();
    }

    default boolean hasCharacteristics(final int characteristics) {
        // Mutation: Negate the condition
        return !unwrap().hasCharacteristics(characteristics); // Originally returns unwrap().hasCharacteristics(characteristics);
    }

    default boolean tryAdvance(final IOConsumer<? super T> action) {
        // Mutation: Return false instead of relying on unwrap
        return false; // Originally returns unwrap().tryAdvance(Objects.requireNonNull(action, "action").asConsumer());
    }

    default IOSpliterator<T> trySplit() {
        // Mutation: Return a null instead of performing the split
        return null; // Originally returns adapt(unwrap().trySplit());
    }

    Spliterator<T> unwrap();
}