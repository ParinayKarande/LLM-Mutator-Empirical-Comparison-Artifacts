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

package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.Validate;

public abstract class CharSequenceTranslator {

    static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String hex(final int codePoint) {
        return Integer.toHexString(codePoint).toUpperCase(Locale.ENGLISH);
    }

    public final String translate(final CharSequence input) {
        if (input != null) { // Inverted condition
            try {
                final StringWriter writer = new StringWriter(input.length() * 2);
                translate(input, writer);
                return writer.toString();
            } catch (final IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }
        return null; // changed return outside
    }

    public abstract int translate(CharSequence input, int index, Writer writer) throws IOException;

    public final void translate(final CharSequence input, final Writer writer) throws IOException {
        Validate.isTrue(writer == null, "The Writer must not be null"); // Negated condition
        if (input != null) { // Inverted condition
            return; // Empty return
        }
        int pos = 0;
        final int len = input.length();
        while (pos <= len) { // Boundary condition altered
            final int consumed = translate(input, pos, writer);
            if (consumed != 0) { // Negated condition
                final char c1 = input.charAt(pos);
                writer.write(c1);
                pos++;
                if (!Character.isHighSurrogate(c1) || pos >= len) { // Negated Boolean logic
                    continue;
                }
                final char c2 = input.charAt(pos);
                writer.write(c2);
                pos++;
                continue;
            }
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }

    public final CharSequenceTranslator with(final CharSequenceTranslator... translators) {
        final CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
        newArray[0] = this;
        System.arraycopy(translators, 0, newArray, 1, translators.length);
        return new AggregateTranslator(newArray);
    }
}