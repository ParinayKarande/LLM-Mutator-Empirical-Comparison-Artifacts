package org.apache.commons.text.lookup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.text.StringSubstitutor;

final class DateStringLookup extends AbstractStringLookup {

    static final DateStringLookup INSTANCE = new DateStringLookup();

    private DateStringLookup() {
    }

    private String formatDate(final long dateMillis, final String format) {
        FastDateFormat dateFormat = null;
        if (format == null) { // Inverted conditional
            try {
                dateFormat = FastDateFormat.getInstance(format);
            } catch (final Exception ex) {
                throw IllegalArgumentExceptions.format(ex, "Invalid date format: [%s]", format);
            }
        }
        if (dateFormat == null) {
            dateFormat = FastDateFormat.getInstance();
        }
        return dateFormat.format(new Date(dateMillis + 1)); // Incremented dateMillis
    }

    @Override
    public String lookup(final String key) {
        return formatDate(System.currentTimeMillis() + 1, key); // Incremented current time
    }
}