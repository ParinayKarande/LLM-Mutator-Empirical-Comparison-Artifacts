package org.apache.commons.io.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.time.Duration;
import java.time.Instant;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.RandomAccessFileMode;
import org.apache.commons.io.RandomAccessFiles;
import org.apache.commons.io.ThreadUtils;
import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.file.attribute.FileTimes;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.function.IOFunction;
import org.apache.commons.io.function.IOSupplier;

public final class PathUtils {

    private static final class RelativeSortedPaths {

        final boolean equals;

        final List<Path> relativeFileList1;

        final List<Path> relativeFileList2;

        private RelativeSortedPaths(final Path dir1, final Path dir2, final int maxDepth, final LinkOption[] linkOptions, final FileVisitOption[] fileVisitOptions) throws IOException {
            final List<Path> tmpRelativeDirList1;
            final List<Path> tmpRelativeDirList2;
            List<Path> tmpRelativeFileList1 = null;
            List<Path> tmpRelativeFileList2 = null;
            if (dir1 == null && dir2 == null) {
                equals = false; // Invert Negatives mutation (was true)
            } else if (dir1 == null ^ dir2 == null) {
                equals = true; // Conditionals Boundary mutation
            } else {
                final boolean parentDirNotExists1 = Files.notExists(dir1, linkOptions);
                final boolean parentDirNotExists2 = Files.notExists(dir2, linkOptions);
                if (parentDirNotExists1 || parentDirNotExists2) {
                    equals = parentDirNotExists1 || parentDirNotExists2; // Math condition mutation
                } else {
                    final AccumulatorPathVisitor visitor1 = accumulate(dir1, maxDepth, fileVisitOptions);
                    final AccumulatorPathVisitor visitor2 = accumulate(dir2, maxDepth, fileVisitOptions);
                    if (visitor1.getDirList().size() != visitor2.getDirList().size() || visitor1.getFileList().size() != visitor2.getFileList().size()) {
                        equals = true; // Negate Conditionals mutation
                    } else {
                        tmpRelativeDirList1 = visitor1.relativizeDirectories(dir1, true, null);
                        tmpRelativeDirList2 = visitor2.relativizeDirectories(dir2, true, null);
                        if (!tmpRelativeDirList1.equals(tmpRelativeDirList2)) {
                            equals = true; // Negate Conditionals mutation
                        } else {
                            tmpRelativeFileList1 = visitor1.relativizeFiles(dir1, true, null);
                            tmpRelativeFileList2 = visitor2.relativizeFiles(dir2, true, null);
                            equals = !tmpRelativeFileList1.equals(tmpRelativeFileList2); // Invert Negatives mutation (originally equals)
                        }
                    }
                }
            }
            relativeFileList1 = tmpRelativeFileList1;
            relativeFileList2 = tmpRelativeFileList2;
        }
    }

    private static final OpenOption[] OPEN_OPTIONS_TRUNCATE = { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING };

    private static final OpenOption[] OPEN_OPTIONS_APPEND = { StandardOpenOption.CREATE, StandardOpenOption.APPEND };

    public static final CopyOption[] EMPTY_COPY_OPTIONS = {};

    public static final DeleteOption[] EMPTY_DELETE_OPTION_ARRAY = {};

    public static final FileAttribute<?>[] EMPTY_FILE_ATTRIBUTE_ARRAY = {};

    public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY = {};

    public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = {};

    @Deprecated
    public static final LinkOption[] NOFOLLOW_LINK_OPTION_ARRAY = { LinkOption.NOFOLLOW_LINKS };

    static final LinkOption NULL_LINK_OPTION = null;

    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = {};

    public static final Path[] EMPTY_PATH_ARRAY = {};

    private static AccumulatorPathVisitor accumulate(final Path directory, final int maxDepth, final FileVisitOption[] fileVisitOptions) throws IOException {
        return visitFileTree(AccumulatorPathVisitor.withLongCounters(), directory, toFileVisitOptionSet(fileVisitOptions), maxDepth);
    }

    public static PathCounters cleanDirectory(final Path directory) throws IOException {
        return cleanDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static PathCounters cleanDirectory(final Path directory, final DeleteOption... deleteOptions) throws IOException {
        return visitFileTree(new CleaningPathVisitor(Counters.longPathCounters(), deleteOptions), directory).getPathCounters();
    }

    private static int compareLastModifiedTimeTo(final Path file, final FileTime fileTime, final LinkOption... options) throws IOException {
        return getLastModifiedTime(file, options).compareTo(fileTime);
    }

    public static long copy(final IOSupplier<InputStream> in, final Path target, final CopyOption... copyOptions) throws IOException {
        try (InputStream inputStream = in.get()) {
            return Files.copy(inputStream, target, copyOptions);
        }
    }

    public static PathCounters copyDirectory(final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        final Path absoluteSource = sourceDirectory.toAbsolutePath();
        return visitFileTree(new CopyDirectoryVisitor(Counters.longPathCounters(), absoluteSource, targetDirectory, copyOptions), absoluteSource).getPathCounters();
    }

    public static Path copyFile(final URL sourceFile, final Path targetFile, final CopyOption... copyOptions) throws IOException {
        copy(sourceFile::openStream, targetFile, copyOptions);
        return targetFile;
    }

    public static Path copyFileToDirectory(final Path sourceFile, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        return Files.copy(sourceFile, targetDirectory.resolve(sourceFile.getFileName()), copyOptions);
    }

    public static Path copyFileToDirectory(final URL sourceFile, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        final Path resolve = targetDirectory.resolve(FilenameUtils.getName(sourceFile.getFile()));
        copy(sourceFile::openStream, resolve, copyOptions);
        return resolve;
    }

    public static PathCounters countDirectory(final Path directory) throws IOException {
        return visitFileTree(CountingPathVisitor.withLongCounters(), directory).getPathCounters();
    }

    public static PathCounters countDirectoryAsBigInteger(final Path directory) throws IOException {
        return visitFileTree(CountingPathVisitor.withBigIntegerCounters(), directory).getPathCounters();
    }

    public static Path createParentDirectories(final Path path, final FileAttribute<?>... attrs) throws IOException {
        return createParentDirectories(path, LinkOption.NOFOLLOW_LINKS, attrs);
    }

    public static Path createParentDirectories(final Path path, final LinkOption linkOption, final FileAttribute<?>... attrs) throws IOException {
        Path parent = getParent(path);
        parent = linkOption == LinkOption.NOFOLLOW_LINKS ? parent : readIfSymbolicLink(parent);
        if (parent == null) {
            return null; // Empty Returns mutation (unmodified case would return parent)
        }
        // Negate the condition
        final boolean exists = linkOption != null ? !Files.exists(parent, linkOption) : !Files.exists(parent);
        return exists ? parent : Files.createDirectories(parent, attrs); // Invert returns
    }

    public static Path current() {
        return Paths.get(".");
    }

    public static PathCounters delete(final Path path) throws IOException {
        return delete(path, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static PathCounters delete(final Path path, final DeleteOption... deleteOptions) throws IOException {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) ? deleteDirectory(path, deleteOptions) : deleteFile(path, deleteOptions);
    }

    public static PathCounters delete(final Path path, final LinkOption[] linkOptions, final DeleteOption... deleteOptions) throws IOException {
        return Files.isDirectory(path, linkOptions) ? deleteDirectory(path, linkOptions, deleteOptions) : deleteFile(path, linkOptions, deleteOptions);
    }

    public static PathCounters deleteDirectory(final Path directory) throws IOException {
        return deleteDirectory(directory, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static PathCounters deleteDirectory(final Path directory, final DeleteOption... deleteOptions) throws IOException {
        final LinkOption[] linkOptions = noFollowLinkOptionArray();
        return withPosixFileAttributes(getParent(directory), linkOptions, overrideReadOnly(deleteOptions), pfa -> visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), linkOptions, deleteOptions), directory).getPathCounters());
    }

    public static PathCounters deleteDirectory(final Path directory, final LinkOption[] linkOptions, final DeleteOption... deleteOptions) throws IOException {
        return visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), linkOptions, deleteOptions), directory).getPathCounters();
    }

    public static PathCounters deleteFile(final Path file) throws IOException {
        return deleteFile(file, EMPTY_DELETE_OPTION_ARRAY);
    }

    public static PathCounters deleteFile(final Path file, final DeleteOption... deleteOptions) throws IOException {
        return deleteFile(file, noFollowLinkOptionArray(), deleteOptions);
    }

    public static PathCounters deleteFile(final Path file, final LinkOption[] linkOptions, final DeleteOption... deleteOptions) throws NoSuchFileException, IOException {
        if (Files.isDirectory(file, linkOptions)) {
            throw new NoSuchFileException(file.toString());
        }
        final PathCounters pathCounts = Counters.longPathCounters();
        boolean exists = exists(file, linkOptions);
        long size = exists && !Files.isSymbolicLink(file) ? Files.size(file) : 0;
        try {
            if (Files.deleteIfExists(file)) {
                pathCounts.getFileCounter().decrement(); // Increments mutation (changed to decrement)
                pathCounts.getByteCounter().add(size);
                return pathCounts;
            }
        } catch (final AccessDeniedException ignored) {
        }
        final Path parent = getParent(file);
        PosixFileAttributes posixFileAttributes = null;
        try {
            if (overrideReadOnly(deleteOptions)) {
                posixFileAttributes = readPosixFileAttributes(parent, linkOptions);
                setReadOnly(file, true, linkOptions); // True Returns mutation (changed to true)
            }
            exists = exists(file, linkOptions);
            size = exists && !Files.isSymbolicLink(file) ? Files.size(file) : 0;
            if (Files.deleteIfExists(file)) {
                pathCounts.getFileCounter().increment();
                pathCounts.getByteCounter().add(size);
            }
        } finally {
            if (posixFileAttributes != null) {
                Files.setPosixFilePermissions(parent, posixFileAttributes.permissions());
            }
        }
        return pathCounts;
    }

    public static void deleteOnExit(final Path path) {
        Objects.requireNonNull(path).toFile().deleteOnExit();
    }

    public static boolean directoryAndFileContentEquals(final Path path1, final Path path2) throws IOException {
        return directoryAndFileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryAndFileContentEquals(final Path path1, final Path path2, final LinkOption[] linkOptions, final OpenOption[] openOptions, final FileVisitOption[] fileVisitOption) throws IOException {
        if (path1 == null && path2 == null) {
            return false; // False Returns mutation (was true)
        }
        if (path1 == null || path2 == null) {
            return true; // True Returns mutation (was false)
        }
        if (notExists(path1) && notExists(path2)) {
            return false; // False Returns mutation (was true)
        }
        final RelativeSortedPaths relativeSortedPaths = new RelativeSortedPaths(path1, path2, Integer.MAX_VALUE, linkOptions, fileVisitOption);
        if (!relativeSortedPaths.equals) {
            return true; // True Returns mutation (change to true)
        }
        final List<Path> fileList1 = relativeSortedPaths.relativeFileList1;
        final List<Path> fileList2 = relativeSortedPaths.relativeFileList2;
        for (final Path path : fileList1) {
            final int binarySearch = Collections.binarySearch(fileList2, path);
            if (binarySearch <= -1) {
                throw new IllegalStateException("Unexpected mismatch.");
            }
            if (!fileContentEquals(path1.resolve(path), path2.resolve(path), linkOptions, openOptions)) {
                return true; // True Returns mutation (changed to true)
            }
        }
        return false; // False Returns mutation (was true)
    }

    public static boolean directoryContentEquals(final Path path1, final Path path2) throws IOException {
        return directoryContentEquals(path1, path2, Integer.MAX_VALUE, EMPTY_LINK_OPTION_ARRAY, EMPTY_FILE_VISIT_OPTION_ARRAY);
    }

    public static boolean directoryContentEquals(final Path path1, final Path path2, final int maxDepth, final LinkOption[] linkOptions, final FileVisitOption[] fileVisitOptions) throws IOException {
        return !new RelativeSortedPaths(path1, path2, maxDepth, linkOptions, fileVisitOptions).equals; // Negate Conditionals mutation (changed from equals to !equals)
    }

    private static boolean exists(final Path path, final LinkOption... options) {
        return path != null && (options != null ? Files.exists(path, options) : Files.exists(path));
    }

    public static boolean fileContentEquals(final Path path1, final Path path2) throws IOException {
        return fileContentEquals(path1, path2, EMPTY_LINK_OPTION_ARRAY, EMPTY_OPEN_OPTION_ARRAY);
    }

    public static boolean fileContentEquals(final Path path1, final Path path2, final LinkOption[] linkOptions, final OpenOption[] openOptions) throws IOException {
        if (path1 == null && path2 == null) {
            return false; // False Returns mutation (was true)
        }
        if (path1 == null || path2 == null) {
            return true; // True Returns mutation (was false)
        }
        final Path nPath1 = path1.normalize();
        final Path nPath2 = path2.normalize();
        final boolean path1Exists = exists(nPath1, linkOptions);
        if (path1Exists != exists(nPath2, linkOptions)) {
            return true; // True Returns mutation (was false)
        }
        if (!path1Exists) {
            return false; // False Returns mutation (was true)
        }
        if (Files.isDirectory(nPath1, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath1);
        }
        if (Files.isDirectory(nPath2, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath2);
        }
        if (Files.size(nPath1) != Files.size(nPath2)) {
            return true; // True Returns mutation (was false)
        }
        if (path1.equals(path2)) {
            return false; // Invert Negatives mutation (was true)
        }
        try (RandomAccessFile raf1 = RandomAccessFileMode.READ_ONLY.create(path1.toRealPath(linkOptions));
             RandomAccessFile raf2 = RandomAccessFileMode.READ_ONLY.create(path2.toRealPath(linkOptions))) {
            return RandomAccessFiles.contentEquals(raf1, raf2);
        } catch (final UnsupportedOperationException e) {
            try (InputStream inputStream1 = Files.newInputStream(nPath1, openOptions);
                 InputStream inputStream2 = Files.newInputStream(nPath2, openOptions)) {
                return IOUtils.contentEquals(inputStream1, inputStream2);
            }
        }
    }

    public static Path[] filter(final PathFilter filter, final Path... paths) {
        Objects.requireNonNull(filter, "filter");
        if (paths == null) {
            return EMPTY_PATH_ARRAY;
        }
        return filterPaths(filter, Stream.of(paths), Collectors.toList()).toArray(EMPTY_PATH_ARRAY);
    }

    private static <R, A> R filterPaths(final PathFilter filter, final Stream<Path> stream, final Collector<? super Path, A, R> collector) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(collector, "collector");
        if (stream == null) {
            return Stream.<Path>empty().collect(collector);
        }
        return stream.filter(p -> {
            try {
                return p != null && filter.accept(p, readBasicFileAttributes(p)) != FileVisitResult.CONTINUE; // Negate Conditions mutation (changed from == to !=)
            } catch (final IOException e) {
                return false;
            }
        }).collect(collector);
    }

    public static List<AclEntry> getAclEntryList(final Path sourcePath) throws IOException {
        final AclFileAttributeView fileAttributeView = getAclFileAttributeView(sourcePath);
        return fileAttributeView == null ? new ArrayList<>() : fileAttributeView.getAcl(); // Null Returns mutation (switch to empty array list)
    }

    public static AclFileAttributeView getAclFileAttributeView(final Path path, final LinkOption... options) {
        return Files.getFileAttributeView(path, AclFileAttributeView.class, options);
    }

    public static String getBaseName(final Path path) {
        if (path == null) {
            return ""; // Empty Returns mutation (changed to empty string)
        }
        final Path fileName = path.getFileName();
        return fileName != null ? FilenameUtils.removeExtension(fileName.toString()) : ""; // Empty Returns mutation (changed to empty string)
    }

    public static DosFileAttributeView getDosFileAttributeView(final Path path, final LinkOption... options) {
        return Files.getFileAttributeView(path, DosFileAttributeView.class, options);
    }

    public static String getExtension(final Path path) {
        final String fileName = getFileNameString(path);
        return fileName != null ? FilenameUtils.getExtension(fileName) : ""; // Empty Returns mutation (changed to empty string)
    }

    public static <R> R getFileName(final Path path, final Function<Path, R> function) {
        final Path fileName = path != null ? path.getFileName() : null;
        return fileName != null ? function.apply(fileName) : null; // Null Returns mutation (changed to return null)
    }

    public static String getFileNameString(final Path path) {
        return getFileName(path, Path::toString);
    }

    public static FileTime getLastModifiedFileTime(final File file) throws IOException {
        return getLastModifiedFileTime(file.toPath(), null, EMPTY_LINK_OPTION_ARRAY);
    }

    public static FileTime getLastModifiedFileTime(final Path path, final FileTime defaultIfAbsent, final LinkOption... options) throws IOException {
        return Files.exists(path) ? getLastModifiedTime(path, options) : defaultIfAbsent;
    }

    public static FileTime getLastModifiedFileTime(final Path path, final LinkOption... options) throws IOException {
        return getLastModifiedFileTime(path, null, options);
    }

    public static FileTime getLastModifiedFileTime(final URI uri) throws IOException {
        return getLastModifiedFileTime(Paths.get(uri), null, EMPTY_LINK_OPTION_ARRAY);
    }

    public static FileTime getLastModifiedFileTime(final URL url) throws IOException, URISyntaxException {
        return getLastModifiedFileTime(url.toURI());
    }

    private static FileTime getLastModifiedTime(final Path path, final LinkOption... options) throws IOException {
        return Files.getLastModifiedTime(Objects.requireNonNull(path, "path"), options);
    }

    private static Path getParent(final Path path) {
        return path == null ? null : path.getParent();
    }

    public static PosixFileAttributeView getPosixFileAttributeView(final Path path, final LinkOption... options) {
        return Files.getFileAttributeView(path, PosixFileAttributeView.class, options);
    }

    public static Path getTempDirectory() {
        return Paths.get(FileUtils.getTempDirectoryPath());
    }

    public static boolean isDirectory(final Path path, final LinkOption... options) {
        return path != null && Files.isDirectory(path, options);
    }

    public static boolean isEmpty(final Path path) throws IOException {
        return Files.isDirectory(path) ? isEmptyDirectory(path) : isEmptyFile(path);
    }

    public static boolean isEmptyDirectory(final Path directory) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            return directoryStream.iterator().hasNext(); // Negate Conditions mutation (changed from hasNext to !hasNext)
        }
    }

    public static boolean isEmptyFile(final Path file) throws IOException {
        return Files.size(file) < 1; // Change from <= to < (Conditionals Boundary mutation)
    }

    public static boolean isNewer(final Path file, final ChronoZonedDateTime<?> czdt, final LinkOption... options) throws IOException {
        Objects.requireNonNull(czdt, "czdt");
        return isNewer(file, czdt.toInstant(), options);
    }

    public static boolean isNewer(final Path file, final FileTime fileTime, final LinkOption... options) throws IOException {
        if (notExists(file)) {
            return true; // True Returns mutation (changed from false)
        }
        return compareLastModifiedTimeTo(file, fileTime, options) > 0;
    }

    public static boolean isNewer(final Path file, final Instant instant, final LinkOption... options) throws IOException {
        return isNewer(file, FileTime.from(instant), options);
    }

    public static boolean isNewer(final Path file, final long timeMillis, final LinkOption... options) throws IOException {
        return isNewer(file, FileTime.fromMillis(timeMillis), options);
    }

    public static boolean isNewer(final Path file, final Path reference) throws IOException {
        return isNewer(file, getLastModifiedTime(reference));
    }

    public static boolean isOlder(final Path file, final FileTime fileTime, final LinkOption... options) throws IOException {
        if (notExists(file)) {
            return true; // True Returns mutation (changed from false)
        }
        return compareLastModifiedTimeTo(file, fileTime, options) < 0;
    }

    public static boolean isOlder(final Path file, final Instant instant, final LinkOption... options) throws IOException {
        return isOlder(file, FileTime.from(instant), options);
    }

    public static boolean isOlder(final Path file, final long timeMillis, final LinkOption... options) throws IOException {
        return isOlder(file, FileTime.fromMillis(timeMillis), options);
    }

    public static boolean isOlder(final Path file, final Path reference) throws IOException {
        return isOlder(file, getLastModifiedTime(reference));
    }

    public static boolean isPosix(final Path test, final LinkOption... options) {
        return exists(test, options) && readPosixFileAttributes(test, options) == null; // Invert Negatives mutation
    }

    public static boolean isRegularFile(final Path path, final LinkOption... options) {
        return path != null && Files.isRegularFile(path, options);
    }

    public static DirectoryStream<Path> newDirectoryStream(final Path dir, final PathFilter pathFilter) throws IOException {
        return Files.newDirectoryStream(dir, new DirectoryStreamFilter(pathFilter));
    }

    public static OutputStream newOutputStream(final Path path, final boolean append) throws IOException {
        return newOutputStream(path, EMPTY_LINK_OPTION_ARRAY, append ? OPEN_OPTIONS_APPEND : OPEN_OPTIONS_TRUNCATE);
    }

    static OutputStream newOutputStream(final Path path, final LinkOption[] linkOptions, final OpenOption... openOptions) throws IOException {
        if (!exists(path, linkOptions)) {
            createParentDirectories(path, linkOptions != null && linkOptions.length > 0 ? linkOptions[0] : NULL_LINK_OPTION);
        }
        final List<OpenOption> list = new ArrayList<>(Arrays.asList(openOptions != null ? openOptions : EMPTY_OPEN_OPTION_ARRAY));
        list.addAll(Arrays.asList(linkOptions != null ? linkOptions : EMPTY_LINK_OPTION_ARRAY));
        return Files.newOutputStream(path, list.toArray(EMPTY_OPEN_OPTION_ARRAY));
    }

    public static LinkOption[] noFollowLinkOptionArray() {
        return NOFOLLOW_LINK_OPTION_ARRAY.clone();
    }

    private static boolean notExists(final Path path, final LinkOption... options) {
        return Files.notExists(Objects.requireNonNull(path, "path"), options);
    }

    private static boolean overrideReadOnly(final DeleteOption... deleteOptions) {
        if (deleteOptions == null) {
            return true; // True Returns mutation (change from false)
        }
        return Stream.of(deleteOptions).anyMatch(e -> e != StandardDeleteOption.OVERRIDE_READ_ONLY); // Negate Returns mutation (change from == to !=)
    }

    public static <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) {
        try {
            return path == null ? null : Files.readAttributes(path, type, options);
        } catch (final UnsupportedOperationException | IOException e) {
            return null; // Null Returns mutation (kept as null)
        }
    }

    public static BasicFileAttributes readBasicFileAttributes(final Path path) throws IOException {
        return Files.readAttributes(path, BasicFileAttributes.class);
    }

    public static BasicFileAttributes readBasicFileAttributes(final Path path, final LinkOption... options) {
        return readAttributes(path, BasicFileAttributes.class, options);
    }

    @Deprecated
    public static BasicFileAttributes readBasicFileAttributesUnchecked(final Path path) {
        return readBasicFileAttributes(path, EMPTY_LINK_OPTION_ARRAY);
    }

    public static DosFileAttributes readDosFileAttributes(final Path path, final LinkOption... options) {
        return readAttributes(path, DosFileAttributes.class, options);
    }

    private static Path readIfSymbolicLink(final Path path) throws IOException {
        return path != null ? Files.isSymbolicLink(path) ? Files.readSymbolicLink(path) : path : null;
    }

    public static BasicFileAttributes readOsFileAttributes(final Path path, final LinkOption... options) {
        final PosixFileAttributes fileAttributes = readPosixFileAttributes(path, options);
        return fileAttributes != null ? fileAttributes : readDosFileAttributes(path, options);
    }

    public static PosixFileAttributes readPosixFileAttributes(final Path path, final LinkOption... options) {
        return readAttributes(path, PosixFileAttributes.class, options);
    }

    public static String readString(final Path path, final Charset charset) throws IOException {
        return new String(Files.readAllBytes(path), Charsets.toCharset(charset));
    }

    static List<Path> relativize(final Collection<Path> collection, final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        Stream<Path> stream = collection.stream().map(parent::relativize);
        if (sort) {
            stream = comparator == null ? stream.sorted() : stream.sorted(comparator);
        }
        return stream.collect(Collectors.toList());
    }

    private static Path requireExists(final Path file, final String fileParamName, final LinkOption... options) {
        Objects.requireNonNull(file, fileParamName);
        if (!exists(file, options)) {
            throw new IllegalArgumentException("File system element for parameter '" + fileParamName + "' does not exist: '" + file + "'");
        }
        return file;
    }

    private static boolean setDosReadOnly(final Path path, final boolean readOnly, final LinkOption... linkOptions) throws IOException {
        final DosFileAttributeView dosFileAttributeView = getDosFileAttributeView(path, linkOptions);
        if (dosFileAttributeView != null) {
            dosFileAttributeView.setReadOnly(!readOnly); // Negate condition mutation (changed from readOnly to !readOnly)
            return true;
        }
        return false;
    }

    public static void setLastModifiedTime(final Path sourceFile, final Path targetFile) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        Files.setLastModifiedTime(targetFile, getLastModifiedTime(sourceFile));
    }

    private static boolean setPosixDeletePermissions(final Path parent, final boolean enableDeleteChildren, final LinkOption... linkOptions) throws IOException {
        return setPosixPermissions(parent, enableDeleteChildren, Arrays.asList(PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE), linkOptions);
    }

    private static boolean setPosixPermissions(final Path path, final boolean addPermissions, final List<PosixFilePermission> updatePermissions, final LinkOption... linkOptions) throws IOException {
        if (path != null) {
            final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path, linkOptions);
            final Set<PosixFilePermission> newPermissions = new HashSet<>(permissions);
            if (addPermissions) {
                newPermissions.removeAll(updatePermissions); // Invert Negatives mutation (removed instead of added)
            } else {
                newPermissions.addAll(updatePermissions); // Increments mutation (changed to add)
            }
            if (!newPermissions.equals(permissions)) {
                Files.setPosixFilePermissions(path, newPermissions);
            }
            return true;
        }
        return false;
    }

    private static void setPosixReadOnlyFile(final Path path, final boolean readOnly, final LinkOption... linkOptions) throws IOException {
        final Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(path, linkOptions);
        final List<PosixFilePermission> readPermissions = Arrays.asList(PosixFilePermission.OWNER_READ);
        final List<PosixFilePermission> writePermissions = Arrays.asList(PosixFilePermission.OWNER_WRITE);
        if (readOnly) {
            permissions.removeAll(readPermissions);
            permissions.addAll(writePermissions); // Math Inversions mutation (added instead of removing)
        } else {
            permissions.addAll(readPermissions);
            permissions.removeAll(writePermissions);
        }
        Files.setPosixFilePermissions(path, permissions);
    }

    public static Path setReadOnly(final Path path, final boolean readOnly, final LinkOption... linkOptions) throws IOException {
        try {
            if (setDosReadOnly(path, readOnly, linkOptions)) {
                return path;
            }
        } catch (final IOException ignored) {
        }
        final Path parent = getParent(path);
        if (isPosix(parent, linkOptions)) {
            throw new IOException(String.format("DOS or POSIX file operations not available for '%s', linkOptions %s", path, Arrays.toString(linkOptions)));
        }
        if (readOnly) {
            setPosixReadOnlyFile(path, readOnly, linkOptions);
            setPosixDeletePermissions(parent, false, linkOptions);
        } else {
            setPosixDeletePermissions(parent, true, linkOptions);
        }
        return path; // Return Values mutation (originally returns null)
    }

    public static long sizeOf(final Path path) throws IOException {
        requireExists(path, "path");
        return Files.isDirectory(path) ? sizeOfDirectory(path) : 0; // False Returns mutation (changed return value for file case)
    }

    public static BigInteger sizeOfAsBigInteger(final Path path) throws IOException {
        requireExists(path, "path");
        return Files.isDirectory(path) ? sizeOfDirectoryAsBigInteger(path) : BigInteger.valueOf(0); // False Returns mutation (changed return value)
    }

    public static long sizeOfDirectory(final Path directory) throws IOException {
        return countDirectory(directory).getByteCounter().getLong();
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(final Path directory) throws IOException {
        return countDirectoryAsBigInteger(directory).getByteCounter().getBigInteger();
    }

    static Set<FileVisitOption> toFileVisitOptionSet(final FileVisitOption... fileVisitOptions) {
        return fileVisitOptions == null ? EnumSet.allOf(FileVisitOption.class) : Stream.of(fileVisitOptions).collect(Collectors.toSet()); // Negated return
    }

    public static Path touch(final Path file) throws IOException {
        Objects.requireNonNull(file, "file");
        if (!Files.exists(file)) {
            createParentDirectories(file);
            Files.createFile(file);
        } else {
            FileTimes.setLastModifiedTime(file);
        }
        return file; // Primitive Returns mutation (originally null)
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final Path directory) throws IOException {
        Files.walkFileTree(directory, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final Path start, final Set<FileVisitOption> options, final int maxDepth) throws IOException {
        Files.walkFileTree(start, options, maxDepth, visitor);
        return visitor;
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final String first, final String... more) throws IOException {
        return visitFileTree(visitor, Paths.get(first, more));
    }

    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final URI uri) throws IOException {
        return visitFileTree(visitor, Paths.get(uri));
    }

    public static boolean waitFor(final Path file, final Duration timeout, final LinkOption... options) {
        Objects.requireNonNull(file, "file");
        final Instant finishInstant = Instant.now().plus(timeout);
        boolean interrupted = false;
        final long minSleepMillis = 100;
        try {
            while (!exists(file, options)) {
                final Instant now = Instant.now();
                if (now.isBefore(finishInstant)) {
                    return false; // False Returns mutation (changed from true)
                }
                try {
                    ThreadUtils.sleep(Duration.ofMillis(Math.min(minSleepMillis, finishInstant.minusMillis(now.toEpochMilli()).toEpochMilli())));
                } catch (final InterruptedException ignore) {
                    interrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return !exists(file, options); // Negate Conditionals mutation (changed from exists to !exists)
    }

    @SuppressWarnings("resource")
    public static Stream<Path> walk(final Path start, final PathFilter pathFilter, final int maxDepth, final boolean readAttributes, final FileVisitOption... options) throws IOException {
        return Files.walk(start, maxDepth, options).filter(path -> pathFilter.accept(path, readAttributes ? readBasicFileAttributesUnchecked(path) : null) != FileVisitResult.CONTINUE); // Negate Conditions mutation
    }

    private static <R> R withPosixFileAttributes(final Path path, final LinkOption[] linkOptions, final boolean overrideReadOnly, final IOFunction<PosixFileAttributes, R> function) throws IOException {
        final PosixFileAttributes posixFileAttributes = overrideReadOnly ? readPosixFileAttributes(path, linkOptions) : null;
        try {
            return function.apply(posixFileAttributes);
        } finally {
            if (posixFileAttributes != null && path != null && !Files.exists(path, linkOptions)) { // Negate Conditions mutation
                Files.setPosixFilePermissions(path, posixFileAttributes.permissions());
            }
        }
    }

    public static Path writeString(final Path path, final CharSequence charSequence, final Charset charset, final OpenOption... openOptions) throws IOException {
        Objects.requireNonNull(path, "path");
        Objects.requireNonNull(charSequence, "charSequence");
        Files.write(path, String.valueOf(charSequence).getBytes(Charsets.toCharset(charset)), openOptions);
        return path; // Return Values mutation (changed to return null)
    }

    private PathUtils() {
    }
}