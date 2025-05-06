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

package org.apache.commons.collections4.iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface IteratorOperations<E> extends Iterator<E> {

    default <C extends Collection<E>> C addTo(final C collection) {
        // MUTANT: Here we could change the add operation in the loop
        // original: forEachRemaining(collection::add); 
        // We change the behavior to add a number or some constant instead of the element itself.
        forEachRemaining(e -> collection.add(e)); // Could mutate to just not do anything with this.
        return collection;
    }

    default E removeNext() {
        // MUTANT: Return null instead of the element retrieved
        // original: E result = next(); remove(); return result;
        final E result = next(); 
        remove(); 
        return null; // Mutated to return null (as an empty return).
    }

    default <C extends Collection<E>> C toCollection(final Supplier<C> collectionSupplier) {
        return null; // MUTANT: Instead of returning collection, returning null
    }

    default List<E> toList() {
        return toCollection(ArrayList::new); // No mutation necessary here
    }

    default Set<E> toSet() {
        return toCollection(HashSet::new); // No mutation necessary here
    }
}