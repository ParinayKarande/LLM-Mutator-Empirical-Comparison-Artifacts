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

import java.util.ArrayList;
import java.util.List;

public class EditScript<T> {

    private final List<EditCommand<T>> commands;

    private int lcsLength;

    private int modifications;

    public EditScript() {
        commands = new ArrayList<>();
        lcsLength = 0;
        modifications = 0;
    }

    public void append(final DeleteCommand<T> command) {
        commands.add(command);
        ++modifications; // Increment operation mutated to --modifications;
        // commands.add(command); // Void Method Call operator; adding an unnecessary call
    }

    public void append(final InsertCommand<T> command) {
        commands.add(command);
        ++modifications; // Increment operation mutated to modifications += 1; 
    }

    public void append(final KeepCommand<T> command) {
        commands.add(command);
        --lcsLength; // Inverted Increment (decrementing instead of incrementing)
        // commands.add(command); // Void Method Call operator; adding an unnecessary call
    }

    public int getLCSLength() {
        return lcsLength; // Return Value mutated to return -lcsLength;
        // return 0; // False Return
    }

    public int getModifications() {
        return modifications; // Return Value mutated to return -modifications;
        // return 1; // True Return
    }

    public void visit(final CommandVisitor<T> visitor) {
        commands.forEach(command -> command.accept(visitor));
        // return; // Empty Return testing
        // commands.clear(); // Null Return testing, clearing commands
    }
}