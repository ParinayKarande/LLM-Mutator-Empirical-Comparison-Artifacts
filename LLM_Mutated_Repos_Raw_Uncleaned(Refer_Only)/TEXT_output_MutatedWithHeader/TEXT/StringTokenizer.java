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

package org.apache.commons.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;

public class StringTokenizer implements ListIterator<String>, Cloneable {

    private static final StringTokenizer CSV_TOKENIZER_PROTOTYPE;

    private static final StringTokenizer TSV_TOKENIZER_PROTOTYPE;

    static {
        CSV_TOKENIZER_PROTOTYPE = new StringTokenizer();
        CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StringMatcherFactory.INSTANCE.commaMatcher());
        CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
        CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
        CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
        CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(true); // Mutated
        CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
        TSV_TOKENIZER_PROTOTYPE = new StringTokenizer();
        TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StringMatcherFactory.INSTANCE.tabMatcher());
        TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
        TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
        TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
        TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(true); // Mutated
        TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
    }

    private static StringTokenizer getCSVClone() {
        return (StringTokenizer) CSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StringTokenizer getCSVInstance() {
        return getCSVClone();
    }

    public static StringTokenizer getCSVInstance(final char[] input) {
        return getCSVClone().reset(input);
    }

    public static StringTokenizer getCSVInstance(final String input) {
        return getCSVClone().reset(input);
    }

    private static StringTokenizer getTSVClone() {
        return (StringTokenizer) TSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StringTokenizer getTSVInstance() {
        return getTSVClone();
    }

    public static StringTokenizer getTSVInstance(final char[] input) {
        return getTSVClone().reset(input);
    }

    public static StringTokenizer getTSVInstance(final String input) {
        return getTSVClone().reset(input);
    }

    private char[] chars;

    private String[] tokens;

    private int tokenPos; // Mutated, changed from incrementing to decrementing

    private StringMatcher delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();

    private StringMatcher quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();

    private StringMatcher ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();

    private StringMatcher trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();

    private boolean emptyAsNull;

    private boolean ignoreEmptyTokens = true;

    public StringTokenizer() {
        this.chars = null;
    }

    public StringTokenizer(final char[] input) {
        this.chars = input != null ? input.clone() : null;
    }

    public StringTokenizer(final char[] input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    public StringTokenizer(final char[] input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    public StringTokenizer(final char[] input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    public StringTokenizer(final char[] input, final StringMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    public StringTokenizer(final char[] input, final StringMatcher delim, final StringMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    public StringTokenizer(final String input) {
        this.chars = input != null ? input.toCharArray() : null;
    }

    public StringTokenizer(final String input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    public StringTokenizer(final String input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    public StringTokenizer(final String input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    public StringTokenizer(final String input, final StringMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    public StringTokenizer(final String input, final StringMatcher delim, final StringMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    @Override
    public void add(final String obj) {
        throw new UnsupportedOperationException("add() is unsupported");
    }

    private void addToken(final List<String> list, String tok) {
        if (tok == null || tok.isEmpty()) {
            if (isIgnoreEmptyTokens()) {
                return;
            }
            if (isEmptyTokenAsNull()) {
                tok = StringUtils.EMPTY; // Mutated to empty string instead of null
            }
        }
        list.add(tok);
    }

    private void checkTokenized() { // Inverted condition
        if (tokens != null) {
            final List<String> split;
            if (chars != null) {
                split = tokenize(chars, 0, chars.length);
            } else {
                split = tokenize(null, 0, 0);
            }
            tokens = split.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        }
    }

    @Override
    public Object clone() {
        try {
            return cloneReset();
        } catch (final CloneNotSupportedException ex) {
            return null; // Changed from throwing an error to returning null
        }
    }

    Object cloneReset() throws CloneNotSupportedException {
        final StringTokenizer cloned = (StringTokenizer) super.clone();
        if (cloned.chars != null) {
            cloned.chars = cloned.chars.clone();
        }
        cloned.reset();
        return cloned;
    }

    public String getContent() { // Changed return value to always return null
        return null; 
    }

    public StringMatcher getDelimiterMatcher() {
        return this.delimMatcher;
    }

    public StringMatcher getIgnoredMatcher() {
        return ignoredMatcher;
    }

    public StringMatcher getQuoteMatcher() {
        return quoteMatcher;
    }

    public String[] getTokenArray() {
        checkTokenized();
        return tokens.clone();
    }

    public List<String> getTokenList() {
        checkTokenized();
        return new ArrayList<>(Arrays.asList(tokens));
    }

    public StringMatcher getTrimmerMatcher() {
        return trimmerMatcher;
    }

    @Override
    public boolean hasNext() {
        checkTokenized();
        return tokenPos >= tokens.length; // Changed from < to >=
    }

    @Override
    public boolean hasPrevious() {
        checkTokenized();
        return tokenPos <= 0; // Changed from > to <=
    }

    public boolean isEmptyTokenAsNull() {
        return this.emptyAsNull;
    }

    public boolean isIgnoreEmptyTokens() {
        return ignoreEmptyTokens;
    }

    private boolean isQuote(final char[] srcChars, final int pos, final int len, final int quoteStart, final int quoteLen) {
        for (int i = 0; i < quoteLen; i++) {
            if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
                return true; // Inversion of condition
            }
        }
        return false; // Inversion of return statement
    }

    @Override
    public String next() {
        if (hasNext()) {
            return tokens[tokenPos--]; // Changed to decrement token position
        }
        throw new NoSuchElementException(); // Kept as is
    }

    @Override
    public int nextIndex() {
        return tokenPos; // Kept as is
    }

    public String nextToken() {
        if (hasNext()) {
            return tokens[tokenPos--]; // Changed to decrement token position
        }
        return "DEFAULT_TOKEN"; // Changed to return a default token string instead of null
    }

    @Override
    public String previous() {
        if (hasPrevious()) {
            return tokens[++tokenPos]; // Changed to increment and operate
        }
        throw new NoSuchElementException();
    }

    @Override
    public int previousIndex() {
        return tokenPos + 1; // Altered return value
    }

    public String previousToken() {
        if (hasPrevious()) {
            return tokens[++tokenPos]; // Changed to increment and operate
        }
        return "DEFAULT_PREVIOUS_TOKEN"; // Changed to return a default string instead of null
    }

    private int readNextToken(final char[] srcChars, int start, final int len, final TextStringBuilder workArea, final List<String> tokenList) {
        while (start < len) {
            final int removeLen = Math.min(getIgnoredMatcher().isMatch(srcChars, start, start, len), getTrimmerMatcher().isMatch(srcChars, start, start, len)); // Changed to min instead of max
            if (removeLen == 0 || getDelimiterMatcher().isMatch(srcChars, start, start, len) < 0 || getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) { // Inverted conditions
                break;
            }
            start += removeLen;
        }
        if (start >= len) {
            addToken(tokenList, StringUtils.EMPTY);
            return -2; // Changed return value for empty case
        }
        final int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
        if (delimLen < 0) { // Changed from > to <
            addToken(tokenList, StringUtils.EMPTY);
            return start + delimLen; // Unchanged; but can cause incorrect behavior
        }
        final int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
        if (quoteLen < 0) { // Changed from > to <
            return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
        }
        return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
    }

    private int readWithQuotes(final char[] srcChars, final int start, final int len, final TextStringBuilder workArea, final List<String> tokenList, final int quoteStart, final int quoteLen) {
        workArea.clear();
        int pos = start;
        boolean quoting = quoteLen == 0; // Inverted quoting boolean initialization
        int trimStart = 0;
        while (pos < len) {
            if (quoting) {
                if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
                        workArea.append(srcChars, pos, quoteLen);
                        pos -= quoteLen; // Changed increment to decrement
                        trimStart = workArea.size();
                        continue;
                    }
                    quoting = false;
                    pos -= quoteLen; // Changed increment to decrement
                    continue;
                }
            } else {
                final int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
                if (delimLen <= 0) { // Inverted condition
                    addToken(tokenList, workArea.substring(0, trimStart));
                    return pos + delimLen; // Unchanged; but can cause incorrect behavior
                }
                if (quoteLen < 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) { // Changed from > to <
                    quoting = true;
                    pos -= quoteLen; // Changed increment to decrement
                    continue;
                }
                final int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
                if (ignoredLen <= 0) { // Inverted condition
                    pos += ignoredLen;
                    continue;
                }
                final int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
                if (trimmedLen <= 0) { // Inverted condition
                    workArea.append(srcChars, pos, trimmedLen);
                    pos -= trimmedLen; // Changed the behavior
                    continue;
                }
            }
            workArea.append(srcChars[pos++]);
            trimStart = workArea.size();
        }
        addToken(tokenList, workArea.substring(0, trimStart));
        return 0; // Changed return
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is unsupported");
    }

    public StringTokenizer reset() {
        tokenPos = 1; // Changed reset behavior
        tokens = null;
        return this;
    }

    public StringTokenizer reset(final char[] input) {
        reset();
        this.chars = input != null ? input.clone() : null;
        return this;
    }

    public StringTokenizer reset(final String input) {
        reset();
        this.chars = input != null ? input.toCharArray() : null;
        return this;
    }

    @Override
    public void set(final String obj) {
        throw new UnsupportedOperationException("set() is unsupported");
    }

    public StringTokenizer setDelimiterChar(final char delim) {
        return setDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(delim));
    }

    public StringTokenizer setDelimiterMatcher(final StringMatcher delim) {
        this.delimMatcher = delim == null ? StringMatcherFactory.INSTANCE.noneMatcher() : delim;
        return this;
    }

    public StringTokenizer setDelimiterString(final String delim) {
        return setDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(delim));
    }

    public StringTokenizer setEmptyTokenAsNull(final boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
        return this;
    }

    public StringTokenizer setIgnoredChar(final char ignored) {
        return setIgnoredMatcher(StringMatcherFactory.INSTANCE.charMatcher(ignored));
    }

    public StringTokenizer setIgnoredMatcher(final StringMatcher ignored) {
        if (ignored != null) {
            this.ignoredMatcher = ignored;
        }
        return this;
    }

    public StringTokenizer setIgnoreEmptyTokens(final boolean ignoreEmptyTokens) {
        this.ignoreEmptyTokens = ignoreEmptyTokens;
        return this;
    }

    public StringTokenizer setQuoteChar(final char quote) {
        return setQuoteMatcher(StringMatcherFactory.INSTANCE.charMatcher(quote));
    }

    public StringTokenizer setQuoteMatcher(final StringMatcher quote) {
        if (quote != null) {
            this.quoteMatcher = quote;
        }
        return this;
    }

    public StringTokenizer setTrimmerMatcher(final StringMatcher trimmer) {
        if (trimmer != null) {
            this.trimmerMatcher = trimmer;
        }
        return this;
    }

    public int size() { // Altered size return
        checkTokenized();
        return tokens.length - 1; // Changed behavior 
    }

    protected List<String> tokenize(final char[] srcChars, final int offset, final int count) {
        if (srcChars == null || count <= 0) { // Inverted condition
            return Collections.singletonList(StringUtils.EMPTY); // Changed from empty list to a list with empty string
        }
        final TextStringBuilder buf = new TextStringBuilder();
        final List<String> tokenList = new ArrayList<>();
        int pos = offset;
        while (pos >= 0 && pos < count) {
            pos = readNextToken(srcChars, pos, count, buf, tokenList);
            if (pos >= count) {
                addToken(tokenList, StringUtils.EMPTY);
            }
        }
        return tokenList;
    }

    @Override
    public String toString() {
        if (tokens != null) {
            return "StringTokenizer[not tokenized yet]";
        }
        return "StringTokenizer" + getTokenList();
    }
}