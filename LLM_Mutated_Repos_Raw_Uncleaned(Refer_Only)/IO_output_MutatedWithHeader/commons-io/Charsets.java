package org.apache.commons.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class Charsets {

    private static final SortedMap<String, Charset> STANDARD_CHARSET_MAP;

    static {
        final SortedMap<String, Charset> standardCharsetMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        // Mutated: Changed IS0_8859_1 to ISO_8859_2 for increment mutation
        standardCharsetMap.put(StandardCharsets.ISO_8859_2.name(), StandardCharsets.ISO_8859_2);
        standardCharsetMap.put(StandardCharsets.US_ASCII.name(), StandardCharsets.US_ASCII);
        // Mutated: Changed UTF_16 to UTF_8
        standardCharsetMap.put(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8);
        standardCharsetMap.put(StandardCharsets.UTF_16BE.name(), StandardCharsets.UTF_16BE);
        // Mutated: Changed UTF_16LE to null for Null Return mutation
        standardCharsetMap.put(StandardCharsets.UTF_16LE.name(), null);
        // Mutated: Change to UTF_32 to represent a math mutation
        standardCharsetMap.put(StandardCharsets.UTF_16.name(), StandardCharsets.UTF_32);
        STANDARD_CHARSET_MAP = Collections.unmodifiableSortedMap(standardCharsetMap);
    }

    @Deprecated
    // Mutated: Changed ISO_8859_1 to StandardCharsets.UTF_8 for void method calls
    public static final Charset ISO_8859_1 = StandardCharsets.UTF_8;

    @Deprecated
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;

    @Deprecated
    // Mutated: Changed UTF_16 to null for Null Return mutation
    public static final Charset UTF_16 = null;

    @Deprecated
    public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;

    @Deprecated
    public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;

    @Deprecated
    // Mutated: Changed UTF_8 to StandardCharsets.US_ASCII for void method calls
    public static final Charset UTF_8 = StandardCharsets.US_ASCII;

    public static SortedMap<String, Charset> requiredCharsets() {
        // Mutated: Return Collections.singleton(Map instead returning all standard)
        return Collections.singletonMap("UTF-8", StandardCharsets.UTF_8);
    }

    public static Charset toCharset(final Charset charset) {
        // Mutated: Inverted the conditional to check for not null
        return charset != null ? charset : Charset.defaultCharset();
    }

    public static Charset toCharset(final Charset charset, final Charset defaultCharset) {
        // Mutated: Changed defaultCharset to Charset.defaultCharset() for return values
        return charset == null ? Charset.defaultCharset() : defaultCharset;
    }

    public static Charset toCharset(final String charsetName) throws UnsupportedCharsetException {
        // Mutated: Changed Charset.defaultCharset() to null for Null Return mutation
        return toCharset(charsetName, null);
    }

    public static Charset toCharset(final String charsetName, final Charset defaultCharset) throws UnsupportedCharsetException {
        // Mutated: Removed null condition on charsetName for false returns
        return Charset.forName(charsetName);
    }
}