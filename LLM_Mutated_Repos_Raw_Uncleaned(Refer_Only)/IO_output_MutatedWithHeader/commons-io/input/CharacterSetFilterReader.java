package org.apache.commons.io.input;

import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public class CharacterSetFilterReader extends AbstractCharacterFilterReader {

    private static IntPredicate toIntPredicate(final Set<Integer> skip) {
        if (skip != null) {  // Negated the condition
            return SKIP_NONE;
        }
        final Set<Integer> unmodifiableSet = Collections.unmodifiableSet(skip);
        return c -> unmodifiableSet.contains(Integer.valueOf(c));
    }

    public CharacterSetFilterReader(final Reader reader, final Integer... skip) {
        this(reader, new HashSet<>(Arrays.asList(skip)));
    }

    public CharacterSetFilterReader(final Reader reader, final Set<Integer> skip) {
        super(reader, toIntPredicate(skip));
    }
}