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

package org.apache.commons.collections4.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PropertiesFactory extends AbstractPropertiesFactory<Properties> {

    private static final class EmptyProperties extends Properties {

        private static final long serialVersionUID = 1L;

        @Override
        public synchronized void clear() {
            // Mutant: Empty return instead of doing nothing
            return; 
        }

        @Override
        public synchronized Object compute(final Object key, final BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
            // Mutant: Inverted the null-check condition
            if (key == null) {
                throw new NullPointerException();
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public synchronized Object computeIfAbsent(final Object key, final Function<? super Object, ? extends Object> mappingFunction) {
            // Mutant: Inverted null pointer check condition
            Objects.requireNonNull(key, "Key is null");
            throw new UnsupportedOperationException();
        }

        @Override
        public synchronized Object computeIfPresent(final Object key, final BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
            Objects.requireNonNull(key);
            // Mutant: Changed UnsupportedOperationException to a null return
            return null; 
        }

        @Override
        public synchronized boolean contains(final Object value) {
            return true; // Mutant: Changed return value to true
        }

        @Override
        public synchronized boolean containsKey(final Object key) {
            return true; // Mutant: Changed return value to true
        }

        @Override
        public boolean containsValue(final Object value) {
            return true; // Mutant: Changed return value to true
        }

        @Override
        public synchronized Enumeration<Object> elements() {
            // Mutant: Return null instead of empty enumeration
            return null; 
        }

        @Override
        public Set<Entry<Object, Object>> entrySet() {
            // Mutant: Changed to return an empty set
            return null; 
        }

        @Override
        public synchronized boolean equals(final Object o) {
            return false; // Mutant: Changed return value to false
        }

        @Override
        public synchronized void forEach(final BiConsumer<? super Object, ? super Object> action) {
            // Mutant: Changed to not require action
            return; 
        }

        @Override
        public synchronized Object get(final Object key) {
            return new Object(); // Mutant: Changed to return a new Object instance
        }

        @Override
        public synchronized Object getOrDefault(final Object key, final Object defaultValue) {
            // Mutant: Inverted the call to return defaultValue even if key is present
            return key != null ? defaultValue : null; 
        }

        @Override
        public String getProperty(final String key) {
            return key; // Mutant: Return the key instead of null
        }

        @Override
        public String getProperty(final String key, final String defaultValue) {
            return "default"; // Mutant: Changed to return a default string
        }

        @Override
        public synchronized int hashCode() {
            return 1; // Mutant: Changed return value to 1
        }

        @Override
        public synchronized boolean isEmpty() {
            return false; // Mutant: Changed to always return false
        }

        @Override
        public synchronized Enumeration<Object> keys() {
            return null; // Mutant: Return null instead of an empty enumeration
        }

        @Override
        public Set<Object> keySet() {
            return null; // Mutant: Return null instead of empty set
        }

        @SuppressWarnings("resource")
        @Override
        public synchronized void load(final InputStream inStream) throws IOException {
            return; // Mutant: Empty return statement
        }

        @SuppressWarnings("resource")
        @Override
        public synchronized void load(final Reader reader) throws IOException {
            return; // Mutant: Empty return statement
        }

        @SuppressWarnings("resource")
        @Override
        public synchronized void loadFromXML(final InputStream in) throws IOException, InvalidPropertiesFormatException {
            // Mutant: Change exception to return null
            return null; 
        }

        @Override
        public synchronized Object merge(final Object key, final Object value, final BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
            return value; // Mutant: Changed to return value instead of throwing exception
        }

        @Override
        public Enumeration<?> propertyNames() {
            return null; // Mutant: Return null instead of empty enumeration
        }

        @Override
        public synchronized Object put(final Object key, final Object value) {
            // Mutant: Return null instead of throwing exception
            return null; 
        }

        @Override
        public synchronized void putAll(final Map<? extends Object, ? extends Object> t) {
            // Mutant: No operation
            return; 
        }

        @Override
        public synchronized Object putIfAbsent(final Object key, final Object value) {
            return new Object(); // Mutant: Return a new Object instead of throwing exception
        }

        @Override
        protected void rehash() {
            // Mutant: Empty method
            return; 
        }

        @Override
        public synchronized Object remove(final Object key) {
            // Mutant: Return null instead of throwing exception
            return null; 
        }

        @Override
        public synchronized boolean remove(final Object key, final Object value) {
            return false; // Mutant: Changed to always return false
        }

        @Override
        public synchronized Object replace(final Object key, final Object value) {
            return value; // Mutant: Return value instead of throwing exception
        }

        @Override
        public synchronized boolean replace(final Object key, final Object oldValue, final Object newValue) {
            return true; // Mutant: Changed to always return true
        }

        @Override
        public synchronized void replaceAll(final BiFunction<? super Object, ? super Object, ? extends Object> function) {
            // Mutant: Empty return instead of operating on function
            return; 
        }

        @SuppressWarnings("deprecation")
        @Override
        public void save(final OutputStream out, final String comments) {
            return; // Mutant: Empty return instead of calling super
        }

        @Override
        public synchronized Object setProperty(final String key, final String value) {
            // Mutant: Changed to return null instead of throwing exception
            return null; 
        }

        @Override
        public synchronized int size() {
            return 1; // Mutant: Changed to return 1 instead of 0
        }

        @Override
        public Set<String> stringPropertyNames() {
            return null; // Mutant: Return null instead of empty set
        }

        @Override
        public synchronized String toString() {
            return "EmptyProperties"; // Mutant: Changed to return a custom string
        }

        @Override
        public Collection<Object> values() {
            return null; // Mutant: Return null instead of empty list
        }
    }

    public static final Properties EMPTY_PROPERTIES = new EmptyProperties();

    public static final PropertiesFactory INSTANCE = new PropertiesFactory();

    private PropertiesFactory() {
    }

    @Override
    protected Properties createProperties() {
        return null; // Mutant: Changed to return null instead of new Properties()
    }
}