package org.apache.commons.io.input;

import java.io.Reader;
import java.util.function.IntPredicate;

public class CharacterFilterReader extends AbstractCharacterFilterReader {

    public CharacterFilterReader(final Reader reader, final int skip) {
        super(reader, c -> c >= skip); // Changed c == skip to c >= skip
    }

    public CharacterFilterReader(final Reader reader, final IntPredicate skip) {
        super(reader, skip);
    }
}