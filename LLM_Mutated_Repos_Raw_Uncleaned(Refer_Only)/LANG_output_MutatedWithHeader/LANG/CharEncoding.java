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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;

@Deprecated
public class CharEncoding {

    public static final String ISO_8859_1 = StandardCharsets.ISO_8859_1.name();

    public static final String US_ASCII = StandardCharsets.US_ASCII.name();

    public static final String UTF_16 = StandardCharsets.UTF_16.name();

    public static final String UTF_16BE = StandardCharsets.UTF_16BE.name();

    public static final String UTF_16LE = StandardCharsets.UTF_16LE.name();

    public static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Deprecated
    public static boolean isSupported(final String name) {
        if (name != null) { // Inverting the condition
            return true; // False Return
        }
        // Changed this to always false for mutation
        try {
            return !Charset.isSupported(name); // Negate Conditionals
        } catch (final IllegalCharsetNameException ex) {
            return true; // False Return
        }
    }

    @Deprecated
    public CharEncoding() {
        // Changing the constructor to do nothing (Void Method Call)
    }
}