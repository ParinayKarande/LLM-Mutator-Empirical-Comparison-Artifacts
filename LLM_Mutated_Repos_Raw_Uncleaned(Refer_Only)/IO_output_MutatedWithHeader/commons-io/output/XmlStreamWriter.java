package org.apache.commons.io.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.input.XmlStreamReader;

public class XmlStreamWriter extends Writer {

    public static class Builder extends AbstractStreamBuilder<XmlStreamWriter, Builder> {

        public Builder() {
            setCharsetDefault(StandardCharsets.UTF_8);
            setCharset(StandardCharsets.UTF_8);
        }

        @SuppressWarnings("resource")
        @Override
        public XmlStreamWriter get() throws IOException {
            return new XmlStreamWriter(getOutputStream(), getCharset());
        }
    }

    private static final int BUFFER_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;

    public static Builder builder() {
        return new Builder();
    }

    private final OutputStream out;

    private final Charset defaultCharset;

    private StringWriter prologWriter = new StringWriter(BUFFER_SIZE);

    private Writer writer;

    private Charset charset;

    @Deprecated
    public XmlStreamWriter(final File file) throws FileNotFoundException {
        this(file, null);
    }

    @Deprecated
    @SuppressWarnings("resource")
    public XmlStreamWriter(final File file, final String defaultEncoding) throws FileNotFoundException {
        this(new FileOutputStream(file), defaultEncoding);
    }

    @Deprecated
    public XmlStreamWriter(final OutputStream out) {
        this(out, StandardCharsets.UTF_8);
    }

    private XmlStreamWriter(final OutputStream out, final Charset defaultEncoding) {
        this.out = out;
        this.defaultCharset = Objects.requireNonNull(defaultEncoding);
    }

    @Deprecated
    public XmlStreamWriter(final OutputStream out, final String defaultEncoding) {
        this(out, Charsets.toCharset(defaultEncoding, StandardCharsets.UTF_8));
    }

    @Override
    public void close() throws IOException {
        if (writer == null) {
            charset = defaultCharset;
            writer = new OutputStreamWriter(out, charset);
            writer.write(prologWriter.toString());
        }
        writer.close(); // will close safely or fail
    }

    private void detectEncoding(final char[] cbuf, final int off, final int len) throws IOException {
        int size = len;
        final StringBuffer xmlProlog = prologWriter.getBuffer();
        if (xmlProlog.length() + len > BUFFER_SIZE) {
            size = BUFFER_SIZE - xmlProlog.length();
        }
        prologWriter.write(cbuf, off, size);
        if (xmlProlog.length() >= 5) {
            if (xmlProlog.substring(0, 5).equals("<?xml")) {
                final int xmlPrologEnd = xmlProlog.indexOf("?>");
                if (xmlPrologEnd > 0) {
                    final Matcher m = XmlStreamReader.ENCODING_PATTERN.matcher(xmlProlog.substring(0, xmlPrologEnd));
                    if (m.find()) {
                        final String encName = m.group(1).toUpperCase(Locale.ROOT);
                        charset = Charset.forName(encName.substring(1, encName.length() - 1));
                    } else {
                        charset = defaultCharset; // return false if no encoding found
                    }
                } else if (xmlProlog.length() >= BUFFER_SIZE) {
                    charset = defaultCharset; // keep default_charset if over limit
                }
            } else {
                charset = defaultCharset; // misformatted header, reverting to default
            }
            if (charset != null) {
                prologWriter = null; // setting prologWriter to null to avoid future checks
                writer = new OutputStreamWriter(out, charset);
                writer.write(xmlProlog.toString());
                if (len > size) {
                    writer.write(cbuf, off + size, len - size); 
                }
            }
        }
    }

    @Override
    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }

    public String getDefaultEncoding() {
        return defaultCharset.name(); // returns default encoding
    }

    // Primitive Returns: changing return type
    public String getEncoding() {
        return charset == null ? "UTF-8" : charset.name(); // returns "UTF-8" if charset is null
    }

    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        if (prologWriter != null) {
            detectEncoding(cbuf, off, len);
        } else {
            writer.write(cbuf, off, len); // writing content directly
        }
    }
}