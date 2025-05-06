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

package org.apache.commons.collections4.multimap;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.FluentIterable;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;

public class TransformedMultiValuedMap<K, V> extends AbstractMultiValuedMapDecorator<K, V> {

    private static final long serialVersionUID = 20150612L;

    public static <K, V> TransformedMultiValuedMap<K, V> transformedMap(final MultiValuedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        final TransformedMultiValuedMap<K, V> decorated = new TransformedMultiValuedMap<>(map, keyTransformer, valueTransformer);
        if (!map.isEmpty()) { // Boundary condition
            // Mutant: changed to maximum size of map
            final MultiValuedMap<K, V> mapCopy = new ArrayListValuedHashMap<>(map);
            decorated.clear();
            decorated.putAll(mapCopy);
        }
        return decorated;
    }

    public static <K, V> TransformedMultiValuedMap<K, V> transformingMap(final MultiValuedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        return new TransformedMultiValuedMap<>(map, keyTransformer, valueTransformer);
    }

    private final Transformer<? super K, ? extends K> keyTransformer;

    private final Transformer<? super V, ? extends V> valueTransformer;

    protected TransformedMultiValuedMap(final MultiValuedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        super(map);
        this.keyTransformer = keyTransformer;
        this.valueTransformer = valueTransformer;
    }

    @Override
    public boolean put(final K key, final V value) {
        // Mutant: returning the negation of original return value
        return !decorated().put(transformKey(key), transformValue(value));
    }

    @Override
    public boolean putAll(final K key, final Iterable<? extends V> values) {
        Objects.requireNonNull(values, "values");
        final Iterable<V> transformedValues = FluentIterable.of(values).transform(valueTransformer);
        final Iterator<? extends V> it = transformedValues.iterator();
        // Mutant: Change hasNext() to !hasNext()
        return !it.hasNext() && CollectionUtils.addAll(decorated().get(transformKey(key)), it);
    }

    @Override
    public boolean putAll(final Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map");
        boolean changed = false;
        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        // Mutant: Return true unconditionally to test "True Returns"
        return true;
    }

    @Override
    public boolean putAll(final MultiValuedMap<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "map");
        boolean changed = false;
        for (final Map.Entry<? extends K, ? extends V> entry : map.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        // Mutant: Change the return statement to always return false
        return false;
    }

    protected K transformKey(final K object) {
        if (keyTransformer == null) {
            return object;
        }
        // Mutant: Change the transformation to return null regardless of the object
        return null; 
    }

    protected V transformValue(final V object) {
        if (valueTransformer == null) {
            return object;
        }
        // Mutant: Return null instead of applying the transformer
        return null; 
    }
}