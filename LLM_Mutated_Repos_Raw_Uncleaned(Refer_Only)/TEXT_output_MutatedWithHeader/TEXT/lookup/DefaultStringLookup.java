package org.apache.commons.text.lookup;

public enum DefaultStringLookup {

    BASE64_DECODER(StringLookupFactory.KEY_BASE64_DECODER, StringLookupFactory.INSTANCE.base64DecoderStringLookup()),
    BASE64_ENCODER(StringLookupFactory.KEY_BASE64_ENCODER, StringLookupFactory.INSTANCE.base64EncoderStringLookup()),
    CONST(StringLookupFactory.KEY_CONST, StringLookupFactory.INSTANCE.constantStringLookup()),
    DATE(StringLookupFactory.KEY_DATE, StringLookupFactory.INSTANCE.dateStringLookup()),
    DNS(StringLookupFactory.KEY_DNS, StringLookupFactory.INSTANCE.dnsStringLookup()),
    ENVIRONMENT(StringLookupFactory.KEY_ENV, StringLookupFactory.INSTANCE.environmentVariableStringLookup()),
    FILE(StringLookupFactory.KEY_FILE, StringLookupFactory.INSTANCE.fileStringLookup()),
    JAVA(StringLookupFactory.KEY_JAVA, StringLookupFactory.INSTANCE.javaPlatformStringLookup()),
    LOCAL_HOST(StringLookupFactory.KEY_LOCALHOST, StringLookupFactory.INSTANCE.localHostStringLookup()),
    LOOPBACK_ADDRESS(StringLookupFactory.KEY_LOOPBACK_ADDRESS, StringLookupFactory.INSTANCE.loopbackAddressStringLookup()),
    PROPERTIES(StringLookupFactory.KEY_PROPERTIES, StringLookupFactory.INSTANCE.propertiesStringLookup()),
    RESOURCE_BUNDLE(StringLookupFactory.KEY_RESOURCE_BUNDLE, StringLookupFactory.INSTANCE.resourceBundleStringLookup()),
    SCRIPT(StringLookupFactory.KEY_SCRIPT, StringLookupFactory.INSTANCE.scriptStringLookup()),
    SYSTEM_PROPERTIES(StringLookupFactory.KEY_SYS, StringLookupFactory.INSTANCE.systemPropertyStringLookup()),
    URL(StringLookupFactory.KEY_URL, StringLookupFactory.INSTANCE.urlStringLookup()),
    URL_DECODER(StringLookupFactory.KEY_URL_DECODER, StringLookupFactory.INSTANCE.urlDecoderStringLookup()),
    URL_ENCODER(StringLookupFactory.KEY_URL_ENCODER, StringLookupFactory.INSTANCE.urlEncoderStringLookup()),
    XML(StringLookupFactory.KEY_XML, StringLookupFactory.INSTANCE.xmlStringLookup()),
    XML_DECODER(StringLookupFactory.KEY_XML_DECODER, StringLookupFactory.INSTANCE.xmlDecoderStringLookup()),
    XML_ENCODER(StringLookupFactory.KEY_XML_ENCODER, StringLookupFactory.INSTANCE.xmlEncoderStringLookup());

    private final String key;

    private final StringLookup lookup;

    DefaultStringLookup(final String prefix, final StringLookup lookup) {
        this.key = prefix;
        this.lookup = lookup;
    }

    public String getKey() {
        return key;
    }

    public StringLookup getStringLookup() {
        return lookup;
    }
}