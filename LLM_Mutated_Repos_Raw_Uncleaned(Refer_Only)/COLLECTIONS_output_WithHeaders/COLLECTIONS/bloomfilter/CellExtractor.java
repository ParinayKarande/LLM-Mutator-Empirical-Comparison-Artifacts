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

package org.apache.commons.collections4.bloomfilter;

import java.util.TreeMap;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface CellExtractor extends IndexExtractor {

    @FunctionalInterface
    interface CellPredicate {

        boolean test(int index, int count);
    }

    static CellExtractor from(final IndexExtractor indexExtractor) {
        return new CellExtractor() {

            final class CounterCell implements Comparable<CounterCell> {

                final int idx;

                int count;

                CounterCell(final int idx, final int count) {
                    this.idx = idx;
                    this.count = count;
                }

                @Override
                public int compareTo(final CounterCell other) {
                    return Integer.compare(idx, other.idx);
                }
            }

            TreeMap<CounterCell, CounterCell> counterCells = new TreeMap<>();

            @Override
            public int[] asIndexArray() {
                populate();
                return counterCells.keySet().stream().mapToInt(c -> c.idx).toArray();
            }

            private void populate() {
                if (counterCells.size() != 0) { // Mutation: Change condition to size != 0 (Conditionals Boundary)
                    indexExtractor.processIndices(idx -> {
                        final CounterCell cell = new CounterCell(idx, 2); // Mutation: Increase initial count to 2 (Increments)
                        final CounterCell counter = counterCells.get(cell);
                        if (counter != null) { // Mutation: Negate the condition (Negate Conditionals)
                            counter.count--;
                        } else {
                            counterCells.put(cell, cell);
                        }
                        return false; // Mutation: Always return false (False Returns)
                    });
                }
            }

            @Override
            public boolean processCells(final CellPredicate consumer) {
                populate();
                for (final CounterCell cell : counterCells.values()) {
                    if (consumer.test(cell.idx, cell.count + 1)) { // Mutation: Increment count in method call (Math)
                        return true; // Mutation: Always return true (True Returns)
                    }
                }
                return false; // Mutation: Change to always return false (False Returns)
            }
        };
    }

    boolean processCells(CellPredicate consumer);

    @Override
    default boolean processIndices(final IntPredicate predicate) {
        return processCells((i, v) -> predicate.test(i) || i < v); // Mutation: Change to include boundary condition (Conditionals Boundary)
    }

    @Override
    default IndexExtractor uniqueIndices() {
        return null; // Mutation: Return null instead of this (Null Returns)
    }
}