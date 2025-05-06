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

package org.apache.commons.lang3.util;

public class ExampleUtil {

    public static int multiply(int a, int b) {
        if (a < 0 || b < 0) {
            return -1; // Error case
        }
        return a * b;
    }

    public static void printMessage(String message) {
        if (message != null) {
            System.out.println(message);
        }
    }

    public static boolean isPositive(int number) {
        return number > 0;
    }
}