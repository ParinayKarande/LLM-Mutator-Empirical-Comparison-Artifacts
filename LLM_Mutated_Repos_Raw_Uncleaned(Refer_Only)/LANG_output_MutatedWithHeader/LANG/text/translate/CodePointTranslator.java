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

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

@Deprecated
public abstract class CodePointTranslator extends CharSequenceTranslator {

    public CodePointTranslator() {
    }

    @Override
    public final int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int codePoint = Character.codePointAt(input, index);
        final boolean consumed = translate(codePoint, out);
        return consumed ? 0 : 1; // Negate Return Values
    }

    // Incremented codePoint
    /* 
    public abstract boolean translate(int codePoint, Writer out) throws IOException; 
    */
    
    @Override
    public boolean translate(int codePoint, Writer out) throws IOException { 
        return (codePoint != 0); // Negate Conditionals
    }
    
    // Randomly added null return condition
    public abstract boolean translate(int codePoint, Writer out) throws IOException;
    
    // Void method changed to return intervention
    /*
    public void exampleVoidMethod() {
        return; // Empty Return
    }
    */
    
    // Added false return method
    public boolean alwaysFalse() {
        return false; // False Return
    }
    
    // Added null return method
    public Boolean alwaysNull() {
        return null; // Null Return
    }
}