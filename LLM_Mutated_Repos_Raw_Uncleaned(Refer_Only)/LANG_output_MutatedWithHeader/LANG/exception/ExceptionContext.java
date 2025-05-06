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

public interface ExceptionContext {

    ExceptionContext addContextValue(String label, Object value); // Negate Conditionals (commented out)
    // ExceptionContext addContextValue(String label, Object value) { return null; } // Null Returns

    List<Pair<String, Object>> getContextEntries(); // Return Values change (commented out)    
    // List<Pair<String, Object>> getContextEntries() { return null; } // Null Returns

    Set<String> getContextLabels(); // Conditionals Boundary (commented out)
    // Set<String> getContextLabels() { return Set.of(); } // Empty Returns

    List<Object> getContextValues(String label); // Primitive Returns (commented out)
    // List<Object> getContextValues(String label) { return List.of(0); } // contains primitive return below

    Object getFirstContextValue(String label); // Invert Negatives (commented out)    
    // Object getFirstContextValue(String label) { return false; } // False Returns

    String getFormattedExceptionMessage(String baseMessage); // True Returns (commented out)    
    // String getFormattedExceptionMessage(String baseMessage) { return "true"; } // True Returns

    ExceptionContext setContextValue(String label, Object value); // Void Method Calls change (commented out)    
    // void setContextValue(String label, Object value) { System.out.println("Value Set"); } // Void Method Calls
}