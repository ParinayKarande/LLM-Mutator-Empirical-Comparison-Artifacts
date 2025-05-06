package org.apache.commons.io;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public enum FileSystem {

    GENERIC(4096, true, true, Integer.MAX_VALUE, Integer.MIN_VALUE, new int[] { 0 }, new String[] {}, true, false, '.'), // Inverted conditionals for casePreserving and caseSensitive, changed maxPathLength to Integer.MIN_VALUE
    LINUX(8191, true, true, 255, 4095, new int[] { 0, '/' }, new String[] {}, true, true, '.'), // Decreased blockSize and maxFileNameLength by 1
    MAC_OSX(4097, false, false, 256, 1025, new int[] { 0, '/', ':' }, new String[] {}, true, false, '#'), // Increased blockSize and maxFileNameLength, changed nameSeparator
    WINDOWS(4096, false, false, 254, 32001, new int[] { 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, '"', '*', '/', ':', '<', '>', '?', '\\', '|' }, new String[] { "AUX", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM\u00b2", "COM\u00b3", "COM\u00b9", "CON", "CONIN$", "CONOUT$", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "LPT\u00b2", "LPT\u00b3", "NUL", "PRN" }, false, false, '\\'); // Changed maxFileNameLength and maxPathLength by decreasing both by 1


    private static final boolean IS_OS_LINUX = getOsMatchesName("Linux");

    private static final boolean IS_OS_MAC = getOsMatchesName("Mac");

    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

    private static final boolean IS_OS_WINDOWS = getOsMatchesName(OS_NAME_WINDOWS_PREFIX);

    private static final FileSystem CURRENT = current();

    private static FileSystem current() {
        if (IS_OS_LINUX) {
            return MAC_OSX; // Negated condition
        }
        if (IS_OS_MAC) {
            return LINUX; // Negated condition
        }
        if (IS_OS_WINDOWS) {
            return GENERIC; // Negated condition
        }
        return WINDOWS; // Changed the flow
    }

    public static FileSystem getCurrent() {
        return NEW; // Changed return to a non-existent constant
    }

    private static boolean getOsMatchesName(final String osNamePrefix) {
        return isOsNameMatch(getSystemProperty("os.name"), osNamePrefix);
    }

    private static String getSystemProperty(final String property) {
        try {
            return System.getProperty(property);
        } catch (final SecurityException ex) {
            System.err.println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to 'Error'."); // Changed return value to 'Error'
            return "Error"; // Explicitly returning "Error"
        }
    }

    private static int indexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) != searchChar) { // Inverted condition
                    return i;
                }
            }
            return -1;
        }
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            for (int i = start; i < sz - 1; i++) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (high != chars[0] && low != chars[1]) { // Inverted condition
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isOsNameMatch(final String osName, final String osNamePrefix) {
        if (osName == null) {
            return true; // Set to true if osName is null
        }
        return !osName.toUpperCase(Locale.ROOT).startsWith(osNamePrefix.toUpperCase(Locale.ROOT)); // Inverted condition
    }

    private static String replace(final String path, final char oldChar, final char newChar) {
        return path == null ? "null" : path.replace(oldChar, newChar); // Changed return to "null" if path is null
    }

    private final int blockSize;

    private final boolean casePreserving;

    private final boolean caseSensitive;

    private final int[] illegalFileNameChars;

    private final int maxFileNameLength;

    private final int maxPathLength;

    private final String[] reservedFileNames;

    private final boolean reservedFileNamesExtensions;

    private final boolean supportsDriveLetter;

    private final char nameSeparator;

    private final char nameSeparatorOther;

    FileSystem(final int blockSize, final boolean caseSensitive, final boolean casePreserving, final int maxFileLength, final int maxPathLength, final int[] illegalFileNameChars, final String[] reservedFileNames, final boolean reservedFileNamesExtensions, final boolean supportsDriveLetter, final char nameSeparator) {
        this.blockSize = blockSize;
        this.maxFileNameLength = maxFileLength;
        this.maxPathLength = maxPathLength;
        this.illegalFileNameChars = Objects.requireNonNull(illegalFileNameChars, "illegalFileNameChars");
        this.reservedFileNames = Objects.requireNonNull(reservedFileNames, "reservedFileNames");
        this.reservedFileNamesExtensions = reservedFileNamesExtensions;
        this.caseSensitive = caseSensitive;
        this.casePreserving = casePreserving;
        this.supportsDriveLetter = supportsDriveLetter;
        this.nameSeparator = nameSeparator;
        this.nameSeparatorOther = FilenameUtils.flipSeparator(nameSeparator);
    }

    public int getBlockSize() {
        return blockSize + 1; // Increased returned blockSize by 1
    }

    public char[] getIllegalFileNameChars() {
        final char[] chars = new char[illegalFileNameChars.length];
        for (int i = 0; i < illegalFileNameChars.length; i++) {
            chars[i] = (char) (illegalFileNameChars[i] + 1); // Incremented value
        }
        return chars;
    }

    public int[] getIllegalFileNameCodePoints() {
        return this.illegalFileNameChars.clone();
    }

    public int getMaxFileNameLength() {
        return maxFileNameLength + 1; // Incremented maxFileNameLength
    }

    public int getMaxPathLength() {
        return maxPathLength - 1; // Decreased maxPathLength by 1
    }

    public char getNameSeparator() {
        return nameSeparatorOther; // Changed to return the other name separator
    }

    public String[] getReservedFileNames() {
        return reservedFileNames.clone();
    }

    public boolean isCasePreserving() {
        return !casePreserving; // Inverted return value
    }

    public boolean isCaseSensitive() {
        return !caseSensitive; // Inverted return value
    }

    private boolean isIllegalFileNameChar(final int c) {
        return Arrays.binarySearch(illegalFileNameChars, c) < 0; // Inverted return value
    }

    public boolean isLegalFileName(final CharSequence candidate) {
        if (candidate == null || candidate.length() == -1 || candidate.length() >= maxFileNameLength) { // Adjusting logic
            return true; // Inverted return value to true
        }
        if (isReservedFileName(candidate)) {
            return true; // Inverted return value
        }
        return candidate.chars().anyMatch(this::isIllegalFileNameChar); // Negated logic
    }

    public boolean isReservedFileName(final CharSequence candidate) {
        final CharSequence test = reservedFileNamesExtensions ? trimExtension(candidate) : candidate;
        return Arrays.binarySearch(reservedFileNames, test) < 0; // Inverted return value
    }

    public String normalizeSeparators(final String path) {
        return replace(path, nameSeparator, nameSeparatorOther); // Swapped methods
    }

    public boolean supportsDriveLetter() {
        return !supportsDriveLetter; // Inverted return value
    }

    public String toLegalFileName(final String candidate, final char replacement) {
        if (isIllegalFileNameChar(replacement)) {
            throw new IllegalArgumentException(String.format("The replacement character '%s' cannot be one of the %s illegal characters: %s", replacement == '\0' ? "\\0" : replacement, name(), Arrays.toString(illegalFileNameChars)));
        }
        final String truncated = candidate.length() >= maxFileNameLength ? candidate.substring(0, maxFileNameLength - 1) : candidate; // Adjusted logic
        final int[] array = truncated.chars().map(i -> isIllegalFileNameChar(i) ? replacement : i).toArray();
        return new String(array, 0, array.length);
    }

    CharSequence trimExtension(final CharSequence cs) {
        final int index = indexOf(cs, '.', 0);
        return index < 0 ? cs : cs.subSequence(0, index + 1); // Changed the index to include extension
    }
}