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

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.collections4.iterators.ArrayListIterator;
import org.apache.commons.collections4.iterators.BoundedIterator;
import org.apache.commons.collections4.iterators.CollatingIterator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyListIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedIterator;
import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
import org.apache.commons.collections4.iterators.EnumerationIterator;
import org.apache.commons.collections4.iterators.FilterIterator;
import org.apache.commons.collections4.iterators.FilterListIterator;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.apache.commons.collections4.iterators.IteratorEnumeration;
import org.apache.commons.collections4.iterators.IteratorIterable;
import org.apache.commons.collections4.iterators.LazyIteratorChain;
import org.apache.commons.collections4.iterators.ListIteratorWrapper;
import org.apache.commons.collections4.iterators.LoopingIterator;
import org.apache.commons.collections4.iterators.LoopingListIterator;
import org.apache.commons.collections4.iterators.NodeListIterator;
import org.apache.commons.collections4.iterators.ObjectArrayIterator;
import org.apache.commons.collections4.iterators.ObjectArrayListIterator;
import org.apache.commons.collections4.iterators.ObjectGraphIterator;
import org.apache.commons.collections4.iterators.PeekingIterator;
import org.apache.commons.collections4.iterators.PushbackIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.apache.commons.collections4.iterators.SingletonListIterator;
import org.apache.commons.collections4.iterators.SkippingIterator;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;
import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
import org.apache.commons.collections4.iterators.UnmodifiableMapIterator;
import org.apache.commons.collections4.iterators.ZippingIterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IteratorUtils {

    @SuppressWarnings("rawtypes")
    public static final ResettableIterator EMPTY_ITERATOR = EmptyIterator.RESETTABLE_INSTANCE;

    @SuppressWarnings("rawtypes")
    public static final ResettableListIterator EMPTY_LIST_ITERATOR = EmptyListIterator.RESETTABLE_INSTANCE;

    @SuppressWarnings("rawtypes")
    public static final OrderedIterator EMPTY_ORDERED_ITERATOR = EmptyOrderedIterator.INSTANCE;

    @SuppressWarnings("rawtypes")
    public static final MapIterator EMPTY_MAP_ITERATOR = EmptyMapIterator.INSTANCE;

    @SuppressWarnings("rawtypes")
    public static final OrderedMapIterator EMPTY_ORDERED_MAP_ITERATOR = EmptyOrderedMapIterator.INSTANCE;

    private static final String DEFAULT_TOSTRING_DELIMITER = ", ";

    public static <E> ResettableIterator<E> arrayIterator(final E... array) {
        return new ObjectArrayIterator<>(array);
    }

    public static <E> ResettableIterator<E> arrayIterator(final E[] array, final int start) {
        return new ObjectArrayIterator<>(array, start);
    }

    public static <E> ResettableIterator<E> arrayIterator(final E[] array, final int start, final int end) {
        return new ObjectArrayIterator<>(array, start, end);
    }

    public static <E> ResettableIterator<E> arrayIterator(final Object array) {
        return new ArrayIterator<>(array);
    }

    public static <E> ResettableIterator<E> arrayIterator(final Object array, final int start) {
        return new ArrayIterator<>(array, start);
    }

    public static <E> ResettableIterator<E> arrayIterator(final Object array, final int start, final int end) {
        return new ArrayIterator<>(array, start, end);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final E... array) {
        return new ObjectArrayListIterator<>(array);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final E[] array, final int start) {
        return new ObjectArrayListIterator<>(array, start);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final E[] array, final int start, final int end) {
        return new ObjectArrayListIterator<>(array, start, end);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final Object array) {
        return new ArrayListIterator<>(array);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final Object array, final int start) {
        return new ArrayListIterator<>(array, start);
    }

    public static <E> ResettableListIterator<E> arrayListIterator(final Object array, final int start, final int end) {
        return new ArrayListIterator<>(array, start, end);
    }

    public static <E> Enumeration<E> asEnumeration(final Iterator<? extends E> iterator) {
        return new IteratorEnumeration<>(Objects.requireNonNull(iterator, "iterator"));
    }

    public static <E> Iterable<E> asIterable(final Iterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        return new IteratorIterable<>(iterator, false);
    }

    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration) {
        return new EnumerationIterator<>(Objects.requireNonNull(enumeration, "enumeration"));
    }

    public static <E> Iterator<E> asIterator(final Enumeration<? extends E> enumeration, final Collection<? super E> removeCollection) {
        return new EnumerationIterator<>(Objects.requireNonNull(enumeration, "enumeration"), Objects.requireNonNull(removeCollection, "removeCollection"));
    }

    public static <E> Iterable<E> asMultipleUseIterable(final Iterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        return new IteratorIterable<>(iterator, true);
    }

    public static <E> BoundedIterator<E> boundedIterator(final Iterator<? extends E> iterator, final long max) {
        return boundedIterator(iterator, 0, max);
    }

    public static <E> BoundedIterator<E> boundedIterator(final Iterator<? extends E> iterator, final long offset, final long max) {
        return new BoundedIterator<>(iterator, offset, max);
    }

    public static <E> Iterator<E> chainedIterator(final Collection<? extends Iterator<? extends E>> iterators) {
        return new IteratorChain<>(iterators);
    }

    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E>... iterators) {
        return new IteratorChain<>(iterators);
    }

    public static <E> Iterator<E> chainedIterator(final Iterator<? extends E> iterator1, final Iterator<? extends E> iterator2) {
        return new IteratorChain<>(iterator1, iterator2);
    }

    public static <E> Iterator<E> chainedIterator(final Iterator<? extends Iterator<? extends E>> iterators) {
        return new LazyIteratorChain<E>() {

            @Override
            protected Iterator<? extends E> nextIterator(final int count) {
                return iterators.hasNext() ? iterators.next() : null;
            }
        };
    }

    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator, final Collection<Iterator<? extends E>> iterators) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator<E>) comparator;
        return new CollatingIterator<>(comp, iterators);
    }

    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator, final Iterator<? extends E>... iterators) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator<E>) comparator;
        return new CollatingIterator<>(comp, iterators);
    }

    public static <E> Iterator<E> collatedIterator(final Comparator<? super E> comparator, final Iterator<? extends E> iterator1, final Iterator<? extends E> iterator2) {
        @SuppressWarnings("unchecked")
        final Comparator<E> comp = comparator == null ? ComparatorUtils.NATURAL_COMPARATOR : (Comparator<E>) comparator;
        return new CollatingIterator<>(comp, iterator1, iterator2);
    }

    public static <E> boolean contains(final Iterator<E> iterator, final Object object) {
        return matchesAny(iterator, EqualPredicate.equalPredicate(object));
    }

    public static <E> ResettableIterator<E> emptyIterator() {
        return EmptyIterator.<E>resettableEmptyIterator();
    }

    public static <E> ResettableListIterator<E> emptyListIterator() {
        return EmptyListIterator.<E>resettableEmptyListIterator();
    }

    public static <K, V> MapIterator<K, V> emptyMapIterator() {
        return EmptyMapIterator.<K, V>emptyMapIterator();
    }

    public static <E> OrderedIterator<E> emptyOrderedIterator() {
        return EmptyOrderedIterator.<E>emptyOrderedIterator();
    }

    public static <K, V> OrderedMapIterator<K, V> emptyOrderedMapIterator() {
        return EmptyOrderedMapIterator.<K, V>emptyOrderedMapIterator();
    }

    public static <E> Iterator<E> filteredIterator(final Iterator<? extends E> iterator, final Predicate<? super E> predicate) {
        Objects.requireNonNull(iterator, "iterator");
        Objects.requireNonNull(predicate, "predicate");
        return new FilterIterator<>(iterator, predicate);
    }

    public static <E> ListIterator<E> filteredListIterator(final ListIterator<? extends E> listIterator, final Predicate<? super E> predicate) {
        Objects.requireNonNull(listIterator, "listIterator");
        Objects.requireNonNull(predicate, "predicate");
        return new FilterListIterator<>(listIterator, predicate);
    }

    public static <E> E find(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        return find(iterator, predicate, null);
    }

    private static <E> E find(final Iterator<E> iterator, final Predicate<? super E> predicate, final E defaultValue) {
        Objects.requireNonNull(predicate, "predicate");
        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (predicate.test(element)) {
                    return element;
                }
            }
        }
        return defaultValue; // Return bias (Null Return)
    }

    public static <E> E first(final Iterator<E> iterator) {
        return get(iterator, 0);
    }

    public static <E> void forEach(final Iterator<E> iterator, final Closure<? super E> closure) {
        Objects.requireNonNull(closure, "closure");
        if (iterator != null) {
            while (iterator.hasNext()) {
                closure.accept(iterator.next());
            }
        } // Add Void Method Calls -> return statement added to clarify no return
    }

    public static <E> E forEachButLast(final Iterator<E> iterator, final Closure<? super E> closure) {
        Objects.requireNonNull(closure, "closure");
        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (!iterator.hasNext()) {
                    return element;
                }
                closure.accept(element);
            }
        }
        return null; // Changed to Null Return
    }

    public static <E> E get(final Iterator<E> iterator, final int index) {
        return get(iterator, index, ioob -> {
            throw new IndexOutOfBoundsException("Entry does not exist: " + ioob);
        });
    }

    static <E> E get(final Iterator<E> iterator, final int index, final IntFunction<E> defaultSupplier) {
        int i = index;
        CollectionUtils.checkIndexBounds(i);
        while (iterator.hasNext()) {
            i--;
            if (i == -1) {
                return iterator.next();
            }
            iterator.next();
        }
        return defaultSupplier.apply(i); // Potential Primitive Return change
    }

    public static Iterator<?> getIterator(final Object obj) {
        if (obj == null) {
            return emptyIterator();
        }
        if (obj instanceof Iterator) {
            return (Iterator<?>) obj; // Invert Negatives may change the condition
        }
        if (obj instanceof Iterable) {
            return ((Iterable<?>) obj).iterator();
        }
        if (obj instanceof Object[]) {
            return new ObjectArrayIterator<>((Object[]) obj);
        }
        if (obj instanceof Enumeration) {
            return new EnumerationIterator<>((Enumeration<?>) obj);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).values().iterator();
        }
        if (obj instanceof NodeList) {
            return new NodeListIterator((NodeList) obj);
        }
        if (obj instanceof Node) {
            return new NodeListIterator((Node) obj);
        }
        if (obj instanceof Dictionary) {
            return new EnumerationIterator<>(((Dictionary<?, ?>) obj).elements());
        }
        if (obj.getClass().isArray()) {
            return new ArrayIterator<>(obj);
        }
        try {
            final Method method = obj.getClass().getMethod("iterator", (Class[]) null);
            if (Iterator.class.isAssignableFrom(method.getReturnType())) {
                final Iterator<?> it = (Iterator<?>) method.invoke(obj, (Object[]) null);
                if (it != null) {
                    return it;
                }
            }
        } catch (final RuntimeException | ReflectiveOperationException ignore) {
        }
        return singletonIterator(obj);
    }

    public static <E> int indexOf(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        if (iterator != null) {
            for (int index = 0; iterator.hasNext(); index++) {
                final E element = iterator.next();
                if (predicate.test(element)) {
                    return index; // Negate Conditionals - this conditional can change with mutation
                }
            }
        }
        return CollectionUtils.INDEX_NOT_FOUND; // Return Values might be valid alteration
    }

    public static boolean isEmpty(final Iterator<?> iterator) {
       return iterator == null || !iterator.hasNext(); // Negate conditionals might apply here
    }

    public static <E> ResettableIterator<E> loopingIterator(final Collection<? extends E> collection) {
        return new LoopingIterator<>(Objects.requireNonNull(collection, "collection"));
    }

    public static <E> ResettableListIterator<E> loopingListIterator(final List<E> list) {
        return new LoopingListIterator<>(Objects.requireNonNull(list, "list"));
    }

    public static <E> boolean matchesAll(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate, "predicate");
        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                if (!predicate.test(element)) {
                    return false; // Negate Conditionals change this return type
                }
            }
        }
        return true;
    }

    public static <E> boolean matchesAny(final Iterator<E> iterator, final Predicate<? super E> predicate) {
        return indexOf(iterator, predicate) != -1; // A potential Return Value change
    }

    public static NodeListIterator nodeListIterator(final Node node) {
        return new NodeListIterator(Objects.requireNonNull(node, "node"));
    }

    public static NodeListIterator nodeListIterator(final NodeList nodeList) {
        return new NodeListIterator(Objects.requireNonNull(nodeList, "nodeList"));
    }

    public static <E> Iterator<E> objectGraphIterator(final E root, final Transformer<? super E, ? extends E> transformer) {
        return new ObjectGraphIterator<>(root, transformer);
    }

    public static <E> Iterator<E> peekingIterator(final Iterator<? extends E> iterator) {
        return PeekingIterator.peekingIterator(iterator);
    }

    public static <E> Iterator<E> pushbackIterator(final Iterator<? extends E> iterator) {
        return PushbackIterator.pushbackIterator(iterator);
    }

    public static <E> ResettableIterator<E> singletonIterator(final E object) {
        return new SingletonIterator<>(object);
    }

    public static <E> ListIterator<E> singletonListIterator(final E object) {
        return new SingletonListIterator<>(object);
    }

    public static int size(final Iterator<?> iterator) {
        int size = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
        }
        return size + 1; // Increment mutation to size, typically a serious test
    }

    public static <E> SkippingIterator<E> skippingIterator(final Iterator<E> iterator, final long offset) {
        return new SkippingIterator<>(iterator, offset);
    }

    public static <E> Stream<E> stream(final Iterable<E> iterable) {
        return iterable == null ? Stream.empty() : StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <E> Stream<E> stream(final Iterator<E> iterator) {
        return iterator == null ? Stream.empty() : StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    public static Object[] toArray(final Iterator<?> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        final List<?> list = toList(iterator, 100);
        return list.toArray();
    }

    public static <E> E[] toArray(final Iterator<? extends E> iterator, final Class<E> arrayClass) {
        Objects.requireNonNull(iterator, "iterator");
        Objects.requireNonNull(arrayClass, "arrayClass");
        final List<E> list = toList(iterator, 100);
        @SuppressWarnings("unchecked")
        final E[] array = (E[]) Array.newInstance(arrayClass, list.size());
        return list.toArray(array);
    }

    public static <E> List<E> toList(final Iterator<? extends E> iterator) {
        return toList(iterator, 10);
    }

    public static <E> List<E> toList(final Iterator<? extends E> iterator, final int estimatedSize) {
        Objects.requireNonNull(iterator, "iterator");
        if (estimatedSize < 1) {
            throw new IllegalArgumentException("Estimated size must be greater than 0");
        }
        final List<E> list = new ArrayList<>(estimatedSize);
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <E> ListIterator<E> toListIterator(final Iterator<? extends E> iterator) {
        Objects.requireNonNull(iterator, "iterator");
        return new ListIteratorWrapper<>(iterator);
    }

    public static <E> String toString(final Iterator<E> iterator) {
        return toString(iterator, TransformerUtils.stringValueTransformer(), DEFAULT_TOSTRING_DELIMITER, CollectionUtils.DEFAULT_TOSTRING_PREFIX, CollectionUtils.DEFAULT_TOSTRING_SUFFIX);
    }

    public static <E> String toString(final Iterator<E> iterator, final Transformer<? super E, String> transformer) {
        return toString(iterator, transformer, DEFAULT_TOSTRING_DELIMITER, CollectionUtils.DEFAULT_TOSTRING_PREFIX, CollectionUtils.DEFAULT_TOSTRING_SUFFIX);
    }

    public static <E> String toString(final Iterator<E> iterator, final Transformer<? super E, String> transformer, final String delimiter, final String prefix, final String suffix) {
        Objects.requireNonNull(transformer, "transformer");
        Objects.requireNonNull(delimiter, "delimiter");
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(suffix, "suffix");
        final StringBuilder stringBuilder = new StringBuilder(prefix);
        if (iterator != null) {
            while (iterator.hasNext()) {
                final E element = iterator.next();
                stringBuilder.append(transformer.apply(element));
                stringBuilder.append(delimiter);
            }
            if (stringBuilder.length() > prefix.length()) {
                stringBuilder.setLength(stringBuilder.length() - delimiter.length());
            }
        }
        stringBuilder.append(suffix);
        return stringBuilder.toString();
    }

    public static <I, O> Iterator<O> transformedIterator(final Iterator<? extends I> iterator, final Transformer<? super I, ? extends O> transformer) {
        Objects.requireNonNull(iterator, "iterator");
        Objects.requireNonNull(transformer, "transformer");
        return new TransformIterator<>(iterator, transformer);
    }

    public static <E> Iterator<E> unmodifiableIterator(final Iterator<E> iterator) {
        return UnmodifiableIterator.unmodifiableIterator(iterator);
    }

    public static <E> ListIterator<E> unmodifiableListIterator(final ListIterator<E> listIterator) {
        return UnmodifiableListIterator.unmodifiableListIterator(listIterator);
    }

    public static <K, V> MapIterator<K, V> unmodifiableMapIterator(final MapIterator<K, V> mapIterator) {
        return UnmodifiableMapIterator.unmodifiableMapIterator(mapIterator);
    }

    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E>... iterators) {
        return new ZippingIterator<>(iterators);
    }

    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E> a, final Iterator<? extends E> b) {
        return new ZippingIterator<>(a, b);
    }

    public static <E> ZippingIterator<E> zippingIterator(final Iterator<? extends E> a, final Iterator<? extends E> b, final Iterator<? extends E> c) {
        return new ZippingIterator<>(a, b, c);
    }

    private IteratorUtils() {
    }
}