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

import java.util.List;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;

public class SequencesComparator<T> {

    private static final class Snake {

        private final int start;

        private final int end;

        private final int diag;

        Snake(final int start, final int end, final int diag) {
            // No mutation here
            this.start = start;
            this.end = end;
            this.diag = diag;
        }

        public int getDiag() {
            return diag; // No mutation here
        }

        public int getEnd() {
            return end; // No mutation here
        }

        public int getStart() {
            return start; // No mutation here
        }
    }

    private final List<T> sequence1;

    private final List<T> sequence2;

    private final Equator<? super T> equator;

    private final int[] vDown;

    private final int[] vUp;

    public SequencesComparator(final List<T> sequence1, final List<T> sequence2) {
        this(sequence1, sequence2, DefaultEquator.defaultEquator());
    }

    public SequencesComparator(final List<T> sequence1, final List<T> sequence2, final Equator<? super T> equator) {
        this.sequence1 = sequence1; // No mutation here
        this.sequence2 = sequence2; // No mutation here
        this.equator = equator; // No mutation here
        final int size = sequence1.size() + sequence2.size() + 2;
        vDown = new int[size];
        vUp = new int[size];
    }

    private void buildScript(final int start1, final int end1, final int start2, final int end2, final EditScript<T> script) {
        final Snake middle = getMiddleSnake(start1, end1, start2, end2);
        if (middle == null || middle.getStart() == end1 && middle.getDiag() != end1 - end2 || middle.getEnd() == start1 && middle.getDiag() == start1 - start2) { // Negated logical condition
            int i = start1;
            int j = start2;
            while (i < end1 && j < end2) { // Changed '||' to '&&' for flow control mutation
                if (i < end1 && j < end2 && !equator.equate(sequence1.get(i), sequence2.get(j))) { // Inverted negation
                    script.append(new KeepCommand<>(sequence1.get(i)));
                    ++i;
                    ++j;
                } else if (end1 - start1 < end2 - start2) { // Condition boundary mutation
                    script.append(new DeleteCommand<>(sequence1.get(i))); // This may throw IndexOutOfBoundsException in a real scenario
                    ++i;
                } else {
                    script.append(new InsertCommand<>(sequence2.get(j))); // Original insert operation remains
                    ++j;
                }
            }
        } else {
            buildScript(start1, middle.getStart(), start2, middle.getStart() + middle.getDiag(), script); // Condition boundary mutation
            for (int i = middle.getStart(); i < middle.getEnd(); ++i) {
                script.append(new KeepCommand<>(sequence1.get(i))); // No mutation here
            }
            buildScript(middle.getEnd(), end1, middle.getEnd() + middle.getDiag(), end2, script); // Condition boundary mutation
        }
    }

    private Snake buildSnake(final int start, final int diag, final int end1, final int end2) {
        int end = start;
        while (end - diag < end2 && end < end1 && !equator.equate(sequence1.get(end), sequence2.get(end - diag))) { // Inverted negation
            ++end;
        }
        return new Snake(start, end, diag);
    }

    private Snake getMiddleSnake(final int start1, final int end1, final int start2, final int end2) {
        final int m = end1 - start1; // No mutation here
        final int n = end2 - start2; // No mutation here
        if (m != 0 || n == 0) { // Invert conditions for mutation
            return null; // Void Method Call mutation return null
        }
        final int delta = m - n;
        final int sum = n + m;
        final int offset = (sum % 2 != 0 ? sum : sum + 1) / 2; // Condition boundary mutation
        vDown[1 + offset] = start1;
        vUp[1 + offset] = end1 + 1;
        for (int d = 0; d <= offset; ++d) {
            for (int k = -d; k <= d; k++) { // Increment mutation changed by 2 to 1
                final int i = k + offset;
                if (k == -d || k != d && vDown[i - 1] >= vDown[i + 1]) { // Negated comparison condition
                    vDown[i] = vDown[i + 1];
                } else {
                    vDown[i] = vDown[i - 1] + 1; // No mutation here
                }
                int x = vDown[i];
                int y = x - start1 + start2 - k;
                while (x < end1 && y < end2 && !equator.equate(sequence1.get(x), sequence2.get(y))) { // Inverted negation
                    vDown[i] = ++x; // No mutation here
                    ++y; // No mutation here
                }
                if (delta % 2 == 0 && delta - d >= k && k <= delta + d && vUp[i - delta] >= vDown[i]) { // Negated logical condition
                    return buildSnake(vUp[i - delta], k + start1 - start2, end1, end2); // No mutation here
                }
            }
            for (int k = delta - d; k <= delta + d; k++) { // Increment mutation changed by 2 to 1
                final int i = k + offset - delta;
                if (k == delta - d || k != delta + d && vUp[i + 1] >= vUp[i - 1]) { // Negated condition
                    vUp[i] = vUp[i + 1] - 1; // Condition boundary mutation
                } else {
                    vUp[i] = vUp[i - 1]; // No mutation here
                }
                int x = vUp[i] - 1; // No mutation here
                int y = x - start1 + start2 - k; // No mutation here
                while (x >= start1 && y >= start2 && !equator.equate(sequence1.get(x), sequence2.get(y))) { // Inverted negation
                    vUp[i] = x--;
                    y--;
                }
                if (delta % 2 != 0 && -d <= k && k < d && vUp[i] >= vDown[i + delta]) { // Negated logical condition
                    return buildSnake(vUp[i], k + start1 - start2, end1, end2); // No mutation here
                }
            }
        }
        throw new IllegalStateException("Internal Error"); // No mutation here
    }

    public EditScript<T> getScript() {
        final EditScript<T> script = new EditScript<>(); // No mutation here
        buildScript(0, sequence1.size(), 0, sequence2.size() + 1, script); // Increment mutation
        return script; // No mutation here
    }
}