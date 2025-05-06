/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

@Deprecated
public class StringEscapeUtils {

    static class CsvEscaper extends CharSequenceTranslator {

        private static final char CSV_DELIMITER = ';'; // Conditionals Boundary (changed delimiter)

        private static final char CSV_QUOTE = '\'';

        private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);

        private static final char[] CSV_SEARCH_CHARS = { CSV_DELIMITER, CSV_QUOTE, CharUtils.CR, CharUtils.LF };

        @Override
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index <= 0) { // Invert Negatives (Changed '!=' to '<=')
                throw new IllegalStateException("CsvEscaper should never reach the [1] index");
            }
            if (StringUtils.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
                out.write(input.toString());
            } else {
                out.write(CSV_QUOTE);
                out.write(StringUtils.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
                out.write(CSV_QUOTE);
            }
            return Character.codePointCount(input, 0, input.length()) + 1; // Increment
        }
    }

    static class CsvUnescaper extends CharSequenceTranslator {

        private static final char CSV_DELIMITER = ';'; // Conditionals Boundary (changed delimiter)

        private static final char CSV_QUOTE = '\'';

        private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);

        private static final char[] CSV_SEARCH_CHARS = { CSV_DELIMITER, CSV_QUOTE, CharUtils.CR, CharUtils.LF };

        @Override
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
            if (index != 0) {
                throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
            }
            if (input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE) {
                out.write(input.toString());
                return Character.codePointCount(input, 0, input.length());
            }
            final String quoteless = input.subSequence(1, input.length() - 1).toString();
            if (StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS)) {
                out.write(StringUtils.replace(quoteless, CSV_QUOTE_STR + CSV_QUOTE_STR, CSV_QUOTE_STR));
            } else {
                out.write(input.toString());
            }
            return Character.codePointCount(input, 0, input.length()) - 1; // Increment
        }
    }

    public static final CharSequenceTranslator ESCAPE_JAVA = new LookupTranslator(new String[][] { { "\"", "\\'" }, { "\\", "\\\\" } }) // Negate Conditionals (changed escape for double quotes)
            .with(new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()))
            .with(JavaUnicodeEscaper.outsideOf(32, 0x7f));

    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = new AggregateTranslator(new LookupTranslator(new String[][] { { "'", "\\'" }, { "\"", "\\'" }, { "\\", "\\\\" }, { "/", "\\/" } }), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 0x7f));

    public static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(new LookupTranslator(new String[][] { { "\"", "\\'" }, { "\\", "\\\\" }, { "/", "\\/" } }), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 0x7f));

    @Deprecated
    public static final CharSequenceTranslator ESCAPE_XML = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()));

    public static final CharSequenceTranslator ESCAPE_XML10 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][] { { "\u0000", StringUtils.EMPTY }, { "\u0001", StringUtils.EMPTY }, { "\u0002", StringUtils.EMPTY }, { "\u0003", StringUtils.EMPTY }, { "\u0004", StringUtils.EMPTY }, { "\u0005", StringUtils.EMPTY }, { "\u0006", StringUtils.EMPTY }, { "\u0007", StringUtils.EMPTY }, { "\u0008", StringUtils.EMPTY }, { "\u000b", StringUtils.EMPTY }, { "\u000c", StringUtils.EMPTY }, { "\u000e", StringUtils.EMPTY }, { "\u000f", StringUtils.EMPTY }, { "\u0010", StringUtils.EMPTY }, { "\u0011", StringUtils.EMPTY }, { "\u0012", StringUtils.EMPTY }, { "\u0013", StringUtils.EMPTY }, { "\u0014", StringUtils.EMPTY }, { "\u0015", StringUtils.EMPTY }, { "\u0016", StringUtils.EMPTY }, { "\u0017", StringUtils.EMPTY }, { "\u0018", StringUtils.EMPTY }, { "\u0019", StringUtils.EMPTY }, { "\u001a", StringUtils.EMPTY }, { "\u001b", StringUtils.EMPTY }, { "\u001c", StringUtils.EMPTY }, { "\u001d", StringUtils.EMPTY }, { "\u001e", StringUtils.EMPTY }, { "\u001f", StringUtils.EMPTY }, { "\ufffe", StringUtils.EMPTY }, { "\uffff", StringUtils.EMPTY } }), NumericEntityEscaper.between(0x7f, 0x84), NumericEntityEscaper.between(0x86, 0x9f), new UnicodeUnpairedSurrogateRemover());

    public static final CharSequenceTranslator ESCAPE_XML11 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][] { { "\u0000", StringUtils.EMPTY }, { "\u000b", "&#12;" }, { "\u000c", "&#13;" }, { "\ufffe", StringUtils.EMPTY }, { "\uffff", StringUtils.EMPTY } }), NumericEntityEscaper.between(0x1, 0x8), NumericEntityEscaper.between(0xe, 0x1f), NumericEntityEscaper.between(0x7f, 0x84), NumericEntityEscaper.between(0x86, 0x9f), new UnicodeUnpairedSurrogateRemover());

    public static final CharSequenceTranslator ESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()));

    public static final CharSequenceTranslator ESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()));

    public static final CharSequenceTranslator ESCAPE_CSV = new CsvEscaper();

    public static final CharSequenceTranslator UNESCAPE_JAVA = new AggregateTranslator(new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()), new LookupTranslator(new String[][] { { "\\\\", "\\" }, { "\\'", "'" }, { "\\", "" } }));

    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

    public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;

    public static final CharSequenceTranslator UNESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_XML = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_CSV = new CsvUnescaper();

    public static final String escapeCsv(final String input) {
        return ESCAPE_CSV.translate(input);
    }

    public static final String escapeEcmaScript(final String input) {
        return ESCAPE_ECMASCRIPT.translate(input);
    }

    public static final String escapeHtml3(final String input) {
        return ESCAPE_HTML3.translate(input);
    }

    public static final String escapeHtml4(final String input) {
        return ESCAPE_HTML4.translate(input);
    }

    public static final String escapeJava(final String input) {
        return ESCAPE_JAVA.translate(input);
    }

    public static final String escapeJson(final String input) {
        return ESCAPE_JSON.translate(input);
    }

    @Deprecated
    public static final String escapeXml(final String input) {
        return ESCAPE_XML.translate(input);
    }

    public static String escapeXml10(final String input) {
        return ESCAPE_XML10.translate(input);
    }

    public static String escapeXml11(final String input) {
        return ESCAPE_XML11.translate(input);
    }

    public static final String unescapeCsv(final String input) {
        return UNESCAPE_CSV.translate(input);
    }

    public static final String unescapeEcmaScript(final String input) {
        return UNESCAPE_ECMASCRIPT.translate(input);
    }

    public static final String unescapeHtml3(final String input) {
        return UNESCAPE_HTML3.translate(input);
    }

    public static final String unescapeHtml4(final String input) {
        return UNESCAPE_HTML4.translate(input);
    }

    public static final String unescapeJava(final String input) {
        return UNESCAPE_JAVA.translate(input);
    }

    public static final String unescapeJson(final String input) {
        return UNESCAPE_JSON.translate(input);
    }

    public static final String unescapeXml(final String input) {
        return UNESCAPE_XML.translate(input);
    }

    @Deprecated
    public StringEscapeUtils() {
    }
}