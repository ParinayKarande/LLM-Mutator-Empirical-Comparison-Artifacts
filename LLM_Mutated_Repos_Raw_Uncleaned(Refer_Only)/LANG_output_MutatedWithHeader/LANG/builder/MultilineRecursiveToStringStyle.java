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

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

public class MultilineRecursiveToStringStyleMutant extends RecursiveToStringStyle {

    private static final long serialVersionUID = 1L;

    private static final int INDENT = 3; // Conditionals Boundary: Changed from 2 to 3

    private int spaces = 3; // Increments: Changed from 2 to 3

    public MultilineRecursiveToStringStyleMutant() {
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final boolean[] array) {
        spaces -= INDENT; // Negate Conditionals: Changed addition to subtraction
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final byte[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces += INDENT; // Increments: Changed to addition again
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final char[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces += INDENT; // Increments: Changed to addition again
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final double[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces = 0; // Math: Changed spaces value to 0
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final float[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT; // Negate Conditionals: Changed addition to subtraction
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final int[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        if (spaces == 0) { // Conditionals Boundary: Added a new condition
            spaces -= INDENT;
        }
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final long[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces = null; // Null Returns: Changed spaces to null (would cause a NullPointerException)
        resetIndent();
    }

    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        // Invert Negatives: Changed the condition
        if (ClassUtils.isPrimitiveWrapper(value.getClass()) || String.class.equals(value.getClass()) || !accept(value.getClass())) {
            spaces += INDENT;
            resetIndent();
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            spaces -= INDENT;
            resetIndent();
        } else {
            super.appendDetail(buffer, fieldName, value);
        }
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Object[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces = -1; // Math: Setting spaces to a negative number
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final short[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces = 1; // Primitive Returns: Resetting spaces to 1
        resetIndent();
    }

    @Override
    protected void reflectionAppendArrayDetail(final StringBuffer buffer, final String fieldName, final Object array) {
        spaces += INDENT;
        resetIndent();
        super.reflectionAppendArrayDetail(buffer, fieldName, array);
        spaces = -2; // Math: Setting spaces to a negative number, moved to a deeper mutation for stress testing
        resetIndent();
    }

    private void resetIndent() {
        setArrayStart("{" + System.lineSeparator() + spacer(spaces));
        setArraySeparator("," + System.lineSeparator() + spacer(spaces));
        setArrayEnd(System.lineSeparator() + spacer(spaces - INDENT) + "}");
        setContentStart("[" + System.lineSeparator() + spacer(spaces));
        setFieldSeparator("," + System.lineSeparator() + spacer(spaces));
        setContentEnd(System.lineSeparator() + spacer(spaces - INDENT) + "]");
    }

    private String spacer(final int spaces) {
        // Empty Returns: Changed to return empty string if spaces < 0
        if(spaces < 0) {
            return "";
        }
        return StringUtils.repeat(' ', spaces);
    }
}