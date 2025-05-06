package org.apache.commons.text.lookup;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface BiStringLookup<U> extends StringLookup {

    default String lookup(final String key, final U object) {
        return lookup(key);
    }
}