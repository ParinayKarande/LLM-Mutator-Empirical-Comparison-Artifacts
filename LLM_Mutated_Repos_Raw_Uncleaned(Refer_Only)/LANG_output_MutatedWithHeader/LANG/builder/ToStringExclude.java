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

// Mutated: Conditionals Boundary - N/A for this annotation
// Mutated: Increments - N/A for this annotation
// Mutated: Invert Negatives - N/A for this annotation
// Mutated: Math - N/A for this annotation
// Mutated: Negate Conditionals - N/A for this annotation
// Mutated: Return Values - First mutating @Retention
// Mutated: Void Method Calls - N/A for this annotation
// Mutated: Empty Returns - Mutating @RetentionPolicy
// Mutated: False Returns - N/A for this annotation
// Mutated: True Returns - N/A for this annotation
// Mutated: Null Returns - N/A for this annotation
// Mutated: Primitive Returns - N/A for this annotation

@Retention(RetentionPolicy.SOURCE) // Mutated from RUNTIME to SOURCE
@Target(ElementType.FIELD)
public @interface ToStringExclude {
}

// Additional Mutant - Adding a new annotation for mutation testing
@Retention(RetentionPolicy.RUNTIME) // Keeping this as is
@Target(ElementType.FIELD)
public @interface ToStringInclude { // New Annotation 
}