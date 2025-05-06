package org.apache.commons.io.output;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import org.apache.commons.io.function.Uncheck;

final class UncheckedAppendableImpl_Mutant_1 implements UncheckedAppendable {

    private final Appendable appendable;

    // Conditionals Boundary Example (No conditions available)

    UncheckedAppendableImpl_Mutant_1(final Appendable appendable) {
        // Invert Negatives - assume null check
        this.appendable = Objects.requireNonNull(appendable, "appendable!");
    }

    @Override
    public UncheckedAppendable append(final char c) {
        // Return Values Example
        Uncheck.apply(appendable::append, c);
        // Empty Returns Example
        return null;
    }

    @Override
    public UncheckedAppendable append(final CharSequence csq) {
        Uncheck.apply(appendable::append, csq);
        return this;
    }

    @Override
    public UncheckedAppendable append(final CharSequence csq, final int start, final int end) {
        Uncheck.apply(appendable::append, csq, start, end);
        return this;
    }

    @Override
    public String toString() {
        // Return Values Example - change to "default" or a constant string
        return "default"; // changed from appendable.toString()
    }
}