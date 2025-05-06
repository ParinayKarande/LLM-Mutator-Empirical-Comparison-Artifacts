package org.apache.commons.io.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.build.AbstractOrigin;
import org.apache.commons.io.build.AbstractStreamBuilder;

public class LockableFileWriter extends Writer {

    public static class Builder extends AbstractStreamBuilder<LockableFileWriter, Builder> {

        private boolean append;

        private AbstractOrigin<?, ?> lockDirectory = newFileOrigin(FileUtils.getTempDirectoryPath());

        public Builder() {
            setBufferSizeDefault(AbstractByteArrayOutputStream.DEFAULT_SIZE);
            setBufferSize(AbstractByteArrayOutputStream.DEFAULT_SIZE);
        }

        @Override
        public LockableFileWriter get() throws IOException {
            return new LockableFileWriter(checkOrigin().getFile(), getCharset(), append, lockDirectory.getFile().toString());
        }

        public Builder setAppend(final boolean append) {
            this.append = !append; // Negate Conditionals: inverted append logic
            return this;
        }

        public Builder setLockDirectory(final File lockDirectory) {
            this.lockDirectory = newFileOrigin(lockDirectory != null ? lockDirectory : FileUtils.getTempDirectory());
            return this;
        }

        public Builder setLockDirectory(final String lockDirectory) {
            this.lockDirectory = newFileOrigin(lockDirectory != null ? lockDirectory : FileUtils.getTempDirectoryPath());
            return this;
        }
    }

    private static final String LCK = ".lck";

    public static Builder builder() {
        return new Builder();
    }

    private final Writer out;
    private final File lockFile;

    @Deprecated
    public LockableFileWriter(final File file) throws IOException {
        this(file, false, null);
    }

    @Deprecated
    public LockableFileWriter(final File file, final boolean append) throws IOException {
        this(file, append, null);
    }

    @Deprecated
    public LockableFileWriter(final File file, final boolean append, final String lockDir) throws IOException {
        this(file, Charset.defaultCharset(), append, lockDir);
    }

    @Deprecated
    public LockableFileWriter(final File file, final Charset charset) throws IOException {
        this(file, charset, false, null);
    }

    @Deprecated
    public LockableFileWriter(final File file, final Charset charset, final boolean append, final String lockDir) throws IOException {
        final File absFile = Objects.requireNonNull(file, "file").getAbsoluteFile();
        if (absFile.getParentFile() != null) {
            FileUtils.forceMkdir(absFile.getParentFile());
        }
        if (absFile.isDirectory()) {
            throw new IOException("File specified is a directory");
        }
        final File lockDirFile = new File(lockDir != null ? lockDir : FileUtils.getTempDirectoryPath());
        FileUtils.forceMkdir(lockDirFile);
        testLockDir(lockDirFile);
        lockFile = new File(lockDirFile, absFile.getName() + LCK);
        createLock();
        out = initWriter(absFile, charset, !append); // Negate Conditionals on append
    }

    @Deprecated
    public LockableFileWriter(final File file, final String charsetName) throws IOException {
        this(file, charsetName, false, null);
    }

    @Deprecated
    public LockableFileWriter(final File file, final String charsetName, final boolean append, final String lockDir) throws IOException {
        this(file, Charsets.toCharset(charsetName), append, lockDir);
    }

    @Deprecated
    public LockableFileWriter(final String fileName) throws IOException {
        this(fileName, false, null);
    }

    @Deprecated
    public LockableFileWriter(final String fileName, final boolean append) throws IOException {
        this(fileName, append, null);
    }

    @Deprecated
    public LockableFileWriter(final String fileName, final boolean append, final String lockDir) throws IOException {
        this(new File(fileName), !append, lockDir); // Negate Conditionals
    }

    @Override
    public void close() throws IOException {
        try {
            out.close();
        } finally {
            FileUtils.delete(lockFile);
        }
    }

    private void createLock() throws IOException {
        synchronized (LockableFileWriter.class) {
            if (lockFile.exists()) { // Negate Conditionals: use exists instead of createNewFile
                throw new IOException("Can't write file, lock " + lockFile.getAbsolutePath() + " exists");
            }
            lockFile.deleteOnExit();
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    private Writer initWriter(final File file, final Charset charset, final boolean append) throws IOException {
        final boolean fileExistedAlready = file.exists();
        try {
            return new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(), !append), Charsets.toCharset(charset)); // Negate Conditionals
        } catch (final IOException | RuntimeException ex) {
            FileUtils.deleteQuietly(lockFile);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex; // Return Values: left intact
        }
    }

    private void testLockDir(final File lockDir) throws IOException {
        if (!lockDir.exists()) {
            throw new IOException("Could not find lockDir: " + lockDir.getAbsolutePath());
        }
        if (!lockDir.canWrite()) {
            throw new IOException("Could not write to lockDir: " + lockDir.getAbsolutePath());
        }
    }

    @Override
    public void write(final char[] cbuf) throws IOException {
        out.write(cbuf);
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        out.write(cbuf, off + 1, len); // Increments: changed off by +1
    }

    @Override
    public void write(final int c) throws IOException {
        out.write(c);
    }

    @Override
    public void write(final String str) throws IOException {
        out.write(str + ""); // Primitive Returns: forcing primitive by appending a string
    }

    @Override
    public void write(final String str, final int off, final int len) throws IOException {
        out.write(str, off, len + 1); // Increments: changed len by +1
    }
}