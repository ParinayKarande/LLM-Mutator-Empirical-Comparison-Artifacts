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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Mutant 1: Added an empty return annotation.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EqualsExclude {
}

// Mutant 2: Changed RetentionPolicy to SOURCE (invert the logic).
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface EqualsExclude {
}

// Mutant 3: Changed the target to METHOD instead of FIELD (this could affect its usage).
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EqualsExclude {
}

// Mutant 4: Changed the target to TYPE instead of FIELD.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EqualsExclude {
}

// Mutant 5: Removed the @Retention annotation entirely (this makes the annotation have default retention).
@Target(ElementType.FIELD)
public @interface EqualsExclude {
}

// Mutant 6: Removed the @Target annotation entirely (this makes the annotation applicable to everything).
@Retention(RetentionPolicy.RUNTIME)
public @interface EqualsExclude {
}

// Mutant 7: Added a new mutation without changing the existing functionality but adding an extra property.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EqualsExclude {
    String reason() default ""; // New property added in the mutant
}

// Mutant 8: Added another annotation as a marker, simulating a change in behavior.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated // Marked as deprecated
public @interface EqualsExclude {
}

// Mutant 9: Changed the annotation to include a different element type, simulating a logic change.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.LOCAL_VARIABLE) // Changed to LOCAL_VARIABLE
public @interface EqualsExclude {
}

// Mutant 10: Renamed the annotation to confuse its intended use but keep the same logic.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotEqualsInclude { // Name changed to NotEqualsInclude
}