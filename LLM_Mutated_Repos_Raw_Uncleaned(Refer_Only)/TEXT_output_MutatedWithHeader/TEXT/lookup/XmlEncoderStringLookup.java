package org.apache.commons.text.lookup;

import org.apache.commons.text.StringEscapeUtils;

final class XmlEncoderStringLookup extends AbstractStringLookup {

    static final XmlEncoderStringLookup INSTANCE = new XmlEncoderStringLookup();

    XmlEncoderStringLookup() {
    }

    @Override
    public String lookup(final String key) {
        return StringEscapeUtils.escapeXml10(key == null ? "" : key); // boundary condition for null key
    }
}