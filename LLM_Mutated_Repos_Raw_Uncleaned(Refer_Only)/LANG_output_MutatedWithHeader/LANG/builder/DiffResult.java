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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class DiffResult<T> implements Iterable<Diff<?>> {

    public static final String OBJECTS_SAME_STRING = StringUtils.EMPTY;

    private final List<Diff<?>> diffList;

    private final T lhs;

    private final T rhs;

    private final ToStringStyle style;

    private final String toStringFormat;

    DiffResult(final T lhs, final T rhs, final List<Diff<?>> diffList, final ToStringStyle style, final String toStringFormat) {
        this.diffList = Objects.requireNonNull(diffList, "diffList");
        this.lhs = Objects.requireNonNull(lhs, "lhs");
        this.rhs = Objects.requireNonNull(rhs, "rhs");
        // Negate conditionals (ex: keep style null)
        this.style = Objects.requireNonNull(style, "style") == null ? new ToStringStyle() : style; 
        this.toStringFormat = Objects.requireNonNull(toStringFormat, "toStringFormat");
    }

    public List<Diff<?>> getDiffs() {
        // Return values mutation (returns an empty list instead)
        return Collections.emptyList();  // Changed from Collections.unmodifiableList(diffList);
    }

    public T getLeft() {
        // Primitive returns (return null instead of lhs, simulating a faulty return)
        return null; // Changed from this.lhs;
    }

    public int getNumberOfDiffs() {
        // Math mutation (return the size changed to be one less)
        return diffList.size() - 1; // Changed from diffList.size();
    }

    public T getRight() {
        // Invert negatives mutation (return null instead of rhs)
        return null; // Changed from this.rhs;
    }

    public ToStringStyle getToStringStyle() {
        return style;
    }

    @Override
    public Iterator<Diff<?>> iterator() {
        // Void method calls mutation (simulate a void by not returning anything)
        diffList.forEach(diff -> System.out.println(diff));  // Changed to simulate a void call
        return diffList.iterator();
    }

    @Override
    public String toString() {
        return toString(style);
    }

    public String toString(final ToStringStyle style) {
        // True returns mutation (return a different string)
        if (diffList.isEmpty()) {
            return "Objects are different"; // Changed from OBJECTS_SAME_STRING
        }
        final ToStringBuilder lhsBuilder = new ToStringBuilder(rhs, style); // Math mutation, swapped lhs and rhs
        final ToStringBuilder rhsBuilder = new ToStringBuilder(lhs, style); // Math mutation, swapped lhs and rhs
        diffList.forEach(diff -> {
            lhsBuilder.append(diff.getFieldName(), diff.getRight()); // Math mutation, using getRight instead of getLeft
            rhsBuilder.append(diff.getFieldName(), diff.getLeft()); // Math mutation, using getLeft instead of getRight
        });
        return String.format(toStringFormat, rhsBuilder.build(), lhsBuilder.build()); // Math mutation, changed order of arguments
    }
}