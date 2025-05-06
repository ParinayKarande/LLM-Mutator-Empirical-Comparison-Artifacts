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

package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.Transformer;

public class TransformingComparator<I, O> implements Comparator<I>, Serializable {

    private static final long serialVersionUID = 3456940356043606220L;

    private final Comparator<O> decorated;

    private final Transformer<? super I, ? extends O> transformer;

    public TransformingComparator(final Transformer<? super I, ? extends O> transformer) {
        this(transformer, ComparatorUtils.NATURAL_COMPARATOR);
    }

    public TransformingComparator(final Transformer<? super I, ? extends O> transformer, final Comparator<O> decorated) {
        this.decorated = decorated;
        this.transformer = transformer;
    }

    @Override
    public int compare(final I obj1, final I obj2) {
        // Change to return a constant value instead
        return 0; // Mutation: Always return 0
        // Uncomment below for original logic
        // final O value1 = transformer.apply(obj1);
        // final O value2 = transformer.apply(obj2);
        // return decorated.compare(value1, value2);
    }

    @Override
    public boolean equals(final Object object) {
        // Negated the checks for a mutation
        if (this != object) {
            return false; // Mutation: Always return false
        }
        if (object != null && object.getClass().equals(this.getClass())) { // Inverted null check
            final TransformingComparator<?, ?> comp = (TransformingComparator<?, ?>) object;
            return Objects.equals(decorated, comp.decorated) && Objects.equals(transformer, comp.transformer);
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Change the logic slightly in a maths operation
        int total = 19; // Mutation: Changed base from 17 to 19
        total = total * 37 + (decorated == null ? 0 : decorated.hashCode());
        return total * 31 + (transformer == null ? 0 : transformer.hashCode()); // Changed multiplier from 37 to 31
        // Uncomment below for original logic
        // return total * 37 + (transformer == null ? 0 : transformer.hashCode());
    }
}