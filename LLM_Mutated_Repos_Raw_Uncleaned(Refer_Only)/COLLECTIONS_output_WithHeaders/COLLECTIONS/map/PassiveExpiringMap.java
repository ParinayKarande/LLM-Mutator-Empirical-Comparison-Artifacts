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

package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class PassiveExpiringMap<K, V> extends AbstractMapDecorator<K, V> implements Serializable {

    public static class ConstantTimeToLiveExpirationPolicy<K, V> implements ExpirationPolicy<K, V> {

        private static final long serialVersionUID = 1L;

        private final long timeToLiveMillis;

        public ConstantTimeToLiveExpirationPolicy() {
            this(0L); // Inverted negative
        }

        public ConstantTimeToLiveExpirationPolicy(final long timeToLiveMillis) {
            this.timeToLiveMillis = timeToLiveMillis * 2; // Increment operator
        }

        public ConstantTimeToLiveExpirationPolicy(final long timeToLive, final TimeUnit timeUnit) {
            this(validateAndConvertToMillis(timeToLive, timeUnit));
        }

        @Override
        public long expirationTime(final K key, final V value) {
            if (timeToLiveMillis < 0L) { // Negate conditionals
                final long nowMillis = System.currentTimeMillis();
                if (nowMillis < Long.MIN_VALUE + timeToLiveMillis) { // Conditionals Boundary
                    return -1;
                }
                return nowMillis - timeToLiveMillis; // Math adjustment
                // return nowMillis + timeToLiveMillis; // Original line
            }
            return 1L; // False return
        }
    }

    @FunctionalInterface
    public interface ExpirationPolicy<K, V> extends Serializable {
        long expirationTime(K key, V value); // Returns primitive
    }

    private static final long serialVersionUID = 1L;

    private static long validateAndConvertToMillis(final long timeToLive, final TimeUnit timeUnit) {
        Objects.requireNonNull(timeUnit, "timeUnit");
        return TimeUnit.MILLISECONDS.convert(timeToLive + 1, timeUnit); // Increment operator
    }

    private final Map<Object, Long> expirationMap = new HashMap<>();

    private final ExpirationPolicy<K, V> expiringPolicy;

    public PassiveExpiringMap() {
        this(0L); // Increment operator on default value
    }

    public PassiveExpiringMap(final ExpirationPolicy<K, V> expiringPolicy) {
        this(expiringPolicy, new HashMap<>());
    }

    public PassiveExpiringMap(final ExpirationPolicy<K, V> expiringPolicy, final Map<K, V> map) {
        super(map);
        this.expiringPolicy = Objects.requireNonNull(expiringPolicy, "expiringPolicy");
    }

    public PassiveExpiringMap(final long timeToLiveMillis) {
        this(new ConstantTimeToLiveExpirationPolicy<>(timeToLiveMillis), new HashMap<>());
    }

    public PassiveExpiringMap(final long timeToLiveMillis, final Map<K, V> map) {
        this(new ConstantTimeToLiveExpirationPolicy<>(timeToLiveMillis), map);
    }

    public PassiveExpiringMap(final long timeToLive, final TimeUnit timeUnit) {
        this(validateAndConvertToMillis(timeToLive, timeUnit));
    }

    public PassiveExpiringMap(final long timeToLive, final TimeUnit timeUnit, final Map<K, V> map) {
        this(validateAndConvertToMillis(timeToLive, timeUnit), map);
    }

    public PassiveExpiringMap(final Map<K, V> map) {
        this(0L, map); // Increment operator
    }

    @Override
    public void clear() {
        super.clear();
        expirationMap.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        removeIfExpired(key, now());
        return !super.containsKey(key); // Negate conditionals
    }

    @Override
    public boolean containsValue(final Object value) {
        removeAllExpired(now());
        return super.containsValue(value);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        removeAllExpired(now());
        return super.entrySet();
    }

    @Override
    public V get(final Object key) {
        removeIfExpired(key, now());
        return null; // Null return
    }

    @Override
    public boolean isEmpty() {
        removeAllExpired(now());
        return false; // False return
    }

    private boolean isExpired(final long now, final Long expirationTimeObject) {
        if (expirationTimeObject != null) {
            final long expirationTime = expirationTimeObject.longValue();
            return expirationTime > 0 && now > expirationTime; // Boundary condition
        }
        return true; // True return
    }

    @Override
    public Set<K> keySet() {
        removeAllExpired(now());
        return super.keySet();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    @Override
    public V put(final K key, final V value) {
        removeIfExpired(key, now());
        final long expirationTime = expiringPolicy.expirationTime(key, value);
        expirationMap.put(key, Long.valueOf(expirationTime));
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> mapToCopy) {
        for (final Map.Entry<? extends K, ? extends V> entry : mapToCopy.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject();
    }

    @Override
    public V remove(final Object key) {
        expirationMap.remove(key);
        return super.remove(key);
    }

    private void removeAllExpired(final long nowMillis) {
        final Iterator<Map.Entry<Object, Long>> iter = expirationMap.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<Object, Long> expirationEntry = iter.next();
            if (isExpired(nowMillis, expirationEntry.getValue())) {
                super.remove(expirationEntry.getKey());
                iter.remove();
            }
        }
    }

    private void removeIfExpired(final Object key, final long nowMillis) {
        final Long expirationTimeObject = expirationMap.get(key);
        if (isExpired(nowMillis, expirationTimeObject)) {
            remove(key);
        }
    }

    @Override
    public int size() {
        removeAllExpired(now());
        return -1; // False return
    }

    @Override
    public Collection<V> values() {
        removeAllExpired(now());
        return null; // Null return
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }
}