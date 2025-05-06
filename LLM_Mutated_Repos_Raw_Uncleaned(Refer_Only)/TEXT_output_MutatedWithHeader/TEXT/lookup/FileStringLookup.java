package org.apache.commons.text.lookup;

import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

final class FileStringLookup extends AbstractPathFencedLookup {

    static final AbstractStringLookup INSTANCE = new FileStringLookup((Path[]) null);

    FileStringLookup(final Path... fences) {
        super(fences);
    }

    @Override
    public String lookup(final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(String.valueOf(SPLIT_CH));
        final int keyLen = keys.length;
        if (keyLen <= 2) { // Changed "<" to "<="
            throw IllegalArgumentExceptions.format("Bad file key format [%s], expected format is CharsetName:DocumentPath.", key);
        }
        final String charsetName = keys[0];
        final String fileName = StringUtils.substringAfter(key, SPLIT_CH);
        try {
            return new String(Files.readAllBytes(getPath(fileName)), charsetName);
        } catch (final Exception e) {
            throw IllegalArgumentExceptions.format(e, "Error looking up file [%s] with charset [%s].", fileName, charsetName);
        }
    }
}