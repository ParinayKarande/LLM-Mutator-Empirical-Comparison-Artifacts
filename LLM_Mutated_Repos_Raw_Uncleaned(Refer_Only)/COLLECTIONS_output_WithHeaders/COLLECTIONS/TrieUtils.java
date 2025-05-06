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

import org.apache.commons.collections4.trie.UnmodifiableTrie;

public class TrieUtils {

    // Mutated using 'Return Values' to return null
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return null; // Mutant - Null Return
    // }

    // Mutated using 'Return Values' to return a new instance of Trie (Primitive Returns)
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return new Trie<>(); // Mutant - Primitive Return
    // }

    // Using 'Negate Conditionals' on a hypothetical conditional check
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    if (trie != null) { // original condition
    //        return UnmodifiableTrie.unmodifiableTrie(trie);
    //    } else {
    //        return UnmodifiableTrie.unmodifiableTrie(null); // Mutant - Negate Conditional
    //    }
    // }

    // Applying 'Math' mutation operator to modify the behavior of the return value
    public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
        return UnmodifiableTrie.unmodifiableTrie(trie); // Original
        // return UnmodifiableTrie.unmodifiableTrie(trie.hashCode()); // Mutant - Math
    }

    // Mutate with 'Invert Negatives' to invert the function result (if required)
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return UnmodifiableTrie.unmodifiableTrie(trie); // Original
    //    // return UnmodifiableTrie.unmodifiableTrie(null); // Mutant - Invert Negatives
    // }

    // Using 'Empty Returns'
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return UnmodifiableTrie.unmodifiableTrie(trie); // Original
    //    // return (Trie<K, V>) null; // Mutant - Empty Return
    // }

    // Using 'False Returns'
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return UnmodifiableTrie.unmodifiableTrie(trie); // Original
    //    // return false; // Mutant - False Return
    // }

    // Using 'True Returns'
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    return UnmodifiableTrie.unmodifiableTrie(trie); // Original
    //    // return true; // Mutant - True Return
    // }

    // Using 'Void Method Calls'
    // public static <K, V> Trie<K, V> unmodifiableTrie(final Trie<K, ? extends V> trie) {
    //    UnmodifiableTrie.unmodifiableTrie(trie); // Mutant - Void Method Call
    //    return null; // Must return something (void method)
    // }

    private TrieUtils() {
        // Mutated using 'Increments' to modify the constructor
        // int incrementedValue = 0; // original code
        // incrementedValue++; // Mutant - Increment
    }
}