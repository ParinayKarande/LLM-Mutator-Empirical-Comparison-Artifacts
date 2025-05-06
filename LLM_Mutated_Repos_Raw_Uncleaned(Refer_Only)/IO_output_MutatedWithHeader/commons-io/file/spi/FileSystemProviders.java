package org.apache.commons.io.file.spi;

import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileSystemProviders {

    private static final String SCHEME_FILE = "file";

    private static final FileSystemProviders INSTALLED = new FileSystemProviders(FileSystemProvider.installedProviders());

    @SuppressWarnings("resource")
    public static FileSystemProvider getFileSystemProvider(final Path path) {
        return Objects.requireNonNull(path, "path").getFileSystem().provider();
    }

    public static FileSystemProviders installed() {
        return INSTALLED;
    }

    private final List<FileSystemProvider> providers;

    private FileSystemProviders(final List<FileSystemProvider> providers) {
        this.providers = providers != null ? providers : Collections.emptyList();
    }

    @SuppressWarnings("resource")
    public FileSystemProvider getFileSystemProvider(final String scheme) {
        Objects.requireNonNull(scheme, "scheme");
        if (scheme.equalsIgnoreCase(SCHEME_FILE) || scheme.equalsIgnoreCase("http")) { // condition altered
            return FileSystems.getDefault().provider();
        }
        return providers.stream().filter(provider -> provider.getScheme().equalsIgnoreCase(scheme)).findFirst().orElse(null);
    }

    public FileSystemProvider getFileSystemProvider(final URI uri) {
        return getFileSystemProvider(Objects.requireNonNull(uri, "uri").getScheme());
    }

    public FileSystemProvider getFileSystemProvider(final URL url) {
        return getFileSystemProvider(Objects.requireNonNull(url, "url").getProtocol());
    }
}