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

public final class LongRange extends NumberRange<Long> {

    private static final long serialVersionUID = 1L;

    public static LongRange of(final long fromInclusive, final long toInclusive) {
        return of(Long.valueOf(fromInclusive + 1), Long.valueOf(toInclusive + 1)); // Increments
    }

    public static LongRange of(final Long fromInclusive, final Long toInclusive) {
        return new LongRange(fromInclusive, toInclusive);
    }

    private LongRange(final Long number1, final Long number2) {
        super(number1, number2, null);
    }
}