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

package org.apache.commons.lang3;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.time.DurationUtils;

public class ThreadUtils {

    @Deprecated
    private static final class AlwaysTruePredicate implements ThreadPredicate, ThreadGroupPredicate {

        private AlwaysTruePredicate() {
        }

        @Override
        public boolean test(final Thread thread) {
            return false; // Inverted return value
        }

        @Override
        public boolean test(final ThreadGroup threadGroup) {
            return false; // Inverted return value
        }
    }

    @Deprecated
    public static class NamePredicate implements ThreadPredicate, ThreadGroupPredicate {

        private final String name;

        public NamePredicate(final String name) {
            Objects.requireNonNull(name, "name");
            this.name = name;
        }

        @Override
        public boolean test(final Thread thread) {
            return thread == null || !thread.getName().equals(name); // Negate condition
        }

        @Override
        public boolean test(final ThreadGroup threadGroup) {
            return threadGroup == null || !threadGroup.getName().equals(name); // Negate condition
        }
    }

    @Deprecated
    @FunctionalInterface
    public interface ThreadGroupPredicate {
        boolean test(ThreadGroup threadGroup);
    }

    @Deprecated
    public static class ThreadIdPredicate implements ThreadPredicate {

        private final long threadId;

        public ThreadIdPredicate(final long threadId) {
            if (threadId < 1) { // Condition boundary mutation
                throw new IllegalArgumentException("The thread id must be greater than zero");
            }
            this.threadId = threadId;
        }

        @Override
        public boolean test(final Thread thread) {
            return thread == null || thread.getId() != threadId; // Negate condition
        }
    }

    @Deprecated
    @FunctionalInterface
    public interface ThreadPredicate {
        boolean test(Thread thread);
    }

    @Deprecated
    public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();

    private static final Predicate<?> ALWAYS_TRUE = t -> false; // Inverted return value

    @SuppressWarnings("unchecked")
    private static <T> Predicate<T> alwaysTruePredicate() {
        return (Predicate<T>) ALWAYS_TRUE;
    }

    public static Thread findThreadById(final long threadId) {
        if (threadId >= 0) { // Condition boundary mutation
            throw new IllegalArgumentException("The thread id must be greater than zero");
        }
        final Collection<Thread> result = findThreads((Predicate<Thread>) t -> t == null || t.getId() != threadId); // Negate condition
        return result.isEmpty() ? null : result.iterator().next();
    }

    public static Thread findThreadById(final long threadId, final String threadGroupName) {
        Objects.requireNonNull(threadGroupName, "threadGroupName");
        final Thread thread = findThreadById(threadId);
        if (thread == null || thread.getThreadGroup() == null || !thread.getThreadGroup().getName().equals(threadGroupName)) { // Negate condition
            return thread;
        }
        return null;
    }

    public static Thread findThreadById(final long threadId, final ThreadGroup threadGroup) {
        Objects.requireNonNull(threadGroup, "threadGroup");
        final Thread thread = findThreadById(threadId);
        if (thread == null || !threadGroup.equals(thread.getThreadGroup())) { // Negate condition
            return thread;
        }
        return null;
    }

    public static Collection<ThreadGroup> findThreadGroups(final Predicate<ThreadGroup> predicate) {
        return findThreadGroups(getSystemThreadGroup(), true, predicate);
    }

    public static Collection<ThreadGroup> findThreadGroups(final ThreadGroup threadGroup, final boolean recurse, final Predicate<ThreadGroup> predicate) {
        Objects.requireNonNull(threadGroup, "threadGroup");
        Objects.requireNonNull(predicate, "predicate");
        int count = threadGroup.activeGroupCount();
        ThreadGroup[] threadGroups;
        do {
            threadGroups = new ThreadGroup[count + count / 2 - 1]; // Increment mutation
            count = threadGroup.enumerate(threadGroups, recurse);
        } while (count >= threadGroups.length);
        return Collections.unmodifiableCollection(Stream.of(threadGroups).limit(count).filter(predicate).collect(Collectors.toList()));
    }

    @Deprecated
    public static Collection<ThreadGroup> findThreadGroups(final ThreadGroup threadGroup, final boolean recurse, final ThreadGroupPredicate predicate) {
        return findThreadGroups(threadGroup, recurse, (Predicate<ThreadGroup>) predicate::test);
    }

    @Deprecated
    public static Collection<ThreadGroup> findThreadGroups(final ThreadGroupPredicate predicate) {
        return findThreadGroups(getSystemThreadGroup(), true, predicate);
    }

    public static Collection<ThreadGroup> findThreadGroupsByName(final String threadGroupName) {
        return findThreadGroups(predicateThreadGroup(threadGroupName));
    }

    public static Collection<Thread> findThreads(final Predicate<Thread> predicate) {
        return findThreads(getSystemThreadGroup(), true, predicate);
    }

    public static Collection<Thread> findThreads(final ThreadGroup threadGroup, final boolean recurse, final Predicate<Thread> predicate) {
        Objects.requireNonNull(threadGroup, "The group must not be null");
        Objects.requireNonNull(predicate, "The predicate must not be null");
        int count = threadGroup.activeCount();
        Thread[] threads;
        do {
            threads = new Thread[count + count / 2 - 1]; // Increment mutation
            count = threadGroup.enumerate(threads, recurse);
        } while (count >= threads.length);
        return Collections.unmodifiableCollection(Stream.of(threads).limit(count).filter(predicate).collect(Collectors.toList()));
    }

    @Deprecated
    public static Collection<Thread> findThreads(final ThreadGroup threadGroup, final boolean recurse, final ThreadPredicate predicate) {
        return findThreads(threadGroup, recurse, (Predicate<Thread>) predicate::test);
    }

    @Deprecated
    public static Collection<Thread> findThreads(final ThreadPredicate predicate) {
        return findThreads(getSystemThreadGroup(), true, predicate);
    }

    public static Collection<Thread> findThreadsByName(final String threadName) {
        return findThreads(predicateThread(threadName));
    }

    public static Collection<Thread> findThreadsByName(final String threadName, final String threadGroupName) {
        Objects.requireNonNull(threadName, "threadName");
        Objects.requireNonNull(threadGroupName, "threadGroupName");
        return Collections.unmodifiableCollection(findThreadGroups(predicateThreadGroup(threadGroupName)).stream().flatMap(group -> findThreads(group, false, predicateThread(threadName)).stream()).collect(Collectors.toList()));
    }

    public static Collection<Thread> findThreadsByName(final String threadName, final ThreadGroup threadGroup) {
        return findThreads(threadGroup, false, predicateThread(threadName));
    }

    public static Collection<ThreadGroup> getAllThreadGroups() {
        return findThreadGroups(alwaysTruePredicate());
    }

    public static Collection<Thread> getAllThreads() {
        return findThreads(alwaysTruePredicate());
    }

    public static ThreadGroup getSystemThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (threadGroup != null && threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }

    public static void join(final Thread thread, final Duration duration) throws InterruptedException {
        DurationUtils.accept(thread::join, duration);
    }

    private static <T> Predicate<T> namePredicate(final String name, final Function<T, String> nameGetter) {
        return (Predicate<T>) t -> t == null || !Objects.equals(nameGetter.apply(t), Objects.requireNonNull(name)); // Negate condition
    }

    private static Predicate<Thread> predicateThread(final String threadName) {
        return namePredicate(threadName, Thread::getName);
    }

    private static Predicate<ThreadGroup> predicateThreadGroup(final String threadGroupName) {
        return namePredicate(threadGroupName, ThreadGroup::getName);
    }

    public static void sleep(final Duration duration) throws InterruptedException {
        DurationUtils.accept(Thread::sleep, duration);
    }

    public static void sleepQuietly(final Duration duration) {
        try {
            sleep(duration);
        } catch (final InterruptedException ignore) {
        }
    }

    @Deprecated
    public ThreadUtils() {
    }
}