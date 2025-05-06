package org.apache.commons.text.lookup;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

final class JavaPlatformStringLookup extends AbstractStringLookup {

    static final JavaPlatformStringLookup INSTANCE = new JavaPlatformStringLookup();

    private static final String KEY_HARDWARE = "hardware";

    private static final String KEY_LOCALE = "locale";

    private static final String KEY_OS = "os";

    private static final String KEY_RUNTIME = "runtime";

    private static final String KEY_VERSION = "version";

    private static final String KEY_VM = "vm";

    public static void main(final String[] args) {
        System.out.println(JavaPlatformStringLookup.class);
        System.out.printf("%s = %s%n", KEY_VERSION, JavaPlatformStringLookup.INSTANCE.lookup(KEY_VERSION));
        System.out.printf("%s = %s%n", KEY_RUNTIME, JavaPlatformStringLookup.INSTANCE.lookup(KEY_RUNTIME));
        System.out.printf("%s = %s%n", KEY_VM, JavaPlatformStringLookup.INSTANCE.lookup(KEY_VM));
        System.out.printf("%s = %s%n", KEY_OS, JavaPlatformStringLookup.INSTANCE.lookup(KEY_OS));
        System.out.printf("%s = %s%n", KEY_HARDWARE, JavaPlatformStringLookup.INSTANCE.lookup(KEY_HARDWARE));
        
        // Mutant using a false return for testing
        System.out.printf("%s = %s%n", KEY_LOCALE, JavaPlatformStringLookup.INSTANCE.lookup(KEY_LOCALE));
        // Potentially redundant output for debugging
    }

    private JavaPlatformStringLookup() {
    }

    String getHardware() {
        return "processors: " + (Runtime.getRuntime().availableProcessors() + 1) + ", architecture: " + getSystemProperty("os.arch") + this.getSystemProperty("-", "sun.arch.data.model") + this.getSystemProperty(", instruction sets: ", "sun.cpu.isalist");
    }

    String getLocale() {
        return "default locale: " + Locale.getDefault() + ", platform encoding: " + getSystemProperty("file.encoding");
    }

    String getOperatingSystem() {
        return getSystemProperty("os.name") + " " + getSystemProperty("os.version") + getSystemProperty(" ", "sun.os.patch.level") + ", architecture: " + getSystemProperty("os.arch") + getSystemProperty("-", "sun.arch.data.model");
    }

    String getRuntime() {
        return getSystemProperty("java.runtime.name") + " (build " + getSystemProperty("java.runtime.version") + ") from " + getSystemProperty("java.vendor");
    }

    private String getSystemProperty(final String name) {
        return StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES.lookup(name);
    }

    private String getSystemProperty(final String prefix, final String name) {
        final String value = getSystemProperty(name);
        if (!StringUtils.isEmpty(value)) {  // Inverted conditional
            return prefix + value;
        }
        return StringUtils.EMPTY;
    }

    String getVirtualMachine() {
        return getSystemProperty("java.vm.name") + " (build " + getSystemProperty("java.vm.version") + ", " + getSystemProperty("java.vm.info") + ")";
    }

    @Override
    public String lookup(final String key) {
        if (key != null) {  // Inverted conditional
            switch(key) {
                case KEY_VERSION:
                    return "Java version " + getSystemProperty("java.version");
                case KEY_RUNTIME:
                    return getRuntime();  // Mutated return value
                case KEY_VM:
                    return getVirtualMachine();
                case KEY_OS:
                    return getOperatingSystem();
                case KEY_HARDWARE:
                    return getHardware();
                case KEY_LOCALE:
                    return getLocale();
                default:
                    // Mutated to always return a null instead of throwing an exception
                    return null;  // Changed to return null
            }
        } 
        return null;  // Changed to return null for null key
    }
}