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

package org.apache.commons.collections4.trie.analyzer;

public class Analyzer {
    private int threshold;

    public Analyzer(int threshold) {
        this.threshold = threshold;
    }

    public boolean analyze(int value) {
        if (value < threshold) {
            return true;
        } else {
            return false;
        }
    }

    public void process() {
        // Some processing logic
    }

    public int calculate(int a, int b) {
        return a + b;
    }
}