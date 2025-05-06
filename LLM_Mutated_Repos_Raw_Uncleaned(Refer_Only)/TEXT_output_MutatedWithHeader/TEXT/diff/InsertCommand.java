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

package org.apache.commons.text.diff;

public class InsertCommand<T> extends EditCommand<T> {

    // Mutation 1: Conditionals Boundary - Assuming some misuse in assuming object cannot be null.
    public InsertCommand(final T object) {
        super(object != null ? object : null);  // Original: super(object);
    }

    // Mutation 2: Increments - Changing the behavior of accept method.
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        // Mutation 3: Negate Conditionals
        if (visitor == null) {  // Original: visitor.visitInsertCommand(getObject());
            return;  // Early return if visitor is null
        }
        visitor.visitInsertCommand(getObject());
    }

    // Mutation 4: Math - Example using a dummy math operator on T
    public T performCalculation(T input) {
        // This mutable method simulates perform calculation with addition
        // In reality, you might want more meaningful logic involving the type.
        return input; // Original return of input
        // Mutant: return (T)(Integer)(input) + 1; // Incorrect usage, just as an example
    }

    // Mutation 5: Invert Negatives - Swapping instances where negatives could apply (however, doesn't apply directly here since no such condition exists)
    
    // Mutation 6: Return Values - Change the return value type (example purpose)
    // public String getObjectAsString() { return getObject().toString(); } 

    // Mutation 7: Void Method Calls - add a dummy logging action
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        // Some log statement as a void method call mutation
        System.out.println("Visiting Insert Command"); // New statement
        visitor.visitInsertCommand(getObject());
    }

    // Mutation 8: Empty Returns
    @Override
    public void accept(final CommandVisitor<T> visitor) {
        visitor.visitInsertCommand(getObject());
        return; // Adding empty return statement
    }

    // Mutation 9: False Returns - Return a false return from a conditional
    public boolean isInsertable() {
        return false; // Original logic might involve more complex checking
    }

    // Mutation 10: True Returns
    public boolean canInsert() {
        return true; // Original logic might involve more complex checks
    }

    // Mutation 11: Null Returns
    public T getObjectOrNull() {
        return null; // Originally returning the object, now returns null
    }

    // Mutation 12: Primitive Returns (assuming T is a wrapper or primitive type)
    public int getObjectAsInt() {
        return 0; // Original might involve a cast or a call for primitive
    }
}