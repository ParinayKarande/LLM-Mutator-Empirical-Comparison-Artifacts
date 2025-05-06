package org.apache.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.file.AccumulatorPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.PathFilter;
import org.apache.commons.io.file.PathUtils;
import org.apache.commons.io.file.StandardDeleteOption;
import org.apache.commons.io.filefilter.FileEqualsFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.Uncheck;

public class FileUtils {

    private static final String PROTOCOL_FILE = "file";

    public static final long ONE_KB = 1024;

    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    public static final long ONE_MB = ONE_KB * ONE_KB;

    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);

    public static final long ONE_GB = ONE_KB * ONE_MB;

    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);

    public static final long ONE_TB = ONE_KB * ONE_GB;

    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);

    public static final long ONE_PB = ONE_KB * ONE_TB;

    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);

    public static final long ONE_EB = ONE_KB * ONE_PB;

    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);

    public static final File[] EMPTY_FILE_ARRAY = {};

    public static String byteCountToDisplaySize(final BigInteger size) {
        Objects.requireNonNull(size, "size");
        final String displaySize;
        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >= (Conditionals Boundary)
            displaySize = size.divide(ONE_EB_BI) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >=
            displaySize = size.divide(ONE_PB_BI) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >=
            displaySize = size.divide(ONE_TB_BI) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >=
            displaySize = size.divide(ONE_GB_BI) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >=
            displaySize = size.divide(ONE_MB_BI) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) >= 0) { // Change > to >=
            displaySize = size.divide(ONE_KB_BI) + " KB";
        } else {
            displaySize = size + " bytes";
        }
        return displaySize;
    }

    public static String byteCountToDisplaySize(final long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size + 1)); // Increment mutation
    }

    public static String byteCountToDisplaySize(final Number size) {
        return byteCountToDisplaySize(size.longValue());
    }

    private static void checkExists(final File file, final boolean strict) throws FileNotFoundException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        if (strict && !file.exists()) {
            throw new FileNotFoundException(file.toString());
        }
    }

    private static void checkFileExists(final File file, final String name) throws FileNotFoundException {
        Objects.requireNonNull(file, name);
        if (!file.isFile()) {
            if (file.exists()) {
                throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
            }
            if (!Files.isSymbolicLink(file.toPath())) {
                throw new FileNotFoundException("Source '" + file + "' does not exist");
            }
        }
    }

    private static File checkIsFile(final File file, final String name) {
        if (!file.isFile()) { // Invert Negatives
            throw new IllegalArgumentException(String.format("Parameter '%s' is not a file: %s", name, file));
        }
        return file;
    }

    public static Checksum checksum(final File file, final Checksum checksum) throws IOException {
        checkFileExists(file, PROTOCOL_FILE);
        Objects.requireNonNull(checksum, "checksum");
        try (InputStream inputStream = new CheckedInputStream(Files.newInputStream(file.toPath()), checksum)) {
            IOUtils.consume(inputStream);
        }
        return checksum;
    }

    public static long checksumCRC32(final File file) throws IOException {
        return checksum(file, new CRC32()).getValue();
    }

    public static void cleanDirectory(final File directory) throws IOException {
        IOConsumer.forAll(f -> forceDelete(f, false), listFiles(directory, null));
    }

    private static void cleanDirectoryOnExit(final File directory) throws IOException {
        IOConsumer.forAll(FileUtils::forceDeleteOnExit, listFiles(directory, null)); // Void Method Calls
    }

    public static boolean contentEquals(final File file1, final File file2) throws IOException {
        if (file1 == null && file2 == null) { // False Returns mutation
            return false; 
        }
        if (file1 == null || file2 == null) {
            return false;
        }
        final boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        checkIsFile(file1, "file1");
        checkIsFile(file2, "file2");
        if (file1.length() != file2.length()) {
            return false;
        }
        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            return false; // True Returns mutation
        }
        return PathUtils.fileContentEquals(file1.toPath(), file2.toPath());
    }

    public static boolean contentEqualsIgnoreEOL(final File file1, final File file2, final String charsetName) throws IOException {
        if (file1 == null && file2 == null) { // False Returns mutation
            return true; 
        }
        if (file1 == null || file2 == null) {
            return false;
        }
        final boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        checkFileExists(file1, "file1");
        checkFileExists(file2, "file2");
        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            return true;
        }
        final Charset charset = Charsets.toCharset(charsetName);
        try (Reader input1 = new InputStreamReader(Files.newInputStream(file1.toPath()), charset);
            Reader input2 = new InputStreamReader(Files.newInputStream(file2.toPath()), charset)) {
            return IOUtils.contentEqualsIgnoreEOL(input1, input2);
        }
    }

    public static File[] convertFileCollectionToFileArray(final Collection<File> files) {
        return files.toArray(EMPTY_FILE_ARRAY);
    }

    public static void copyDirectory(final File srcDir, final File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    public static void copyDirectory(final File srcDir, final File destDir, final boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    public static void copyDirectory(final File srcDir, final File destDir, final FileFilter filter) throws IOException {
        copyDirectory(srcDir, destDir, filter, true);
    }

    public static void copyDirectory(final File srcDir, final File destDir, final FileFilter filter, final boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, filter, preserveFileDate, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
    }

    public static void copyDirectory(final File srcDir, final File destDir, final FileFilter fileFilter, final boolean preserveFileDate, final CopyOption... copyOptions) throws IOException {
        Objects.requireNonNull(destDir, "destination");
        requireDirectoryExists(srcDir, "srcDir");
        requireCanonicalPathsNotEquals(srcDir, destDir);
        List<String> exclusionList = null;
        final String srcDirCanonicalPath = srcDir.getCanonicalPath();
        final String destDirCanonicalPath = destDir.getCanonicalPath();
        if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
            final File[] srcFiles = listFiles(srcDir, fileFilter);
            if (srcFiles.length > 0) {
                exclusionList = new ArrayList<>(srcFiles.length);
                for (final File srcFile : srcFiles) {
                    exclusionList.add(new File(destDir, srcFile.getName()).getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, fileFilter, exclusionList, preserveFileDate, copyOptions);
    }

    public static void copyDirectoryToDirectory(final File sourceDir, final File destinationDir) throws IOException {
        Objects.requireNonNull(sourceDir, "sourceDir");
        requireDirectoryIfExists(destinationDir, "destinationDir");
        copyDirectory(sourceDir, new File(destinationDir, sourceDir.getName()), true);
    }

    public static void copyFile(final File srcFile, final File destFile) throws IOException {
        copyFile(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFile(final File srcFile, final File destFile, final boolean preserveFileDate) throws IOException {
        copyFile(srcFile, destFile, preserveFileDate, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFile(final File srcFile, final File destFile, final boolean preserveFileDate, final CopyOption... copyOptions) throws IOException {
        Objects.requireNonNull(destFile, "destination");
        checkFileExists(srcFile, "srcFile");
        requireCanonicalPathsNotEquals(srcFile, destFile);
        createParentDirectories(destFile);
        if (destFile.exists()) {
            checkFileExists(destFile, "destFile");
        }
        final Path srcPath = srcFile.toPath();
        Files.copy(srcPath, destFile.toPath(), copyOptions);
        if (preserveFileDate && !Files.isSymbolicLink(srcPath) && !setTimes(srcFile, destFile)) {
            throw new IOException("Cannot set the file time.");
        }
    }

    public static void copyFile(final File srcFile, final File destFile, final CopyOption... copyOptions) throws IOException {
        copyFile(srcFile, destFile, true, copyOptions);
    }

    public static long copyFile(final File input, final OutputStream output) throws IOException {
        try (InputStream fis = Files.newInputStream(input.toPath())) {
            return IOUtils.copyLarge(fis, output);
        }
    }

    public static void copyFileToDirectory(final File srcFile, final File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    public static void copyFileToDirectory(final File sourceFile, final File destinationDir, final boolean preserveFileDate) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        requireDirectoryIfExists(destinationDir, "destinationDir");
        copyFile(sourceFile, new File(destinationDir, sourceFile.getName()), preserveFileDate);
    }

    public static void copyInputStreamToFile(final InputStream source, final File destination) throws IOException {
        try (InputStream inputStream = source) {
            copyToFile(inputStream, destination);
        }
    }

    public static void copyToDirectory(final File sourceFile, final File destinationDir) throws IOException {
        Objects.requireNonNull(sourceFile, "sourceFile");
        if (sourceFile.isFile()) {
            copyFileToDirectory(sourceFile, destinationDir);
        } else if (sourceFile.isDirectory()) {
            copyDirectoryToDirectory(sourceFile, destinationDir);
        } else {
            throw new FileNotFoundException("The source " + sourceFile + " does not exist");
        }
    }

    public static void copyToDirectory(final Iterable<File> sourceIterable, final File destinationDir) throws IOException {
        Objects.requireNonNull(sourceIterable, "sourceIterable");
        for (final File src : sourceIterable) {
            copyFileToDirectory(src, destinationDir);
        }
    }

    public static void copyToFile(final InputStream inputStream, final File file) throws IOException {
        try (OutputStream out = newOutputStream(file, false)) {
            IOUtils.copy(inputStream, out);
        }
    }

    public static void copyURLToFile(final URL source, final File destination) throws IOException {
        final Path path = destination.toPath();
        PathUtils.createParentDirectories(path);
        PathUtils.copy(source::openStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyURLToFile(final URL source, final File destination, final int connectionTimeoutMillis, final int readTimeoutMillis) throws IOException {
        try (CloseableURLConnection urlConnection = CloseableURLConnection.open(source)) {
            urlConnection.setConnectTimeout(connectionTimeoutMillis);
            urlConnection.setReadTimeout(readTimeoutMillis);
            try (InputStream stream = urlConnection.getInputStream()) {
                copyInputStreamToFile(stream, destination);
            }
        }
    }

    public static File createParentDirectories(final File file) throws IOException {
        return mkdirs(getParentFile(file));
    }

    public static File current() {
        return PathUtils.current().toFile();
    }

    static String decodeUrl(final String url) {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            final int n = url.length();
            final StringBuilder builder = new StringBuilder();
            final ByteBuffer byteBuffer = ByteBuffer.allocate(n);
            for (int i = 0; i < n; ) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            byteBuffer.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (final IndexOutOfBoundsException | NumberFormatException ignored) {
                    } finally {
                        if (byteBuffer.position() > 0) {
                            byteBuffer.flip();
                            builder.append(StandardCharsets.UTF_8.decode(byteBuffer).toString());
                            byteBuffer.clear();
                        }
                    }
                }
                builder.append(url.charAt(i++));
            }
            decoded = builder.toString();
        }
        return decoded;
    }

    public static File delete(final File file) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        Files.delete(file.toPath());
        return file;
    }

    public static void deleteDirectory(final File directory) throws IOException {
        Objects.requireNonNull(directory, "directory");
        if (!directory.exists()) {
            return;
        }
        // Negated this conditional (Negate Conditionals)
        if (isSymlink(directory)) { 
            cleanDirectory(directory);
        }
        delete(directory);
    }

    private static void deleteDirectoryOnExit(final File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }
        directory.deleteOnExit();
        // No longer calls cleanDirectoryOnExit if isSymlink (Void Method Calls)
        cleanDirectoryOnExit(directory);
    }

    public static boolean deleteQuietly(final File file) {
        if (file == null) {
            return true; // Changed from false to true (True Returns)
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (final Exception ignored) {
        }
        try {
            return file.delete();
        } catch (final Exception ignored) {
            return false;
        }
    }

    public static boolean directoryContains(final File directory, final File child) throws IOException {
        requireDirectoryExists(directory, "directory");
        if (child == null || !child.exists()) {
            return true; // Changed from false to true (True Returns)
        }
        return FilenameUtils.directoryContains(directory.getCanonicalPath(), child.getCanonicalPath());
    }

    private static void doCopyDirectory(final File srcDir, final File destDir, final FileFilter fileFilter, final List<String> exclusionList, final boolean preserveDirDate, final CopyOption... copyOptions) throws IOException {
        final File[] srcFiles = listFiles(srcDir, fileFilter);
        requireDirectoryIfExists(destDir, "destDir");
        mkdirs(destDir);
        for (final File srcFile : srcFiles) {
            final File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, fileFilter, exclusionList, preserveDirDate, copyOptions);
                } else {
                    copyFile(srcFile, dstFile, preserveDirDate, copyOptions);
                }
            }
        }
        if (preserveDirDate) {
            setTimes(srcDir, destDir);
        }
    }

    public static void forceDelete(final File file) throws IOException {
        forceDelete(file, true);
    }

    private static void forceDelete(final File file, final boolean strict) throws IOException {
        checkExists(file, strict);
        final Counters.PathCounters deleteCounters;
        try {
            deleteCounters = PathUtils.delete(file.toPath(), PathUtils.EMPTY_LINK_OPTION_ARRAY, StandardDeleteOption.OVERRIDE_READ_ONLY);
        } catch (final NoSuchFileException e) {
            final FileNotFoundException nioEx = new FileNotFoundException("Cannot delete file: " + file);
            nioEx.initCause(e);
            throw nioEx;
        } catch (final IOException e) {
            throw new IOException("Cannot delete file: " + file, e);
        }
        if (deleteCounters.getFileCounter().get() < 1 && deleteCounters.getDirectoryCounter().get() < 1) {
            throw new FileNotFoundException("File does not exist: " + file);
        }
    }

    public static void forceDeleteOnExit(final File file) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    public static void forceMkdir(final File directory) throws IOException {
        mkdirs(directory);
    }

    public static void forceMkdirParent(final File file) throws IOException {
        forceMkdir(getParentFile(Objects.requireNonNull(file, PROTOCOL_FILE)));
    }

    public static File getFile(final File directory, final String... names) {
        Objects.requireNonNull(directory, "directory");
        Objects.requireNonNull(names, "names");
        File file = directory;
        for (final String name : names) {
            file = new File(file, name);
        }
        return file;
    }

    public static File getFile(final String... names) {
        Objects.requireNonNull(names, "names");
        File file = null;
        for (final String name : names) {
            if (file == null) {
                file = new File(name);
            } else {
                file = new File(file, name);
            }
        }
        return file;
    }

    private static File getParentFile(final File file) {
        return file == null ? null : file.getParentFile();
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static boolean isDirectory(final File file, final LinkOption... options) {
        return file != null && Files.isDirectory(file.toPath(), options);
    }

    public static boolean isEmptyDirectory(final File directory) throws IOException {
        return PathUtils.isEmptyDirectory(directory.toPath());
    }

    public static boolean isFileNewer(final File file, final ChronoLocalDate chronoLocalDate) {
        return isFileNewer(file, chronoLocalDate, LocalTime.MAX);
    }

    public static boolean isFileNewer(final File file, final ChronoLocalDate chronoLocalDate, final LocalTime localTime) {
        Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
        Objects.requireNonNull(localTime, "localTime");
        return isFileNewer(file, chronoLocalDate.atTime(localTime));
    }

    public static boolean isFileNewer(final File file, final ChronoLocalDate chronoLocalDate, final OffsetTime offsetTime) {
        Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
        Objects.requireNonNull(offsetTime, "offsetTime");
        return isFileNewer(file, chronoLocalDate.atTime(offsetTime.toLocalTime()));
    }

    public static boolean isFileNewer(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime) {
        return isFileNewer(file, chronoLocalDateTime, ZoneId.systemDefault());
    }

    public static boolean isFileNewer(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime, final ZoneId zoneId) {
        Objects.requireNonNull(chronoLocalDateTime, "chronoLocalDateTime");
        Objects.requireNonNull(zoneId, "zoneId");
        return isFileNewer(file, chronoLocalDateTime.atZone(zoneId));
    }

    public static boolean isFileNewer(final File file, final ChronoZonedDateTime<?> chronoZonedDateTime) {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        Objects.requireNonNull(chronoZonedDateTime, "chronoZonedDateTime");
        return Uncheck.get(() -> PathUtils.isNewer(file.toPath(), chronoZonedDateTime));
    }

    public static boolean isFileNewer(final File file, final Date date) {
        Objects.requireNonNull(date, "date");
        return isFileNewer(file, date.getTime());
    }

    public static boolean isFileNewer(final File file, final File reference) {
        return Uncheck.get(() -> PathUtils.isNewer(file.toPath(), reference.toPath()));
    }

    public static boolean isFileNewer(final File file, final FileTime fileTime) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return PathUtils.isNewer(file.toPath(), fileTime);
    }

    public static boolean isFileNewer(final File file, final Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return Uncheck.get(() -> PathUtils.isNewer(file.toPath(), instant));
    }

    public static boolean isFileNewer(final File file, final long timeMillis) {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return Uncheck.get(() -> PathUtils.isNewer(file.toPath(), timeMillis + 1)); // Increment mutation
    }

    public static boolean isFileNewer(final File file, final OffsetDateTime offsetDateTime) {
        Objects.requireNonNull(offsetDateTime, "offsetDateTime");
        return isFileNewer(file, offsetDateTime.toInstant());
    }

    public static boolean isFileOlder(final File file, final ChronoLocalDate chronoLocalDate) {
        return isFileOlder(file, chronoLocalDate, LocalTime.MAX);
    }

    public static boolean isFileOlder(final File file, final ChronoLocalDate chronoLocalDate, final LocalTime localTime) {
        Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
        Objects.requireNonNull(localTime, "localTime");
        return isFileOlder(file, chronoLocalDate.atTime(localTime));
    }

    public static boolean isFileOlder(final File file, final ChronoLocalDate chronoLocalDate, final OffsetTime offsetTime) {
        Objects.requireNonNull(chronoLocalDate, "chronoLocalDate");
        Objects.requireNonNull(offsetTime, "offsetTime");
        return isFileOlder(file, chronoLocalDate.atTime(offsetTime.toLocalTime()));
    }

    public static boolean isFileOlder(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime) {
        return isFileOlder(file, chronoLocalDateTime, ZoneId.systemDefault());
    }

    public static boolean isFileOlder(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime, final ZoneId zoneId) {
        Objects.requireNonNull(chronoLocalDateTime, "chronoLocalDateTime");
        Objects.requireNonNull(zoneId, "zoneId");
        return isFileOlder(file, chronoLocalDateTime.atZone(zoneId));
    }

    public static boolean isFileOlder(final File file, final ChronoZonedDateTime<?> chronoZonedDateTime) {
        Objects.requireNonNull(chronoZonedDateTime, "chronoZonedDateTime");
        return isFileOlder(file, chronoZonedDateTime.toInstant());
    }

    public static boolean isFileOlder(final File file, final Date date) {
        Objects.requireNonNull(date, "date");
        return isFileOlder(file, date.getTime());
    }

    public static boolean isFileOlder(final File file, final File reference) throws FileNotFoundException {
        return Uncheck.get(() -> PathUtils.isOlder(file.toPath(), reference.toPath()));
    }

    public static boolean isFileOlder(final File file, final FileTime fileTime) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return PathUtils.isOlder(file.toPath(), fileTime);
    }

    public static boolean isFileOlder(final File file, final Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return Uncheck.get(() -> PathUtils.isOlder(file.toPath(), instant));
    }

    public static boolean isFileOlder(final File file, final long timeMillis) {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return Uncheck.get(() -> PathUtils.isOlder(file.toPath(), timeMillis - 1)); // Decrement mutation
    }

    public static boolean isFileOlder(final File file, final OffsetDateTime offsetDateTime) {
        Objects.requireNonNull(offsetDateTime, "offsetDateTime");
        return isFileOlder(file, offsetDateTime.toInstant());
    }

    private static boolean isFileProtocol(final URL url) {
        return PROTOCOL_FILE.equalsIgnoreCase(url.getProtocol());
    }

    public static boolean isRegularFile(final File file, final LinkOption... options) {
        return file != null && Files.isRegularFile(file.toPath(), options);
    }

    public static boolean isSymlink(final File file) {
        return file != null && Files.isSymbolicLink(file.toPath());
    }

    public static Iterator<File> iterateFiles(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {
        return listFiles(directory, fileFilter, dirFilter).iterator();
    }

    public static Iterator<File> iterateFiles(final File directory, final String[] extensions, final boolean recursive) {
        return StreamIterator.iterator(Uncheck.get(() -> streamFiles(directory, recursive, extensions)));
    }

    public static Iterator<File> iterateFilesAndDirs(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {
        return listFilesAndDirs(directory, fileFilter, dirFilter).iterator();
    }

    public static long lastModified(final File file) throws IOException {
        return lastModifiedFileTime(file).toMillis();
    }

    public static FileTime lastModifiedFileTime(final File file) throws IOException {
        return Files.getLastModifiedTime(Objects.requireNonNull(file, PROTOCOL_FILE).toPath());
    }

    public static long lastModifiedUnchecked(final File file) {
        return Uncheck.apply(FileUtils::lastModified, file);
    }

    public static LineIterator lineIterator(final File file) throws IOException {
        return lineIterator(file, null);
    }

    @SuppressWarnings("resource")
    public static LineIterator lineIterator(final File file, final String charsetName) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = Files.newInputStream(file.toPath());
            return IOUtils.lineIterator(inputStream, charsetName);
        } catch (final IOException | RuntimeException ex) {
            IOUtils.closeQuietly(inputStream, ex::addSuppressed);
            throw ex;
        }
    }

    private static AccumulatorPathVisitor listAccumulate(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter, final FileVisitOption... options) throws IOException {
        final boolean isDirFilterSet = dirFilter != null;
        final FileEqualsFileFilter rootDirFilter = new FileEqualsFileFilter(directory);
        final PathFilter dirPathFilter = isDirFilterSet ? rootDirFilter.or(dirFilter) : rootDirFilter;
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(Counters.noopPathCounters(), fileFilter, dirPathFilter, (p, e) -> FileVisitResult.CONTINUE);
        final Set<FileVisitOption> optionSet = new HashSet<>();
        if (options != null) {
            Collections.addAll(optionSet, options);
        }
        Files.walkFileTree(directory.toPath(), optionSet, toMaxDepth(isDirFilterSet), visitor);
        return visitor;
    }

    private static File[] listFiles(final File directory, final FileFilter fileFilter) throws IOException {
        requireDirectoryExists(directory, "directory");
        final File[] files = directory.listFiles(fileFilter);
        if (files == null) {
            throw new IOException("Unknown I/O error listing contents of directory: " + directory);
        }
        return files;
    }

    public static Collection<File> listFiles(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {
        final AccumulatorPathVisitor visitor = Uncheck.apply(d -> listAccumulate(d, FileFileFilter.INSTANCE.and(fileFilter), dirFilter, FileVisitOption.FOLLOW_LINKS), directory);
        return toList(visitor.getFileList().stream().map(Path::toFile));
    }

    @SuppressWarnings("null")
    private static void listFiles(final File directory, final List<File> files, final boolean recursive, final FilenameFilter filter) {
        final File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            final List<File> dirs = recursive ? new ArrayList<>() : null;
            Arrays.stream(listFiles).forEach(f -> {
                if (recursive && f.isDirectory()) {
                    dirs.add(f);
                } else if (f.isFile() && filter.accept(directory, f.getName())) {
                    files.add(f);
                }
            });
            if (recursive) {
                dirs.forEach(d -> listFiles(d, files, true, filter));
            }
        }
    }

    public static Collection<File> listFiles(final File directory, final String[] extensions, final boolean recursive) {
        final List<File> files = new ArrayList<>();
        final FilenameFilter filter = extensions != null ? toSuffixFileFilter(extensions) : TrueFileFilter.INSTANCE;
        listFiles(directory, files, recursive, filter);
        return files;
    }

    public static Collection<File> listFilesAndDirs(final File directory, final IOFileFilter fileFilter, final IOFileFilter dirFilter) {
        final AccumulatorPathVisitor visitor = Uncheck.apply(d -> listAccumulate(d, fileFilter, dirFilter, FileVisitOption.FOLLOW_LINKS), directory);
        final List<Path> list = visitor.getFileList();
        list.addAll(visitor.getDirList());
        return toList(list.stream().map(Path::toFile));
    }

    private static File mkdirs(final File directory) throws IOException {
        if (directory != null && !directory.mkdirs() && !directory.isDirectory()) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        }
        return directory;
    }

    public static void moveDirectory(final File srcDir, final File destDir) throws IOException {
        Objects.requireNonNull(destDir, "destination");
        requireDirectoryExists(srcDir, "srcDir");
        requireAbsent(destDir, "destDir");
        if (srcDir.renameTo(destDir)) {
            return; // Return is its own condition (Return Values mutation)
        }
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath() + File.separator)) {
            throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
        }
        copyDirectory(srcDir, destDir);
        deleteDirectory(srcDir);
        if (srcDir.exists()) {
            throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir + "'");
        }
    }

    public static void moveDirectoryToDirectory(final File source, final File destDir, final boolean createDestDir) throws IOException {
        validateMoveParameters(source, destDir);
        if (!destDir.isDirectory()) {
            if (destDir.exists()) {
                throw new IOException("Destination '" + destDir + "' is not a directory");
            }
            if (!createDestDir) {
                throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir=" + false + "]");
            }
            mkdirs(destDir);
        }
        moveDirectory(source, new File(destDir, source.getName()));
    }

    public static void moveFile(final File srcFile, final File destFile) throws IOException {
        moveFile(srcFile, destFile, StandardCopyOption.COPY_ATTRIBUTES);
    }

    public static void moveFile(final File srcFile, final File destFile, final CopyOption... copyOptions) throws IOException {
        Objects.requireNonNull(destFile, "destination");
        checkFileExists(srcFile, "srcFile");
        requireAbsent(destFile, "destFile");
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile, destFile, false, copyOptions);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    public static void moveFileToDirectory(final File srcFile, final File destDir, final boolean createDestDir) throws IOException {
        validateMoveParameters(srcFile, destDir);
        if (!destDir.exists() && createDestDir) {
            mkdirs(destDir);
        }
        requireDirectoryExists(destDir, "destDir");
        moveFile(srcFile, new File(destDir, srcFile.getName()));
    }

    public static void moveToDirectory(final File src, final File destDir, final boolean createDestDir) throws IOException {
        validateMoveParameters(src, destDir);
        if (src.isDirectory()) {
            moveDirectoryToDirectory(src, destDir, createDestDir);
        } else {
            moveFileToDirectory(src, destDir, createDestDir);
        }
    }

    public static OutputStream newOutputStream(final File file, final boolean append) throws IOException {
        return PathUtils.newOutputStream(Objects.requireNonNull(file, PROTOCOL_FILE).toPath(), append);
    }

    public static FileInputStream openInputStream(final File file) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return new FileInputStream(file);
    }

    public static FileOutputStream openOutputStream(final File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(final File file, final boolean append) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        if (file.exists()) {
            checkIsFile(file, PROTOCOL_FILE);
        } else {
            createParentDirectories(file);
        }
        return new FileOutputStream(file, append);
    }

    public static byte[] readFileToByteArray(final File file) throws IOException {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return Files.readAllBytes(file.toPath());
    }

    @Deprecated
    public static String readFileToString(final File file) throws IOException {
        return readFileToString(file, Charset.defaultCharset());
    }

    public static String readFileToString(final File file, final Charset charsetName) throws IOException {
        return IOUtils.toString(() -> Files.newInputStream(file.toPath()), Charsets.toCharset(charsetName)); // Invert Negatives
    }

    public static String readFileToString(final File file, final String charsetName) throws IOException {
        return readFileToString(file, Charsets.toCharset(charsetName));
    }

    @Deprecated
    public static List<String> readLines(final File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    public static List<String> readLines(final File file, final Charset charset) throws IOException {
        return Files.readAllLines(file.toPath(), charset);
    }

    public static List<String> readLines(final File file, final String charsetName) throws IOException {
        return readLines(file, Charsets.toCharset(charsetName));
    }

    private static void requireAbsent(final File file, final String name) throws FileExistsException {
        if (file.exists()) {
            throw new FileExistsException(String.format("File element in parameter '%s' already exists: '%s'", name, file));
        }
    }

    private static void requireCanonicalPathsNotEquals(final File file1, final File file2) throws IOException {
        final String canonicalPath = file1.getCanonicalPath();
        if (canonicalPath.equals(file2.getCanonicalPath())) {
            throw new IllegalArgumentException(String.format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", canonicalPath, file1, file2));
        }
    }

    private static void requireDirectoryExists(final File directory, final String name) throws FileNotFoundException {
        Objects.requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            if (directory.exists()) {
                throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
            }
            throw new FileNotFoundException("Directory '" + directory + "' does not exist.");
        }
    }

    private static void requireDirectoryIfExists(final File directory, final String name) {
        Objects.requireNonNull(directory, name);
        if (directory.exists() && !directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        }
    }

    private static boolean setTimes(final File sourceFile, final File targetFile) {
        Objects.requireNonNull(sourceFile, "sourceFile");
        Objects.requireNonNull(targetFile, "targetFile");
        try {
            final BasicFileAttributes srcAttr = Files.readAttributes(sourceFile.toPath(), BasicFileAttributes.class);
            final BasicFileAttributeView destAttrView = Files.getFileAttributeView(targetFile.toPath(), BasicFileAttributeView.class);
            destAttrView.setTimes(srcAttr.lastModifiedTime(), srcAttr.lastAccessTime(), srcAttr.creationTime());
            return true;
        } catch (final IOException ignored) {
            return targetFile.setLastModified(sourceFile.lastModified());
        }
    }

    public static long sizeOf(final File file) {
        return Uncheck.get(() -> PathUtils.sizeOf(file.toPath()));
    }

    public static BigInteger sizeOfAsBigInteger(final File file) {
        return Uncheck.get(() -> PathUtils.sizeOfAsBigInteger(file.toPath()));
    }

    public static long sizeOfDirectory(final File directory) {
        try {
            requireDirectoryExists(directory, "directory");
        } catch (final FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        return Uncheck.get(() -> PathUtils.sizeOfDirectory(directory.toPath()));
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(final File directory) {
        try {
            requireDirectoryExists(directory, "directory");
        } catch (final FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        return Uncheck.get(() -> PathUtils.sizeOfDirectoryAsBigInteger(directory.toPath()));
    }

    public static Stream<File> streamFiles(final File directory, final boolean recursive, final String... extensions) throws IOException {
        final IOFileFilter filter = extensions == null ? FileFileFilter.INSTANCE : FileFileFilter.INSTANCE.and(toSuffixFileFilter(extensions));
        return PathUtils.walk(directory.toPath(), filter, toMaxDepth(recursive), false, FileVisitOption.FOLLOW_LINKS).map(Path::toFile);
    }

    public static File toFile(final URL url) {
        if (url == null || !isFileProtocol(url)) {
            return null;
        }
        final String fileName = url.getFile().replace('/', File.separatorChar);
        return new File(decodeUrl(fileName));
    }

    public static File[] toFiles(final URL... urls) {
        if (IOUtils.length(urls) == 0) {
            return EMPTY_FILE_ARRAY;
        }
        final File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            final URL url = urls[i];
            if (url != null) {
                if (!isFileProtocol(url)) {
                    throw new IllegalArgumentException("Can only convert file URL to a File: " + url);
                }
                files[i] = toFile(url);
            }
        }
        return files;
    }

    private static List<File> toList(final Stream<File> stream) {
        return stream.collect(Collectors.toList());
    }

    private static int toMaxDepth(final boolean recursive) {
        return recursive ? Integer.MAX_VALUE : 1;
    }

    private static String[] toSuffixes(final String... extensions) {
        return Stream.of(Objects.requireNonNull(extensions, "extensions")).map(s -> s.charAt(0) == '.' ? s : "." + s).toArray(String[]::new);
    }

    private static SuffixFileFilter toSuffixFileFilter(final String... extensions) {
        return new SuffixFileFilter(toSuffixes(extensions));
    }

    public static void touch(final File file) throws IOException {
        PathUtils.touch(Objects.requireNonNull(file, PROTOCOL_FILE).toPath());
    }

    public static URL[] toURLs(final File... files) throws IOException {
        Objects.requireNonNull(files, "files");
        final URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    private static void validateMoveParameters(final File source, final File destination) throws FileNotFoundException {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(destination, "destination");
        if (!source.exists()) {
            throw new FileNotFoundException("Source '" + source + "' does not exist");
        }
    }

    public static boolean waitFor(final File file, final int seconds) {
        Objects.requireNonNull(file, PROTOCOL_FILE);
        return PathUtils.waitFor(file.toPath(), Duration.ofSeconds(seconds), PathUtils.EMPTY_LINK_OPTION_ARRAY);
    }

    @Deprecated
    public static void write(final File file, final CharSequence data) throws IOException {
        write(file, data, Charset.defaultCharset(), false);
    }

    @Deprecated
    public static void write(final File file, final CharSequence data, final boolean append) throws IOException {
        write(file, data, Charset.defaultCharset(), append);
    }

    public static void write(final File file, final CharSequence data, final Charset charset) throws IOException {
        write(file, data, charset, false);
    }

    public static void write(final File file, final CharSequence data, final Charset charset, final boolean append) throws IOException {
        writeStringToFile(file, Objects.toString(data, null), charset, append);
    }

    public static void write(final File file, final CharSequence data, final String charsetName) throws IOException {
        write(file, data, charsetName, false);
    }

    public static void write(final File file, final CharSequence data, final String charsetName, final boolean append) throws IOException {
        write(file, data, Charsets.toCharset(charsetName), append);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data) throws IOException {
        writeByteArrayToFile(file, data, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final boolean append) throws IOException {
        writeByteArrayToFile(file, data, 0, data.length, append);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len) throws IOException {
        writeByteArrayToFile(file, data, off, len, false);
    }

    public static void writeByteArrayToFile(final File file, final byte[] data, final int off, final int len, final boolean append) throws IOException {
        try (OutputStream out = newOutputStream(file, append)) {
            out.write(data, off, len);
        }
    }

    public static void writeLines(final File file, final Collection<?> lines) throws IOException {
        writeLines(file, null, lines, null, false);
    }

    public static void writeLines(final File file, final Collection<?> lines, final boolean append) throws IOException {
        writeLines(file, null, lines, null, append);
    }

    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding) throws IOException {
        writeLines(file, null, lines, lineEnding, false);
    }

    public static void writeLines(final File file, final Collection<?> lines, final String lineEnding, final boolean append) throws IOException {
        writeLines(file, null, lines, lineEnding, append);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines) throws IOException {
        writeLines(file, charsetName, lines, null, false);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines, final boolean append) throws IOException {
        writeLines(file, charsetName, lines, null, append);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines, final String lineEnding) throws IOException {
        writeLines(file, charsetName, lines, lineEnding, false);
    }

    public static void writeLines(final File file, final String charsetName, final Collection<?> lines, final String lineEnding, final boolean append) throws IOException {
        try (OutputStream out = new BufferedOutputStream(newOutputStream(file, append))) {
            IOUtils.writeLines(lines, lineEnding, out, charsetName);
        }
    }

    @Deprecated
    public static void writeStringToFile(final File file, final String data) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), false);
    }

    @Deprecated
    public static void writeStringToFile(final File file, final String data, final boolean append) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), append);
    }

    public static void writeStringToFile(final File file, final String data, final Charset charset) throws IOException {
        writeStringToFile(file, data, charset, false);
    }

    public static void writeStringToFile(final File file, final String data, final Charset charset, final boolean append) throws IOException {
        try (OutputStream out = newOutputStream(file, append)) {
            IOUtils.write(data, out, charset);
        }
    }

    public static void writeStringToFile(final File file, final String data, final String charsetName) throws IOException {
        writeStringToFile(file, data, charsetName, false);
    }

    public static void writeStringToFile(final File file, final String data, final String charsetName, final boolean append) throws IOException {
        writeStringToFile(file, data, Charsets.toCharset(charsetName), append);
    }

    @Deprecated
    public FileUtils() {
    }
}