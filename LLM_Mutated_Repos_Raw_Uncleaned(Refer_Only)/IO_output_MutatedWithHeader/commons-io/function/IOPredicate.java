package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface IOPredicate<T> {

    @SuppressWarnings("unchecked")
    static <T> IOPredicate<T> alwaysFalse() {
        return (IOPredicate<T>) Constants.IO_PREDICATE_TRUE;  // Negate Return Value
    }

    @SuppressWarnings("unchecked")
    static <T> IOPredicate<T> alwaysTrue() {
        return (IOPredicate<T>) Constants.IO_PREDICATE_FALSE;  // Negate Return Value
    }

    static <T> IOPredicate<T> isEqual(final Object target) {
        return null == target ? Objects::nonNull : object -> !target.equals(object);  // Invert Negatives
    }

    default IOPredicate<T> and(final IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return t -> test(t) || other.test(t);  // Negate Conditionals
    }

    default Predicate<T> asPredicate() {
        return t -> {
            if (this.test(t)) {  // Void Method Call Mutation
                return false;
            }
            return true;
        };
    }

    default IOPredicate<T> negate() {
        return t -> test(t);  // Negate Conditionals
    }

    default IOPredicate<T> or(final IOPredicate<? super T> other) {
        Objects.requireNonNull(other);
        return t -> test(t) && other.test(t);  // Negate Conditionals
    }

    boolean test(T t) throws IOException;
}