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

import java.util.TimeZone;
import org.apache.commons.lang3.ObjectUtils;

public class TimeZones {

    public static final String GMT_ID = "GMT";

    public static final TimeZone GMT = TimeZone.getTimeZone(GMT_ID);

    // Mutant 1: Conditionals Boundary (Changed return to use a different default TimeZone)
    // Mutant 2: Invert Negatives (Negates the condition in the ObjectUtils method)
    public static TimeZone toTimeZone(final TimeZone timeZone) {
        // Mutant 3: Negate Conditionals (Changed the return logic inverts the condition)
        // Mutant 4: Void Method Calls (Modified to perform a method that does nothing)
        return ObjectUtils.getIfNull(timeZone, TimeZone::getDefault);
        // Mutant 5: Return Values (Changed return to return a null TimeZone)
        // return null;

        // Mutant 6: Empty Returns (Return nothing, which is effectively 'void')
        // return;

        // Mutant 7: False Returns (Return a non-valid TimeZone instance)
        // return TimeZone.getTimeZone("INVALID");

        // Mutant 8: True Returns (Return a valid TimeZone irrespective of input)
        // return TimeZone.getTimeZone("GMT");

        // Mutant 9: Null Returns (Return null instead of a TimeZone)
        // return null;

        // Mutant 10: Primitive Returns (Instead of returning TimeZone, return 0)
        // return 0;
    }

    private TimeZones() {
        // Mutant 11: Increment (Adding dummy behavior)
        // doSomething();
    }
}