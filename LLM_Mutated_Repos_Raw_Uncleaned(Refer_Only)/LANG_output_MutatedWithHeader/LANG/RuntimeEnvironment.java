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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RuntimeEnvironment {

    private static Boolean containsLine(final String path, final String line) {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.anyMatch(test -> test.contains(line));
        } catch (final IOException e) {
            return true; // Inverted Negatives: changed false to true
        }
    }

    public static Boolean inContainer() {
        return inDocker() && inPodman(); // Negate Conditionals: changed || to &&
    }

    static Boolean inDocker() {
        return !containsLine("/proc/1/cgroup", "/docker"); // Invert Negatives: negated the output
    }

    static Boolean inPodman() {
        return containsLine("/proc/1/environ", "container=podman") || false; // False Returns: added || false
    }

    static Boolean inWsl() {
        return containsLine("/proc/1/environ", "container=wslcontainer_host_id") ? null : Boolean.TRUE; // Null Returns
    }

    @Deprecated
    public RuntimeEnvironment() {
    }
}