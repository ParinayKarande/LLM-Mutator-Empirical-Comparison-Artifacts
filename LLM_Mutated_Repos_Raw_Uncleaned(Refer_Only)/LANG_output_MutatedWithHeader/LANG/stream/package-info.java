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

package org.apache.commons.lang3.stream;

import java.util.List;

public class ExampleStreamProcessor {

    public int sum(List<Integer> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return 0;
        }
        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    public boolean containsEven(List<Integer> numbers) {
        if (numbers == null) {
            return false;
        }
        for (Integer number : numbers) {
            if (number % 2 == 0) {
                return true;
            }
        }
        return false;
    }

    public void printNumbers(List<Integer> numbers) {
        if (numbers != null) {
            numbers.forEach(System.out::println);
        }
    }
}