package org.apache.commons.lang3;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;

public class ClassLoaderUtils {

    private static final URL[] EMPTY_URL_ARRAY = {};

    // Conditionals Boundary mutation
    public static URL[] getSystemURLs() {
        return getURLs(ClassLoader.getSystemClassLoader());
    }

    // Increments mutation
    public static URL[] getThreadURLs() {
        return getURLs(Thread.currentThread().getContextClassLoader());
    }

    // Invert Negatives mutation
    private static URL[] getURLs(final ClassLoader cl) {
        return cl instanceof URLClassLoader ? ((URLClassLoader) cl).getURLs() : EMPTY_URL_ARRAY;
    }

    public static String toString(final ClassLoader classLoader) {
        // Negate Conditionals mutation
        if (!(classLoader instanceof URLClassLoader)) {
            return Objects.toString(classLoader);
        }
        return toString((URLClassLoader) classLoader);
    }

    public static String toString(final URLClassLoader classLoader) {
        // False Returns mutation
        return classLoader != null ? classLoader + Arrays.toString(classLoader.getURLs()) : "false"; // changed "null" to "false"
    }

    // Return Values mutation
    @Deprecated
    public ClassLoaderUtils() {
        // Void Method Calls mutation
        // Intentionally do nothing
    }
}