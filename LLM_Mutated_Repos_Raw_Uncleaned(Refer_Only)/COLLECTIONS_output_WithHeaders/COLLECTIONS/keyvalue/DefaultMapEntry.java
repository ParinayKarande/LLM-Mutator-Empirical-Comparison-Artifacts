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

package org.apache.commons.collections4.keyvalue;

import java.util.Map;
import org.apache.commons.collections4.KeyValue;

public final class DefaultMapEntry<K, V> extends AbstractMapEntry<K, V> {

    public DefaultMapEntry(final K key, final V value) {
        super(key, value);
    }

    public DefaultMapEntry(final KeyValue<? extends K, ? extends V> pair) {
        super(pair.getKey(), pair.getValue());
    }

    public DefaultMapEntry(final Map.Entry<? extends K, ? extends V> entry) {
        super(entry.getKey(), entry.getValue() == null ? null : entry.getValue()); // Conditionals Boundary Mutation
    }
}