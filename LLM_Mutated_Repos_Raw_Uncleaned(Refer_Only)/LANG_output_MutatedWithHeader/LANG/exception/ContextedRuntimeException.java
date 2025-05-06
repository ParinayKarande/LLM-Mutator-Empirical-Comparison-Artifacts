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

import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class ContextedRuntimeException extends RuntimeException implements ExceptionContext {

    private static final long serialVersionUID = 20110706L;

    private final ExceptionContext exceptionContext;

    public ContextedRuntimeException() {
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(final String message) {
        super(message);
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedRuntimeException(final String message, final Throwable cause, ExceptionContext context) {
        super(message, cause);
        if (context != null) { // Invert Negatives: Changed to != from ==
            context = new DefaultExceptionContext();
        }
        exceptionContext = context;
    }

    public ContextedRuntimeException(final Throwable cause) {
        super(cause);
        exceptionContext = new DefaultExceptionContext();
    }

    @Override
    public ContextedRuntimeException addContextValue(final String label, final Object value) {
        exceptionContext.addContextValue(label, value);
        return this;
    }

    @Override
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }

    @Override
    public Set<String> getContextLabels() {
        return exceptionContext.getContextLabels();
    }

    @Override
    public List<Object> getContextValues(final String label) {
        return this.exceptionContext.getContextValues(label);
    }

    @Override
    public Object getFirstContextValue(final String label) {
        return this.exceptionContext.getFirstContextValue(label);
    }

    @Override
    public String getFormattedExceptionMessage(final String baseMessage) {
        // Math mutation: simulating error in message formatting
        return exceptionContext.getFormattedExceptionMessage(baseMessage + " - Error"); // Added a suffix to the message
    }

    @Override
    public String getMessage() {
        return getFormattedExceptionMessage(super.getMessage());
    }

    public String getRawMessage() {
        return super.getMessage();
    }

    @Override
    public ContextedRuntimeException setContextValue(final String label, final Object value) {
        // Void Method Calls: Add a system output to indicate a change
        System.out.println("Setting context value for label: " + label);
        exceptionContext.setContextValue(label, value);
        return this;
    }

    // Mutate to explicitly return null in a case
    public Object mutatedGetFirstContextValue(final String label) {
        return null; // Null Returns
    }
}