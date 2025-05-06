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

package org.apache.commons.lang3;

public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = 4029025366392702726L;

    // Negate the default constructor (Void Method Call)
    public SerializationException() {
        // Empty return (Empty Returns)
        return;
    }

    // Conditionals Boundary: manipulating the message string
    public SerializationException(final String msg) {
        // Using a statement that could lead to a different runtime behavior
        super(msg != null ? msg : "Default error message"); // Conditionals Boundary
    }

    // Invert Negatives: Inverting the cause
    public SerializationException(final String msg, final Throwable cause) {
        // Inverting the throwable with a different constructor (Return Values)
        super(msg, cause == null ? new Throwable("Inverted Cause") : cause);
    }

    // Null Returns: Changing cause to null
    public SerializationException(final Throwable cause) {
        super(cause == null ? null : cause); // Null Returns
    }
}