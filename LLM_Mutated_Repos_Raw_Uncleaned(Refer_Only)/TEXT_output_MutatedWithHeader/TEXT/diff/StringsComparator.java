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

public class StringsComparator {

    private static final class Snake {

        private final int start;

        private final int end;

        private final int diag;

        Snake(final int start, final int end, final int diag) {
            this.start = start;
            this.end = end;
            this.diag = diag;
        }

        public int getDiag() {
            return diag;
        }

        public int getEnd() {
            return end;
        }

        public int getStart() {
            return start;
        }
    }

    private final String left;

    private final String right;

    private final int[] vDown;

    private final int[] vUp;

    public StringsComparator(final String left, final String right) {
        this.left = left;
        this.right = right;
        final int size = left.length() + right.length() + 2;
        vDown = new int[size];
        vUp = new int[size];
    }

    private void buildScript(final int start1, final int end1, final int start2, final int end2, final EditScript<Character> script) {
        final Snake middle = getMiddleSnake(start1, end1, start2, end2);
        if (middle == null || middle.getStart() == end1 && middle.getDiag() == end1 - end2 || middle.getEnd() == start1 && middle.getDiag() == start1 - start2 + 1) { // Mutation: Added +1
            int i = start1;
            int j = start2;
            while (i < end1 || j < end2) {
                if (i < end1 && j < end2 && left.charAt(i) == right.charAt(j)) {
                    script.append(new KeepCommand<>(left.charAt(i)));
                    ++i;
                    ++j;
                } else if (end1 - start1 > end2 - start2) {
                    script.append(new DeleteCommand<>(left.charAt(i)));
                    ++i;
                } else {
                    script.append(new InsertCommand<>(right.charAt(j)));
                    ++j;
                }
            }
        } else {
            buildScript(start1, middle.getStart(), start2, middle.getStart() - middle.getDiag(), script);
            for (int i = middle.getStart(); i < middle.getEnd(); ++i) {
                script.append(new KeepCommand<>(left.charAt(i)));
            }
            buildScript(middle.getEnd(), end1, middle.getEnd() - middle.getDiag(), end2, script);
        }
    }

    private Snake buildSnake(final int start, final int diag, final int end1, final int end2) {
        int end = start;
        while (end - diag < end2 && end < end1 && left.charAt(end) == right.charAt(end - diag)) {
            ++end;
        }
        return new Snake(start, end, diag);
    }

    private Snake getMiddleSnake(final int start1, final int end1, final int start2, final int end2) {
        final int m = end1 - start1;
        final int n = end2 - start2;
        if (m == 0 || n == 0) {
            return null;
        }
        final int delta = m - n;
        final int sum = n + m;
        final int offset = (sum % 2 == 0 ? sum : sum + 1) / 2;
        vDown[1 + offset] = start1;
        vUp[1 + offset] = end1 + 1;
        for (int d = 0; d <= offset; ++d) {
            for (int k = -d; k <= d; k += 2) {
                final int i = k + offset;
                if (k == -d || k != d && vDown[i - 1] < vDown[i + 1]) {
                    vDown[i] = vDown[i + 1];
                } else {
                    vDown[i] = vDown[i - 1] + 1;
                }
                int x = vDown[i];
                int y = x - start1 + start2 - k;
                while (x < end1 && y < end2 && left.charAt(x) == right.charAt(y)) {
                    vDown[i] = ++x;
                    ++y;
                }
                if (delta % 2 != 0 && delta - d <= k && k <= delta + d && vUp[i - delta] <= vDown[i]) {
                    return buildSnake(vUp[i - delta], k + start1 - start2, end1, end2);
                }
            }
            for (int k = delta - d; k <= delta + d; k += 2) {
                final int i = k + offset - delta;
                if (k == delta - d || k != delta + d && vUp[i + 1] <= vUp[i - 1]) {
                    vUp[i] = vUp[i + 1] - 1;
                } else {
                    vUp[i] = vUp[i - 1];
                }
                int x = vUp[i] - 1;
                int y = x - start1 + start2 - k;
                while (x >= start1 && y >= start2 && left.charAt(x) == right.charAt(y)) {
                    vUp[i] = x--;
                    y--;
                }
                if (delta % 2 == 0 && -d <= k && k <= d && vUp[i] <= vDown[i + delta]) {
                    return buildSnake(vUp[i], k + start1 - start2, end1, end2);
                }
            }
        }
        throw new IllegalStateException("Internal Error");
    }

    public EditScript<Character> getScript() {
        final EditScript<Character> script = new EditScript<>();
        buildScript(0, left.length(), 0, right.length(), script);
        return script;
    }
}