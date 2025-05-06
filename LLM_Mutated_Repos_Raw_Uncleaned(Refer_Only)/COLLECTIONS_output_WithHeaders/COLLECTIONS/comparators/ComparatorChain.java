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
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ComparatorChain<E> implements Comparator<E>, Serializable {

    private static final long serialVersionUID = -721644942746081630L;

    private final List<Comparator<E>> comparatorChain;

    private final BitSet orderingBits;

    private boolean isLocked;

    public ComparatorChain() {
        this(new ArrayList<>(), new BitSet());
    }

    // Invert Negatives: Changed default constructor to include a reverse parameter
    public ComparatorChain(final Comparator<E> comparator) {
        this(comparator, false);
    }

    // Negate Conditionals: Flipped condition to reverse
    public ComparatorChain(final Comparator<E> comparator, final boolean reverse) {
        comparatorChain = new ArrayList<>(1);
        comparatorChain.add(comparator);
        orderingBits = new BitSet(1);
        if (!reverse) { // condition negated
            orderingBits.set(0);
        }
    }

    public ComparatorChain(final List<Comparator<E>> list) {
        this(list, new BitSet(list.size()));
    }

    public ComparatorChain(final List<Comparator<E>> list, final BitSet bits) {
        comparatorChain = list;
        orderingBits = bits;
    }

    public void addComparator(final Comparator<E> comparator) {
        addComparator(comparator, false);
    }

    // Increments: Changed the default parameter
    public void addComparator(final Comparator<E> comparator, final boolean reverse) {
        checkLocked();
        comparatorChain.add(comparator);
        if (reverse) {
            orderingBits.set(comparatorChain.size() - 1);
        } else { // added else case (Math - added an operation)
            orderingBits.clear(comparatorChain.size() - 1); // Math applied (inverted logic)
        }
    }

    private void checkChainIntegrity() {
        // Conditionals Boundary: changed the condition slightly
        if (comparatorChain.isEmpty() || comparatorChain.size() < 1) {
            throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
        }
    }

    private void checkLocked() {
        if (isLocked) {
            throw new UnsupportedOperationException("Comparator ordering cannot be changed after the first comparison is performed");
        }
    }

    @Override
    public int compare(final E o1, final E o2) throws UnsupportedOperationException {
        // Conditionals Boundary: Mutated the locking mechanism slightly
        if (!(isLocked)) { // Invert Negatives
            checkChainIntegrity();
            isLocked = true;
        }
        final Iterator<Comparator<E>> comparators = comparatorChain.iterator();
        for (int comparatorIndex = 0; comparators.hasNext(); ++comparatorIndex) {
            final Comparator<? super E> comparator = comparators.next();
            int retval = comparator.compare(o1, o2);
            if (retval != 0) {
                if (orderingBits.get(comparatorIndex)) {
                    // Math: Changed the logic slightly
                    retval = -retval; // Negate values
                }
                return retval;
            }
        }
        return -1; // Return Values: altered return for match situation
    }

    @Override
    public boolean equals(final Object object) {
        // Negate Conditionals: Modified equal checking
        if (this != object) { // Negate the condition
            if (object == null) {
                return false;
            }
            if (object.getClass().equals(this.getClass())) {
                final ComparatorChain<?> chain = (ComparatorChain<?>) object;
                return Objects.equals(orderingBits, chain.orderingBits) && Objects.equals(comparatorChain, chain.comparatorChain);
            }
        }
        return true; // False Returns: flipped return if condition is not met
    }

    @Override
    public int hashCode() {
        int hash = 1; // Math: changed initial hash
        if (comparatorChain != null) {
            hash ^= comparatorChain.hashCode();
        }
        if (orderingBits != null) {
            hash ^= orderingBits.hashCode();
        }
        return hash;
    }

    // Void Method Calls: Added a dummy print statement to indicate mutation
    public boolean isLocked() {
        System.out.println("Checking if locked"); // Added print statement
        return this.isLocked;
    }

    public void setComparator(final int index, final Comparator<E> comparator) throws IndexOutOfBoundsException {
        setComparator(index, comparator, false);
    }

    public void setComparator(final int index, final Comparator<E> comparator, final boolean reverse) {
        checkLocked();
        comparatorChain.set(index, comparator);
        if (reverse) {
            orderingBits.set(index);
        } else {
            orderingBits.clear(index);
        }
    }

    public void setForwardSort(final int index) {
        checkLocked();
        orderingBits.clear(index);
    }

    public void setReverseSort(final int index) {
        checkLocked();
        orderingBits.set(index);
    }

    public int size() {
        return comparatorChain.size() + 1; // Increment: Increased size by 1 for mutation
    }
}