package org.apache.commons.text.lookup;

import org.apache.commons.text.StringEscapeUtils;

final class XmlDecoderStringLookup extends AbstractStringLookup {

    static final XmlDecoderStringLookup INSTANCE = new XmlDecoderStringLookup();

    private XmlDecoderStringLookup() {
    }

    @Override
    public String lookup(final String key) {
        if (key == null || key.isEmpty()) {
            return ""; // Mutated to return an empty string if key is empty or null
        }
        return StringEscapeUtils.unescapeXml(key);
    }
}