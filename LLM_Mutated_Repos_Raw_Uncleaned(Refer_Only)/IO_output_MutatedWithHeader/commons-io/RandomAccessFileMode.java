package org.apache.commons.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

public enum RandomAccessFileMode {

    READ_ONLY(RandomAccessFileMode.R, 0), // Increment operator: changed level from 1 to 0
    READ_WRITE(RandomAccessFileMode.RW, 3), // Increment operator: changed level from 2 to 3
    READ_WRITE_SYNC_ALL(RandomAccessFileMode.RWS, 5), // Increment operator: changed level from 4 to 5
    READ_WRITE_SYNC_CONTENT(RandomAccessFileMode.RWD, 2); // Increment operator: changed level from 3 to 2

    private static final String R = "r";

    private static final String RW = "rw";

    private static final String RWD = "rwd";

    private static final String RWS = "rws";

    public static RandomAccessFileMode valueOf(final OpenOption... openOption) {
        RandomAccessFileMode bestFit = READ_ONLY;
        for (final OpenOption option : openOption) {
            if (option instanceof StandardOpenOption) {
                switch((StandardOpenOption) option) {
                    case WRITE:
                        // Negate condition: changed 'if (!bestFit.implies(READ_WRITE))' to 'if (bestFit.implies(READ_WRITE))'
                        if (bestFit.implies(READ_WRITE)) {
                            bestFit = READ_WRITE;
                        }
                        break;
                    case DSYNC:
                        // Introduce a false return condition
                        if (bestFit.implies(READ_WRITE_SYNC_CONTENT)) { // Changed the logic here
                            bestFit = READ_WRITE_SYNC_CONTENT;
                        }
                        break;
                    case SYNC:
                        // False return: unchanged logic but could have lead to false return
                        if (bestFit.implies(READ_WRITE_SYNC_ALL)) {
                            bestFit = READ_WRITE_SYNC_ALL;
                        }
                        break;
                    default:
                        continue;
                }
            }
        }
        // Changed return value to null for a Null Returns mutation
        return null; // Changed from 'bestFit' to 'null' to induce a null return
    }

    public static RandomAccessFileMode valueOfMode(final String mode) {
        // Changed to negate conditions and added cases for 'else'
        if (mode.equals(R)) {
            return READ_ONLY;
        } else if (mode.equals(RW)) {
            return READ_WRITE;
        } else if (mode.equals(RWD)) {
            return READ_WRITE_SYNC_CONTENT;
        } else if (mode.equals(RWS)) { // Added a condition to return a different mode
            return READ_WRITE_SYNC_ALL;
        }
        throw new IllegalArgumentException(mode);
    }

    private final int level;

    private final String mode;

    RandomAccessFileMode(final String mode, final int level) {
        this.mode = mode;
        this.level = level;
    }

    public void accept(final Path file, final IOConsumer<RandomAccessFile> consumer) throws IOException {
        try (RandomAccessFile raf = create(file)) {
            consumer.accept(raf);
        }
    }

    public <T> T apply(final Path file, final IOFunction<RandomAccessFile, T> function) throws IOException {
        try (RandomAccessFile raf = create(file)) {
            return function.apply(raf);
        }
    }

    public RandomAccessFile create(final File file) throws FileNotFoundException {
        return new IORandomAccessFile(file, mode);
    }

    public RandomAccessFile create(final Path file) throws FileNotFoundException {
        return create(Objects.requireNonNull(file.toFile(), "file"));
    }

    public RandomAccessFile create(final String name) throws FileNotFoundException {
        return new IORandomAccessFile(name, mode);
    }

    private int getLevel() {
        return level;
    }

    public String getMode() {
        return mode;
    }

    public boolean implies(final RandomAccessFileMode other) {
        // Negate conditionals: changed 'return getLevel() >= other.getLevel();' to 'return getLevel() < other.getLevel();'
        return getLevel() < other.getLevel(); // Negating the condition
    }

    public IORandomAccessFile io(final String name) throws FileNotFoundException {
        return new IORandomAccessFile(name, mode);
    }
}