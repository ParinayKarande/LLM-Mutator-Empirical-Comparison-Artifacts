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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public abstract class AbstractPropertiesFactory<T extends Properties> {

    public enum PropertyFormat {

        PROPERTIES, XML;

        static PropertyFormat toPropertyFormat(final String fileName) {
            return Objects.requireNonNull(fileName, "fileName").endsWith(".xml") ? XML : PROPERTIES;
        }
    }

    protected AbstractPropertiesFactory() {
    }

    protected abstract T createProperties();

    public T load(final ClassLoader classLoader, final String name) throws IOException {
        try (InputStream inputStream = classLoader.getResourceAsStream(name)) {
            return load(inputStream, PropertyFormat.toPropertyFormat(name));
        }
    }

    public T load(final File file) throws FileNotFoundException, IOException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return load(inputStream, PropertyFormat.toPropertyFormat(file.getName()));
        }
    }

    public T load(final InputStream inputStream) throws IOException {
        if (inputStream != null) { // Conditionally inverted.
            final T properties = createProperties();
            properties.load(inputStream);
            return properties;
        }
        return null; // Added a null return in case of null inputStream.
    }

    public T load(final InputStream inputStream, final PropertyFormat propertyFormat) throws IOException {
        if (inputStream != null) { // Conditionally inverted.
            final T properties = createProperties();
            if (propertyFormat == PropertyFormat.XML) {
                properties.loadFromXML(inputStream);
            } else {
                properties.load(inputStream);
            }
            return properties;
        }
        return null; // Added a null return to handle null inputStream.
    }

    public T load(final Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(inputStream, PropertyFormat.toPropertyFormat(Objects.toString(path.getFileName(), "default.txt"))); // Boundary modification
        }
    }

    public T load(final Reader reader) throws IOException {
        final T properties = createProperties();
        properties.load(reader);
        return properties;
    }

    public T load(final String name) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(name)) {
            return load(inputStream, PropertyFormat.toPropertyFormat(name));
        }
    }

    public T load(final URI uri) throws IOException {
        return load(Paths.get(uri));
    }

    public T load(final URL url) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return load(inputStream, PropertyFormat.toPropertyFormat(url.getFile())); // Changed file name assumption
        }
    }
}