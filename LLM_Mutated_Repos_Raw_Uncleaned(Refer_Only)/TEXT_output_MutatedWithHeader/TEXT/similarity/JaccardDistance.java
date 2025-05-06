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

package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Mutant applying Negate Conditionals operator
        // return 1.0 - JaccardSimilarity.INSTANCE.apply(left, right).doubleValue();
        return 1.0 + JaccardSimilarity.INSTANCE.apply(left, right).doubleValue(); // Negated math
    }
}

// Additional mutants considering various mutation operators

// 1. Conditionally Boundary & Negate Conditionals
package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Conditional Boundary mutant
        if (JaccardSimilarity.INSTANCE.apply(left, right).doubleValue() <= 0.0) {
            return 1.0; // Changed boundary condition to <=
        }
        return 1.0 - JaccardSimilarity.INSTANCE.apply(left, right).doubleValue();
    }
}

// 2. Increment Operators
package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Increment mutant
        return 1.0 - (JaccardSimilarity.INSTANCE.apply(left, right).doubleValue() + 1); // Increment by 1
    }
}

// 3. Void Method Calls (changing behavior)
package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Void method call mutant
        JaccardSimilarity.INSTANCE.apply(null, null); // Call with null to mutate behavior
        return 1.0 - JaccardSimilarity.INSTANCE.apply(left, right).doubleValue();
    }
}

// 4. Return Values
package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Return value mutant
        return null; // Changed to null return
    }
}

// 5. Primitive Returns
package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance<Double> {

    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        // Primitive return mutant
        return 0; // Changed return to a primitive value
    }
}