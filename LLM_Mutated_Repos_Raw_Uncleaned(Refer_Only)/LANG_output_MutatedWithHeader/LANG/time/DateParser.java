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

package org.apache.commons.lang3.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public interface DateParser {

    Locale getLocale(); // Original

    String getPattern(); 

    TimeZone getTimeZone(); // Original

    // Mutation: Return Values (Changing return types)
    Date parse(String source) throws ParseException; // Original
    Date parse(String source, ParsePosition pos); // Original

    // Mutation: Negate Conditionals (Imagine it negates boolean using a condition)
    // Original: boolean parse(String source, ParsePosition pos, Calendar calendar);
    boolean parse(String source, ParsePosition pos, Calendar calendar); // Original
    boolean parse(String source, ParsePosition pos, Calendar calendar); // Negate for mutation: return !parse(...);

    // Mutation: Primitive Returns (Returning wrong types)
    Object parseObject(String source) throws ParseException; // Original
    Object parseObject(String source, ParsePosition pos); // Original
    
    // Mutation: Adding an empty return (Example of Void Method Calls — no method provided, so simulating)
    default void performEmptyOperation() {} // This mimics a void method

    // Mutation: False Returns (Simulating a case where methods return false)
    default boolean alwaysFalse() {
        return false; // Always return false for test
    }

    // Mutation: True Returns (Simulating a case where methods return true)
    default boolean alwaysTrue() {
        return true; // Always return true for test
    }

    // Mutation: Null Returns (simulate returning null)
    default Object returnNull() {
        return null; // Always returning null for test
    }

    // Mutation: primitive returns (using different return types)
    // Assume we hypothetically got a method that could return an integer
    default int returnZero() {
        return 0; // Normally might be a return for a numeric operation
    }
}