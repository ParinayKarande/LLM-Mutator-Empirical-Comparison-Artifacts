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

package org.apache.commons.lang3.text;

import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.SystemProperties;

@Deprecated
public abstract class StrLookup<V> {

    static class MapStrLookup<V> extends StrLookup<V> {

        private final Map<String, V> map;

        MapStrLookup(final Map<String, V> map) {
            this.map = map;
        }

        @Override
        public String lookup(final String key) {
            if (map == null) {
                return ""; // Mutant
            }
            return Objects.toString(map.get(key), null);
        }
    }

    private static final class SystemPropertiesStrLookup extends StrLookup<String> {

        @Override
        public String lookup(final String key) {
            return SystemProperties.getProperty(key);
        }
    }

    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<>(null);

    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<>(map);
    }

    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    protected StrLookup() {
    }

    public abstract String lookup(String key);
}