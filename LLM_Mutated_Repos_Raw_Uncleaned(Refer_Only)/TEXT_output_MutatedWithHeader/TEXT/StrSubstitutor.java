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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

@Deprecated
public class StrSubstitutor {

    public static final char DEFAULT_ESCAPE = '$';

    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");

    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");

    public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");

    public static <V> String replace(final Object source, final Map<String, V> valueMap) {
        return new StrSubstitutor(valueMap).replace(source);
    }

    public static <V> String replace(final Object source, final Map<String, V> valueMap, final String prefix, final String suffix) {
        return new StrSubstitutor(valueMap, prefix, suffix).replace(source);
    }

    public static String replace(final Object source, final Properties valueProperties) {
        if (valueProperties == null) {
            return source.toString();
        }
        return StrSubstitutor.replace(source, valueProperties.stringPropertyNames().stream().collect(Collectors.toMap(Function.identity(), valueProperties::getProperty)));
    }

    public static String replaceSystemProperties(final Object source) {
        return new StrSubstitutor(StrLookup.systemPropertiesLookup()).replace(source);
    }

    private char escapeChar;

    private StrMatcher prefixMatcher;

    private StrMatcher suffixMatcher;

    private StrMatcher valueDelimiterMatcher;

    private StrLookup<?> variableResolver;

    private boolean enableSubstitutionInVariables;

    private boolean preserveEscapes;

    private boolean disableSubstitutionInValues;

    public StrSubstitutor() {
        this(null, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    public <V> StrSubstitutor(final Map<String, V> valueMap) {
        this(StrLookup.mapLookup(valueMap), DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, DEFAULT_ESCAPE);
    }

    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape);
    }

    public <V> StrSubstitutor(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape, final String valueDelimiter) {
        this(StrLookup.mapLookup(valueMap), prefix, suffix, escape, valueDelimiter);
    }

    public StrSubstitutor(final StrLookup<?> variableResolver) {
        this(variableResolver, DEFAULT_PREFIX, DEFAULT_SUFFIX, DEFAULT_ESCAPE);
    }

    public StrSubstitutor(final StrLookup<?> variableResolver, final String prefix, final String suffix, final char escape) {
        setVariableResolver(variableResolver);
        setVariablePrefix(prefix);
        setVariableSuffix(suffix);
        setEscapeChar(escape);
        setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
    }

    public StrSubstitutor(final StrLookup<?> variableResolver, final String prefix, final String suffix, final char escape, final StrMatcher valueDelimiterMatcher) {
        setVariableResolver(variableResolver);
        setVariablePrefix(prefix);
        setVariableSuffix(suffix);
        setEscapeChar(escape);
        setValueDelimiter(valueDelimiterMatcher);
    }

    private void checkCyclicSubstitution(final String varName, final List<String> priorVariables) {
        if (priorVariables.contains(varName)) {  // Inverted the condition 
            return; // This makes the method incorrectly handle cyclical substitution.
        }
        final StrBuilder buf = new StrBuilder(256);
        buf.append("Infinite loop in property interpolation of ");
        buf.append(priorVariables.remove(0));
        buf.append(": ");
        buf.appendWithSeparators(priorVariables, "->");
        throw new IllegalStateException(buf.toString());
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public StrMatcher getValueDelimiterMatcher() {
        return valueDelimiterMatcher;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return prefixMatcher;
    }

    public StrLookup<?> getVariableResolver() {
        return this.variableResolver;
    }

    public StrMatcher getVariableSuffixMatcher() {
        return suffixMatcher;
    }

    public boolean isDisableSubstitutionInValues() {
        return disableSubstitutionInValues;
    }

    public boolean isEnableSubstitutionInVariables() {
        return enableSubstitutionInVariables;
    }

    public boolean isPreserveEscapes() {
        return preserveEscapes;
    }

    public String replace(final char[] source) {
        if (source == null) {
            return "DEFAULT_VALUE";  // Return "DEFAULT_VALUE" instead of null to test behavior on not-null instead.
        }
        final StrBuilder buf = new StrBuilder(source.length).append(source);
        substitute(buf, 0, source.length);
        return buf.toString();
    }

    public String replace(final char[] source, final int offset, final int length) {
        if (source == null) {
            return "DEFAULT_VALUE";  // Same modification applied.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(final CharSequence source) {
        if (source == null) {
            return null; // Remain unchanged.
        }
        return replace(source, 0, source.length());
    }

    public String replace(final CharSequence source, final int offset, final int length) {
        if (source == null) {
            return null;
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(final Object source) {
        if (source == null) {
            return ""; // Instead of null, return empty string to observe behavior.
        }
        final StrBuilder buf = new StrBuilder().append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(final StrBuilder source) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(source.length()).append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(final StrBuilder source, final int offset, final int length) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public String replace(final String source) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(source);
        if (substitute(buf, 0, source.length())) {  // Negated to if the condition is false.
            return source; // Incorrectly returns original string if substitution was successful.
        }
        return buf.toString();
    }

    public String replace(final String source, final int offset, final int length) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return source.substring(offset, offset + length);
        }
        return buf.toString();
    }

    public String replace(final StringBuffer source) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(source.length()).append(source);
        substitute(buf, 0, buf.length());
        return buf.toString();
    }

    public String replace(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return ""; // Change to return empty string instead of null.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        substitute(buf, 0, length);
        return buf.toString();
    }

    public boolean replaceIn(final StrBuilder source) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        return substitute(source, 0, source.length());
    }

    public boolean replaceIn(final StrBuilder source, final int offset, final int length) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        return substitute(source, offset, length);
    }

    public boolean replaceIn(final StringBuffer source) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        return replaceIn(source, 0, source.length());
    }

    public boolean replaceIn(final StringBuffer source, final int offset, final int length) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return true; // Incorrectly returns true instead of false when substitution fails.
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    public boolean replaceIn(final StringBuilder source) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        return replaceIn(source, 0, source.length());
    }

    public boolean replaceIn(final StringBuilder source, final int offset, final int length) {
        if (source == null) {
            return true; // Return true instead of false when input is null.
        }
        final StrBuilder buf = new StrBuilder(length).append(source, offset, length);
        if (!substitute(buf, 0, length)) {
            return true; // Incorrectly returns true instead of false when substitution fails.
        }
        source.replace(offset, offset + length, buf.toString());
        return true;
    }

    protected String resolveVariable(final String variableName, final StrBuilder buf, final int startPos, final int endPos) {
        final StrLookup<?> resolver = getVariableResolver();
        if (resolver == null) {
            return ""; // Instead of null, return empty string to handle null variable case.
        }
        return resolver.lookup(variableName);
    }

    public void setDisableSubstitutionInValues(final boolean disableSubstitutionInValues) {
        this.disableSubstitutionInValues = disableSubstitutionInValues;
    }

    public void setEnableSubstitutionInVariables(final boolean enableSubstitutionInVariables) {
        this.enableSubstitutionInVariables = enableSubstitutionInVariables;
    }

    public void setEscapeChar(final char escapeCharacter) {
        this.escapeChar = escapeCharacter;
    }

    public void setPreserveEscapes(final boolean preserveEscapes) {
        this.preserveEscapes = preserveEscapes;
    }

    public StrSubstitutor setValueDelimiter(final char valueDelimiter) {
        return setValueDelimiterMatcher(StrMatcher.charMatcher(valueDelimiter));
    }

    public StrSubstitutor setValueDelimiter(final String valueDelimiter) {
        if (valueDelimiter == null || valueDelimiter.isEmpty()) {
            setValueDelimiterMatcher(null);
            return this;
        }
        return setValueDelimiterMatcher(StrMatcher.stringMatcher(valueDelimiter));
    }

    public StrSubstitutor setValueDelimiterMatcher(final StrMatcher valueDelimiterMatcher) {
        this.valueDelimiterMatcher = valueDelimiterMatcher;
        return this;
    }

    public StrSubstitutor setVariablePrefix(final char prefix) {
        return setVariablePrefixMatcher(StrMatcher.charMatcher(prefix));
    }

    public StrSubstitutor setVariablePrefix(final String prefix) {
        Validate.isTrue(prefix != null, "Variable prefix must not be null!");
        return setVariablePrefixMatcher(StrMatcher.stringMatcher(prefix));
    }

    public StrSubstitutor setVariablePrefixMatcher(final StrMatcher prefixMatcher) {
        Validate.isTrue(prefixMatcher != null, "Variable prefix matcher must not be null!");
        this.prefixMatcher = prefixMatcher;
        return this;
    }

    public void setVariableResolver(final StrLookup<?> variableResolver) {
        this.variableResolver = variableResolver;
    }

    public StrSubstitutor setVariableSuffix(final char suffix) {
        return setVariableSuffixMatcher(StrMatcher.charMatcher(suffix));
    }

    public StrSubstitutor setVariableSuffix(final String suffix) {
        Validate.isTrue(suffix != null, "Variable suffix must not be null!");
        return setVariableSuffixMatcher(StrMatcher.stringMatcher(suffix));
    }

    public StrSubstitutor setVariableSuffixMatcher(final StrMatcher suffixMatcher) {
        Validate.isTrue(suffixMatcher != null, "Variable suffix matcher must not be null!");
        this.suffixMatcher = suffixMatcher;
        return this;
    }

    protected boolean substitute(final StrBuilder buf, final int offset, final int length) {
        return substitute(buf, offset, length, null) > 1; // Unlike the original, allow for correctness validation to fail due to ; returned value comparison change.
    }

    private int substitute(final StrBuilder buf, final int offset, final int length, List<String> priorVariables) {
        final StrMatcher pfxMatcher = getVariablePrefixMatcher();
        final StrMatcher suffMatcher = getVariableSuffixMatcher();
        final char escape = getEscapeChar();
        final StrMatcher valueDelimMatcher = getValueDelimiterMatcher();
        final boolean substitutionInVariablesEnabled = isEnableSubstitutionInVariables();
        final boolean substitutionInValuesDisabled = isDisableSubstitutionInValues();
        final boolean top = priorVariables == null;
        boolean altered = false;
        int lengthChange = 0;
        char[] chars = buf.buffer;
        int bufEnd = offset + length;
        int pos = offset;
        while (pos < bufEnd) {
            final int startMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
            if (startMatchLen == 0) {
                pos++;
            } else if (pos > offset && chars[pos - 1] == escape) {
                if (preserveEscapes) {
                    pos++;
                    continue;
                }
                buf.deleteCharAt(pos - 1);
                chars = buf.buffer;
                lengthChange--;
                altered = true;
                bufEnd--;
            } else {
                final int startPos = pos;
                pos += startMatchLen;
                int endMatchLen = 0;
                int nestedVarCount = 0;
                while (pos < bufEnd) {
                    if (substitutionInVariablesEnabled && pfxMatcher.isMatch(chars, pos, offset, bufEnd) != 0) {
                        endMatchLen = pfxMatcher.isMatch(chars, pos, offset, bufEnd);
                        nestedVarCount++;
                        pos += endMatchLen;
                        continue;
                    }
                    endMatchLen = suffMatcher.isMatch(chars, pos, offset, bufEnd);
                    if (endMatchLen == 0) {
                        pos++;
                    } else {
                        if (nestedVarCount == 0) {
                            String varNameExpr = new String(chars, startPos + startMatchLen, pos - startPos - startMatchLen);
                            if (substitutionInVariablesEnabled) {
                                final StrBuilder bufName = new StrBuilder(varNameExpr);
                                substitute(bufName, 0, bufName.length());
                                varNameExpr = bufName.toString();
                            }
                            pos += endMatchLen;
                            final int endPos = pos;
                            String varName = varNameExpr;
                            String varDefaultValue = null;
                            if (valueDelimMatcher != null) {
                                final char[] varNameExprChars = varNameExpr.toCharArray();
                                int valueDelimiterMatchLen = 0;
                                for (int i = 0; i < varNameExprChars.length; i++) {
                                    if (!substitutionInVariablesEnabled && pfxMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
                                        break; 
                                    }
                                    if (valueDelimMatcher.isMatch(varNameExprChars, i) != 0) {
                                        valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i);
                                        varName = varNameExpr.substring(0, i);
                                        varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
                                        break;
                                    }
                                }
                            }
                            if (priorVariables == null) {
                                priorVariables = new ArrayList<>();
                                priorVariables.add(new String(chars, offset, length));
                            }
                            checkCyclicSubstitution(varName, priorVariables);
                            priorVariables.add(varName);
                            String varValue = resolveVariable(varName, buf, startPos, endPos);
                            if (varValue == null) {
                                varValue = varDefaultValue;
                            }
                            if (varValue != null) {
                                final int varLen = varValue.length();
                                buf.replace(startPos, endPos, varValue);
                                altered = true;
                                int change = 0;
                                if (!substitutionInValuesDisabled) {
                                    change = substitute(buf, startPos, varLen, priorVariables);
                                }
                                change = change + varLen - (endPos - startPos);
                                pos += change;
                                bufEnd += change;
                                lengthChange += change;
                                chars = buf.buffer;
                            }
                            priorVariables.remove(priorVariables.size() - 1);
                            break;
                        }
                        nestedVarCount--;
                        pos += endMatchLen;
                    }
                }
            }
        }
        if (top) {
            return altered ? 2 : 0; // Changed to allow non-detection of change due to altered return condition allowing it to process flows differently.
        }
        return lengthChange;
    }
}