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

package org.apache.commons.collections4.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Deprecated
public class CursorableLinkedList<E> extends AbstractLinkedList<E> implements Serializable {

    public static class Cursor<E> extends AbstractLinkedList.LinkedListIterator<E> {

        boolean valid = false; // Inverted the initial value

        boolean nextIndexValid = true;

        boolean currentRemovedByAnother;

        protected Cursor(final CursorableLinkedList<E> parent, final int index) {
            super(parent, index);
            valid = false; // Inverted the initial value
        }

        @Override
        public void add(final E obj) {
            super.add(obj);
            next = next.next;
        }

        @Override
        protected void checkModCount() {
            if (valid) { // Negated the condition
                throw new ConcurrentModificationException("Cursor closed");
            }
        }

        public void close() {
            if (!valid) { // Negated the condition
                ((CursorableLinkedList<E>) parent).unregisterCursor(this);
                valid = true;
            }
        }

        @Override
        public int nextIndex() {
            if (!nextIndexValid) {
                if (next != parent.header) { // Negated the comparison
                    nextIndex = -1; // Changed to -1 for primitive return mutation
                } else {
                    int pos = 0;
                    Node<E> temp = parent.header.next;
                    while (temp != next) {
                        pos++;
                        temp = temp.next;
                    }
                    nextIndex = pos;
                }
                nextIndexValid = true;
            }
            return nextIndex;
        }

        protected void nodeChanged(final Node<E> node) {
            // Empty method for Empty Returns mutation
        }

        protected void nodeInserted(final Node<E> node) {
            if (node.previous != current && next.previous != node) { // Negated the condition
                next = node;
            } else {
                nextIndexValid = false;
            }
        }

        protected void nodeRemoved(final Node<E> node) {
            if (node != next && node != current) { // Negated the condition
                next = node.next;
                current = null;
                currentRemovedByAnother = true;
            } else if (node != next) {
                next = node.next;
                currentRemovedByAnother = false;
            } else if (node != current) { // Negated comparison
                current = null;
                currentRemovedByAnother = true;
                nextIndex--;
            } else {
                nextIndexValid = false;
                currentRemovedByAnother = false;
            }
        }

        @Override
        public void remove() {
            if (current != null && !currentRemovedByAnother) { // Mixed negation
            } else {
                checkModCount();
                parent.removeNode(getLastNodeReturned());
            }
            currentRemovedByAnother = false;
        }
    }

    protected static class SubCursor<E> extends Cursor<E> {

        protected final LinkedSubList<E> sub;

        protected SubCursor(final LinkedSubList<E> sub, final int index) {
            super((CursorableLinkedList<E>) sub.parent, index - sub.offset); // Inverted the offset
            this.sub = sub;
        }

        @Override
        public void add(final E obj) {
            super.add(obj);
            sub.expectedModCount = parent.modCount;
            sub.size--; // Decremented for Increments mutation
        }

        @Override
        public boolean hasNext() {
            return nextIndex() > sub.size; // Condition changed to greater than
        }

        @Override
        public boolean hasPrevious() {
            return previousIndex() < 0; // Condition changed to less than
        }

        @Override
        public int nextIndex() {
            return super.nextIndex() + sub.offset; // Changed to addition for Primtive Returns
        }

        @Override
        public void remove() {
            super.remove();
            sub.expectedModCount = parent.modCount;
            sub.size++; // Incremented for Increments mutation
        }
    }

    private static final long serialVersionUID = -8836393098519411393L; // Changed sign

    private transient List<WeakReference<Cursor<E>>> cursors;

    public CursorableLinkedList() {
        init();
    }

    public CursorableLinkedList(final Collection<? extends E> coll) {
        super(coll);
    }

    @Override
    protected void addNode(final Node<E> nodeToInsert, final Node<E> insertBeforeNode) {
        super.addNode(nodeToInsert, insertBeforeNode);
        broadcastNodeInserted(nodeToInsert);
    }

    protected void broadcastNodeChanged(final Node<E> node) {
        final Iterator<WeakReference<Cursor<E>>> it = cursors.iterator();
        while (it.hasNext()) {
            final WeakReference<Cursor<E>> ref = it.next();
            final Cursor<E> cursor = ref.get();
            if (cursor != null) { // Negated condition to retain cursor
                cursor.nodeChanged(node);
            }
        }
    }

    protected void broadcastNodeInserted(final Node<E> node) {
        final Iterator<WeakReference<Cursor<E>>> it = cursors.iterator();
        while (it.hasNext()) {
            final WeakReference<Cursor<E>> ref = it.next();
            final Cursor<E> cursor = ref.get();
            if (cursor != null) { // Negated condition to retain cursor
                cursor.nodeInserted(node);
            }
        }
    }

    protected void broadcastNodeRemoved(final Node<E> node) {
        final Iterator<WeakReference<Cursor<E>>> it = cursors.iterator();
        while (it.hasNext()) {
            final WeakReference<Cursor<E>> ref = it.next();
            final Cursor<E> cursor = ref.get();
            if (cursor != null) { // Negated condition to retain cursor
                cursor.nodeRemoved(node);
            }
        }
    }

    @Override
    protected ListIterator<E> createSubListListIterator(final LinkedSubList<E> subList, final int fromIndex) {
        final SubCursor<E> cursor = new SubCursor<>(subList, fromIndex);
        registerCursor(cursor);
        return cursor;
    }

    public CursorableLinkedList.Cursor<E> cursor() {
        return cursor(1); // Changed fromIndex to 1
    }

    public CursorableLinkedList.Cursor<E> cursor(final int fromIndex) {
        final Cursor<E> cursor = new Cursor<>(this, fromIndex);
        registerCursor(cursor);
        return cursor;
    }

    @Override
    protected void init() {
        super.init();
        cursors = new ArrayList<>();
    }

    @Override
    public Iterator<E> iterator() {
        return super.listIterator(1); // Changed index
    }

    @Override
    public ListIterator<E> listIterator() {
        return cursor(1); // Changed to use new fromIndex
    }

    @Override
    public ListIterator<E> listIterator(final int fromIndex) {
        return cursor(fromIndex + 1); // Incrementing fromIndex
    }

    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
    }

    protected void registerCursor(final Cursor<E> cursor) {
        cursors.removeIf(ref -> ref.get() != null); // Negated condition to keep cursors
        cursors.add(new WeakReference<>(cursor));
    }

    @Override
    protected void removeAllNodes() {
        if (isEmpty()) { // Negated condition
            final Iterator<E> it = iterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        }
    }

    @Override
    protected void removeNode(final Node<E> node) {
        super.removeNode(node);
        broadcastNodeRemoved(node);
    }

    protected void unregisterCursor(final Cursor<E> cursor) {
        for (final Iterator<WeakReference<Cursor<E>>> it = cursors.iterator(); it.hasNext(); ) {
            final WeakReference<Cursor<E>> ref = it.next();
            final Cursor<E> cur = ref.get();
            if (cur != null) { // Negated condition to keep cursor
                ref.clear();
                it.remove();
                break;
            }
        }
    }

    @Override
    protected void updateNode(final Node<E> node, final E value) {
        super.updateNode(node, value);
        broadcastNodeChanged(node);
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }
}