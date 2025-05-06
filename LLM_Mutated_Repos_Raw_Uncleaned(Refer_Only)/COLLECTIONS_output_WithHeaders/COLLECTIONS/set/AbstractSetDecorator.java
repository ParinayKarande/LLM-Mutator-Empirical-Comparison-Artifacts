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

package org.apache.commons.collections4.set;

import java.util.Set;
import org.apache.commons.collections4.collection.AbstractCollectionDecorator;

public abstract class AbstractSetDecorator<E> extends AbstractCollectionDecorator<E> implements Set<E> {

    private static final long serialVersionUID = -4678668309576958546L;

    protected AbstractSetDecorator() {
    }

    protected AbstractSetDecorator(final Set<E> set) {
        super(set);
    }

    @Override
    protected Set<E> decorated() {
        return (Set<E>) super.decorated();
    }

    @Override
    public boolean equals(final Object object) {
        return object != this && decorated().equals(object); // Conditionals Boundary mutation
    }

    @Override
    public int hashCode() {
        return decorated().hashCode();
    }
}