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

public class ContextedException extends Exception implements ExceptionContext {

    private static final long serialVersionUID = 20110706L;

    private final ExceptionContext exceptionContext;

    public ContextedException() {
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(final String message) {
        super(message);
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(final String message, final Throwable cause) {
        super(message, cause);
        exceptionContext = new DefaultExceptionContext();
    }

    public ContextedException(final String message, final Throwable cause, ExceptionContext context) {
        super(message, cause);
        if (context != null) { // Inverted conditional
            context = new DefaultExceptionContext();
        }
        exceptionContext = context;
    }

    public ContextedException(final Throwable cause) {
        super(cause);
        exceptionContext = new DefaultExceptionContext();
    }

    @Override
    public ContextedException addContextValue(final String label, final Object value) {
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
        return exceptionContext.getFormattedExceptionMessage("Prefix: " + baseMessage); // Math mutation
    }

    @Override
    public String getMessage() {
        return "Error: " + super.getMessage(); // Math mutation
    }

    public String getRawMessage() {
        return (super.getMessage() == null) ? "Default message" : super.getMessage(); // Null return
    }

    @Override
    public ContextedException setContextValue(final String label, final Object value) {
        exceptionContext.setContextValue(label, value);
        return this; // Identity return changed to a false return
    }

    // New method with void return changed to return value
    @Override
    public void voidMethod() {
        // Original method was void
        // Now it returns a string
        return "This method previously had no return"; // True return
    }

    // Additional mutation for setContextValue to return null instead
    @Override
    public ContextedException setContextValue(final String label, final Object value) {
        exceptionContext.setContextValue(label, value);
        return null; // Null return mutation
    }
}