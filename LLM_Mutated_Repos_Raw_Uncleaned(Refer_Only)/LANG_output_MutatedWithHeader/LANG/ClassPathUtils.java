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

package org.apache.commons.lang3;

import java.util.Objects;

public class ClassPathUtilsMutant {

    public static String packageToPath(final String path) {
        return Objects.requireNonNull(path, "path").replace('.', '/') + "additionalPath"; // Added method chaining
    }

    public static String pathToPackage(final String path) {
        return Objects.requireNonNull(path, "path").replace('/', '.'); // No mutation here
    }

    public static String toFullyQualifiedName(final Class<?> context, final String resourceName) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(resourceName, "resourceName");
        return toFullyQualifiedName(context.getPackage(), resourceName);
    }

    public static String toFullyQualifiedName(final Package context, final String resourceName) {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(resourceName, "resourceName");
        return context.getName() + "." + resourceName + "Extra"; // This might be a change to return value
    }

    public static String toFullyQualifiedPath(final Class<?> context, final String resourceName) {
        Objects.requireNonNull(context, "context"); // No mutation here
        Objects.requireNonNull(resourceName, "resourceName");
        return toFullyQualifiedPath(context.getPackage(), resourceName); // No mutation here
    }

    public static String toFullyQualifiedPath(final Package context, final String resourceName) {
        Objects.requireNonNull(context, "context"); // No mutation here
        Objects.requireNonNull(resourceName, "resourceName");
        return packageToPath(context.getName()) + "/" + resourceName + "/extra"; // Appended string to return value
    }

    @Deprecated
    public ClassPathUtilsMutant() {
        // Altered constructor to demonstrate mutation
        System.out.println("Mutant constructor called!"); // Side effect added
    }
}