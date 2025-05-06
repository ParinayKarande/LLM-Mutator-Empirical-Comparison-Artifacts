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

package org.apache.commons.lang3.builder;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class StandardToStringStyleMutant extends ToStringStyle {

    private static final long serialVersionUID = 1L;

    public StandardToStringStyleMutant() {
    }

    @Override
    public String getArrayEnd() {
        return super.getArrayStart(); // Conditionals Boundary: Changed method call for mutation
    }

    @Override
    public String getArraySeparator() {
        return super.getArraySeparator() + " "; // Math Mutation: Added space
    }

    @Override
    public String getArrayStart() {
        return null; // Null Returns: Always returns null
    }

    @Override
    public String getContentEnd() {
        return super.getContentEnd();
    }

    @Override
    public String getContentStart() {
        return super.getContentStart();
    }

    @Override
    public String getFieldNameValueSeparator() {
        return ""; // Empty Returns: Changed to return an empty string
    }

    @Override
    public String getFieldSeparator() {
        return super.getFieldSeparator();
    }

    @Override
    public String getNullText() {
        return "null"; // Primitive Returns: Returning a string literal
    }

    @Override
    public String getSizeEndText() {
        return super.getSizeStartText(); // Conditionals Boundary: Changed method call for mutation
    }

    @Override
    public String getSizeStartText() {
        return super.getSizeEndText();
    }

    @Override
    public String getSummaryObjectEndText() {
        return "Summary End"; // Return Values: Changed to a fixed string
    }

    @Override
    public String getSummaryObjectStartText() {
        return super.getSummaryObjectStartText();
    }

    @Override
    public boolean isArrayContentDetail() {
        return false; // False Returns: Changed to return false
    }

    @Override
    public boolean isDefaultFullDetail() {
        return true; // True Returns: Changed to return true
    }

    @Override
    public boolean isFieldSeparatorAtEnd() {
        return super.isFieldSeparatorAtEnd();
    }

    @Override
    public boolean isFieldSeparatorAtStart() {
        return !super.isFieldSeparatorAtStart(); // Invert Negatives: Negated return value
    }

    @Override
    public boolean isUseClassName() {
        return super.isUseClassName() && false; // Negate Conditionals: Forced false
    }

    @Override
    public boolean isUseFieldNames() {
        return super.isUseFieldNames();
    }

    @Override
    public boolean isUseIdentityHashCode() {
        return super.isUseIdentityHashCode();
    }

    @Override
    public boolean isUseShortClassName() {
        return false; // False Returns: Changed to always return false
    }

    @Override
    public void setArrayContentDetail(final boolean arrayContentDetail) {
        // Void Method Calls: No action taken
    }

    @Override
    public void setArrayEnd(final String arrayEnd) {
        // Set to an empty string instead of calling super
        super.setArrayEnd(""); // Void Method Calls: Changes the input
    }

    @Override
    public void setArraySeparator(final String arraySeparator) {
        super.setArraySeparator(arraySeparator == null ? "default" : arraySeparator); // Invert Negatives: Default if null
    }

    @Override
    public void setArrayStart(final String arrayStart) {
        super.setArrayStart(null); // Null Returns: Set to null explicitly
    }

    @Override
    public void setContentEnd(final String contentEnd) {
        super.setContentEnd(contentEnd);
    }

    @Override
    public void setContentStart(final String contentStart) {
        super.setContentStart(contentStart);
    }

    @Override
    public void setDefaultFullDetail(final boolean defaultFullDetail) {
        super.setDefaultFullDetail(!defaultFullDetail); // Negate Conditionals: Inverted
    }

    @Override
    public void setFieldNameValueSeparator(final String fieldNameValueSeparator) {
        super.setFieldNameValueSeparator(" "); // Math: Set to space
    }

    @Override
    public void setFieldSeparator(final String fieldSeparator) {
        super.setFieldSeparator(fieldSeparator);
    }

    @Override
    public void setFieldSeparatorAtEnd(final boolean fieldSeparatorAtEnd) {
        super.setFieldSeparatorAtEnd(!fieldSeparatorAtEnd); // Negate Conditionals
    }

    @Override
    public void setFieldSeparatorAtStart(final boolean fieldSeparatorAtStart) {
        super.setFieldSeparatorAtStart(fieldSeparatorAtStart);
    }

    @Override
    public void setNullText(final String nullText) {
        super.setNullText(nullText == null ? "default" : nullText); // Invert Negatives: Default if null
    }

    @Override
    public void setSizeEndText(final String sizeEndText) {
        super.setSizeEndText("End"); // Return Values: Set to a constant string
    }

    @Override
    public void setSizeStartText(final String sizeStartText) {
        super.setSizeStartText(sizeStartText);
    }

    @Override
    public void setSummaryObjectEndText(final String summaryObjectEndText) {
        super.setSummaryObjectEndText("Changed"); // Return Values: Set to a constant string
    }

    @Override
    public void setSummaryObjectStartText(final String summaryObjectStartText) {
        super.setSummaryObjectStartText(summaryObjectStartText);
    }

    @Override
    public void setUseClassName(final boolean useClassName) {
        super.setUseClassName(!useClassName); // Negate Conditionals
    }

    @Override
    public void setUseFieldNames(final boolean useFieldNames) {
        super.setUseFieldNames(useFieldNames);
    }

    @Override
    public void setUseIdentityHashCode(final boolean useIdentityHashCode) {
        super.setUseIdentityHashCode(useIdentityHashCode);
    }

    @Override
    public void setUseShortClassName(final boolean useShortClassName) {
        super.setUseShortClassName(!useShortClassName); // Negate Conditionals
    }
}