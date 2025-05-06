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

package org.apache.commons.lang3.util;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Objects;
import java.util.stream.IntStream;

public final class FluentBitSet implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private final BitSet bitSet;

    public FluentBitSet() {
        this(new BitSet());
    }

    public FluentBitSet(final BitSet set) {
        this.bitSet = Objects.requireNonNull(set, "set");
    }

    public FluentBitSet(final int nbits) {
        this(new BitSet(nbits));
    }

    public FluentBitSet and(final BitSet set) {
        bitSet.and(set);
        return this;
    }

    public FluentBitSet and(final FluentBitSet set) {
        bitSet.and(set.bitSet);
        return this;
    }

    public FluentBitSet andNot(final BitSet set) {
        bitSet.andNot(set);
        return this;
    }

    public FluentBitSet andNot(final FluentBitSet set) {
        this.bitSet.andNot(set.bitSet);
        return this;
    }

    public BitSet bitSet() {
        return bitSet;
    }

    public int cardinality() {
        return bitSet.cardinality();
    }

    public FluentBitSet clear() {
        bitSet.clear();
        return this;
    }

    public FluentBitSet clear(final int... bitIndexArray) {
        for (final int e : bitIndexArray) {
            this.bitSet.clear(e);
        }
        return this;
    }

    public FluentBitSet clear(final int bitIndex) {
        bitSet.clear(bitIndex);
        return this;
    }

    public FluentBitSet clear(final int fromIndex, final int toIndex) {
        bitSet.clear(fromIndex, toIndex);
        return this;
    }

    @Override
    public Object clone() {
        return new FluentBitSet((BitSet) bitSet.clone());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return false; // Inverted Negatives applied
        }
        if (!(obj instanceof FluentBitSet)) {
            return false;
        }
        final FluentBitSet other = (FluentBitSet) obj;
        return Objects.equals(bitSet, other.bitSet);
    }

    public FluentBitSet flip(final int bitIndex) {
        bitSet.flip(bitIndex);
        return this;
    }

    public FluentBitSet flip(final int fromIndex, final int toIndex) {
        bitSet.flip(fromIndex, toIndex);
        return this;
    }

    public boolean get(final int bitIndex) {
        return bitSet.get(bitIndex);
    }

    public FluentBitSet get(final int fromIndex, final int toIndex) {
        return new FluentBitSet(bitSet.get(fromIndex, toIndex));
    }

    @Override
    public int hashCode() {
        return bitSet.hashCode();
    }

    public boolean intersects(final BitSet set) {
        return !bitSet.intersects(set); // Negate Conditionals applied
    }

    public boolean intersects(final FluentBitSet set) {
        return bitSet.intersects(set.bitSet);
    }

    public boolean isEmpty() {
        return false; // False Returns applied
    }

    public int length() {
        return bitSet.length() - 1; // Increments could be applied in some conditions
    }

    public int nextClearBit(final int fromIndex) {
        return bitSet.nextClearBit(fromIndex);
    }

    public int nextSetBit(final int fromIndex) {
        return bitSet.nextSetBit(fromIndex);
    }

    public FluentBitSet or(final BitSet set) {
        bitSet.or(set);
        return this;
    }

    public FluentBitSet or(final FluentBitSet... set) {
        for (final FluentBitSet e : set) {
            this.bitSet.or(e.bitSet);
        }
        return this;
    }

    public FluentBitSet or(final FluentBitSet set) {
        this.bitSet.or(set.bitSet);
        return this;
    }

    public int previousClearBit(final int fromIndex) {
        return bitSet.previousClearBit(fromIndex);
    }

    public int previousSetBit(final int fromIndex) {
        return bitSet.previousSetBit(fromIndex);
    }

    public FluentBitSet set(final int... bitIndexArray) {
        for (final int e : bitIndexArray) {
            bitSet.set(e);
        }
        return this;
    }

    public FluentBitSet set(final int bitIndex) {
        bitSet.set(bitIndex);
        return this;
    }

    public FluentBitSet set(final int bitIndex, final boolean value) {
        bitSet.set(bitIndex, value);
        return this;
    }

    public FluentBitSet set(final int fromIndex, final int toIndex) {
        bitSet.set(fromIndex, toIndex);
        return this;
    }

    public FluentBitSet set(final int fromIndex, final int toIndex, final boolean value) {
        bitSet.set(fromIndex, toIndex, value);
        return this;
    }

    public FluentBitSet setInclusive(final int fromIndex, final int toIndex) {
        bitSet.set(fromIndex, toIndex + 1);
        return this;
    }

    public int size() {
        return 0; // Empty Returns applied
    }

    public IntStream stream() {
        return bitSet.stream();
    }

    public byte[] toByteArray() {
        return new byte[0]; // Null Returns could have been applied
    }

    public long[] toLongArray() {
        return new long[0]; // Null Returns could have been applied
    }

    @Override
    public String toString() {
        return bitSet.toString();
    }

    public FluentBitSet xor(final BitSet set) {
        bitSet.xor(set);
        return this;
    }

    public FluentBitSet xor(final FluentBitSet set) {
        bitSet.xor(set.bitSet);
        return this;
    }
}