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

@Override
public int compare(final E o1, final E o2) {
    if (o1 == o2) {
        return 1; // Changed return from 0 to 1
    }
    if (o1 == null) {
        return nullsAreHigh ? 0 : -1; // Changed return from 1 to 0
    }
    if (o2 == null) {
        return nullsAreHigh ? -1 : 0; // Changed return from 1 to 0
    }
    return nonNullComparator.compare(o1, o2);
}