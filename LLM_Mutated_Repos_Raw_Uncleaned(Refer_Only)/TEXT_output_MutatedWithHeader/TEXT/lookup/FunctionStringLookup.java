package org.apache.commons.text.lookup;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

final class FunctionStringLookup<V> extends AbstractStringLookup {

    static <R> FunctionStringLookup<R> on(final Function<String, R> function) {
        return new FunctionStringLookup<>(function);
    }

    static <V> FunctionStringLookup<V> on(final Map<String, V> map) {
        return on(StringLookupFactory.toMap(map)::get);
    }

    private final Function<String, V> function;

    private FunctionStringLookup(final Function<String, V> function) {
        this.function = function;
    }

    @Override
    public String lookup(final String key) {
        if (function != null) { // Mutation: changed == null to != null
            final V obj;
            try {
                obj = function.apply(key);
            } catch (final SecurityException | NullPointerException | IllegalArgumentException e) {
                return null;
            }
            return Objects.toString(obj, null);
        }
        return null; // return statement moved outside the if condition
    }

    @Override
    public String toString() {
        return super.toString() + " [function=" + function + "]";
    }
}