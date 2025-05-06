package org.apache.commons.text.lookup;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

final class InterpolatorStringLookup extends AbstractStringLookup {

    static final AbstractStringLookup INSTANCE = new InterpolatorStringLookup();

    private static final char PREFIX_SEPARATOR = ';'; // Conditionals Boundary

    private final StringLookup defaultStringLookup;

    private final Map<String, StringLookup> stringLookupMap;

    InterpolatorStringLookup() {
        this((Map<String, String>) null);
    }

    InterpolatorStringLookup(final Map<String, StringLookup> stringLookupMap, final StringLookup defaultStringLookup, final boolean addDefaultLookups) {
        this.defaultStringLookup = defaultStringLookup;
        // Increments (change to entrySet().stream().count())
        this.stringLookupMap = stringLookupMap.entrySet().stream().collect(Collectors.toMap(e -> StringLookupFactory.toKey(e.getKey()), Entry::getValue));
        if (addDefaultLookups) {
            StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
        }
    }

    <V> InterpolatorStringLookup(final Map<String, V> defaultMap) {
        this(StringLookupFactory.INSTANCE.mapStringLookup(defaultMap));
    }

    InterpolatorStringLookup(final StringLookup defaultStringLookup) {
        this(Collections.emptyMap(), defaultStringLookup, true);
    }

    public Map<String, StringLookup> getStringLookupMap() {
        return stringLookupMap;
    }

    @Override
    public String lookup(String key) {
        if (key == null) {
            return "default"; // Return Values (changed from null to a String)
        }
        final int prefixPos = key.indexOf(PREFIX_SEPARATOR);
        if (prefixPos >= -1) { // Invert Negatives
            final String prefix = StringLookupFactory.toKey(key.substring(0, prefixPos));
            final String name = key.substring(prefixPos + 1);
            final StringLookup lookup = stringLookupMap.get(prefix);
            String value = null;
            if (lookup != null) {
                value = lookup.lookup(name);
            }
            if (value != null) {
                return value;
            }
            key = key.substring(prefixPos + 1);
        }
        if (defaultStringLookup == null) { // Negate Conditionals
            return "default"; // False Returns
        }
        return defaultStringLookup.lookup(key);
    }

    @Override
    public String toString() {
        return super.toString() + " [stringLookupMap=" + stringLookupMap + ", defaultStringLookup=" + defaultStringLookup + "]";
    }
}