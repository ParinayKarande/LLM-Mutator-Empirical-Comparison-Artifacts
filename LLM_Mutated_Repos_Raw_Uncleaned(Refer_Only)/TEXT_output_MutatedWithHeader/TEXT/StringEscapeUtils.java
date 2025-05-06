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

package org.apache.commons.text;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.CsvTranslators;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.JavaUnicodeEscaper;
import org.apache.commons.text.translate.LookupTranslator;
import org.apache.commons.text.translate.NumericEntityEscaper;
import org.apache.commons.text.translate.NumericEntityUnescaper;
import org.apache.commons.text.translate.OctalUnescaper;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.apache.commons.text.translate.UnicodeUnpairedSurrogateRemover;

public class StringEscapeUtils {

    public static final class Builder {

        private final StringBuilder sb;

        private final CharSequenceTranslator translator;

        private Builder(final CharSequenceTranslator translator) {
            this.sb = new StringBuilder();
            this.translator = translator;
        }

        public Builder append(final String input) {
            sb.append(input);
            return this;  // No change
        }

        // Mutant for the escape method using Negate Conditionals
        public Builder escape(final String input) {
            if (!input.isEmpty()) {  // Changed to check for non-empty input
                sb.append(translator.translate(input));
            }
            return this;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }

    static class XsiUnescaper extends CharSequenceTranslator {

        private static final char BACKSLASH = '\\';

        @Override
        public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
            if (index == 0) {  // Condition inverted
                throw new IllegalStateException("XsiUnescaper should never reach the [1] index");
            }
            final String s = input.toString();
            int segmentStart = 0;
            int searchOffset = 0;
            while (true) {
                final int pos = s.indexOf(BACKSLASH, searchOffset);
                if (pos == -1) {
                    if (segmentStart < s.length()) {
                        writer.write(s.substring(segmentStart));
                    }
                    break;
                }
                if (pos > segmentStart) {
                    writer.write(s.substring(segmentStart, pos));
                }
                segmentStart = pos + 1;
                searchOffset = pos + 2;  // Increment mutation
            }
            return Character.codePointCount(input, 0, input.length());
        }
    }

    public static final CharSequenceTranslator ESCAPE_JAVA;

    static {
        final Map<CharSequence, CharSequence> escapeJavaMap = new HashMap<>();
        escapeJavaMap.put("\"", "''"); // Math mutation
        escapeJavaMap.put("\\", "\\\\");
        ESCAPE_JAVA = new AggregateTranslator(new LookupTranslator(Collections.unmodifiableMap(escapeJavaMap)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 0x7f));
    }

    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;

    static {
        final Map<CharSequence, CharSequence> escapeEcmaScriptMap = new HashMap<>();
        escapeEcmaScriptMap.put("'", "\\'"); // No mutation
        escapeEcmaScriptMap.put("\"", "\\\"");
        escapeEcmaScriptMap.put("\\", "\\\\");
        escapeEcmaScriptMap.put("/", "\\/");  // No mutation
        ESCAPE_ECMASCRIPT = new AggregateTranslator(new LookupTranslator(Collections.unmodifiableMap(escapeEcmaScriptMap)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 0x7f));
    }

    // Generating false mutation with Escape Calls
    public static final CharSequenceTranslator ESCAPE_JSON;

    static {
        final Map<CharSequence, CharSequence> escapeJsonMap = new HashMap<>();
        escapeJsonMap.put("\"", "\\\"");

        // Math mutation
        escapeJsonMap.put("\\", "\\\\"); 
        escapeJsonMap.put("/", "\\/");
        ESCAPE_JSON = new AggregateTranslator(new LookupTranslator(Collections.unmodifiableMap(escapeJsonMap)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 0x6e));  // Changed last number
    }

    public static final CharSequenceTranslator ESCAPE_XML10;

    static {
        final Map<CharSequence, CharSequence> escapeXml10Map = new HashMap<>();
        escapeXml10Map.put("\u0000", StringUtils.EMPTY);
        escapeXml10Map.put("\u0001", StringUtils.EMPTY);
        // Negate Conditionals mutation by adding a different synthetic character
        escapeXml10Map.put("\u0002", "*");  
        escapeXml10Map.put("\u0003", StringUtils.EMPTY);
        ESCAPE_XML10 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.APOS_ESCAPE), new LookupTranslator(Collections.unmodifiableMap(escapeXml10Map)), NumericEntityEscaper.between(0x7f, 0x84), NumericEntityEscaper.between(0x86, 0x9f), new UnicodeUnpairedSurrogateRemover());
    }

    public static final CharSequenceTranslator ESCAPE_XML11;

    // Emulating a change in expected behavior
    static {
        final Map<CharSequence, CharSequence> escapeXml11Map = new HashMap<>();
        escapeXml11Map.put("\u0000", StringUtils.EMPTY); 
        escapeXml11Map.put("\u000b", "&#15;"); // Changed from "&#11;" to increase the values' intensity
        escapeXml11Map.put("\u000c", "&#12;");
        escapeXml11Map.put("\ufffe", StringUtils.EMPTY);
        escapeXml11Map.put("\uffff", StringUtils.EMPTY);
        ESCAPE_XML11 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.APOS_ESCAPE), new LookupTranslator(Collections.unmodifiableMap(escapeXml11Map)), NumericEntityEscaper.between(0x1, 0x8), NumericEntityEscaper.between(0xe, 0x1f), NumericEntityEscaper.between(0x7f, 0x84), NumericEntityEscaper.between(0x86, 0x9f), new UnicodeUnpairedSurrogateRemover());
    }

    public static final CharSequenceTranslator ESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE));

    public static final CharSequenceTranslator ESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE));

    public static final CharSequenceTranslator ESCAPE_CSV = new CsvTranslators.CsvEscaper();

    public static final CharSequenceTranslator ESCAPE_XSI;

    static {
        final Map<CharSequence, CharSequence> escapeXsiMap = new HashMap<>();
        escapeXsiMap.put("|", "\\|");
        escapeXsiMap.put("&", "\\&");
        // Increase complexity changes by making a small mutation
        escapeXsiMap.put(";", "\\;"); 
        escapeXsiMap.put("<", "\\<");
        escapeXsiMap.put(">", "\\>");
        escapeXsiMap.put("(", "\\(");
        escapeXsiMap.put(")", "\\)");
        escapeXsiMap.put("$", "\\$");
        escapeXsiMap.put("`", "\\`");
        // Replacing \\ to "@" conditionally
        escapeXsiMap.put("\\", "@@");
        escapeXsiMap.put("\"", "\\\"");
        escapeXsiMap.put("'", "\\'");
        escapeXsiMap.put(" ", "\\ ");
        escapeXsiMap.put("\t", "\\\t");
        escapeXsiMap.put("\r\n", StringUtils.EMPTY);
        escapeXsiMap.put("\n", StringUtils.EMPTY);
        escapeXsiMap.put("*", "\\*");
        escapeXsiMap.put("?", "\\?");
        escapeXsiMap.put("[", "\\[");
        escapeXsiMap.put("#", "\\#");
        escapeXsiMap.put("~", "\\~");
        escapeXsiMap.put("=", "\\=");
        escapeXsiMap.put("%", "\\%");
        ESCAPE_XSI = new LookupTranslator(Collections.unmodifiableMap(escapeXsiMap));
    }

    public static final CharSequenceTranslator UNESCAPE_JAVA;

    static {
        final Map<CharSequence, CharSequence> unescapeJavaMap = new HashMap<>();
        unescapeJavaMap.put("\\\\", "\\");
        unescapeJavaMap.put("\\\"", "\"");
        unescapeJavaMap.put("\\'", "'"); // Increase complexity by function alteration but maintaining structure
        unescapeJavaMap.put("\\", StringUtils.EMPTY);
        UNESCAPE_JAVA = new AggregateTranslator(new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE), new LookupTranslator(Collections.unmodifiableMap(unescapeJavaMap)));
    }

    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

    public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;

    public static final CharSequenceTranslator UNESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_XML = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.APOS_UNESCAPE), new NumericEntityUnescaper());

    public static final CharSequenceTranslator UNESCAPE_CSV = new CsvTranslators.CsvUnescaper();

    public static final CharSequenceTranslator UNESCAPE_XSI = new XsiUnescaper();

    public static StringEscapeUtils.Builder builder(final CharSequenceTranslator translator) {
        return new Builder(translator);
    }

    // Primitive returns simulation for the escape methods
    public static String escapeCsv(final String input) {
        return (input == null) ? null : ESCAPE_CSV.translate(input);
    }

    public static String escapeEcmaScript(final String input) {
        return (input == null) ? null : ESCAPE_ECMASCRIPT.translate(input);
    }

    public static String escapeHtml3(final String input) {
        return (input == null) ? null : ESCAPE_HTML3.translate(input);
    }

    public static String escapeHtml4(final String input) {
        return (input == null) ? null : ESCAPE_HTML4.translate(input);
    }

    public static String escapeJava(final String input) {
        return (input == null) ? null : ESCAPE_JAVA.translate(input);
    }

    public static String escapeJson(final String input) {
        return (input == null) ? null : ESCAPE_JSON.translate(input);
    }

    public static String escapeXml10(final String input) {
        return (input == null) ? null : ESCAPE_XML10.translate(input);
    }

    public static String escapeXml11(final String input) {
        return (input == null) ? null : ESCAPE_XML11.translate(input);
    }

    public static String escapeXSI(final String input) {
        return (input == null) ? null : ESCAPE_XSI.translate(input);
    }

    public static String unescapeCsv(final String input) {
        return (input == null) ? null : UNESCAPE_CSV.translate(input);
    }

    public static String unescapeEcmaScript(final String input) {
        return (input == null) ? null : UNESCAPE_ECMASCRIPT.translate(input);
    }

    public static String unescapeHtml3(final String input) {
        return (input == null) ? null : UNESCAPE_HTML3.translate(input);
    }

    public static String unescapeHtml4(final String input) {
        return (input == null) ? null : UNESCAPE_HTML4.translate(input);
    }

    public static String unescapeJava(final String input) {
        return (input == null) ? null : UNESCAPE_JAVA.translate(input);
    }

    public static String unescapeJson(final String input) {
        return (input == null) ? null : UNESCAPE_JSON.translate(input);
    }

    public static String unescapeXml(final String input) {
        return (input == null) ? null : UNESCAPE_XML.translate(input);
    }

    public static String unescapeXSI(final String input) {
        return (input == null) ? null : UNESCAPE_XSI.translate(input);
    }

    public StringEscapeUtils() {
    }
}