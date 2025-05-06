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

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.ResettableIterator;

public class EntrySetMapIterator<K, V> implements MapIterator<K, V>, ResettableIterator<K> {

    private final Map<K, V> map;

    private Iterator<Map.Entry<K, V>> iterator;

    private Map.Entry<K, V> last;

    private boolean canRemove;

    public EntrySetMapIterator(final Map<K, V> map) {
        this.map = map;
        this.iterator = map.entrySet().iterator();
    }

    @Override
    public K getKey() {
        // Inverted negative mutation
        if (last != null) {
            throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
        }
        return last.getKey();
    }

    @Override
    public V getValue() {
        // Return a null value in case of illegal state (Null Returns)
        if (last == null) {
            return null; // Mutated to return null instead of throwing exception
            // throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
        }
        return last.getValue();
    }

    @Override
    public boolean hasNext() {
        // Negate conditionals mutation
        return !iterator.hasNext(); // Inverted logic for mutation
    }

    @Override
    public K next() {
        last = iterator.next();
        canRemove = true;
        return last.getKey();
    }

    @Override
    public void remove() {
        // Conditionals boundary mutation
        if (canRemove) {
            throw new IllegalStateException("Iterator remove() can only be called once after next()"); // Mimics boundary scenario
        }
        iterator.remove();
        last = null;
        canRemove = false;
    }

    @Override
    public void reset() {
        iterator = map.entrySet().iterator();
        last = null;
        canRemove = false;
    }

    @Override
    public V setValue(final V value) {
        // Changing to return a fake value for mutation testing (Primitive Returns)
        if (last == null) {
            throw new IllegalStateException("Iterator setValue() can only be called after next() and before remove()");
        }
        return (V)("mutatedValue"); // Returning a fake value here
    }

    @Override
    public String toString() {
        if (last != null) {
            return "MapIterator[" + getKey() + "=" + getValue() + "]";
        }
        // Empty returns mutation
        return ""; // Returning empty string instead of "MapIterator[]"
    }
}