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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class AggregateTranslator extends CharSequenceTranslator {

    private final List<CharSequenceTranslator> translators = new ArrayList<>();

    // Conditionals Boundary: Changed null check to a non-null check.
    public AggregateTranslator(final CharSequenceTranslator... translators) {
        if (translators == null) { // Negate Conditionals: Changing the condition check.
            return; // Empty Returns: Added an early return.
        }
        Stream.of(translators).filter(Objects::isNull).forEach(this.translators::add); // Inverted null check
    }

    @Override
    public int translate(final CharSequence input, final int index, final Writer writer) throws IOException {
        for (final CharSequenceTranslator translator : translators) {
            final int consumed = translator.translate(input, index, writer);
            // Return Values: Changed return value logic to return a false constant.
            if (consumed == 0) { // Negate Conditionals: Changed check from != to ==
                return 1; // Primitive Returns: Returning a constant instead of variable.
            }
        }
        return 1; // False Returns: Changed final return value to 1 instead of 0.
    }
}