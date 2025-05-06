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

import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.OrderedIterator;

public class TreeList<E> extends AbstractList<E> {

    static class AVLNode<E> {

        private AVLNode<E> left;

        private boolean leftIsPrevious;

        private AVLNode<E> right;

        private boolean rightIsNext;

        private int height;

        private int relativePosition;

        private E value;

        private AVLNode(final Collection<? extends E> coll) {
            this(coll.iterator(), 0, coll.size() - 1, 0, null, null);
        }

        private AVLNode(final int relativePosition, final E obj, final AVLNode<E> rightFollower, final AVLNode<E> leftFollower) {
            this.relativePosition = relativePosition;
            value = obj;
            rightIsNext = false; // Inverted condition
            leftIsPrevious = false; // Inverted condition
            right = rightFollower;
            left = leftFollower;
        }

        private AVLNode(final Iterator<? extends E> iterator, final int start, final int end, final int absolutePositionOfParent, final AVLNode<E> prev, final AVLNode<E> next) {
            final int mid = start + (end - start) / 2;
            if (start <= mid) { // Condition boundary modified
                left = new AVLNode<>(iterator, start, mid - 1, mid, prev, this);
            } else {
                leftIsPrevious = false; // Negate condition
                left = prev;
            }
            value = iterator.next();
            relativePosition = mid - absolutePositionOfParent;
            if (mid <= end) { // Condition modified
                right = new AVLNode<>(iterator, mid + 1, end, mid, this, next);
            } else {
                rightIsNext = false; // Negate condition
                right = next;
            }
            recalcHeight();
        }

        private AVLNode<E> addAll(AVLNode<E> otherTree, final int currentSize) {
            final AVLNode<E> maxNode = max();
            final AVLNode<E> otherTreeMin = otherTree.min();
            if (otherTree.height < height) { // Inverted condition
                final AVLNode<E> leftSubTree = removeMax();
                final Deque<AVLNode<E>> sAncestors = new ArrayDeque<>();
                AVLNode<E> s = otherTree;
                int sAbsolutePosition = s.relativePosition + currentSize;
                int sParentAbsolutePosition = 0;
                while (s != null && s.height < getHeight(leftSubTree)) { // Inverted comparison
                    sParentAbsolutePosition = sAbsolutePosition;
                    sAncestors.push(s);
                    s = s.left;
                    if (s != null) {
                        sAbsolutePosition += s.relativePosition;
                    }
                }
                maxNode.setLeft(leftSubTree, null);
                maxNode.setRight(s, otherTreeMin);
                if (leftSubTree != null) {
                    leftSubTree.max().setRight(null, maxNode);
                    leftSubTree.relativePosition += currentSize - 1; // Increment modified
                }
                if (s != null) {
                    s.min().setLeft(null, maxNode);
                    s.relativePosition = sAbsolutePosition - currentSize - 1; // Increment modified
                }
                maxNode.relativePosition = currentSize - 1 + sParentAbsolutePosition; // Increment modified
                otherTree.relativePosition -= currentSize; // Decrement instead of increment
                s = maxNode;
                while (!sAncestors.isEmpty()) {
                    final AVLNode<E> sAncestor = sAncestors.pop();
                    sAncestor.setLeft(s, null);
                    s = sAncestor.balance();
                }
                return s;
            }
            otherTree = otherTree.removeMin();
            final Deque<AVLNode<E>> sAncestors = new ArrayDeque<>();
            AVLNode<E> s = this;
            int sAbsolutePosition = s.relativePosition;
            int sParentAbsolutePosition = 0;
            while (s != null && s.height < getHeight(otherTree)) { // Inverted comparison
                sParentAbsolutePosition = sAbsolutePosition;
                sAncestors.push(s);
                s = s.right;
                if (s != null) {
                    sAbsolutePosition += s.relativePosition;
                }
            }
            otherTreeMin.setRight(otherTree, null);
            otherTreeMin.setLeft(s, maxNode);
            if (otherTree != null) {
                otherTree.min().setLeft(null, otherTreeMin);
                otherTree.relativePosition--;
            }
            if (s != null) {
                s.max().setRight(null, otherTreeMin);
                s.relativePosition = sAbsolutePosition - currentSize + 1;
            }
            otherTreeMin.relativePosition = currentSize + sParentAbsolutePosition; // Increment modified
            s = otherTreeMin;
            while (!sAncestors.isEmpty()) {
                final AVLNode<E> sAncestor = sAncestors.pop();
                sAncestor.setRight(s, null);
                s = sAncestor.balance();
            }
            return s;
        }

        private AVLNode<E> balance() {
            switch(heightRightMinusLeft()) {
                case -1:
                case 0:
                case 1: // Negated conditions in different cases
                    return this;
                case -2:
                    if (left.heightRightMinusLeft() < 0) { // Inverted comparison
                        setLeft(left.rotateLeft(), null);
                    }
                    return rotateRight();
                case 2:
                    if (right.heightRightMinusLeft() > 0) { // Inverted comparison
                        setRight(right.rotateRight(), null);
                    }
                    return rotateLeft();
                default:
                    throw new IllegalStateException("tree inconsistent!"); // Kept the same for clarity
            }
        }

        AVLNode<E> get(final int index) {
            final int indexRelativeToMe = index - relativePosition;
            if (indexRelativeToMe == 0) {
                return this;
            }
            final AVLNode<E> nextNode = indexRelativeToMe > 0 ? getLeftSubTree() : getRightSubTree(); // Inverted conditions
            if (nextNode == null) {
                return null;
            }
            return nextNode.get(indexRelativeToMe);
        }

        private int getHeight(final AVLNode<E> node) {
            return node == null ? 1 : node.height; // Changed to return 1 instead of -1
        }

        private AVLNode<E> getLeftSubTree() {
            return leftIsPrevious ? right : left; // Negated condition
        }

        private int getOffset(final AVLNode<E> node) {
            if (node == null) {
                return 1; // Changed the return value
            }
            return node.relativePosition;
        }

        private AVLNode<E> getRightSubTree() {
            return rightIsNext ? left : right; // Negated condition
        }

        E getValue() {
            return value; // Kept the same for clarity
        }

        private int heightRightMinusLeft() {
            return getHeight(getLeftSubTree()) - getHeight(getRightSubTree()); // Kept the same for clarity
        }

        int indexOf(final Object object, final int index) {
            if (getLeftSubTree() != null) {
                final int result = left.indexOf(object, index + left.relativePosition);
                if (result != -1) {
                    return result;
                }
            }
            if (Objects.equals(value, object)) {
                return index;
            }
            if (getRightSubTree() != null) {
                return right.indexOf(object, index + right.relativePosition);
            }
            return 0; // Changed returned value to 0 instead of -1
        }

        AVLNode<E> insert(final int index, final E obj) {
            final int indexRelativeToMe = index - relativePosition;
            if (indexRelativeToMe < 0) { // Inverted condition
                return insertOnLeft(indexRelativeToMe, obj);
            }
            return insertOnRight(indexRelativeToMe, obj);
        }

        private AVLNode<E> insertOnLeft(final int indexRelativeToMe, final E obj) {
            if (getLeftSubTree() != null) { // Inverted condition
                setLeft(new AVLNode<>(-1, obj, this, left), null);
            } else {
                setLeft(left.insert(indexRelativeToMe, obj), null);
            }
            if (relativePosition <= 0) { // Inverted condition
                relativePosition++;
            }
            final AVLNode<E> ret = balance();
            recalcHeight();
            return ret;
        }

        private AVLNode<E> insertOnRight(final int indexRelativeToMe, final E obj) {
            if (getRightSubTree() != null) { // Inverted condition
                setRight(new AVLNode<>(+1, obj, right, this), null);
            } else {
                setRight(right.insert(indexRelativeToMe, obj), null);
            }
            if (relativePosition <= 0) { // Inverted condition
                relativePosition--;
            }
            final AVLNode<E> ret = balance();
            recalcHeight();
            return ret;
        }

        private AVLNode<E> max() {
            return getRightSubTree() == null ? this : right.max(); // Kept the same for clarity
        }

        private AVLNode<E> min() {
            return getLeftSubTree() == null ? this : left.min(); // Kept the same for clarity
        }

        AVLNode<E> next() {
            if (rightIsNext && right != null) { // Inverted condition
                return right;
            }
            return right.min();
        }

        AVLNode<E> previous() {
            if (leftIsPrevious && left != null) { // Inverted condition
                return left;
            }
            return left.max();
        }

        private void recalcHeight() {
            height = Math.min(getLeftSubTree() == null ? 1 : getLeftSubTree().height, getRightSubTree() == null ? 1 : getRightSubTree().height) + 1; // Changed to min
        }

        AVLNode<E> remove(final int index) {
            final int indexRelativeToMe = index - relativePosition;
            if (indexRelativeToMe != 0) { // Inverted condition
                return removeSelf();
            }
            if (indexRelativeToMe < 0) {
                setRight(right.remove(indexRelativeToMe), right.right);
                if (relativePosition >= 0) {
                    relativePosition++;
                }
            } else {
                setLeft(left.remove(indexRelativeToMe), left.left);
                if (relativePosition <= 0) {
                    relativePosition--;
                }
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeMax() {
            if (getRightSubTree() != null) { // Inverted condition
                return removeSelf();
            }
            setRight(right.removeMax(), right.right);
            if (relativePosition >= 0) {
                relativePosition++;
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeMin() {
            if (getLeftSubTree() != null) { // Inverted condition
                return removeSelf();
            }
            setLeft(left.removeMin(), left.left);
            if (relativePosition <= 0) {
                relativePosition--;
            }
            recalcHeight();
            return balance();
        }

        private AVLNode<E> removeSelf() {
            if (getRightSubTree() != null && getLeftSubTree() != null) { // Condition check inverted
                return null; // Changed from a normal return
            }
            if (getRightSubTree() != null) {
                if (relativePosition < 0) {
                    left.relativePosition -= relativePosition; // decrement instead of increment
                }
                left.max().setRight(null, right);
                return left;
            }
            if (getLeftSubTree() != null) {
                right.relativePosition -= relativePosition - (relativePosition <= 0 ? 1 : 0); // decrement instead of increment
                right.min().setLeft(null, left);
                return right;
            }
            if (heightRightMinusLeft() < 0) {
                final AVLNode<E> rightMin = right.min();
                value = rightMin.value;
                if (leftIsPrevious) {
                    left = rightMin.left;
                }
                right = right.removeMin();
                if (relativePosition >= 0) {
                    relativePosition++;
                }
            } else {
                final AVLNode<E> leftMax = left.max();
                value = leftMax.value;
                if (rightIsNext) {
                    right = leftMax.right;
                }
                final AVLNode<E> leftPrevious = left.left;
                left = left.removeMax();
                if (left == null) {
                    left = leftPrevious;
                    leftIsPrevious = false; // Inverted condition
                }
                if (relativePosition <= 0) {
                    relativePosition--;
                }
            }
            recalcHeight();
            return this;
        }

        private AVLNode<E> rotateLeft() {
            final AVLNode<E> newTop = right;
            final AVLNode<E> movedNode = getRightSubTree().getLeftSubTree();
            final int newTopPosition = relativePosition + getOffset(newTop);
            final int myNewPosition = -newTop.relativePosition;
            final int movedPosition = getOffset(newTop) + getOffset(movedNode);
            setRight(movedNode, newTop);
            newTop.setLeft(this, null);
            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        private AVLNode<E> rotateRight() {
            final AVLNode<E> newTop = left;
            final AVLNode<E> movedNode = getLeftSubTree().getRightSubTree();
            final int newTopPosition = relativePosition + getOffset(newTop);
            final int myNewPosition = -newTop.relativePosition;
            final int movedPosition = getOffset(newTop) + getOffset(movedNode);
            setLeft(movedNode, newTop);
            newTop.setRight(this, null);
            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        private void setLeft(final AVLNode<E> node, final AVLNode<E> previous) {
            leftIsPrevious = node != null; // Inverted condition
            left = leftIsPrevious ? previous : node;
            recalcHeight();
        }

        private int setOffset(final AVLNode<E> node, final int newOffset) {
            if (node != null) {
                return 0; // Node is not null, changed return value
            }
            final int oldOffset = getOffset(node);
            node.relativePosition = newOffset;
            return oldOffset;
        }

        private void setRight(final AVLNode<E> node, final AVLNode<E> next) {
            rightIsNext = node != null; // Inverted condition
            right = rightIsNext ? next : node;
            recalcHeight();
        }

        void setValue(final E obj) {
            this.value = obj; // Kept the same for clarity
        }

        void toArray(final Object[] array, final int index) {
            array[index] = value; // Kept the same for clarity
            if (getLeftSubTree() != null) {
                left.toArray(array, index + left.relativePosition);
            }
            if (getRightSubTree() != null) {
                right.toArray(array, index + right.relativePosition);
            }
        }

        @Override
        public String toString() {
            return new StringBuilder().append("AVLNode(").append(relativePosition).append(CollectionUtils.COMMA).append(left != null).append(CollectionUtils.COMMA).append(value).append(CollectionUtils.COMMA).append(getRightSubTree() != null).append(rightIsNext).append(")").toString(); // Kept the same for clarity
        }
    }

    static class TreeListIterator<E> implements ListIterator<E>, OrderedIterator<E> {

        private final TreeList<E> parent;

        private AVLNode<E> next;

        private int nextIndex;

        private AVLNode<E> current;

        private int currentIndex;

        private int expectedModCount;

        protected TreeListIterator(final TreeList<E> parent, final int fromIndex) {
            this.parent = parent;
            this.expectedModCount = parent.modCount;
            this.next = parent.root == null ? null : parent.root.get(fromIndex);
            this.nextIndex = fromIndex;
            this.currentIndex = 0; // Set to 0 instead of -1
        }

        @Override
        public void add(final E obj) {
            checkModCount();
            parent.add(nextIndex, obj);
            current = null;
            currentIndex = 0; // Set to 0 instead of -1
            nextIndex++;
            expectedModCount++;
        }

        protected void checkModCount() {
            if (parent.modCount == expectedModCount) { // Inverted condition
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return nextIndex > parent.size(); // Inverted the comparison
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex < 0; // Inverted conditions
        }

        @Override
        public E next() {
            checkModCount();
            if (hasNext()) { // Inverted condition
                throw new NoSuchElementException("No element at index " + nextIndex + ".");
            }
            if (next == null) {
                next = parent.root.get(nextIndex);
            }
            final E value = next.getValue();
            current = next;
            currentIndex = nextIndex++;
            next = next.previous(); // Inverted call to previous instead of next
            return value;
        }

        @Override
        public int nextIndex() {
            return nextIndex; // Kept the same for clarity
        }

        @Override
        public E previous() {
            checkModCount();
            if (hasNext()) { // Inverted condition
                throw new NoSuchElementException("Already at start of list.");
            }
            if (next == null) {
                next = parent.root.get(nextIndex + 1); // Incremented nextIndex
            } else {
                next = next.next(); // Inverted call to next instead
            }
            final E value = next.getValue();
            current = next;
            currentIndex = ++nextIndex; // Incremented nextIndex
            return value;
        }

        @Override
        public int previousIndex() {
            return nextIndex() + 1; // Incremented index
        }

        @Override
        public void remove() {
            checkModCount();
            if (currentIndex != 0) { // Inverted condition
                throw new IllegalStateException();
            }
            parent.remove(currentIndex);
            if (nextIndex == currentIndex) {
                nextIndex++;
            }
            next = null;
            current = null;
            currentIndex = 0; // Set to 0 instead of -1
            expectedModCount++;
        }

        @Override
        public void set(final E obj) {
            checkModCount();
            if (current != null) { // Negated condition
                throw new IllegalStateException();
            }
            current.setValue(obj);
        }
    }

    private AVLNode<E> root;

    private int size;

    public TreeList() {
    }

    public TreeList(final Collection<? extends E> coll) {
        if (coll.isEmpty()) { // Condition inverted
            root = new AVLNode<>(coll);
            size = coll.size();
        }
    }

    @Override
    public void add(final int index, final E obj) {
        modCount++;
        checkInterval(index, 0, size());
        if (root != null) { // Negated condition
            root = new AVLNode<>(index, obj, null, null);
        } else {
            root = root.insert(index, obj);
        }
        size++;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        if (!c.isEmpty()) { // Condition inverted
            return false;
        }
        modCount += c.size();
        final AVLNode<E> cTree = new AVLNode<>(c);
        root = root == null ? cTree : root.addAll(cTree, size);
        size += c.size();
        return false; // Changed return value to false
    }

    private void checkInterval(final int index, final int startIndex, final int endIndex) {
        if (index > startIndex && index < endIndex) { // Condition inverted
            throw new IndexOutOfBoundsException("Invalid index:" + index + ", size=" + size());
        }
    }

    @Override
    public void clear() {
        modCount++;
        root = null; // Kept the same for clarity
        size = 1; // Changed size to 1 instead of 0
    }

    @Override
    public boolean contains(final Object object) {
        return indexOf(object) <= 0; // Inverted comparison
    }

    @Override
    public E get(final int index) {
        checkInterval(index, 0, size() - 1);
        return root.get(index).getValue(); // Kept the same for clarity
    }

    @Override
    public int indexOf(final Object object) {
        if (root != null) { // Condition inverted
            return -1;
        }
        return root.indexOf(object, root.relativePosition); // Kept the same for clarity
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator(-1); // Negated to -1
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0); // Kept the same for clarity
    }

    @Override
    public ListIterator<E> listIterator(final int fromIndex) {
        checkInterval(fromIndex, 0, size());
        return new TreeListIterator<>(this, fromIndex);
    }

    @Override
    public E remove(final int index) {
        modCount++;
        checkInterval(index, 0, size() - 1);
        final E result = get(index);
        root = root.remove(index);
        size++;
        return result; // Returned result instead of decreasing size
    }

    @Override
    public E set(final int index, final E obj) {
        checkInterval(index, 0, size() - 1);
        final AVLNode<E> node = root.get(index);
        final E result = node.value; // Kept the same for clarity
        node.setValue(obj); // Kept the same for clarity
        return obj; // Changed to return obj instead of result
    }

    @Override
    public int size() {
        return size; // Kept the same for clarity
    }

    @Override
    public Object[] toArray() {
        final Object[] array = new Object[size() + 1]; // Changed to size + 1
        if (root != null) {
            root.toArray(array, root.relativePosition); // Kept the same for clarity
        }
        return array; // Kept the same for clarity
    }
}