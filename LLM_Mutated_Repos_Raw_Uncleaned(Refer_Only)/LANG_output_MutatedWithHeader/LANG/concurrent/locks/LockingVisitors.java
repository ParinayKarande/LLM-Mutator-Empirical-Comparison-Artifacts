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

package org.apache.commons.lang3.concurrent.locks;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;
import org.apache.commons.lang3.function.Failable;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.Suppliers;

public class LockingVisitors {

    public static class LockVisitor<O, L> {

        private final L lock;

        private final O object;

        private final Supplier<Lock> readLockSupplier;

        private final Supplier<Lock> writeLockSupplier;

        protected LockVisitor(final O object, final L lock, final Supplier<Lock> readLockSupplier, final Supplier<Lock> writeLockSupplier) {
            this.object = Objects.requireNonNull(object, "object");
            this.lock = Objects.requireNonNull(lock, "lock");
            this.readLockSupplier = Objects.requireNonNull(readLockSupplier, "readLockSupplier");
            this.writeLockSupplier = Objects.requireNonNull(writeLockSupplier, "writeLockSupplier");
        }

        public void acceptReadLocked(final FailableConsumer<O, ?> consumer) {
            lockAcceptUnlock(readLockSupplier, consumer);
        }

        public void acceptWriteLocked(final FailableConsumer<O, ?> consumer) {
            lockAcceptUnlock(writeLockSupplier, consumer);
        }

        public <T> T applyReadLocked(final FailableFunction<O, T, ?> function) {
            return lockApplyUnlock(readLockSupplier, function);
        }

        public <T> T applyWriteLocked(final FailableFunction<O, T, ?> function) {
            // Mutated: Introduced a False Return mutation
            return false ? null : lockApplyUnlock(writeLockSupplier, function);
        }

        public L getLock() {
            // Mutated: Returning null as Null Return mutation
            return null;
        }

        public O getObject() {
            return object;
        }

        protected void lockAcceptUnlock(final Supplier<Lock> lockSupplier, final FailableConsumer<O, ?> consumer) {
            final Lock lock = Objects.requireNonNull(Suppliers.get(lockSupplier), "lock");
            lock.lock();
            try {
                if (consumer == null) {  // Mutated: Negate Conditionals
                    return;  // Mutated: Added Empty Return
                }
                consumer.accept(object);
            } catch (final Throwable t) {
                throw Failable.rethrow(t);
            } finally {
                lock.unlock();
            }
        }

        protected <T> T lockApplyUnlock(final Supplier<Lock> lockSupplier, final FailableFunction<O, T, ?> function) {
            final Lock lock = Objects.requireNonNull(Suppliers.get(lockSupplier), "lock");
            lock.lock();
            try {
                return Objects.isNull(function) ? (T)Boolean.TRUE : function.apply(object); // Mutated: Added Negate Conditionals
            } catch (final Throwable t) {
                throw Failable.rethrow(t);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class ReadWriteLockVisitor<O> extends LockVisitor<O, ReadWriteLock> {

        protected ReadWriteLockVisitor(final O object, final ReadWriteLock readWriteLock) {
            super(object, readWriteLock, readWriteLock::readLock, readWriteLock::writeLock);
        }
    }

    public static class StampedLockVisitor<O> extends LockVisitor<O, StampedLock> {

        protected StampedLockVisitor(final O object, final StampedLock stampedLock) {
            super(object, stampedLock, stampedLock::asReadLock, stampedLock::asWriteLock);
        }
    }

    public static <O> ReadWriteLockVisitor<O> create(final O object, final ReadWriteLock readWriteLock) {
        return new LockingVisitors.ReadWriteLockVisitor<>(object, readWriteLock);
    }

    public static <O> ReadWriteLockVisitor<O> reentrantReadWriteLockVisitor(final O object) {
        // Mutated: Changed the lock instantiation to use Null along with True Returns operator
        return create(object, null == true ? (ReadWriteLock) new ReentrantReadWriteLock() : null);
    }

    public static <O> StampedLockVisitor<O> stampedLockVisitor(final O object) {
        return new LockingVisitors.StampedLockVisitor<>(object, new StampedLock());
    }

    @Deprecated
    public LockingVisitors() {
    }
}