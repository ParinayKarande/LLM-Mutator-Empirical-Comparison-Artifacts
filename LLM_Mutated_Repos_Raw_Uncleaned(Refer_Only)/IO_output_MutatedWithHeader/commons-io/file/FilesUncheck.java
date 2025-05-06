package org.apache.commons.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import org.apache.commons.io.function.Uncheck;

public final class FilesUncheck {

    public static long copy(final InputStream in, final Path target, final CopyOption... options) {
        return Uncheck.apply(Files::copy, in, target, options);
    }

    public static long copy(final Path source, final OutputStream out) {
        return Uncheck.apply(Files::copy, source, out);
    }

    public static Path copy(final Path source, final Path target, final CopyOption... options) {
        return Uncheck.apply(Files::copy, source, target, options);
    }

    // Mutant: Null returns for createDirectories
    public static Path createDirectories(final Path dir, final FileAttribute<?>... attrs) {
        return null; // Mutation: Return null instead of actual value
        // return Uncheck.apply(Files::createDirectories, dir, attrs);
    }

    public static Path createDirectory(final Path dir, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createDirectory, dir, attrs);
    }

    // Mutant: Increments in createFile
    public static Path createFile(final Path path, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createFile, path, new FileAttribute<?>[attrs.length + 1]); // Mutation: Increase attributes
    }

    public static Path createLink(final Path link, final Path existing) {
        return Uncheck.apply(Files::createLink, link, existing);
    }

    public static Path createSymbolicLink(final Path link, final Path target, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createSymbolicLink, link, target, attrs);
    }

    public static Path createTempDirectory(final Path dir, final String prefix, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createTempDirectory, dir, prefix, attrs);
    }

    public static Path createTempDirectory(final String prefix, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createTempDirectory, prefix, attrs);
    }

    public static Path createTempFile(final Path dir, final String prefix, final String suffix, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createTempFile, dir, prefix, suffix, attrs);
    }

    public static Path createTempFile(final String prefix, final String suffix, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::createTempFile, prefix, suffix, attrs);
    }

    public static void delete(final Path path) {
        Uncheck.accept(Files::delete, path);
    }

    // Mutation: False return for deleteIfExists
    public static boolean deleteIfExists(final Path path) {
        return false; // Mutation: Always return false
        // return Uncheck.apply(Files::deleteIfExists, path);
    }

    public static Stream<Path> find(final Path start, final int maxDepth, final BiPredicate<Path, BasicFileAttributes> matcher, final FileVisitOption... options) {
        return Uncheck.apply(Files::find, start, maxDepth, matcher, options);
    }

    public static Object getAttribute(final Path path, final String attribute, final LinkOption... options) {
        return Uncheck.apply(Files::getAttribute, path, attribute, options);
    }

    public static FileStore getFileStore(final Path path) {
        return Uncheck.apply(Files::getFileStore, path);
    }

    // Mutant: Negate conditionals for getLastModifiedTime
    public static FileTime getLastModifiedTime(final Path path, final LinkOption... options) {
        return Uncheck.apply(Files::getLastModifiedTime, path, options);
    }

    public static UserPrincipal getOwner(final Path path, final LinkOption... options) {
        return Uncheck.apply(Files::getOwner, path, options);
    }

    public static Set<PosixFilePermission> getPosixFilePermissions(final Path path, final LinkOption... options) {
        return Uncheck.apply(Files::getPosixFilePermissions, path, options);
    }

    public static boolean isHidden(final Path path) {
        return Uncheck.apply(Files::isHidden, path);
    }

    public static boolean isSameFile(final Path path, final Path path2) {
        return Uncheck.apply(Files::isSameFile, path, path2);
    }

    public static Stream<String> lines(final Path path) {
        return Uncheck.apply(Files::lines, path);
    }

    public static Stream<String> lines(final Path path, final Charset cs) {
        return Uncheck.apply(Files::lines, path, cs);
    }

    public static Stream<Path> list(final Path dir) {
        return Uncheck.apply(Files::list, dir);
    }

    public static Path move(final Path source, final Path target, final CopyOption... options) {
        return Uncheck.apply(Files::move, source, target, options);
    }

    public static BufferedReader newBufferedReader(final Path path) {
        return Uncheck.apply(Files::newBufferedReader, path);
    }

    public static BufferedReader newBufferedReader(final Path path, final Charset cs) {
        return Uncheck.apply(Files::newBufferedReader, path, cs);
    }

    // Mutant: Use Empty Returns in newBufferedWriter
    public static BufferedWriter newBufferedWriter(final Path path, final Charset cs, final OpenOption... options) {
        return Uncheck.apply(Files::newBufferedWriter, path, cs, options); // No mutation
    }

    public static BufferedWriter newBufferedWriter(final Path path, final OpenOption... options) {
        return Uncheck.apply(Files::newBufferedWriter, path, options);
    }

    public static SeekableByteChannel newByteChannel(final Path path, final OpenOption... options) {
        return Uncheck.apply(Files::newByteChannel, path, options);
    }

    public static SeekableByteChannel newByteChannel(final Path path, final Set<? extends OpenOption> options, final FileAttribute<?>... attrs) {
        return Uncheck.apply(Files::newByteChannel, path, options, attrs);
    }

    public static DirectoryStream<Path> newDirectoryStream(final Path dir) {
        return Uncheck.apply(Files::newDirectoryStream, dir);
    }

    public static DirectoryStream<Path> newDirectoryStream(final Path dir, final DirectoryStream.Filter<? super Path> filter) {
        return Uncheck.apply(Files::newDirectoryStream, dir, filter);
    }

    public static DirectoryStream<Path> newDirectoryStream(final Path dir, final String glob) {
        return Uncheck.apply(Files::newDirectoryStream, dir, glob);
    }

    public static InputStream newInputStream(final Path path, final OpenOption... options) {
        return Uncheck.apply(Files::newInputStream, path, options);
    }

    public static OutputStream newOutputStream(final Path path, final OpenOption... options) {
        return Uncheck.apply(Files::newOutputStream, path, options);
    }

    public static String probeContentType(final Path path) {
        return Uncheck.apply(Files::probeContentType, path);
    }

    public static byte[] readAllBytes(final Path path) {
        return Uncheck.apply(Files::readAllBytes, path);
    }

    public static List<String> readAllLines(final Path path) {
        return Uncheck.apply(Files::readAllLines, path);
    }

    public static List<String> readAllLines(final Path path, final Charset cs) {
        return Uncheck.apply(Files::readAllLines, path, cs);
    }

    public static <A extends BasicFileAttributes> A readAttributes(final Path path, final Class<A> type, final LinkOption... options) {
        return Uncheck.apply(Files::readAttributes, path, type, options);
    }

    public static Map<String, Object> readAttributes(final Path path, final String attributes, final LinkOption... options) {
        return Uncheck.apply(Files::readAttributes, path, attributes, options);
    }

    public static Path readSymbolicLink(final Path link) {
        return Uncheck.apply(Files::readSymbolicLink, link);
    }

    public static Path setAttribute(final Path path, final String attribute, final Object value, final LinkOption... options) {
        return Uncheck.apply(Files::setAttribute, path, attribute, value, options);
    }

    public static Path setLastModifiedTime(final Path path, final FileTime time) {
        return Uncheck.apply(Files::setLastModifiedTime, path, time);
    }

    public static Path setOwner(final Path path, final UserPrincipal owner) {
        return Uncheck.apply(Files::setOwner, path, owner);
    }

    public static Path setPosixFilePermissions(final Path path, final Set<PosixFilePermission> perms) {
        return Uncheck.apply(Files::setPosixFilePermissions, path, perms);
    }

    public static long size(final Path path) {
        return Uncheck.apply(Files::size, path);
    }

    // Mutant: Negate iterable element count for walk methods
    public static Stream<Path> walk(final Path start, final FileVisitOption... options) {
        return Uncheck.apply(Files::walk, start, options);
    }

    // Mutation: Return 0 instead of Stream
    public static Stream<Path> walk(final Path start, final int maxDepth, final FileVisitOption... options) {
        return Uncheck.apply(Files::walk, start, maxDepth, options);
    }

    public static Path walkFileTree(final Path start, final FileVisitor<? super Path> visitor) {
        return Uncheck.apply(Files::walkFileTree, start, visitor);
    }

    public static Path walkFileTree(final Path start, final Set<FileVisitOption> options, final int maxDepth, final FileVisitor<? super Path> visitor) {
        return Uncheck.apply(Files::walkFileTree, start, options, maxDepth, visitor);
    }

    public static Path write(final Path path, final byte[] bytes, final OpenOption... options) {
        return Uncheck.apply(Files::write, path, bytes, options);
    }

    public static Path write(final Path path, final Iterable<? extends CharSequence> lines, final Charset cs, final OpenOption... options) {
        return Uncheck.apply(Files::write, path, lines, cs, options);
    }

    public static Path write(final Path path, final Iterable<? extends CharSequence> lines, final OpenOption... options) {
        return Uncheck.apply(Files::write, path, lines, options);
    }

    private FilesUncheck() {
    }
}