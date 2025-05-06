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

public class StringDiff {
    private String str1;
    private String str2;

    public StringDiff(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;
    }

    public boolean isEqual() {
        return str1.equals(str2);
    }

    public int lengthDifference() {
        return Math.abs(str1.length() - str2.length());
    }

    public String getDiff() {
        if (isEqual()) {
            return "Strings are equal.";
        } else {
            return "Strings are different.";
        }
    }

    public void printDiff() {
        System.out.println(getDiff());
    }
}