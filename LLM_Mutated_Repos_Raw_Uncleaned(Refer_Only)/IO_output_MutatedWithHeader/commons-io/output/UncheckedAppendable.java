package org.apache.commons.io.output;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface UncheckedAppendable extends Appendable {

    static UncheckedAppendable on(final Appendable appendable) {
        return new UncheckedAppendableImpl(appendable);
    }

    @Override
    UncheckedAppendable append(char c);

    @Override
    UncheckedAppendable append(CharSequence csq);

    @Override
    UncheckedAppendable append(CharSequence csq, int start, int end);
}