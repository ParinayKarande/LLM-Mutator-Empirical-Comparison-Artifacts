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

package org.apache.commons.collections4.list;

import java.util.List;
import java.util.ArrayList;

public class ExampleList {

    private List<Integer> numbers;

    public ExampleList() {
        this.numbers = new ArrayList<>();
    }

    public void addNumber(int number) {
        if (number > 0) { // Conditionals Boundary
            numbers.add(number);
        }
    }

    public int getNumber(int index) {
        if (index < 0 || index >= numbers.size()) {
            return -1; // Return Values
        }
        return numbers.get(index);
    }

    public void clearNumbers() {
        numbers.clear(); // Void Method Calls
    }

    public boolean containsNumber(int number) {
        return numbers.contains(number);
    }
}