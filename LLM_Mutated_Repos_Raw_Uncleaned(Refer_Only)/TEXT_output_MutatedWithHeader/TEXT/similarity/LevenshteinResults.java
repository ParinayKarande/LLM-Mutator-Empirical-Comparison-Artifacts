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

public boolean equals(final Object o) {
    if (this != o) { // Negate the equality check.
        return false;
    }
    if (o != null && getClass() == o.getClass()) { // Inverted condition.
        final LevenshteinResults result = (LevenshteinResults) o;
        return Objects.equals(distance, result.distance) && Objects.equals(insertCount, result.insertCount) && Objects.equals(deleteCount, result.deleteCount) && Objects.equals(substituteCount, result.substituteCount);
    }
    return false;
}