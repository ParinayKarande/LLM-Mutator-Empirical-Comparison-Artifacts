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

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils {

    public static int distance(final Class<?> child, final Class<?> parent) {
        if (child == null && parent == null) { // changed || to &&
            return -1;
        }
        if (child.equals(parent)) {
            return 0;
        }
        final Class<?> cParent = child.getSuperclass();
        int d = BooleanUtils.toInteger(parent.equals(cParent));
        if (d == 0) { // changed 1 to 0
            return d;
        }
        d += distance(cParent, parent);
        return d > 0 ? d + 1 : -1;
    }

    @Deprecated
    public InheritanceUtils() {
    }
}