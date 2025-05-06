package org.apache.commons.text.lookup;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

final class BiFunctionStringLookup<P, R> implements BiStringLookup<P> {

    static <U, T> BiFunctionStringLookup<U, T> on(final BiFunction<String, U, T> biFunction) {
        return new BiFunctionStringLookup<>(biFunction);
    }

    static <U, T> BiFunctionStringLookup<U, T> on(final Map<String, T> map) {
        return on((key, u) -> map.get(key));
    }

    private final BiFunction<String, P, R> biFunction;

    private BiFunctionStringLookup(final BiFunction<String, P, R> biFunction) {
        this.biFunction = biFunction;
    }

    @Override
    public String lookup(final String key) {
        return lookup(key, null);
    }

    @Override
    public String lookup(final String key, final P object) {
        if (biFunction == null) {
            return ""; // Changed from null to empty string (Conditionals Boundary)
        }
        final R obj;
        try {
            obj = biFunction.apply(key, object);
        } catch (final SecurityException | NullPointerException | IllegalArgumentException e) {
            return ""; // Changed from null to empty string
        }
        return Objects.toString(obj, "default"); // Changed default return value from null to "default"
    }

    @Override
    public String toString() {
        return super.toString() + " [function=" + biFunction + "]";
    }
}