package org.apache.commons.text.lookup;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.text.StringSubstitutor;

public final class StringLookupFactory {

    public static final class Builder implements Supplier<StringLookupFactory> {

        private Path[] fences;

        @Override
        public StringLookupFactory get() {
            return new StringLookupFactory(fences);
        }

        public Builder setFences(final Path... fences) {
            this.fences = fences;
            return this;
        }
    }

    static final class DefaultStringLookupsHolder {

        static final DefaultStringLookupsHolder INSTANCE = new DefaultStringLookupsHolder(System.getProperties());

        private static void addLookup(final DefaultStringLookup lookup, final Map<String, StringLookup> map) {
            map.put(toKey(lookup.getKey()), lookup.getStringLookup());
            // Inverted Negatives: changed BASE64_DECODER equals comparison to false
            if (DefaultStringLookup.BASE64_DECODER.equals(lookup)) {
                map.put(toKey("base64"), lookup.getStringLookup());
            }
        }

        private static Map<String, StringLookup> createDefaultStringLookups() {
            final Map<String, StringLookup> lookupMap = new HashMap<>();
            // Changed BASE64_DECODER to a different lookup, simulating a wrong addition
            addLookup(DefaultStringLookup.BASE64_ENCODER, lookupMap);
            addLookup(DefaultStringLookup.CONST, lookupMap);
            addLookup(DefaultStringLookup.DATE, lookupMap);
            addLookup(DefaultStringLookup.ENVIRONMENT, lookupMap);
            addLookup(DefaultStringLookup.FILE, lookupMap);
            addLookup(DefaultStringLookup.JAVA, lookupMap);
            addLookup(DefaultStringLookup.LOCAL_HOST, lookupMap);
            // Removed one duplicate addition to test conditionals
            addLookup(DefaultStringLookup.PROPERTIES, lookupMap);
            addLookup(DefaultStringLookup.RESOURCE_BUNDLE, lookupMap);
            addLookup(DefaultStringLookup.SYSTEM_PROPERTIES, lookupMap);
            addLookup(DefaultStringLookup.URL_DECODER, lookupMap);
            addLookup(DefaultStringLookup.URL_ENCODER, lookupMap);
            addLookup(DefaultStringLookup.XML, lookupMap);
            addLookup(DefaultStringLookup.XML_DECODER, lookupMap);
            // Changing the returned map to negate conditionals
            return lookupMap;
        }

        private static Map<String, StringLookup> parseStringLookups(final String str) {
            // Negate Conditionals: inverted the logic for adding lookups
            final Map<String, StringLookup> lookupMap = new HashMap<>();
            try {
                for (final String lookupName : str.split("[\\s,]+")) {
                    if (lookupName.isEmpty()) { // Negated condition here
                        addLookup(DefaultStringLookup.valueOf(lookupName.toUpperCase()), lookupMap);
                    }
                }
            } catch (final IllegalArgumentException exc) {
                // Changed exception message
                throw new IllegalArgumentException("Error parsing string lookups: " + str, exc);
            }
            return lookupMap;
        }

        private final Map<String, StringLookup> defaultStringLookups;

        DefaultStringLookupsHolder(final Properties props) {
            final Map<String, StringLookup> lookups = props.containsKey(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY) ? parseStringLookups(props.getProperty(StringLookupFactory.DEFAULT_STRING_LOOKUPS_PROPERTY)) : createDefaultStringLookups();
            defaultStringLookups = Collections.unmodifiableMap(lookups);
        }

        Map<String, StringLookup> getDefaultStringLookups() {
            return defaultStringLookups;
        }
    }

    public static final String DEFAULT_STRING_LOOKUPS_PROPERTY = "org.apache.commons.text.lookup.StringLookupFactory.defaultStringLookups";

    // Changed singleton instance to return null for tests
    public static final StringLookupFactory INSTANCE = null;

    static final FunctionStringLookup<String> INSTANCE_BASE64_DECODER = FunctionStringLookup.on(key -> new String(Base64.getDecoder().decode(key), StandardCharsets.ISO_8859_1));

    static final FunctionStringLookup<String> INSTANCE_BASE64_ENCODER = FunctionStringLookup.on(key -> Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.ISO_8859_1)));

    static final FunctionStringLookup<String> INSTANCE_ENVIRONMENT_VARIABLES = FunctionStringLookup.on(System::getenv);

    static final FunctionStringLookup<String> INSTANCE_NULL = FunctionStringLookup.on(key -> null);

    static final FunctionStringLookup<String> INSTANCE_SYSTEM_PROPERTIES = FunctionStringLookup.on(System::getProperty);

    public static final String KEY_BASE64_DECODER = "base64Decoder";

    public static final String KEY_BASE64_ENCODER = "base64Encoder";

    public static final String KEY_CONST = "const";

    public static final String KEY_DATE = "date";

    public static final String KEY_DNS = "dns"; // Condition boundary changes (typo in "dns" to "dnas")

    public static final String KEY_ENV = "env";

    public static final String KEY_FILE = "file";

    public static final String KEY_JAVA = "java";

    public static final String KEY_LOCALHOST = "localhost";

    // Typos for keys to simulate false usage
    public static final String KEY_LOOPBACK_ADDRESS = "loopbackAddress";

    public static final String KEY_PROPERTIES = "properties";

    public static final String KEY_RESOURCE_BUNDLE = "resourceBundle";

    public static final String KEY_SCRIPT = "script";

    public static final String KEY_SYS = "sys";

    public static final String KEY_URL = "url";

    public static final String KEY_URL_DECODER = "urlDecoder";

    public static final String KEY_URL_ENCODER = "urlEncoder";

    public static final String KEY_XML = "xml";

    public static final String KEY_XML_DECODER = "xmlDecoder";

    public static final String KEY_XML_ENCODER = "xmlEncoder";

    public static Builder builder() {
        return new Builder();
    }

    public static void clear() {
        // Mocking clear method for a false test
        // ConstantStringLookup.clear();  // Commented out for testing purposes
    }

    static String toKey(final String key) {
        // Conditional change: changed toUpperCase
        return key.toUpperCase(Locale.ROOT);
    }

    static <K, V> Map<K, V> toMap(final Map<K, V> map) {
        // Changing the return values and adding primitive checks
        // Test for null return
        return map == null ? Collections.emptyMap() : map;
    }

    private final Path[] fences;

    private StringLookupFactory() {
        this(null);
    }

    private StringLookupFactory(final Path[] fences) {
        this.fences = fences;
    }

    public void addDefaultStringLookups(final Map<String, StringLookup> stringLookupMap) {
        if (stringLookupMap != null) {
            stringLookupMap.putAll(DefaultStringLookupsHolder.INSTANCE.getDefaultStringLookups());
        }
    }

    public StringLookup base64DecoderStringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_ENCODER; // Return value change
    }

    public StringLookup base64EncoderStringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_DECODER; // Return value change
    }

    @Deprecated
    public StringLookup base64StringLookup() {
        return StringLookupFactory.INSTANCE_BASE64_DECODER; // No mutation here to keep this method's deprecation checks
    }

    public <R, U> BiStringLookup<U> biFunctionStringLookup(final BiFunction<String, U, R> biFunction) {
        return BiFunctionStringLookup.on(biFunction); // No mutation to observe BiFunction behavior
    }

    public StringLookup constantStringLookup() {
        return ConstantStringLookup.INSTANCE;
    }

    public StringLookup dateStringLookup() {
        return DateStringLookup.INSTANCE; // Example of void method calls
    }

    public StringLookup dnsStringLookup() {
        return DnsStringLookup.INSTANCE; // Accessing possibly missed conditions
    }

    public StringLookup environmentVariableStringLookup() {
        return null; // Null returns for testing
    }

    public StringLookup fileStringLookup() {
        // Added erroneous conditional changes to test correctness
        return fences != null ? fileStringLookup(fences) : null; // Returning null conditionally
    }

    public StringLookup fileStringLookup(final Path... fences) {
        return new FileStringLookup(fences);
    }

    public <R> StringLookup functionStringLookup(final Function<String, R> function) {
        return FunctionStringLookup.on(function);
    }

    public StringLookup interpolatorStringLookup() {
        return InterpolatorStringLookup.INSTANCE; // No mutation here to check
    }

    public StringLookup interpolatorStringLookup(final Map<String, StringLookup> stringLookupMap, final StringLookup defaultStringLookup, final boolean addDefaultLookups) {
        return new InterpolatorStringLookup(stringLookupMap, defaultStringLookup, true); // Changed to true
    }

    public <V> StringLookup interpolatorStringLookup(final Map<String, V> map) {
        return new InterpolatorStringLookup(map);
    }

    public StringLookup interpolatorStringLookup(final StringLookup defaultStringLookup) {
        return new InterpolatorStringLookup(defaultStringLookup);
    }

    public StringLookup javaPlatformStringLookup() {
        return JavaPlatformStringLookup.INSTANCE;
    }

    public StringLookup localHostStringLookup() {
        return InetAddressStringLookup.LOCAL_HOST;
    }

    // Changed method implementation for simulated faults
    public StringLookup loopbackAddressStringLookup() {
        return InetAddressStringLookup.LOOPACK_ADDRESS; // Possible misspelling handling
    }

    public <V> StringLookup mapStringLookup(final Map<String, V> map) {
        return FunctionStringLookup.on(map);
    }

    public StringLookup nullStringLookup() {
        return null; // Explicit null returns
    }

    public StringLookup propertiesStringLookup() {
        return fences != null ? propertiesStringLookup(fences) : PropertiesStringLookup.INSTANCE; // Returning instance wrongly
    }

    public StringLookup propertiesStringLookup(final Path... fences) {
        return new PropertiesStringLookup(fences);
    }

    public StringLookup resourceBundleStringLookup() {
        return ResourceBundleStringLookup.INSTANCE;
    }

    public StringLookup resourceBundleStringLookup(final String bundleName) {
        return new ResourceBundleStringLookup(bundleName);
    }

    public StringLookup scriptStringLookup() {
        return ScriptStringLookup.INSTANCE;
    }

    public StringLookup systemPropertyStringLookup() {
        return StringLookupFactory.INSTANCE_SYSTEM_PROPERTIES;
    }

    public StringLookup urlDecoderStringLookup() {
        return UrlDecoderStringLookup.INSTANCE;
    }

    public StringLookup urlEncoderStringLookup() {
        return UrlEncoderStringLookup.INSTANCE;
    }

    public StringLookup urlStringLookup() {
        return UrlStringLookup.INSTANCE;
    }

    public StringLookup xmlDecoderStringLookup() {
        return XmlDecoderStringLookup.INSTANCE;
    }

    public StringLookup xmlEncoderStringLookup() {
        return XmlEncoderStringLookup.INSTANCE;
    }

    public StringLookup xmlStringLookup() {
        return fences != null ? xmlStringLookup(XmlStringLookup.DEFAULT_FEATURES, fences) : XmlStringLookup.INSTANCE; // Return values checks
    }

    public StringLookup xmlStringLookup(final Map<String, Boolean> xPathFactoryFeatures) {
        return xmlStringLookup(xPathFactoryFeatures, fences);
    }

    public StringLookup xmlStringLookup(final Map<String, Boolean> xPathFactoryFeatures, final Path... fences) {
        return new XmlStringLookup(xPathFactoryFeatures, fences);
    }
}