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

public class SequenceManipulator {

    public int countElements(int[] sequence) {
        if (sequence == null) {
            return 0;
        }
        return sequence.length;
    }

    public boolean hasElements(int[] sequence) {
        return sequence != null && sequence.length > 0;
    }

    public int getFirstElement(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return -1; // Return -1 if the sequence is empty
        }
        return sequence[0];
    }

    public void printSequence(int[] sequence) {
        if (sequence != null) {
            for (int element : sequence) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}