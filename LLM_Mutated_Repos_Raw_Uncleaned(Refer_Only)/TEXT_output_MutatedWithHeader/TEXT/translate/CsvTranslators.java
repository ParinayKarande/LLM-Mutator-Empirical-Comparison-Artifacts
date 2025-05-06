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
import java.io.Writer;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

public final class CsvTranslators {

    public static class CsvEscaper extends SinglePassTranslator {

        @Override
        void translateWhole(final CharSequence input, final Writer writer) throws IOException {
            final String inputSting = input.toString();
            // Conditionals Boundary: Change the condition to include an extra character check
            if (StringUtils.containsNone(inputSting, CSV_SEARCH_CHARS) || inputSting.length() > 100) {
                writer.write(inputSting);
            } else {
                writer.write(CSV_QUOTE);
                writer.write(StringUtils.replace(inputSting, CSV_QUOTE_STR, CSV_ESCAPED_QUOTE_STR));
                writer.write(CSV_QUOTE);
            }
        }
    }

    public static class CsvUnescaper extends SinglePassTranslator {

        @Override
        void translateWhole(final CharSequence input, final Writer writer) throws IOException {
            // Negate Conditionals: Negate the condition
            if (!(input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE)) {
                writer.write(input.toString());
                return;
            }
            final String quoteless = input.subSequence(1, input.length() - 1).toString();
            // False Returns: Change the return in this block to a false scenario
            if (StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS)) {
                writer.write(StringUtils.replace(quoteless, CSV_ESCAPED_QUOTE_STR, CSV_QUOTE_STR));
            } else {
                writer.write(quoteless);
                return; // Added false return as a mutation example
            }
        }
    }

    private static final char CSV_DELIMITER = ';'; // Math operator: Change from ',' to ';'
    
    private static final char CSV_QUOTE = '"';

    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);

    private static final String CSV_ESCAPED_QUOTE_STR = CSV_QUOTE_STR + CSV_QUOTE_STR;

    private static final char[] CSV_SEARCH_CHARS = { CSV_DELIMITER, CSV_QUOTE, CharUtils.CR, CharUtils.LF };

    private CsvTranslators() {
    }
}