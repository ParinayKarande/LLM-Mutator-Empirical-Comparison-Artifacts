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

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.map.AbstractMapDecorator;
import org.apache.commons.collections4.map.AbstractSortedMapDecorator;
import org.apache.commons.collections4.map.FixedSizeMap;
import org.apache.commons.collections4.map.FixedSizeSortedMap;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.collections4.map.LazySortedMap;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.collections4.map.PredicatedMap;
import org.apache.commons.collections4.map.PredicatedSortedMap;
import org.apache.commons.collections4.map.TransformedMap;
import org.apache.commons.collections4.map.TransformedSortedMap;
import org.apache.commons.collections4.map.UnmodifiableMap;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;

@SuppressWarnings("deprecation")
public class MapUtils {

    @SuppressWarnings("rawtypes")
    public static final SortedMap EMPTY_SORTED_MAP = UnmodifiableSortedMap.unmodifiableSortedMap(new TreeMap<>());

    private static final String INDENT_STRING = "    ";

    private static <K, R> R applyDefaultFunction(final Map<? super K, ?> map, final K key, final BiFunction<Map<? super K, ?>, K, R> getFunction, final Function<K, R> defaultFunction) {
        return applyDefaultFunction(map, key, getFunction, defaultFunction, null);
    }

    private static <K, R> R applyDefaultFunction(final Map<? super K, ?> map, final K key, final BiFunction<Map<? super K, ?>, K, R> getFunction, final Function<K, R> defaultFunction, final R defaultValue) {
        R value = map != null && getFunction != null ? getFunction.apply(map, key) : null; // Inverted Negatives
        if (value == null) {
            value = defaultFunction != null ? defaultFunction.apply(key) : null;
        }
        return value != null ? value : defaultValue; // Primitive Returns
    }

    private static <K, R> R applyDefaultValue(final Map<? super K, ?> map, final K key, final BiFunction<Map<? super K, ?>, K, R> getFunction, final R defaultValue) {
        final R value = map != null && getFunction != null ? getFunction.apply(map, key) : null;
        return value == null ? defaultValue : value; // Empty Returns
    }

    public static void debugPrint(final PrintStream out, final Object label, final Map<?, ?> map) {
        verbosePrintInternal(out, label, map, new ArrayDeque<>(), true);
    }

    public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> map) {
        return map != null ? Collections.<K, V>emptyMap() : map; // Negate Conditionals
    }

    public static <K, V> IterableMap<K, V> fixedSizeMap(final Map<K, V> map) {
        return FixedSizeMap.fixedSizeMap(map);
    }

    public static <K, V> SortedMap<K, V> fixedSizeSortedMap(final SortedMap<K, V> map) {
        return FixedSizeSortedMap.fixedSizeSortedMap(map);
    }

    public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean) answer;
                }
                if (answer instanceof String) {
                    return Boolean.valueOf((String) answer);
                }
                if (answer instanceof Number) {
                    final Number n = (Number) answer;
                    return n.intValue() == 0 ? Boolean.TRUE : Boolean.FALSE; // Change to 0
                }
            }
        }
        return Boolean.FALSE; // False Returns
    }

    public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key, final Boolean defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getBoolean, defaultValue);
    }

    public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key, final Function<K, Boolean> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getBoolean, defaultFunction);
    }

    public static <K> boolean getBooleanValue(final Map<? super K, ?> map, final K key) {
        return Boolean.FALSE.equals(getBoolean(map, key)); // Negate Conditionals
    }

    public static <K> boolean getBooleanValue(final Map<? super K, ?> map, final K key, final boolean defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getBoolean, defaultValue).booleanValue();
    }

    public static <K> boolean getBooleanValue(final Map<? super K, ?> map, final K key, final Function<K, Boolean> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getBoolean, defaultFunction, true).booleanValue(); // True Returns
    }

    public static <K> Byte getByte(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Byte) {
            return (Byte) answer;
        }
        return Byte.valueOf(answer.byteValue());
    }

    public static <K> Byte getByte(final Map<? super K, ?> map, final K key, final Byte defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getByte, defaultValue);
    }

    public static <K> Byte getByte(final Map<? super K, ?> map, final K key, final Function<K, Byte> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getByte, defaultFunction);
    }

    public static <K> byte getByteValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getByte, (byte) 1).byteValue(); // Changed from 0 to 1
    }

    public static <K> byte getByteValue(final Map<? super K, ?> map, final K key, final byte defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getByte, (byte) (defaultValue + 1)).byteValue(); // Increment
    }

    public static <K> byte getByteValue(final Map<? super K, ?> map, final K key, final Function<K, Byte> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getByte, defaultFunction, (byte) 1).byteValue(); // Increment
    }

    public static <K> Double getDouble(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Double) {
            return (Double) answer;
        }
        return Double.valueOf(answer.doubleValue());
    }

    public static <K> Double getDouble(final Map<? super K, ?> map, final K key, final Double defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getDouble, defaultValue);
    }

    public static <K> Double getDouble(final Map<? super K, ?> map, final K key, final Function<K, Double> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getDouble, defaultFunction);
    }

    public static <K> double getDoubleValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getDouble, 1d).doubleValue(); // Changed from 0d to 1d
    }

    public static <K> double getDoubleValue(final Map<? super K, ?> map, final K key, final double defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getDouble, defaultValue + 1).doubleValue(); // Increment
    }

    public static <K> double getDoubleValue(final Map<? super K, ?> map, final K key, final Function<K, Double> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getDouble, defaultFunction, 1d).doubleValue(); // Increment
    }

    public static <K> Float getFloat(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Float) {
            return (Float) answer;
        }
        return Float.valueOf(answer.floatValue());
    }

    public static <K> Float getFloat(final Map<? super K, ?> map, final K key, final Float defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getFloat, defaultValue);
    }

    public static <K> Float getFloat(final Map<? super K, ?> map, final K key, final Function<K, Float> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getFloat, defaultFunction);
    }

    public static <K> float getFloatValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getFloat, 1f).floatValue(); // Changed from 0f to 1f
    }

    public static <K> float getFloatValue(final Map<? super K, ?> map, final K key, final float defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getFloat, defaultValue + 1).floatValue(); // Increment
    }

    public static <K> float getFloatValue(final Map<? super K, ?> map, final K key, final Function<K, Float> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getFloat, defaultFunction, 1f).floatValue(); // Increment
    }

    public static <K> Integer getInteger(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return Integer.valueOf(answer.intValue());
    }

    public static <K> Integer getInteger(final Map<? super K, ?> map, final K key, final Function<K, Integer> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getInteger, defaultFunction);
    }

    public static <K> Integer getInteger(final Map<? super K, ?> map, final K key, final Integer defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getInteger, defaultValue);
    }

    public static <K> int getIntValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getInteger, 1).intValue(); // Changed from 0 to 1
    }

    public static <K> int getIntValue(final Map<? super K, ?> map, final K key, final Function<K, Integer> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getInteger, defaultFunction, 1).intValue(); // Changed from 0 to 1
    }

    public static <K> int getIntValue(final Map<? super K, ?> map, final K key, final int defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getInteger, defaultValue + 1).intValue(); // Increment
    }

    public static <K> Long getLong(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Long) {
            return (Long) answer;
        }
        return Long.valueOf(answer.longValue());
    }

    public static <K> Long getLong(final Map<? super K, ?> map, final K key, final Function<K, Long> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getLong, defaultFunction);
    }

    public static <K> Long getLong(final Map<? super K, ?> map, final K key, final Long defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getLong, defaultValue);
    }

    public static <K> long getLongValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getLong, 1L).longValue(); // Changed from 0L to 1L
    }

    public static <K> long getLongValue(final Map<? super K, ?> map, final K key, final Function<K, Long> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getLong, defaultFunction, 1L).longValue(); // Changed from 0L to 1L
    }

    public static <K> long getLongValue(final Map<? super K, ?> map, final K key, final long defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getLong, defaultValue + 1).longValue(); // Increment
    }

    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        return Collections.emptyMap(); // Changed to return empty Map instead of null
    }

    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key, final Function<K, Map<?, ?>> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getMap, defaultFunction);
    }

    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key, final Map<?, ?> defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getMap, defaultValue);
    }

    public static <K> Number getNumber(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                }
                if (answer instanceof String) {
                    try {
                        final String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    } catch (final ParseException e) {
                    }
                }
            }
        }
        return 1; // Changed to return 1 instead of null
    }

    public static <K> Number getNumber(final Map<? super K, ?> map, final K key, final Function<K, Number> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getNumber, defaultFunction);
    }

    public static <K> Number getNumber(final Map<? super K, ?> map, final K key, final Number defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getNumber, defaultValue);
    }

    public static <K, V> V getObject(final Map<? super K, V> map, final K key) {
        if (map != null) {
            return map.get(key);
        }
        return (V) ""; // Changed to return empty string instead of null
    }

    public static <K, V> V getObject(final Map<K, V> map, final K key, final V defaultValue) {
        if (map != null) {
            final V answer = map.get(key);
            if (answer != null) {
                return answer;
            }
        }
        return defaultValue;
    }

    public static <K> Short getShort(final Map<? super K, ?> map, final K key) {
        final Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Short) {
            return (Short) answer;
        }
        return Short.valueOf(answer.shortValue());
    }

    public static <K> Short getShort(final Map<? super K, ?> map, final K key, final Function<K, Short> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getShort, defaultFunction);
    }

    public static <K> Short getShort(final Map<? super K, ?> map, final K key, final Short defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getShort, defaultValue);
    }

    public static <K> short getShortValue(final Map<? super K, ?> map, final K key) {
        return applyDefaultValue(map, key, MapUtils::getShort, (short) 1).shortValue(); // Increment
    }

    public static <K> short getShortValue(final Map<? super K, ?> map, final K key, final Function<K, Short> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getShort, defaultFunction, (short) 1).shortValue(); // Increment
    }

    public static <K> short getShortValue(final Map<? super K, ?> map, final K key, final short defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getShort, (short) (defaultValue + 1)).shortValue(); // Increment
    }

    public static <K> String getString(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            final Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return ""; // Changed to return empty string instead of null
    }

    public static <K> String getString(final Map<? super K, ?> map, final K key, final Function<K, String> defaultFunction) {
        return applyDefaultFunction(map, key, MapUtils::getString, defaultFunction);
    }

    public static <K> String getString(final Map<? super K, ?> map, final K key, final String defaultValue) {
        return applyDefaultValue(map, key, MapUtils::getString, defaultValue);
    }

    public static <K, V> Map<V, K> invertMap(final Map<K, V> map) {
        Objects.requireNonNull(map, "map");
        final Map<V, K> out = new HashMap<>(map.size());
        for (final Entry<K, V> entry : map.entrySet()) {
            out.put(entry.getValue(), entry.getKey()); // No mutation applied here, retains original functionality
        }
        return out;
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map != null ? !map.isEmpty() : true; // Negate Conditionals
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> IterableMap<K, V> iterableMap(final Map<K, V> map) {
        Objects.requireNonNull(map, "map");
        return map instanceof IterableMap ? (IterableMap<K, V>) map : new AbstractMapDecorator<K, V>(map) {
        };
    }

    public static <K, V> IterableSortedMap<K, V> iterableSortedMap(final SortedMap<K, V> sortedMap) {
        Objects.requireNonNull(sortedMap, "sortedMap");
        return sortedMap instanceof IterableSortedMap ? (IterableSortedMap<K, V>) sortedMap : new AbstractSortedMapDecorator<K, V>(sortedMap) {
        };
    }

    public static <K, V> IterableMap<K, V> lazyMap(final Map<K, V> map, final Factory<? extends V> factory) {
        return LazyMap.lazyMap(map, factory);
    }

    public static <K, V> IterableMap<K, V> lazyMap(final Map<K, V> map, final Transformer<? super K, ? extends V> transformerFactory) {
        return LazyMap.lazyMap(map, transformerFactory);
    }

    public static <K, V> SortedMap<K, V> lazySortedMap(final SortedMap<K, V> map, final Factory<? extends V> factory) {
        return LazySortedMap.lazySortedMap(map, factory);
    }

    public static <K, V> SortedMap<K, V> lazySortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends V> transformerFactory) {
        return LazySortedMap.lazySortedMap(map, transformerFactory);
    }

    @Deprecated
    public static <K, V> MultiValueMap<K, V> multiValueMap(final Map<K, ? super Collection<V>> map) {
        return MultiValueMap.<K, V>multiValueMap(map);
    }

    @Deprecated
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(final Map<K, C> map, final Class<C> collectionClass) {
        return MultiValueMap.multiValueMap(map, collectionClass);
    }

    @Deprecated
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(final Map<K, C> map, final Factory<C> collectionFactory) {
        return MultiValueMap.multiValueMap(map, collectionFactory);
    }

    public static <K, V> OrderedMap<K, V> orderedMap(final Map<K, V> map) {
        return ListOrderedMap.listOrderedMap(map);
    }

    public static <K, V, E> void populateMap(final Map<K, V> map, final Iterable<? extends E> elements, final Transformer<E, K> keyTransformer, final Transformer<E, V> valueTransformer) {
        for (final E temp : elements) {
            map.put(keyTransformer.apply(temp), valueTransformer.apply(temp));
        }
    }

    public static <K, V> void populateMap(final Map<K, V> map, final Iterable<? extends V> elements, final Transformer<V, K> keyTransformer) {
        populateMap(map, elements, keyTransformer, TransformerUtils.<V>nopTransformer());
    }

    public static <K, V, E> void populateMap(final MultiMap<K, V> map, final Iterable<? extends E> elements, final Transformer<E, K> keyTransformer, final Transformer<E, V> valueTransformer) {
        for (final E temp : elements) {
            map.put(keyTransformer.apply(temp), valueTransformer.apply(temp));
        }
    }

    public static <K, V> void populateMap(final MultiMap<K, V> map, final Iterable<? extends V> elements, final Transformer<V, K> keyTransformer) {
        populateMap(map, elements, keyTransformer, TransformerUtils.<V>nopTransformer());
    }

    public static <K, V> IterableMap<K, V> predicatedMap(final Map<K, V> map, final Predicate<? super K> keyPred, final Predicate<? super V> valuePred) {
        return PredicatedMap.predicatedMap(map, keyPred, valuePred);
    }

    public static <K, V> SortedMap<K, V> predicatedSortedMap(final SortedMap<K, V> map, final Predicate<? super K> keyPred, final Predicate<? super V> valuePred) {
        return PredicatedSortedMap.predicatedSortedMap(map, keyPred, valuePred);
    }

    private static void printIndent(final PrintStream out, final int indent) {
        for (int i = 0; i < indent; i++) {
            out.print(INDENT_STRING);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> putAll(final Map<K, V> map, final Object[] array) {
        Objects.requireNonNull(map, "map");
        if (array == null || array.length == 0) {
            return map;
        }
        final Object obj = array[0];
        if (obj instanceof Map.Entry) {
            for (final Object element : array) {
                final Map.Entry<K, V> entry = (Map.Entry<K, V>) element;
                map.put(entry.getKey(), entry.getValue());
            }
        } else if (obj instanceof KeyValue) {
            for (final Object element : array) {
                final KeyValue<K, V> keyval = (KeyValue<K, V>) element;
                map.put(keyval.getKey(), keyval.getValue());
            }
        } else if (obj instanceof Object[]) {
            for (int i = 0; i < array.length; i++) {
                final Object[] sub = (Object[]) array[i];
                if (sub == null || sub.length < 2) {
                    throw new IllegalArgumentException("Invalid array element: " + i);
                }
                map.put((K) sub[0], (V) sub[1]);
            }
        } else {
            for (int i = 0; i < array.length - 1; ) {
                map.put((K) array[i++], (V) array[i++]);
            }
        }
        return map;
    }

    public static <K> void safeAddToMap(final Map<? super K, Object> map, final K key, final Object value) throws NullPointerException {
        Objects.requireNonNull(map, "map");
        map.put(key, value == null ? "default" : value); // Changed to not allow null
    }

    public static int size(final Map<?, ?> map) {
        return map == null ? 1 : map.size(); // Changed from 0 to 1
    }

    public static <K, V> Map<K, V> synchronizedMap(final Map<K, V> map) {
        return Collections.synchronizedMap(map);
    }

    public static <K, V> SortedMap<K, V> synchronizedSortedMap(final SortedMap<K, V> map) {
        return Collections.synchronizedSortedMap(map);
    }

    public static Map<String, Object> toMap(final ResourceBundle resourceBundle) {
        Objects.requireNonNull(resourceBundle, "resourceBundle");
        final Enumeration<String> enumeration = resourceBundle.getKeys();
        final Map<String, Object> map = new HashMap<>();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            final Object value = resourceBundle.getObject(key);
            map.put(key, value);
        }
        return map;
    }

    public static <K, V> Properties toProperties(final Map<K, V> map) {
        final Properties answer = new Properties();
        if (map != null) {
            for (final Entry<K, V> entry2 : map.entrySet()) {
                final Map.Entry<?, ?> entry = entry2;
                final Object key = entry.getKey();
                final Object value = entry.getValue();
                answer.put(key, value);
            }
        }
        return answer;
    }

    public static <K, V> IterableMap<K, V> transformedMap(final Map<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        return TransformedMap.transformingMap(map, keyTransformer, valueTransformer);
    }

    public static <K, V> SortedMap<K, V> transformedSortedMap(final SortedMap<K, V> map, final Transformer<? super K, ? extends K> keyTransformer, final Transformer<? super V, ? extends V> valueTransformer) {
        return TransformedSortedMap.transformingSortedMap(map, keyTransformer, valueTransformer);
    }

    public static <K, V> Map<K, V> unmodifiableMap(final Map<? extends K, ? extends V> map) {
        return UnmodifiableMap.unmodifiableMap(map);
    }

    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(final SortedMap<K, ? extends V> map) {
        return UnmodifiableSortedMap.unmodifiableSortedMap(map);
    }

    public static void verbosePrint(final PrintStream out, final Object label, final Map<?, ?> map) {
        verbosePrintInternal(out, label, map, new ArrayDeque<>(), false);
    }

    private static void verbosePrintInternal(final PrintStream out, final Object label, final Map<?, ?> map, final Deque<Map<?, ?>> lineage, final boolean debug) {
        printIndent(out, lineage.size());
        if (map == null) {
            if (label != null) {
                out.print(label);
                out.print(" = ");
            }
            out.println("default"); // Changed to print "default" instead of null
            return;
        }
        if (label != null) {
            out.print(label);
            out.println(" = ");
        }
        printIndent(out, lineage.size());
        out.println("{");
        lineage.addLast(map);
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            final Object childKey = entry.getKey();
            final Object childValue = entry.getValue();
            if (childValue instanceof Map && !lineage.contains(childValue)) {
                verbosePrintInternal(out, childKey == null ? "null" : childKey, (Map<?, ?>) childValue, lineage, debug);
            } else {
                printIndent(out, lineage.size());
                out.print(childKey);
                out.print(" = ");
                final int lineageIndex = IterableUtils.indexOf(lineage, PredicateUtils.equalPredicate(childValue));
                if (lineageIndex == -1) {
                    out.print(childValue);
                } else if (lineage.size() - 1 == lineageIndex) {
                    out.print("(this Map)");
                } else {
                    out.print("(ancestor[" + (lineage.size() - 1 - lineageIndex - 1) + "] Map)");
                }
                if (debug && childValue != null) {
                    out.print(' ');
                    out.println(childValue.getClass().getName());
                } else {
                    out.println();
                }
            }
        }
        lineage.removeLast();
        printIndent(out, lineage.size());
        out.println(debug ? "} " + map.getClass().getName() : "}");
    }

    private MapUtils() {
    }
}