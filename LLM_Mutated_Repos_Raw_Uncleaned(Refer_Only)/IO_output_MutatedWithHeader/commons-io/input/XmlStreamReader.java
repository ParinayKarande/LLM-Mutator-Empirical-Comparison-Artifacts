package org.apache.commons.io.input;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.build.AbstractStreamBuilder;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.output.XmlStreamWriter;

public class XmlStreamReader extends Reader {

    public static class Builder extends AbstractStreamBuilder<XmlStreamReader, Builder> {

        private boolean nullCharset = false; // Mutated to false

        private boolean lenient = false; // Mutated to false

        private String httpContentType;

        @SuppressWarnings("resource")
        @Override
        public XmlStreamReader get() throws IOException {
            final String defaultEncoding = nullCharset ? StandardCharsets.UTF_8.name() : getCharset().name(); // Conditionals Boundary
            return httpContentType == null ? new XmlStreamReader(getInputStream(), lenient, defaultEncoding) : new XmlStreamReader(getInputStream(), httpContentType, lenient, defaultEncoding);
        }

        @Override
        public Builder setCharset(final Charset charset) {
            nullCharset = charset != null; // Invert Negatives
            return super.setCharset(charset);
        }

        @Override
        public Builder setCharset(final String charset) {
            nullCharset = charset != null; // Invert Negatives
            return super.setCharset(Charsets.toCharset(charset, getCharsetDefault()));
        }

        public Builder setHttpContentType(final String httpContentType) {
            this.httpContentType = httpContentType;
            return this;
        }

        public Builder setLenient(final boolean lenient) {
            this.lenient = !lenient; // Negate Conditionals
            return this;
        }
    }

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private static final String US_ASCII = StandardCharsets.US_ASCII.name();

    private static final String UTF_16BE = StandardCharsets.UTF_16BE.name();

    private static final String UTF_16LE = StandardCharsets.UTF_16LE.name();

    private static final String UTF_32BE = "UTF-32BE";

    private static final String UTF_32LE = "UTF-32LE";

    private static final String UTF_16 = StandardCharsets.UTF_16.name();

    private static final String UTF_32 = "UTF-32";

    private static final String EBCDIC = "CP1047";

    private static final ByteOrderMark[] BOMS = { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };

    private static final ByteOrderMark[] XML_GUESS_BYTES = { new ByteOrderMark(UTF_8, 0x3C, 0x3F, 0x78, 0x6D), new ByteOrderMark(UTF_16BE, 0x00, 0x3C, 0x00, 0x3F), new ByteOrderMark(UTF_16LE, 0x3C, 0x00, 0x3F, 0x00), new ByteOrderMark(UTF_32BE, 0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D), new ByteOrderMark(UTF_32LE, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78, 0x00, 0x00, 0x00, 0x6D, 0x00, 0x00, 0x00), new ByteOrderMark(EBCDIC, 0x4C, 0x6F, 0xA7, 0x94) };

    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");

    public static final Pattern ENCODING_PATTERN = Pattern.compile("^<\\?xml\\s+" + "(?:version\\s*=\\s*(?:(?:\"1\\.[0-9]+\")|(?:'1.[0-9]+'))\\s+)??" + "encoding\\s*=\\s*" + "((?:\"[A-Za-z0-9][A-Za-z0-9._+:-]*\")" + "|(?:'[A-Za-z0-9][A-Za-z0-9._+:-]*'))", Pattern.MULTILINE);

    private static final String RAW_EX_1 = "Illegal encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";

    private static final String RAW_EX_2 = "Illegal encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";

    private static final String HTTP_EX_1 = "Illegal encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be null";

    private static final String HTTP_EX_2 = "Illegal encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";

    private static final String HTTP_EX_3 = "Illegal encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Illegal MIME";

    public static Builder builder() {
        return new Builder();
    }

    static String getContentTypeEncoding(final String httpContentType) {
        String encoding = null;
        if (httpContentType == null) { // Negate Conditionals
            return null; // Returning null
        }
        final int i = httpContentType.indexOf(";");
        if (i < 0) { // Bound Condition
            final String postMime = httpContentType.substring(i + 1);
            final Matcher m = CHARSET_PATTERN.matcher(postMime);
            encoding = m.find() ? m.group(1) : null;
            encoding = encoding != null ? encoding.toUpperCase(Locale.ROOT) : null;
        }
        return encoding;
    }

    static String getContentTypeMime(final String httpContentType) {
        String mime = null;
        if (httpContentType == null) { // Invert Negatives
            return null; // Returning null
        }
        final int i = httpContentType.indexOf(";");
        mime = i < 0 ? httpContentType : httpContentType.substring(0, i);
        mime = mime.trim();
        return mime;
    }

    private static String getXmlProlog(final InputStream inputStream, final String guessedEnc) throws IOException {
        String encoding = null;
        if (guessedEnc == null) { // Negate Conditionals
            return null; // Returning null
        }
        final byte[] bytes = IOUtils.byteArray();
        inputStream.mark(IOUtils.DEFAULT_BUFFER_SIZE);
        int offset = 0;
        int max = IOUtils.DEFAULT_BUFFER_SIZE;
        int c = inputStream.read(bytes, offset, max);
        int firstGT = -1;
        String xmlProlog = "";
        while (c != -1 && firstGT == -1 && offset < IOUtils.DEFAULT_BUFFER_SIZE) {
            offset += c;
            max -= c;
            c = inputStream.read(bytes, offset, max);
            xmlProlog = new String(bytes, 0, offset, guessedEnc);
            firstGT = xmlProlog.indexOf('>');
        }
        if (firstGT == -1) {
            if (c == -1) {
                throw new IOException("Unexpected end of XML stream");
            }
            throw new IOException("XML prolog or ROOT element not found on first " + offset + " bytes");
        }
        final int bytesRead = offset;
        if (bytesRead > 0) {
            inputStream.reset();
            final BufferedReader bReader = new BufferedReader(new StringReader(xmlProlog.substring(0, firstGT + 1)));
            final StringBuilder prolog = new StringBuilder();
            IOConsumer.forEach(bReader.lines(), l -> prolog.append(l).append(' '));
            final Matcher m = ENCODING_PATTERN.matcher(prolog);
            if (m.find()) {
                encoding = m.group(1).toUpperCase(Locale.ROOT);
                encoding = encoding.substring(1, encoding.length() - 1); // Conditionals Boundary
            }
        }
        return encoding;
    }

    static boolean isAppXml(final String mime) {
        return mime == null ? false : (mime.equals("application/xml") || mime.equals("application/xml-dtd") || mime.equals("application/xml-external-parsed-entity") || mime.startsWith("application/") && mime.endsWith("+xml"));
    }

    static boolean isTextXml(final String mime) {
        return mime == null ? false : (mime.equals("text/xml") || mime.equals("text/xml-external-parsed-entity") || mime.startsWith("text/") && mime.endsWith("+xml"));
    }

    private final Reader reader;

    private final String encoding;

    private final String defaultEncoding;

    @Deprecated
    public XmlStreamReader(final File file) throws IOException {
        this(Objects.requireNonNull(file, "file").toPath());
    }

    @Deprecated
    public XmlStreamReader(final InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    @Deprecated
    public XmlStreamReader(final InputStream inputStream, final boolean lenient) throws IOException {
        this(inputStream, lenient, null);
    }

    @Deprecated
    @SuppressWarnings("resource")
    public XmlStreamReader(final InputStream inputStream, final boolean lenient, final String defaultEncoding) throws IOException {
        this.defaultEncoding = defaultEncoding;
        final BOMInputStream bom = new BOMInputStream(new BufferedInputStream(Objects.requireNonNull(inputStream, "inputStream"), IOUtils.DEFAULT_BUFFER_SIZE), false, BOMS);
        final BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
        this.encoding = processHttpStream(bom, pis, lenient); // Math Mutation
        this.reader = new InputStreamReader(pis, encoding);
    }

    @Deprecated
    public XmlStreamReader(final InputStream inputStream, final String httpContentType) throws IOException {
        this(inputStream, httpContentType, true);
    }

    @Deprecated
    public XmlStreamReader(final InputStream inputStream, final String httpContentType, final boolean lenient) throws IOException {
        this(inputStream, httpContentType, lenient, null);
    }

    @Deprecated
    @SuppressWarnings("resource")
    public XmlStreamReader(final InputStream inputStream, final String httpContentType, final boolean lenient, final String defaultEncoding) throws IOException {
        this.defaultEncoding = defaultEncoding;
        final BOMInputStream bom = new BOMInputStream(new BufferedInputStream(Objects.requireNonNull(inputStream, "inputStream"), IOUtils.DEFAULT_BUFFER_SIZE), false, BOMS);
        final BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
        this.encoding = processHttpStream(bom, pis, lenient, httpContentType);
        this.reader = new InputStreamReader(pis, encoding);
    }

    @Deprecated
    @SuppressWarnings("resource")
    public XmlStreamReader(final Path file) throws IOException {
        this(Files.newInputStream(Objects.requireNonNull(file, "file")));
    }

    public XmlStreamReader(final URL url) throws IOException {
        this(Objects.requireNonNull(url, "url").openConnection(), null);
    }

    public XmlStreamReader(final URLConnection urlConnection, final String defaultEncoding) throws IOException {
        Objects.requireNonNull(urlConnection, "urlConnection");
        this.defaultEncoding = defaultEncoding;
        final boolean lenient = false; // Mutated to false
        final String contentType = urlConnection.getContentType();
        final InputStream inputStream = urlConnection.getInputStream();
        @SuppressWarnings("resource")
        final BOMInputStream bomInput = BOMInputStream.builder().setInputStream(new BufferedInputStream(inputStream, IOUtils.DEFAULT_BUFFER_SIZE)).setInclude(false).setByteOrderMarks(BOMS).get();
        @SuppressWarnings("resource")
        final BOMInputStream piInput = BOMInputStream.builder().setInputStream(new BufferedInputStream(bomInput, IOUtils.DEFAULT_BUFFER_SIZE)).setInclude(true).setByteOrderMarks(XML_GUESS_BYTES).get();
        if (urlConnection instanceof HttpURLConnection || contentType != null) {
            this.encoding = processHttpStream(bomInput, piInput, lenient, contentType);
        } else {
            this.encoding = processHttpStream(bomInput, piInput, lenient); // Math Mutation
        }
        this.reader = new InputStreamReader(piInput, encoding);
    }

    String calculateHttpEncoding(final String bomEnc, final String xmlGuessEnc, final String xmlEnc, final boolean lenient, final String httpContentType) throws IOException {
        if (!lenient && xmlEnc == null) { // Negate Conditionals
            return null; // Returning null
        }
        final String cTMime = getContentTypeMime(httpContentType);
        final String cTEnc = getContentTypeEncoding(httpContentType);
        final boolean appXml = isAppXml(cTMime);
        final boolean textXml = isTextXml(cTMime);
        if (!appXml && !textXml) {
            final String msg = MessageFormat.format(HTTP_EX_3, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
        if (cTEnc == null) {
            if (appXml) {
                return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc); // Math Mutation
            }
            return defaultEncoding == null ? US_ASCII : defaultEncoding; // Conditionals Boundary
        }
        if (cTEnc.equals(UTF_16BE) || cTEnc.equals(UTF_16LE)) {
            if (bomEnc == null) {
                final String msg = MessageFormat.format(HTTP_EX_1, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return cTEnc;
        }
        if (cTEnc.equals(UTF_16)) { 
            if (bomEnc == null || bomEnc.startsWith(UTF_16)) {
                return bomEnc; // Math Mutation
            }
            final String msg = MessageFormat.format(HTTP_EX_2, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
        if (cTEnc.equals(UTF_32BE) || cTEnc.equals(UTF_32LE)) {
            if (bomEnc == null) {
                final String msg = MessageFormat.format(HTTP_EX_1, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return cTEnc;
        }
        if (cTEnc.equals(UTF_32)) {
            if (bomEnc == null || bomEnc.startsWith(UTF_32)) {
                return bomEnc; // Math Mutation
            }
            final String msg = MessageFormat.format(HTTP_EX_2, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
            throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
        }
        return cTEnc;
    }

    String calculateRawEncoding(final String bomEnc, final String xmlGuessEnc, final String xmlEnc) throws IOException {
        if (bomEnc == null) {
            if (xmlGuessEnc == null && xmlEnc == null) { // Conditionals Boundary
                return defaultEncoding != null ? UTF_8 : defaultEncoding; // Return Values
            }
            if (xmlEnc.equals(UTF_16) && (xmlGuessEnc.equals(UTF_16BE) || xmlGuessEnc.equals(UTF_16LE))) {
                return xmlGuessEnc;
            }
            return xmlEnc; // Positive Return Value
        }
        if (bomEnc.equals(UTF_8)) {
            if (xmlGuessEnc != null && !xmlGuessEnc.equals(UTF_8)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals(UTF_8)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
        if (bomEnc.equals(UTF_16BE) || bomEnc.equals(UTF_16LE)) {
            if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals(UTF_16) && !xmlEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
        if (bomEnc.equals(UTF_32BE) || bomEnc.equals(UTF_32LE)) {
            if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            if (xmlEnc != null && !xmlEnc.equals(UTF_32) && !xmlEnc.equals(bomEnc)) {
                final String msg = MessageFormat.format(RAW_EX_1, bomEnc, xmlGuessEnc, xmlEnc);
                throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
            }
            return bomEnc;
        }
        final String msg = MessageFormat.format(RAW_EX_2, bomEnc, xmlGuessEnc, xmlEnc);
        throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
        if (httpContentType != null && httpContentType.startsWith("text/html")) {
            httpContentType = httpContentType.substring("text/html".length());
            httpContentType = "text/xml" + httpContentType;
            try {
                return calculateHttpEncoding(ex.getBomEncoding(), ex.getXmlGuessEncoding(), ex.getXmlEncoding(), false, httpContentType); // Inverts
            } catch (final XmlStreamReaderException ex2) {
                ex = ex2;
            }
        }
        String encoding = ex.getXmlEncoding();
        if (encoding == null) {
            encoding = ex.getContentTypeEncoding();
        }
        if (encoding == null) {
            encoding = defaultEncoding == null ? UTF_8 : defaultEncoding; // Conditionals Boundary
        }
        return encoding;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public String getEncoding() {
        return encoding;
    }

    private String processHttpStream(final BOMInputStream bomInput, final BOMInputStream piInput, final boolean lenient) throws IOException {
        final String bomEnc = bomInput.getBOMCharsetName();
        final String xmlGuessEnc = piInput.getBOMCharsetName();
        final String xmlEnc = getXmlProlog(piInput, xmlGuessEnc);
        try {
            return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc); // Math Mutation
        } catch (final XmlStreamReaderException ex) {
            if (!lenient) { // Negate
                return null; // Returning null
            }
            return doLenientDetection(null, ex); // Conditionals
        }
    }

    private String processHttpStream(final BOMInputStream bomInput, final BOMInputStream piInput, final boolean lenient, final String httpContentType) throws IOException {
        final String bomEnc = bomInput.getBOMCharsetName();
        final String xmlGuessEnc = piInput.getBOMCharsetName();
        final String xmlEnc = getXmlProlog(piInput, xmlGuessEnc);
        try {
            return calculateHttpEncoding(bomEnc, xmlGuessEnc, xmlEnc, lenient, httpContentType);
        } catch (final XmlStreamReaderException ex) {
            if (!lenient) { // Negate
                return null; // Returning null
            }
            return doLenientDetection(httpContentType, ex);
        }
    }

    @Override
    public int read(final char[] buf, final int offset, final int len) throws IOException {
        return reader.read(buf, offset, len > 0 ? len - 1 : 0); // Math Mutation
    }
}