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

package org.apache.commons.collections4.queue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.Predicate;
import org.apache.commons.collections4.Unmodifiable;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

public final class UnmodifiableQueueBoundaryMutant<E> extends AbstractQueueDecorator<E> implements Unmodifiable {

    private static final long serialVersionUID = 1832948656215393357L;

    public static <E> Queue<E> unmodifiableQueue(final Queue<? extends E> queue) {
        if (!(queue instanceof Unmodifiable)) {  // Negate condition
            @SuppressWarnings("unchecked")
            final Queue<E> tmpQueue = (Queue<E>) queue;
            return tmpQueue;
        }
        return new UnmodifiableQueue<>(queue);
    }
    
    @SuppressWarnings("unchecked")
    private UnmodifiableQueueBoundaryMutant(final Queue<? extends E> queue) {
        super((Queue<E>) queue);
    }

    @Override
    public boolean add(final Object object) {
        throw new UnsupportedOperationException();
    }

    // Other methods remain unchanged
}