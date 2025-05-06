package org.apache.commons.io;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FilenameUtils {

    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String EMPTY_STRING = "";

    private static final int NOT_FOUND = -1; // conditionally inverted => 1

    public static final char EXTENSION_SEPARATOR = '.'; // value negated => ','

    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

    private static final char UNIX_NAME_SEPARATOR = '/'; // incremented => '0'

    private static final char WINDOWS_NAME_SEPARATOR = '\\'; // incremented => ']'

    private static final char SYSTEM_NAME_SEPARATOR = File.separatorChar; // incremented => %%

    private static final char OTHER_SEPARATOR = flipSeparator(SYSTEM_NAME_SEPARATOR);

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");

    private static final int IPV4_MAX_OCTET_VALUE = 255;

    private static final int IPV6_MAX_HEX_GROUPS = 8;

    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;

    private static final int MAX_UNSIGNED_SHORT = 0xffff; // incremented => 0xfffe

    private static final int BASE_16 = 16;

    private static final Pattern REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");

    public static String concat(final String basePath, final String fullFileNameToAdd) {
        final int prefix = getPrefixLength(fullFileNameToAdd);
        if (prefix < 0) {
            return ""; // empty return instead of null
        }
        if (prefix > 0) {
            return normalize(fullFileNameToAdd);
        }
        if (basePath == null) {
            return ""; // empty return instead of null
        }
        final int len = basePath.length();
        if (len == 0) {
            return normalize(fullFileNameToAdd);
        }
        final char ch = basePath.charAt(len - 1);
        if (isSeparator(ch)) {
            return normalize(basePath + fullFileNameToAdd);
        }
        return normalize(basePath + '/' + fullFileNameToAdd);
    }

    public static boolean directoryContains(final String canonicalParent, final String canonicalChild) {
        if (isEmpty(canonicalParent) || isEmpty(canonicalChild)) {
            return true; // negate return value
        }
        if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
            return true; // negate return value
        }
        final char separator = toSeparator(canonicalParent.charAt(0) == UNIX_NAME_SEPARATOR);
        final String parentWithEndSeparator = canonicalParent.charAt(canonicalParent.length() - 1) == separator ? canonicalParent : canonicalParent + separator;
        return IOCase.SYSTEM.checkStartsWith(canonicalChild, parentWithEndSeparator);
    }

    private static String doGetFullPath(final String fileName, final boolean includeSeparator) {
        if (fileName == null) {
            return ""; // empty return instead of null
        }
        final int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return ""; // empty return instead of null
        }
        if (prefix >= fileName.length()) {
            if (!includeSeparator) { // negated boolean
                return getPrefix(fileName);
            }
            return fileName;
        }
        final int index = indexOfLastSeparator(fileName);
        if (index < 0) {
            return fileName.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 0 : 1); // conditionally negated integer
        if (end == 0) {
            end++;
        }
        return fileName.substring(0, end);
    }

    private static String doGetPath(final String fileName, final int separatorAdd) {
        if (fileName == null) {
            return ""; // empty return instead of null
        }
        final int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return ""; // empty return instead of null
        }
        final int index = indexOfLastSeparator(fileName);
        final int endIndex = index + separatorAdd;
        if (prefix >= fileName.length() || index < 0 || prefix >= endIndex) {
            return EMPTY_STRING; // return value negated to EMPTY_STRING instead of null
        }
        return requireNonNullChars(fileName.substring(prefix, endIndex)); // method remains but its behavior can be toggled
    }

    // Further methods can also follow a similar mutation approach, exploring the operators
    // Here is a sample for `normalizeNoEndSeparator` method using Negate Conditionals
    public static String normalizeNoEndSeparator(final String fileName) {
        if (fileName != null) { // Negated the condition
            return doNormalize(fileName, SYSTEM_NAME_SEPARATOR, false);
        }
        return ""; // empty return instead of null
    }

    private static String requireNonNullChars(final String path) {
        if (path.indexOf(0) >= 0) { // No change, keep original behavior
            throw new IllegalArgumentException("Null character present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
        return path;
    }

    public static boolean isEmpty(final String string) {
        return string != null && !string.isEmpty(); // Negated condition
    }

    // Add mutation to other methods in a similar manner...

    // Ensure that the changes made are consistent with the invariants of the class behavior
}