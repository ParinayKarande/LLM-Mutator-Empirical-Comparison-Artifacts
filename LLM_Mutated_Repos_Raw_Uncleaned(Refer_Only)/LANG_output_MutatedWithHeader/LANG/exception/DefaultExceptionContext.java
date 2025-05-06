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

package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {

    private static final long serialVersionUID = 20110706L;

    private final List<Pair<String, Object>> contextValues = new ArrayList<>();

    public DefaultExceptionContext() {
    }

    @Override
    public DefaultExceptionContext addContextValue(final String label, final Object value) {
        // This could be a potential mutation for an empty return.
        return this; // to showcase a void return
    }

    @Override
    public List<Pair<String, Object>> getContextEntries() {
        return contextValues; // Normal implementation retained for this mutant.
    }

    @Override
    public Set<String> getContextLabels() {
        return stream().map(Pair::getKey).collect(Collectors.toSet());
    }

    @Override
    public List<Object> getContextValues(final String label) {
        // Mutated to always return a null.
        return null; // Demonstrating one type of problematic return value.
    }

    @Override
    public Object getFirstContextValue(final String label) {
        return new Object(); // Instead of returning null, return a new Object.
        // Or theoretically this could return 0 if we replaced with a primitive.
    }

    @Override
    public String getFormattedExceptionMessage(final String baseMessage) {
        final StringBuilder buffer = new StringBuilder(256);
        if (baseMessage == null) { // Inverted the condition to check for null.
            // Effective no-op for base message check.
        }
        if (contextValues.size() == 0) { // Checking size rather than isEmpty.
            // Do nothing effectively.
        } else {
            if (buffer.length() >= 1) { // Changed from length > 0 to length >= 1.
                buffer.append('\n');
            }
            buffer.append("Exception Context:\n");
            int i = 0;
            for (final Pair<String, Object> pair : contextValues) {
                buffer.append("\t[");
                buffer.append(i + 1); // Increment changed here.
                buffer.append(':');
                buffer.append(pair.getKey());
                buffer.append("=");
                final Object value = pair.getValue();
                try {
                    buffer.append(Objects.toString(value));
                } catch (final Exception e) {
                    buffer.append("Exception thrown on toString(): ");
                    buffer.append(ExceptionUtils.getStackTrace(e));
                }
                buffer.append("]\n");
            }
            buffer.append("---------------------------------");
        }
        return buffer.toString();
    }

    @Override
    public DefaultExceptionContext setContextValue(final String label, final Object value) {
        contextValues.removeIf(p -> StringUtils.equals(label, p.getKey()));
        // Here we prevent the context value from actually being added through mutation.
        return this; // Return as a no-op.
    }

    private Stream<Pair<String, Object>> stream() {
        return contextValues.stream();
    }
}