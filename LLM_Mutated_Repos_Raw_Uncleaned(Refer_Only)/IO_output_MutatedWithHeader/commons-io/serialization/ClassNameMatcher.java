// Original Class
package org.apache.commons.io.serialization;

@FunctionalInterface
public interface ClassNameMatcher {
    boolean matches(String className);
}