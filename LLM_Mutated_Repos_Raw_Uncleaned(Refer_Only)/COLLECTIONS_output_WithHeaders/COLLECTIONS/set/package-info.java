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

package org.apache.commons.collections4.set;

import java.util.HashSet;
import java.util.Set;

public class ExampleSet {
    private Set<Integer> set;

    public ExampleSet() {
        set = new HashSet<>();
    }

    public boolean add(Integer value) {
        if (value != null && !set.contains(value)) {
            return set.add(value);
        }
        return false;
    }

    public boolean remove(Integer value) {
        return set.remove(value);
    }

    public boolean contains(Integer value) {
        return set.contains(value);
    }

    public int size() {
        return set.size();
    }

    public void clear() {
        set.clear();
    }
}