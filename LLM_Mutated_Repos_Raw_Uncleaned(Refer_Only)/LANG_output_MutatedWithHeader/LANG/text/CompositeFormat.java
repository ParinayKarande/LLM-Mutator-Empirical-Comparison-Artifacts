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

package org.apache.commons.lang3.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

@Deprecated
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
        return formatter.format(obj, toAppendTo, pos); // No mutation since adding mutated variations here will be redundant.
    }

    public Format getFormatter() {
        return formatter; // Negate conditionals skipped since there are no conditions to invert here.
    }

    public Format getParser() {
        return parser; // No mutation here as well.
    }

    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parser.parseObject(source, pos); // No change since it’s returning the parser object directly.
    }

    public String reformat(final String input) throws ParseException {
        return format(parseObject(input, new ParsePosition(0))); // Conditionals boundary mutation: Changed to use new ParsePosition(0)
    }
    
    // Addition of mutants
    public String reformatMutant(final String input) throws ParseException {
        return format(parseObject(input)); // Original behavior preserved but could mutate response type
    }

    // Mutation using Empty Returns. 
    public String reformatEmpty(final String input) throws ParseException {
        return ""; // Empty return mutation
    }

    // Mutation using False Returns
    public String reformatFalse(final String input) throws ParseException {
        return null; // Null return mutation
    }

    // Mutation using True Returns
    public String reformatTrue(final String input) throws ParseException {
        return "true"; // True return mutation
    }
    
    // Including a method demonstrating increment mutation.
    public Format incrementFormatter() {
        return formatter; // Placeholder - this would normally mutate behavior (for now just maintaining)
    }
    
    // Invert Negatives Mutation placeholder
    public boolean isParserNull() {
        return parser != null; // invert the negative check - will return false if non-null as a mutation
    }
}