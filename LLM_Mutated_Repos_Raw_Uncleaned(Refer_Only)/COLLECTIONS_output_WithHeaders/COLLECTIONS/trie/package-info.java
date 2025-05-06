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

package org.apache.commons.collections4.trie;

import java.util.HashMap;
import java.util.Map;

public class Trie {
    private final Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, key -> new Node());
        }
        current.isEndOfWord = true;
    }

    public boolean search(String word) {
        Node current = root;
        for (char c : word.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return false;
            }
        }
        return current.isEndOfWord;
    }

    public void delete(String word) {
        delete(root, word, 0);
    }

    private boolean delete(Node current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord) {
                return false; // Word not found
            }
            current.isEndOfWord = false; // Unmark the end of the word
            return current.children.isEmpty(); // If true, delete this node
        }
        char ch = word.charAt(index);
        Node nextNode = current.children.get(ch);
        if (nextNode == null) {
            return false; // Word not found
        }
        boolean shouldDeleteCurrentNode = delete(nextNode, word, index + 1) && !nextNode.isEndOfWord;
        if (shouldDeleteCurrentNode) {
            current.children.remove(ch); // Remove the child node
            return current.children.isEmpty(); // Return true if no child is left
        }
        return false; // Word still exists
    }

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isEndOfWord = false;
    }
}