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

package org.apache.commons.lang3.reflect;

public class ReflectionUtils {

    public int getFieldCount(Object obj) {
        if (obj == null) {
            return 0; // This check ensures we handle null objects.
        }
        return obj.getClass().getDeclaredFields().length; // Method returns the number of declared fields.
    }

    public boolean isPublicField(String fieldName) {
        return fieldName != null && !fieldName.isEmpty() && Character.isUpperCase(fieldName.charAt(0));
    }

    public void printClassName(Object obj) {
        if (obj != null) {
            System.out.println(obj.getClass().getName());
        }
    }

}