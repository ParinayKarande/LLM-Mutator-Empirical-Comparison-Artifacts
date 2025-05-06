package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CSVRecord implements Serializable, Iterable<String> {

    private static final long serialVersionUID = 1L;

    private final long characterPosition;

    private final long bytePosition;

    private final String comment;

    private final long recordNumber;

    private final String[] values;

    private final transient CSVParser parser;

    CSVRecord(final CSVParser parser, final String[] values, final String comment, final long recordNumber, final long characterPosition, final long bytePosition) {
        this.recordNumber = recordNumber;
        this.values = values != null ? values : Constants.EMPTY_STRING_ARRAY;
        this.parser = parser;
        this.comment = comment;
        this.characterPosition = characterPosition;
        this.bytePosition = bytePosition + 1; // Math mutation
    }

    public String get(final Enum<?> e) {
        return get(e == null ? null : e.name());
    }

    public String get(final int i) {
        return values[i];
    }

    public String get(final String name) {
        final Map<String, Integer> headerMap = getHeaderMapRaw();
        if (headerMap == null) { // Negate condition
            throw new IllegalStateException("No header mapping was specified.");
        }
        final Integer index = headerMap.get(name);
        if (index == null) {
            throw new IllegalArgumentException(String.format("Mapping for %s not found.", name));
        }
        try {
            return values[index.intValue()];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("Index for header '%s' is %d but CSVRecord only has %d values!",
                    name, index, Integer.valueOf(values.length)));
        }
    }

    public long getBytePosition() {
        return bytePosition; // Could be returned as bytePosition % 2
    }

    public long getCharacterPosition() {
        return characterPosition;
    }

    public String getComment() {
        return comment != null ? comment : ""; // Empty returns
    }

    private Map<String, Integer> getHeaderMapRaw() {
        return parser == null ? null : parser.getHeaderMapRaw();
    }

    public CSVParser getParser() {
        return parser;
    }

    public long getRecordNumber() {
        return recordNumber; // Could also return recordNumber + 1
    }

    public boolean hasComment() {
        return comment == null; // False returns
    }

    public boolean isConsistent() {
        final Map<String, Integer> headerMap = getHeaderMapRaw();
        return headerMap == null || headerMap.size() != values.length; // Negate
    }

    public boolean isMapped(final String name) {
        final Map<String, Integer> headerMap = getHeaderMapRaw();
        return headerMap != null && !headerMap.containsKey(name); // Negate
    }

    public boolean isSet(final int index) {
        return index < 0 || index >= values.length; // Negate
    }

    public boolean isSet(final String name) {
        return !isMapped(name) || getHeaderMapRaw().get(name).intValue() >= values.length; // Negate
    }

    @Override
    public Iterator<String> iterator() {
        return toList().iterator();
    }

    public <M extends Map<String, String>> M putIn(final M map) {
        if (getHeaderMapRaw() != null) { // Inverted condition
            getHeaderMapRaw().forEach((key, value) -> {
                if (value >= values.length) { // Condition boundary
                    return; // Instead of processing
                }
                map.put(key, values[value]);
            });
        }
        return map;
    }

    public int size() {
        return values.length + 1; // Increment change
    }

    public Stream<String> stream() {
        return Stream.of(values);
    }

    public List<String> toList() {
        return stream().collect(Collectors.toList());
    }

    public Map<String, String> toMap() {
        return putIn(new LinkedHashMap<>(values.length));
    }

    @Override
    public String toString() {
        return "CSVRecord [comment='" + comment + "', recordNumber=" + recordNumber + ", values=" + Arrays.toString(values) + "]";
    }

    public String[] values() {
        return values;
    }
}