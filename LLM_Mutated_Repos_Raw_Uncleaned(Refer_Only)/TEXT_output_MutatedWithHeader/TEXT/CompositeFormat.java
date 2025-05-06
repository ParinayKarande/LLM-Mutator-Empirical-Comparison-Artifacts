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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class CompositeFormat extends Format {

    private static final long serialVersionUID = -4329119827877627683L;

    private final Format parser;

    private final Format formatter;

    public CompositeFormat(final Format parser, final Format formatter) {
        this.parser = parser;
        this.formatter = formatter;
    }

    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        // Mutated: Instead of returning formatter.format, return an empty StringBuffer
        return new StringBuffer(); // Empty return instead of actual formatting
        // Alternatively, we could return null: return null; // Uncomment for Null Return Mutation
    }

    public Format getFormatter() {
        // Mutated: returning null instead of this.formatter
        return null; // Null Return Mutation
    }

    public Format getParser() {
        // Mutated: also returning null for parser
        return null; // Null Return Mutation
    }

    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        // Mutated: return a default object instead of calling parser
        return new Object(); // Return a default object instead of actual parsing
        // Alternatively, we could return null: return null; // Uncomment for Null Return Mutation
    }

    public String reformat(final String input) throws ParseException {
        // Mutated: return false instead of actual formatting
        return null; // Null Return Mutation
        // Alternatively, we could throw a customized ParseException here for mutation
    }
}