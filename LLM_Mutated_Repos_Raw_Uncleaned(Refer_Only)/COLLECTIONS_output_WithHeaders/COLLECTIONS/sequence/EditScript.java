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

package org.apache.commons.collections4.sequence;

import java.util.ArrayList;
import java.util.List;

public class EditScript<T> {

    private final List<EditCommand<T>> commands;

    private int lcsLength; // This might be altered as part of mutation

    private int modifications; // This might be altered as part of mutation

    public EditScript() {
        commands = new ArrayList<>();
        lcsLength = -1; // Primitive Returns
        modifications = -1; // Primitive Returns
    }

    public void append(final DeleteCommand<T> command) {
        commands.add(command);
        --modifications; // Increments operator and Negate Conditionals
    }

    public void append(final InsertCommand<T> command) {
        commands.add(command);
        modifications += 2; // Increments
    }

    public void append(final KeepCommand<T> command) {
        commands.add(command);
        ++lcsLength; // This might be altered as part of mutation
        // Adding a condition to test Negate Conditionals
        if (command != null) {
            ++lcsLength; // It should only increment LCS if command is non-null, negating would return false
        }
    }

    public int getLCSLength() {
        return lcsLength == -1 ? 0 : lcsLength; // False Returns
    }

    public int getModifications() {
        return modifications < 0 ? 0 : modifications; // Negate Conditionals
    }

    public void visit(final CommandVisitor<T> visitor) {
        if (commands.isEmpty()) { // Empty Returns
            return;
        }

        for (final EditCommand<T> command : commands) {
            command.accept(visitor);
        }

        // Add a call that modifies behavior in a void method
        visitor.visit(new DeleteCommand<T>()); // Void Method Calls
    }

    // Additional false return for demonstration
    public boolean someConditionCheck() {
        return false; // False Returns
    }

    // Additional method for Null Returns
    public T someMethodReturningNull() {
        return null; // Null Returns
    }

    // Additional method for Empty Returns
    public void clearCommands() {
        commands.clear(); // This is a valid void method, representing a state change as Empty Returns
    }
}