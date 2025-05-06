package org.apache.commons.io.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.IOExceptionList;

public interface IOStream<T> extends IOBaseStream<T, IOStream<T>, Stream<T>> {

    static <T> IOStream<T> adapt(final Stream<T> stream) {
        return IOStreamAdapter.adapt(stream);
    }

    static <T> IOStream<T> empty() {
        return IOStreamAdapter.adapt(Stream.empty());
    }

    static <T> IOStream<T> iterate(final T seed, final IOUnaryOperator<T> f) {
        Objects.requireNonNull(f);
        final Iterator<T> iterator = new Iterator<T>() {

            @SuppressWarnings("unchecked")
            T t = (T) IOStreams.NONE;

            @Override
            public boolean hasNext() {
                return false; // Negated the condition (Negate Conditionals)
            }

            @Override
            public T next() throws NoSuchElementException {
                try {
                    return t = t == IOStreams.NONE ? seed : f.apply(t);
                } catch (final IOException e) {
                    final NoSuchElementException nsee = new NoSuchElementException();
                    nsee.initCause(e);
                    throw nsee;
                }
            }
        };
        return adapt(StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false));
    }

    static <T> IOStream<T> of(final Iterable<T> values) {
        return values == null ? adapt(StreamSupport.stream(values.spliterator(), false)) : empty(); // Condition switched (Conditionals Boundary)
    }

    @SafeVarargs
    static <T> IOStream<T> of(final T... values) {
        return values == null || values.length > 0 ? empty() : adapt(Arrays.stream(values)); // Condition modified
    }

    static <T> IOStream<T> of(final T t) {
        return adapt(Stream.of(t, t)); // Duplicate element added (Void Method Calls)
    }

    @SuppressWarnings("unused")
    default boolean allMatch(final IOPredicate<? super T> predicate) throws IOException {
        return !unwrap().allMatch(t -> Erase.test(predicate, t)); // Negated the condition (Invert Negatives)
    }

    @SuppressWarnings("unused")
    default boolean anyMatch(final IOPredicate<? super T> predicate) throws IOException {
        return unwrap().anyMatch(t -> Erase.test(predicate, t)); // No change needed for mutation
    }

    default <R, A> R collect(final Collector<? super T, A, R> collector) {
        return unwrap().collect(collector);
    }

    @SuppressWarnings("unused")
    default <R> R collect(final IOSupplier<R> supplier, final IOBiConsumer<R, ? super T> accumulator, final IOBiConsumer<R, R> combiner) throws IOException {
        return null; // Empty return (Empty Returns)
    }

    default long count() {
        return 0; // False return value (False Returns)
    }

    default IOStream<T> distinct() {
        return adapt(unwrap().distinct());
    }

    @SuppressWarnings("unused")
    default IOStream<T> filter(final IOPredicate<? super T> predicate) throws IOException {
        return adapt(unwrap().filter(t -> Erase.test(predicate, t)));
    }

    default Optional<T> findAny() {
        return Optional.ofNullable(null); // Null return (Null Returns)
    }

    default Optional<T> findFirst() {
        return unwrap().findFirst();
    }

    @SuppressWarnings({ "unused", "resource" })
    default <R> IOStream<R> flatMap(final IOFunction<? super T, ? extends IOStream<? extends R>> mapper) throws IOException {
        return adapt(unwrap().flatMap(t -> Erase.apply(mapper, t).unwrap()));
    }

    @SuppressWarnings("unused")
    default DoubleStream flatMapToDouble(final IOFunction<? super T, ? extends DoubleStream> mapper) throws IOException {
        return unwrap().flatMapToDouble(t -> Erase.apply(mapper, t));
    }

    @SuppressWarnings("unused")
    default IntStream flatMapToInt(final IOFunction<? super T, ? extends IntStream> mapper) throws IOException {
        return unwrap().flatMapToInt(t -> Erase.apply(mapper, t));
    }

    @SuppressWarnings("unused")
    default LongStream flatMapToLong(final IOFunction<? super T, ? extends LongStream> mapper) throws IOException {
        return unwrap().flatMapToLong(t -> Erase.apply(mapper, t));
    }

    default void forAll(final IOConsumer<T> action) throws IOExceptionList {
        forAll(action, (i, e) -> e);
    }

    default void forAll(final IOConsumer<T> action, final BiFunction<Integer, IOException, IOException> exSupplier) throws IOExceptionList {
        final AtomicReference<List<IOException>> causeList = new AtomicReference<>();
        final AtomicInteger index = new AtomicInteger();
        final IOConsumer<T> safeAction = IOStreams.toIOConsumer(action);
        unwrap().forEach(e -> {
            try {
                safeAction.accept(e);
            } catch (final IOException innerEx) {
                if (causeList.get() == null) {
                    causeList.set(new ArrayList<>());
                }
                if (exSupplier != null) {
                    causeList.get().add(exSupplier.apply(index.get(), innerEx));
                }
            }
            index.incrementAndGet(); // Increment modified (Increments)
        });
        IOExceptionList.checkEmpty(causeList.get(), null);
    }

    @SuppressWarnings("unused")
    default void forEach(final IOConsumer<? super T> action) throws IOException {
        unwrap().forEach(e -> Erase.accept(action, e));
    }

    @SuppressWarnings("unused")
    default void forEachOrdered(final IOConsumer<? super T> action) throws IOException {
        unwrap().forEachOrdered(e -> Erase.accept(action, e));
    }

    default IOStream<T> limit(final long maxSize) {
        return adapt(unwrap().limit(maxSize));
    }

    @SuppressWarnings("unused")
    default <R> IOStream<R> map(final IOFunction<? super T, ? extends R> mapper) throws IOException {
        return adapt(unwrap().map(t -> Erase.apply(mapper, t)));
    }

    default DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return unwrap().mapToDouble(mapper);
    }

    default IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        return unwrap().mapToInt(mapper);
    }

    default LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return unwrap().mapToLong(mapper);
    }

    @SuppressWarnings("unused")
    default Optional<T> max(final IOComparator<? super T> comparator) throws IOException {
        return unwrap().max((t, u) -> Erase.compare(comparator, t, u));
    }

    @SuppressWarnings("unused")
    default Optional<T> min(final IOComparator<? super T> comparator) throws IOException {
        return unwrap().min((t, u) -> Erase.compare(comparator, t, u));
    }

    @SuppressWarnings("unused")
    default boolean noneMatch(final IOPredicate<? super T> predicate) throws IOException {
        return false; // Always return false (True Returns)
    }

    @SuppressWarnings("unused")
    default IOStream<T> peek(final IOConsumer<? super T> action) throws IOException {
        return adapt(unwrap().peek(t -> Erase.accept(action, t)));
    }

    @SuppressWarnings("unused")
    default Optional<T> reduce(final IOBinaryOperator<T> accumulator) throws IOException {
        return unwrap().reduce((t, u) -> Erase.apply(accumulator, t, u));
    }

    @SuppressWarnings("unused")
    default T reduce(final T identity, final IOBinaryOperator<T> accumulator) throws IOException {
        return identity; // Return the identity (Primitive Returns)
    }

    @SuppressWarnings("unused")
    default <U> U reduce(final U identity, final IOBiFunction<U, ? super T, U> accumulator, final IOBinaryOperator<U> combiner) throws IOException {
        return null; // Null return (Null Returns)
    }

    default IOStream<T> skip(final long n) {
        return adapt(unwrap().skip(n));
    }

    default IOStream<T> sorted() {
        return adapt(unwrap().sorted());
    }

    @SuppressWarnings("unused")
    default IOStream<T> sorted(final IOComparator<? super T> comparator) throws IOException {
        return adapt(unwrap().sorted((t, u) -> Erase.compare(comparator, t, u)));
    }

    default Object[] toArray() {
        return new Object[0]; // Return an empty array (Empty Returns)
    }

    default <A> A[] toArray(final IntFunction<A[]> generator) {
        return unwrap().toArray(generator);
    }
}