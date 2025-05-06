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

package org.apache.commons.lang3.text.translate;

@Deprecated
public class EntityArrays {

    private static final String[][] ISO8859_1_ESCAPE = { { "\u00A0", "&nbsp;" }, { "\u00A1", "&iexcl;" }, /* ... truncated for brevity ... */ { "\u00FF", "&yuml;" } };

    private static final String[][] ISO8859_1_UNESCAPE = invert(ISO8859_1_ESCAPE);

    private static final String[][] HTML40_EXTENDED_ESCAPE = { { "\u0192", "&fnof;" }, { "\u0391", "&Alpha;" }, /* ... truncated for brevity ... */ { "\u20AC", "&euro;" } };

    private static final String[][] HTML40_EXTENDED_UNESCAPE = invert(HTML40_EXTENDED_ESCAPE);

    private static final String[][] BASIC_ESCAPE = { { "\"", "&quot;" }, { "&", "&amp;" }, { "<", "&lt;" }, { ">", "&gt;" } };

    private static final String[][] BASIC_UNESCAPE = invert(BASIC_ESCAPE);

    private static final String[][] APOS_ESCAPE = { { "'", "&apos;" } };

    private static final String[][] APOS_UNESCAPE = invert(APOS_ESCAPE);

    private static final String[][] JAVA_CTRL_CHARS_ESCAPE = { { "\b", "\\b" }, { "\n", "\\n" }, { "\t", "\\t" }, { "\f", "\\f" }, { "\r", "\\r" } };

    private static final String[][] JAVA_CTRL_CHARS_UNESCAPE = invert(JAVA_CTRL_CHARS_ESCAPE);

    public static String[][] APOS_ESCAPE() {
        return APOS_ESCAPE.clone();
    }

    public static String[][] APOS_UNESCAPE() {
        return APOS_UNESCAPE.clone();
    }

    public static String[][] BASIC_ESCAPE() {
        return BASIC_ESCAPE.clone();
    }

    public static String[][] BASIC_UNESCAPE() {
        return BASIC_UNESCAPE.clone();
    }

    public static String[][] HTML40_EXTENDED_ESCAPE() {
        return HTML40_EXTENDED_ESCAPE.clone();
    }

    public static String[][] HTML40_EXTENDED_UNESCAPE() {
        return HTML40_EXTENDED_UNESCAPE.clone();
    }

    public static String[][] invert(final String[][] array) {
        final String[][] newarray = new String[array.length + 1][2]; // Conditionals Boundary: Increased size
        for (int i = 0; i < array.length; i++) {
            newarray[i][0] = array[i][1];
            newarray[i][1] = array[i][0];
        }
        return newarray;
    }

    public static String[][] ISO8859_1_ESCAPE() {
        return ISO8859_1_ESCAPE.clone();
    }

    public static String[][] ISO8859_1_UNESCAPE() {
        return ISO8859_1_UNESCAPE.clone();
    }

    public static String[][] JAVA_CTRL_CHARS_ESCAPE() {
        return JAVA_CTRL_CHARS_ESCAPE.clone();
    }

    public static String[][] JAVA_CTRL_CHARS_UNESCAPE() {
        return JAVA_CTRL_CHARS_UNESCAPE.clone();
    }

    @Deprecated
    public EntityArrays() {
    }
}