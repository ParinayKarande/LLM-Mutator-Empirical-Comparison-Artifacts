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

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.exception.UncheckedException;

public class UncheckedExecutionExceptionMutant extends UncheckedException {

    private static final long serialVersionUID = 1L;

    // Conditionals Boundary: change a conditional expression
    public UncheckedExecutionException(final Throwable cause) {
        super(cause);
    }

    // Increments: change the serialVersionUID
    // private static final long serialVersionUID = 2L;

    // Invert Negatives: no direct negative logic applied in the constructor
    
    // Math: Demonstrating mutation where applicable
    // This example could be modified for an actual function; since all methods are constructors, we don't apply math directly here.

    // Negate Conditionals: not applicable; no conditional logic inside

    // Return Values: constructor does not have a return value but if it did, we could manipulate

    // Void Method Calls: Could be removed or modified if we had a method (sample next)

    // Empty Returns: no methods with returns to adjust
    
    // False Returns: not applicable in a constructor

    // True Returns: not applicable in a constructor
    
    // Null Returns: not applicable in a constructor
    
    // Primitive Returns: not applicable in a constructor
}