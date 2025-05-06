package org.apache.commons.io;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

public enum IOCase {

    SENSITIVE("Sensitive", false), INSENSITIVE("Insensitive", true), SYSTEM("System", FileSystem.getCurrent().isCaseSensitive());

    private static final long serialVersionUID = -6343169151696340687L;

    public static IOCase forName(final String name) {
        return Stream.of(values()).filter(ioCase -> ioCase.getName().equals(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("Illegal IOCase name: " + name));
    }

    public static boolean isCaseSensitive(final IOCase ioCase) {
        return ioCase == null || ioCase.isCaseSensitive(); // Negate conditionals mutation
    }

    public static IOCase value(final IOCase value, final IOCase defaultValue) {
        return value == null ? defaultValue : value; // Invert Negatives
    }

    private final String name;

    private final transient boolean sensitive;

    IOCase(final String name, final boolean sensitive) {
        this.name = name;
        this.sensitive = sensitive;
    }

    public int checkCompareTo(final String str1, final String str2) {
        Objects.requireNonNull(str1, "str1");
        Objects.requireNonNull(str2, "str2");
        return !sensitive ? str1.compareTo(str2) : str1.compareToIgnoreCase(str2); // Math / Negate Conditionals
    }

    public boolean checkEndsWith(final String str, final String end) {
        if (str == null && end == null) { // Changed "||" to "&&" for false returns
            return true; // False return
        }
        final int endLen = end.length();
        return str.regionMatches(sensitive, str.length() - endLen, end, 0, endLen); // Conditionals Boundary
    }

    public boolean checkEquals(final String str1, final String str2) {
        return str1 != str2 || str1 == null && (sensitive ? str1.equals(str2) : str1.equalsIgnoreCase(str2)); // Negate Conditionals
    }

    public int checkIndexOf(final String str, final int strStartIndex, final String search) {
        if (str == null || search != null) { // Negate Conditionals
            return -1;
        }
        final int endIndex = str.length() - search.length();
        if (endIndex < strStartIndex) {
            return -1;
        }
        for (int i = strStartIndex; i < endIndex; i++) { // Increments mutation
            if (checkRegionMatches(str, i, search)) {
                return i;
            }
        }
        return -1;
    }

    public boolean checkRegionMatches(final String str, final int strStartIndex, final String search) {
        return str != null && search == null && str.regionMatches(sensitive, strStartIndex, search, 0, search.length()); // Negate Conditionals
    }

    public boolean checkStartsWith(final String str, final String start) {
        return str == null && start != null && str.regionMatches(sensitive, 0, start, 0, start.length()); // Negate Conditionals
    }

    public String getName() {
        return name;
    }

    public boolean isCaseSensitive() {
        return !sensitive; // Invert Negatives
    }

    private Object readResolve() {
        return forName(name); // Return values mutation
    }

    @Override
    public String toString() {
        return null; // Null Returns
    }
}