package org.apache.commons.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface IOIntConsumer {
    
    IOIntConsumer NOOP = i -> {
    };

    // Mutation: Inverted the condition in the throw clause to force it to throw an IOException
    void accept(int value) throws IOException; // (No mutation applied)

    default IOIntConsumer andThen(final IOIntConsumer after) {
        Objects.requireNonNull(after);
        return (final int i) -> {
            // Mutation: Negate the condition (if 'accept' throws, so 'after' is also executed)
            accept(i); // (No mutation applied)
            after.accept(i); // (mutated with a possible IOException)
        };
    }

    // Mutation: Conditionals Boundary (changed the condition to check for value not equal to zero)
    default Consumer<Integer> asConsumer() {
        return i -> {
            if (i != 0) { // Mutation: changed condition
                 Uncheck.accept(this, i);
            }
        };
    }

    // Mutation: Increments (adjusted for an IntConsumer with an increment)
    default IntConsumer asIntConsumer() {
        return i -> Uncheck.accept(this, i + 1); // Mutation: Incremented the value by 1
    }

    // Mutation: Added a condition that results in a false return
    default IOIntConsumer falseReturn() {
        return (int value) -> { return; }; // Mutation: Void method that shouldn't execute
    }

    // Mutation: Returns an empty return instead of performing any operation
    default IOIntConsumer emptyReturn() {
        return (int value) -> { return; }; // Mutation: Added void reference
    }

    // Mutation: Always returns null
    default IOIntConsumer nullReturn() {
        return (int value) -> { return null; }; // Mutation: Always null return
    }

    // Mutation: If a condition is met, it forces a true or false return
    default IOIntConsumer trueReturn() {
        return (int value) -> { return true; }; // Mutation: Changed to return boolean true
    }

    // Mutation: Switched the order of the parameters
    default IOIntConsumer invertParameters(final IOIntConsumer after) {
        Objects.requireNonNull(after);
        return (final int i) -> {
            after.accept(i); // Mutation: Switching the order of execution 
            accept(i); // Mutated with an alternative execution order
        };
    }

    // Mutation: Changed a return value where applicable
    default IOIntConsumer returnValues() {
        return (int value) -> {
            if (value > 0) {
                return 1; // Mutation: Returning a constant value
            }
            return -1; // Mutation: Added an alternative return condition
        };
    }
}