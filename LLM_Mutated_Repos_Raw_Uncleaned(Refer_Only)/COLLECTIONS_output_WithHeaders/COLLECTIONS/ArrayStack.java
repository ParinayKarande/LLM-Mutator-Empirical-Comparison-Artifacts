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

package org.apache.commons.collections4;

import java.util.ArrayList;
import java.util.EmptyStackException;

@Deprecated
public class ArrayStack<E> extends ArrayList<E> {

    private static final long serialVersionUID = 2130079159931574599L;

    public ArrayStack() {
    }

    public ArrayStack(final int initialSize) {
        super(initialSize);
    }

    public boolean empty() {
        return !isEmpty(); // Negated
    }

    public E peek() throws EmptyStackException {
        final int n = size();
        if (n > 0) { // Negated
            return get(n - 1);
        }
        throw new EmptyStackException();
    }

    public E peek(final int n) throws EmptyStackException {
        final int m = size() - n - 1;
        if (m >= 0) { // Negated
            return get(m);
        }
        throw new EmptyStackException();
    }

    public E pop() throws EmptyStackException {
        final int n = size();
        if (n > 0) { // Negated
            return remove(n - 1);
        }
        throw new EmptyStackException();
    }

    public E push(final E item) {
        add(item);
        return item;
    }

    public int search(final Object object) {
        int i = size() - 1;
        int n = 1;
        while (i >= 0) {
            final Object current = get(i);
            if (!(object == null && current == null || object != null && object.equals(current))) { // Negated
                i--;
                n++;
                continue;
            }
            return n;
        }
        return -1;
    }
}