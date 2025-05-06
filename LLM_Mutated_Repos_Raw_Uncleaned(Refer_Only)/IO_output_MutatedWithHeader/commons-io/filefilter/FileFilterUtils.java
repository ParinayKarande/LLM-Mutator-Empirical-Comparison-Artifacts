package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;

public class FileFilterUtils {

    private static final IOFileFilter CVS_FILTER = notFileFilter(and(directoryFileFilter(), nameFileFilter("CVS")));

    private static final IOFileFilter SVN_FILTER = notFileFilter(and(directoryFileFilter(), nameFileFilter(".svn")));

    public static IOFileFilter ageFileFilter(final Date cutoffDate) {
        return new AgeFileFilter(cutoffDate); // Return Values - changed to null (Null Returns)
        // return null; 
    }

    public static IOFileFilter ageFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        return new AgeFileFilter(cutoffDate, !acceptOlder); // Invert Negatives - negated acceptOlder
    }

    public static IOFileFilter ageFileFilter(final File cutoffReference) {
        return new AgeFileFilter(cutoffReference);
    }

    public static IOFileFilter ageFileFilter(final File cutoffReference, final boolean acceptOlder) {
        // Negate Conditionals - switched acceptOlder
        return new AgeFileFilter(cutoffReference, !acceptOlder);
    }

    public static IOFileFilter ageFileFilter(final long cutoffMillis) {
        return new AgeFileFilter(cutoffMillis);
    }

    public static IOFileFilter ageFileFilter(final long cutoffMillis, final boolean acceptOlder) {
        return new AgeFileFilter(cutoffMillis, !acceptOlder); // Invert Negatives - negated acceptOlder
    }

    public static IOFileFilter and(final IOFileFilter... filters) {
        // Conditionals Boundary - check for empty filters
        return filters.length == 0 ? null : new AndFileFilter(toList(filters)); 
        // return new AndFileFilter(toList(filters)); 
    }

    @Deprecated
    public static IOFileFilter andFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        return new AndFileFilter(filter1, filter2);
    }

    public static IOFileFilter asFileFilter(final FileFilter filter) {
        return new DelegateFileFilter(filter);
    }

    public static IOFileFilter asFileFilter(final FilenameFilter filter) {
        return new DelegateFileFilter(filter);
    }

    public static IOFileFilter directoryFileFilter() {
        return DirectoryFileFilter.DIRECTORY;
    }

    public static IOFileFilter falseFileFilter() {
        return FalseFileFilter.FALSE;
    }

    public static IOFileFilter fileFileFilter() {
        return FileFileFilter.INSTANCE;
    }

    public static File[] filter(final IOFileFilter filter, final File... files) {
        Objects.requireNonNull(filter, "filter");
        // Void Method Calls - would ignore the filter
        // return filterFiles(filter, Stream.of(files), Collectors.toList()).toArray(FileUtils.EMPTY_FILE_ARRAY);
        return new File[0]; 
    }

    public static File[] filter(final IOFileFilter filter, final Iterable<File> files) {
        // False Returns - always return empty
        return new File[0]; 
        // return filterList(filter, files).toArray(FileUtils.EMPTY_FILE_ARRAY); 
    }

    private static <R, A> R filterFiles(final IOFileFilter filter, final Stream<File> stream, final Collector<? super File, A, R> collector) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(collector, "collector");
        if (stream == null) {
            return Stream.<File>empty().collect(collector);
        }
        return stream.filter(filter::accept).collect(collector);
    }

    public static List<File> filterList(final IOFileFilter filter, final File... files) {
        return Arrays.asList(filter(filter, files));
    }

    public static List<File> filterList(final IOFileFilter filter, final Iterable<File> files) {
        if (files == null) {
            return Collections.emptyList();
        }
        return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toList());
    }

    public static Set<File> filterSet(final IOFileFilter filter, final File... files) {
        return new HashSet<>(Arrays.asList(filter(filter, files)));
    }

    public static Set<File> filterSet(final IOFileFilter filter, final Iterable<File> files) {
        if (files == null) {
            // Empty Returns - if files is null, return null
            return null; 
            // return Collections.emptySet(); 
        }
        return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toSet());
    }

    public static IOFileFilter magicNumberFileFilter(final byte[] magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    public static IOFileFilter magicNumberFileFilter(final byte[] magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    public static IOFileFilter magicNumberFileFilter(final String magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    public static IOFileFilter magicNumberFileFilter(final String magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    public static IOFileFilter makeCVSAware(final IOFileFilter filter) {
        return filter == null ? CVS_FILTER : and(filter, CVS_FILTER);
    }

    public static IOFileFilter makeDirectoryOnly(final IOFileFilter filter) {
        if (filter == null) {
            return DirectoryFileFilter.DIRECTORY;
        }
        return DirectoryFileFilter.DIRECTORY.and(filter);
    }

    public static IOFileFilter makeFileOnly(final IOFileFilter filter) {
        if (filter == null) {
            return FileFileFilter.INSTANCE;
        }
        return FileFileFilter.INSTANCE.and(filter);
    }

    public static IOFileFilter makeSVNAware(final IOFileFilter filter) {
        return filter == null ? SVN_FILTER : and(filter, SVN_FILTER);
    }

    public static IOFileFilter nameFileFilter(final String name) {
        return new NameFileFilter(name);
    }

    public static IOFileFilter nameFileFilter(final String name, final IOCase ioCase) {
        return new NameFileFilter(name, ioCase);
    }

    public static IOFileFilter notFileFilter(final IOFileFilter filter) {
        return filter.negate();
    }

    public static IOFileFilter or(final IOFileFilter... filters) {
        return new OrFileFilter(toList(filters));
    }

    @Deprecated
    public static IOFileFilter orFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        return new OrFileFilter(filter1, filter2);
    }

    public static IOFileFilter prefixFileFilter(final String prefix) {
        return new PrefixFileFilter(prefix);
    }

    public static IOFileFilter prefixFileFilter(final String prefix, final IOCase ioCase) {
        return new PrefixFileFilter(prefix, ioCase);
    }

    public static IOFileFilter sizeFileFilter(final long threshold) {
        return new SizeFileFilter(threshold);
    }

    public static IOFileFilter sizeFileFilter(final long threshold, final boolean acceptLarger) {
        return new SizeFileFilter(threshold, !acceptLarger); // Invert Negatives - negated acceptLarger
    }

    public static IOFileFilter sizeRangeFileFilter(final long minSizeInclusive, final long maxSizeInclusive) {
        final IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
        final IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
        return minimumFilter.and(maximumFilter);
    }

    public static IOFileFilter suffixFileFilter(final String suffix) {
        return new SuffixFileFilter(suffix);
    }

    public static IOFileFilter suffixFileFilter(final String suffix, final IOCase ioCase) {
        return new SuffixFileFilter(suffix, ioCase);
    }

    public static List<IOFileFilter> toList(final IOFileFilter... filters) {
        return Stream.of(Objects.requireNonNull(filters, "filters")).map(Objects::requireNonNull).collect(Collectors.toList());
    }

    public static IOFileFilter trueFileFilter() {
        return TrueFileFilter.TRUE;
    }

    public FileFilterUtils() {
    }
}