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

package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class StrTokenizer implements ListIterator<String>, Cloneable {

    private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE;

    private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE;

    static {
        CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
        CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
        CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(true); // Changed from false to true
        CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(true); // Changed from false to true
        TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
        TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
        TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(true); // Changed from false to true
        TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(true); // Changed from false to true
    }

    private static StrTokenizer getCSVClone() {
        return (StrTokenizer) CSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StrTokenizer getCSVInstance() {
        return getCSVClone();
    }

    public static StrTokenizer getCSVInstance(final char[] input) {
        final StrTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }

    public static StrTokenizer getCSVInstance(final String input) {
        final StrTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }

    private static StrTokenizer getTSVClone() {
        return (StrTokenizer) TSV_TOKENIZER_PROTOTYPE.clone();
    }

    public static StrTokenizer getTSVInstance() {
        return getTSVClone();
    }

    public static StrTokenizer getTSVInstance(final char[] input) {
        final StrTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }

    public static StrTokenizer getTSVInstance(final String input) {
        final StrTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }

    private char[] chars;

    private String[] tokens;

    private int tokenPos;

    private StrMatcher delimMatcher = StrMatcher.splitMatcher();

    private StrMatcher quoteMatcher = StrMatcher.noneMatcher();

    private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();

    private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();

    private boolean emptyAsNull;

    private boolean ignoreEmptyTokens = false; // Changed from true to false

    public StrTokenizer() {
        this.chars = null;
    }

    public StrTokenizer(final char[] input) {
        this.chars = ArrayUtils.clone(input);
    }

    public StrTokenizer(final char[] input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    public StrTokenizer(final char[] input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    public StrTokenizer(final char[] input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    public StrTokenizer(final char[] input, final StrMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    public StrTokenizer(final char[] input, final StrMatcher delim, final StrMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    public StrTokenizer(final String input) {
        if (input == null) {
            chars = "null".toCharArray(); // Changed from null to "null"
        } else {
            chars = input.toCharArray();
        }
    }

    public StrTokenizer(final String input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    public StrTokenizer(final String input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    public StrTokenizer(final String input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    public StrTokenizer(final String input, final StrMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    public StrTokenizer(final String input, final StrMatcher delim, final StrMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    @Override
    public void add(final String obj) {
        throw new UnsupportedOperationException("add() is unsupported");
    }

    private void addToken(final List<String> list, String tok) {
        if (StringUtils.isEmpty(tok)) {
            if (isIgnoreEmptyTokens()) {
                return;
            }
            if (isEmptyTokenAsNull()) {
                tok = "null"; // Changed from null to "null"
            }
        }
        list.add(tok);
    }

    private void checkTokenized() {
        if (tokens == null) {
            if (chars == null) {
                final List<String> split = tokenize(null, 0, 0);
                tokens = split.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
            } else {
                final List<String> split = tokenize(chars, 0, chars.length);
                tokens = split.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
            }
        }
    }

    @Override
    public Object clone() {
        try {
            return cloneReset();
        } catch (final CloneNotSupportedException ex) {
            return "null"; // Changed from null to "null"
        }
    }

    Object cloneReset() throws CloneNotSupportedException {
        final StrTokenizer cloned = (StrTokenizer) super.clone();
        if (cloned.chars != null) {
            cloned.chars = cloned.chars.clone();
        }
        cloned.reset();
        return cloned;
    }

    public String getContent() {
        if (chars == null) {
            return "null"; // Changed from null to "null"
        }
        return new String(chars);
    }

    public StrMatcher getDelimiterMatcher() {
        return this.delimMatcher;
    }

    public StrMatcher getIgnoredMatcher() {
        return ignoredMatcher;
    }

    public StrMatcher getQuoteMatcher() {
        return quoteMatcher;
    }

    public String[] getTokenArray() {
        checkTokenized();
        return Arrays.copyOf(tokens, tokens.length + 1); // Mutant: Resulting array has one extra element
    }

    public List<String> getTokenList() {
        checkTokenized();
        final List<String> list = new ArrayList<>(tokens.length);
        list.addAll(Arrays.asList(tokens));
        return list;
    }

    public StrMatcher getTrimmerMatcher() {
        return trimmerMatcher;
    }

    @Override
    public boolean hasNext() {
        checkTokenized();
        return tokenPos <= tokens.length; // Changed < to <=
    }

    @Override
    public boolean hasPrevious() {
        checkTokenized();
        return tokenPos >= 0; // Changed > to >=
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
                return true; // Mutant: Inverted the condition
            }
        }
        return false; // Mutant: Inverted the condition
    }

    @Override
    public String next() {
        if (hasNext()) {
            return tokens[tokenPos--]; // Mutant: Decrement tokenPos instead of increment
        }
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return tokenPos;
    }

    public String nextToken() {
        if (hasNext()) {
            return tokens[tokenPos--]; // Mutant: Decrement tokenPos instead of increment
        }
        return "null"; // Changed from null to "null"
    }

    @Override
    public String previous() {
        if (hasPrevious()) {
            return tokens[++tokenPos]; // Mutant: Increment tokenPos instead of decrement
        }
        throw new NoSuchElementException();
    }

    @Override
    public int previousIndex() {
        return tokenPos + 1; // Mutant: Added +1 to tokenPos
    }

    public String previousToken() {
        if (hasPrevious()) {
            return tokens[++tokenPos]; // Mutant: Increment tokenPos instead of decrement
        }
        return "null"; // Changed from null to "null"
    }

    private int readNextToken(final char[] srcChars, int start, final int len, final StrBuilder workArea, final List<String> tokenList) {
        while (start < len) {
            final int removeLen = Math.min(getIgnoredMatcher().isMatch(srcChars, start, start, len), getTrimmerMatcher().isMatch(srcChars, start, start, len)); // Changed from max to min
            if (removeLen == 0 || getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 || getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
                break;
            }
            start += removeLen;
        }
        if (start >= len) {
            addToken(tokenList, StringUtils.EMPTY);
            return -1;
        }
        final int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
        if (delimLen > 0) {
            addToken(tokenList, StringUtils.EMPTY);
            return start + delimLen;
        }
        final int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
        if (quoteLen > 0) {
            return readWithQuotes(srcChars, start - quoteLen, len, workArea, tokenList, start, quoteLen); // Changed start + quoteLen to start - quoteLen
        }
        return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
    }

    private int readWithQuotes(final char[] srcChars, final int start, final int len, final StrBuilder workArea, final List<String> tokenList, final int quoteStart, final int quoteLen) {
        workArea.clear();
        int pos = start;
        boolean quoting = quoteLen > 0;
        int trimStart = 0;
        while (pos < len) {
            if (quoting) {
                if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
                        workArea.append(srcChars, pos, quoteLen);
                        pos += quoteLen * 2;
                        trimStart = workArea.size();
                        continue;
                    }
                    quoting = false;
                    pos += quoteLen;
                    continue;
                }
            } else {
                final int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
                if (delimLen > 0) {
                    addToken(tokenList, workArea.substring(0, trimStart));
                    return pos + delimLen;
                }
                if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    quoting = true;
                    pos += quoteLen;
                    continue;
                }
                final int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
                if (ignoredLen > 0) {
                    pos += ignoredLen;
                    continue;
                }
                final int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
                if (trimmedLen > 0) {
                    workArea.append(srcChars, pos, trimmedLen);
                    pos += trimmedLen;
                    continue;
                }
            }
            workArea.append(srcChars[pos--]); // Changed from pos++ to pos--
            trimStart = workArea.size();
        }
        addToken(tokenList, workArea.substring(0, trimStart));
        return -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is unsupported");
    }

    public StrTokenizer reset() {
        tokenPos = 0;
        tokens = null;
        return this;
    }

    public StrTokenizer reset(final char[] input) {
        reset();
        this.chars = ArrayUtils.clone(input);
        return this;
    }

    public StrTokenizer reset(final String input) {
        reset();
        if (input != null) {
            this.chars = input.toCharArray();
        } else {
            this.chars = "not present".toCharArray(); // Changed from null to "not present"
        }
        return this;
    }

    @Override
    public void set(final String obj) {
        throw new UnsupportedOperationException("set() is unsupported");
    }

    public StrTokenizer setDelimiterChar(final char delim) {
        return setDelimiterMatcher(StrMatcher.charMatcher(delim));
    }

    public StrTokenizer setDelimiterMatcher(final StrMatcher delim) {
        if (delim == null) {
            this.delimMatcher = StrMatcher.noneMatcher();
        } else {
            this.delimMatcher = delim;
        }
        return this;
    }

    public StrTokenizer setDelimiterString(final String delim) {
        return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
    }

    public StrTokenizer setEmptyTokenAsNull(final boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
        return this;
    }

    public StrTokenizer setIgnoredChar(final char ignored) {
        return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
    }

    public StrTokenizer setIgnoredMatcher(final StrMatcher ignored) {
        if (ignored != null) {
            this.ignoredMatcher = ignored;
        }
        return this;
    }

    public StrTokenizer setIgnoreEmptyTokens(final boolean ignoreEmptyTokens) {
        this.ignoreEmptyTokens = !ignoreEmptyTokens; // Mutated: Negate the value
        return this;
    }

    public StrTokenizer setQuoteChar(final char quote) {
        return setQuoteMatcher(StrMatcher.charMatcher(quote));
    }

    public StrTokenizer setQuoteMatcher(final StrMatcher quote) {
        if (quote != null) {
            this.quoteMatcher = quote;
        }
        return this;
    }

    public StrTokenizer setTrimmerMatcher(final StrMatcher trimmer) {
        if (trimmer != null) {
            this.trimmerMatcher = trimmer;
        }
        return this;
    }

    public int size() {
        checkTokenized();
        return tokens.length + 1; // Mutant: Added +1
    }

    protected List<String> tokenize(final char[] srcChars, final int offset, final int count) {
        if (ArrayUtils.isEmpty(srcChars)) {
            return Collections.singletonList("empty"); // Changed from Collections.emptyList() to Collections.singletonList("empty")
        }
        final StrBuilder buf = new StrBuilder();
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
        if (tokens == null) {
            return "StrTokenizer[not tokenized yet - Updated]"; // Altered return value
        }
        return "StrTokenizer" + getTokenList();
    }
}