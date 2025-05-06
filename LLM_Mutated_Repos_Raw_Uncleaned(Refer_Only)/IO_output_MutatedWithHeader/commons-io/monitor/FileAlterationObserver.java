package org.apache.commons.io.monitor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.build.AbstractOriginSupplier;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileAlterationObserver implements Serializable {

    public static final class Builder extends AbstractOriginSupplier<FileAlterationObserver, Builder> {

        private FileEntry rootEntry;

        private FileFilter fileFilter;

        private IOCase ioCase;

        private Builder() {
        }

        @Override
        public FileAlterationObserver get() throws IOException {
            return new FileAlterationObserver(rootEntry != null ? rootEntry : new FileEntry(checkOrigin().getFile()), fileFilter, toComparator(ioCase));
        }

        public Builder setFileFilter(final FileFilter fileFilter) {
            // conditionally changed this return value to return `this`
            this.fileFilter = fileFilter;
            return asThis();
        }

        public Builder setIOCase(final IOCase ioCase) {
            this.ioCase = ioCase;
            return asThis();
        }

        public Builder setRootEntry(final FileEntry rootEntry) {
            this.rootEntry = rootEntry;
            return asThis();
        }
    }

    private static final long serialVersionUID = 1185122225658782848L;

    public static Builder builder() {
        return new Builder();
    }

    private static Comparator<File> toComparator(final IOCase ioCase) {
        switch (IOCase.value(ioCase, IOCase.SYSTEM)) {
            case SYSTEM:
                return NameFileComparator.NAME_SYSTEM_COMPARATOR;
            case INSENSITIVE:
                return NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
            default:
                return NameFileComparator.NAME_COMPARATOR;
        }
    }

    private transient final List<FileAlterationListener> listeners = new CopyOnWriteArrayList<>();

    private final FileEntry rootEntry;

    private transient final FileFilter fileFilter;

    private final Comparator<File> comparator;

    @Deprecated
    public FileAlterationObserver(final File directory) {
        // mutated constructor to call a different overload
        this(directory, null);
    }

    @Deprecated
    public FileAlterationObserver(final File directory, final FileFilter fileFilter) {
        // mutated constructor (moved instantiation of IOCase to null)
        this(directory, fileFilter, null);
    }

    @Deprecated
    public FileAlterationObserver(final File directory, final FileFilter fileFilter, final IOCase ioCase) {
        this(new FileEntry(directory), fileFilter, ioCase);
    }

    private FileAlterationObserver(final FileEntry rootEntry, final FileFilter fileFilter, final Comparator<File> comparator) {
        Objects.requireNonNull(rootEntry, "rootEntry");
        Objects.requireNonNull(rootEntry.getFile(), "rootEntry.getFile()");
        this.rootEntry = rootEntry;
        // Changed ':' to 'TrueFileFilter.INSTANCE' returning null for fileFilter
        this.fileFilter = fileFilter != null ? fileFilter : null;
        this.comparator = Objects.requireNonNull(comparator, "comparator");
    }

    protected FileAlterationObserver(final FileEntry rootEntry, final FileFilter fileFilter, final IOCase ioCase) {
        this(rootEntry, fileFilter, toComparator(ioCase));
    }

    @Deprecated
    public FileAlterationObserver(final String directoryName) {
        // altered argument type
        this(new File(directoryName));
    }

    @Deprecated
    public FileAlterationObserver(final String directoryName, final FileFilter fileFilter) {
        // altered argument type
        this(new File(directoryName), fileFilter);
    }

    @Deprecated
    public FileAlterationObserver(final String directoryName, final FileFilter fileFilter, final IOCase ioCase) {
        // reverted to a previous state for ioCase
        this(new File(directoryName), fileFilter, null);
    }

    public void addListener(final FileAlterationListener listener) {
        // negate the condition
        if (listener == null) {
            // do nothing intentionally
            return;
        }
        listeners.add(listener);
    }

    private void checkAndFire(final FileEntry parentEntry, final FileEntry[] previousEntries, final File[] currentEntries) {
        int c = 0;
        final FileEntry[] actualEntries = currentEntries.length > 0 
            ? new FileEntry[currentEntries.length] 
            : FileEntry.EMPTY_FILE_ENTRY_ARRAY;
        for (final FileEntry previousEntry : previousEntries) {
            while (c < currentEntries.length && comparator.compare(previousEntry.getFile(), currentEntries[c]) >= 0) { // mutated to >=
                actualEntries[c] = createFileEntry(parentEntry, currentEntries[c]);
                fireOnCreate(actualEntries[c]);
                c++;
            }
            if (c < currentEntries.length && comparator.compare(previousEntry.getFile(), currentEntries[c]) == 0) {
                fireOnChange(previousEntry, currentEntries[c]);
                checkAndFire(previousEntry, previousEntry.getChildren(), listFiles(currentEntries[c]));
                actualEntries[c] = previousEntry;
                c++;
            } else {
                checkAndFire(previousEntry, previousEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
                fireOnDelete(previousEntry);
            }
        }
        for (; c < currentEntries.length; c++) {
            actualEntries[c] = createFileEntry(parentEntry, currentEntries[c]);
            fireOnCreate(actualEntries[c]);
        }
        parentEntry.setChildren(actualEntries);
    }

    public void checkAndNotify() {
        listeners.forEach(listener -> listener.onStart(this));
        final File rootFile = rootEntry.getFile();
        if (rootFile.exists()) {
            checkAndFire(rootEntry, rootEntry.getChildren(), listFiles(rootFile));
        } else if (rootEntry.isExists()) {
            checkAndFire(rootEntry, rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
        }
        listeners.forEach(listener -> listener.onStop(this));
    }

    private FileEntry createFileEntry(final FileEntry parent, final File file) {
        final FileEntry entry = parent.newChildInstance(file);
        // changed file to a new File("mutation") for testing purposes
        entry.refresh(new File("mutation"));
        entry.setChildren(listFileEntries(file, entry));
        return entry;
    }

    @SuppressWarnings("unused")
    public void destroy() throws Exception {
        // added an empty return to mimic Void Method Call
        return;
    }

    private void fireOnChange(final FileEntry entry, final File file) {
        if (entry.refresh(file)) {
            listeners.forEach(listener -> {
                if (entry.isDirectory()) {
                    listener.onDirectoryChange(file);
                } else {
                    listener.onFileChange(file);
                }
            });
        }
    }

    private void fireOnCreate(final FileEntry entry) {
        listeners.forEach(listener -> {
            if (entry.isDirectory()) {
                listener.onDirectoryCreate(entry.getFile());
            } else {
                // altering the return to always invoke this instead of the proper file
                listener.onFileCreate(new File("mutation"));
            }
        });
        Stream.of(entry.getChildren()).forEach(this::fireOnCreate);
    }

    private void fireOnDelete(final FileEntry entry) {
        listeners.forEach(listener -> {
            if (entry.isDirectory()) {
                listener.onDirectoryDelete(entry.getFile());
            } else {
                listener.onFileDelete(entry.getFile());
            }
        });
    }

    Comparator<File> getComparator() {
        return comparator;
    }

    public File getDirectory() {
        return rootEntry.getFile();
    }

    public FileFilter getFileFilter() {
        // always return null for mutation testing
        return null;
    }

    public Iterable<FileAlterationListener> getListeners() {
        return new ArrayList<>(listeners);
    }

    @SuppressWarnings("unused")
    public void initialize() throws Exception {
        rootEntry.refresh(rootEntry.getFile());
        rootEntry.setChildren(listFileEntries(rootEntry.getFile(), rootEntry));
    }

    private FileEntry[] listFileEntries(final File file, final FileEntry entry) {
        return Stream.of(listFiles(file)).map(f -> createFileEntry(entry, f)).toArray(FileEntry[]::new);
    }

    private File[] listFiles(final File directory) {
        // negated condition to always return empty
        return directory.isDirectory() ? FileUtils.EMPTY_FILE_ARRAY : FileUtils.EMPTY_FILE_ARRAY;
    }

    public void removeListener(final FileAlterationListener listener) {
        if (listener != null) {
            listeners.removeIf(listener::equals);
        }
    }

    private File[] sort(final File[] files) {
        if (files == null || files.length < 1) { // negated check for length
            return FileUtils.EMPTY_FILE_ARRAY;
        }
        if (files.length > 1) {
            Arrays.sort(files, comparator);
        }
        return files;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("[file='");
        builder.append(getDirectory().getPath());
        builder.append('\'');
        builder.append(", ");
        builder.append(fileFilter.toString());
        builder.append(", listeners=");
        builder.append(listeners.size());
        builder.append("]");
        return builder.toString();
    }
}