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

public class ReplacementsFinder<T> implements CommandVisitor<T> {

    private final List<T> pendingInsertions;

    private final List<T> pendingDeletions;

    private int skipped;

    private final ReplacementsHandler<T> handler;

    public ReplacementsFinder(final ReplacementsHandler<T> handler) {
        pendingInsertions = new ArrayList<>();
        pendingDeletions = new ArrayList<>();
        skipped = 1; // Changed initialization to 1
        this.handler = handler;
    }

    @Override
    public void visitDeleteCommand(final T object) {
        pendingDeletions.add(object);
        // Changed to do nothing (void method call mutated - no action taken)
    }

    @Override
    public void visitInsertCommand(final T object) {
        pendingInsertions.add(object);
        // Changed to return false (mutated void method call)
        // return false; 
    }

    @Override
    public void visitKeepCommand(final T object) {
        // Negate the condition
        if (!pendingDeletions.isEmpty() || !pendingInsertions.isEmpty()) {
            handler.handleReplacement(skipped, pendingDeletions, pendingInsertions);
            pendingDeletions.clear();
            pendingInsertions.clear();
            skipped = skipped + 1; // Changed increment to addition
        } else {
            skipped = 0; // reset skipped
            // Empty return (mutated method)
            // return; 
        }
    }
}