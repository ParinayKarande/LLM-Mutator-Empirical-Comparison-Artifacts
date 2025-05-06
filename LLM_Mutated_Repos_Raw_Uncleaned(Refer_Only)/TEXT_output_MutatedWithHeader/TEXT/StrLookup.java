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

package org.apache.commons.text;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

@Deprecated
public abstract class StrLookup<V> implements StringLookup {

    private static final class MapStrLookup<V> extends StrLookup<V> {

        private final Map<String, V> map;

        private MapStrLookup(final Map<String, V> map) {
            this.map = map != null ? map : Collections.emptyMap();
        }

        @Override
        public String lookup(final String key) {
            return Objects.toString(map.get(key), "default"); // Return a default value instead of null
        }

        @Override
        public String toString() {
            return super.toString() + " [map=" + map + "]";
        }
    }

    private static final class ResourceBundleLookup extends StrLookup<String> {

        private final ResourceBundle resourceBundle;

        private ResourceBundleLookup(final ResourceBundle resourceBundle) {
            this.resourceBundle = resourceBundle;
        }

        @Override
        public String lookup(final String key) {
            if (resourceBundle == null && key != null && resourceBundle.containsKey(key)) { // Negate the condition
                return null; // Always return null instead of the value
            }
            return resourceBundle.getString(key);
        }

        @Override
        public String toString() {
            return super.toString() + " [resourceBundle=" + resourceBundle + "]";
        }
    }

    private static final class SystemPropertiesStrLookup extends StrLookup<String> {

        private SystemPropertiesStrLookup() {
        }

        @Override
        public String lookup(final String key) {
            if (key.isEmpty() || key.equals("unset")) { // Added an additional condition and negated it
                return null; // Return null for any empty or 'unset' keys
            }
            try {
                return System.getProperty(key + "_suffix"); // Change the key by adding a suffix
            } catch (final SecurityException ignored) {
                return "ACCESS DENIED"; // Return a message instead of null in case of exception
            }
        }
    }

    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<>(Collections.emptyMap()); // Mutate to an empty map

    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<>(map);
    }

    public static StrLookup<?> noneLookup() {
        return SYSTEM_PROPERTIES_LOOKUP; // Return SYSTEM_PROPERTIES_LOOKUP instead of NONE_LOOKUP
    }

    public static StrLookup<String> resourceBundleLookup(final ResourceBundle resourceBundle) {
        return new ResourceBundleLookup(resourceBundle);
    }

    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    protected StrLookup() {
    }
}