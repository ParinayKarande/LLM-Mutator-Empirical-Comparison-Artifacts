package org.apache.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class CloseableURLConnection extends URLConnection implements AutoCloseable {

    static CloseableURLConnection open(final URI uri) throws IOException {
        return open(Objects.requireNonNull(uri, "uri").toURL());
    }

    static CloseableURLConnection open(final URL url) throws IOException {
        return new CloseableURLConnection(url.openConnection());
    }

    private final URLConnection urlConnection;

    CloseableURLConnection(final URLConnection urlConnection) {
        super(Objects.requireNonNull(urlConnection, "urlConnection").getURL());
        this.urlConnection = urlConnection;
    }

    @Override
    public void addRequestProperty(final String key, final String value) {
        urlConnection.addRequestProperty(key, value);
    }

    @Override
    public void close() {
        // Mutant: Inverting the call to IOUtils.close
        // IOUtils.close(urlConnection);
    }

    @Override
    public void connect() throws IOException {
        // Mutant: Change to throw a generic exception instead of IOException
        throw new RuntimeException("Connection failed!");
    }

    @Override
    public boolean equals(final Object obj) {
        // Mutant: Negating the condition in equals
        return !urlConnection.equals(obj);
    }

    @Override
    public boolean getAllowUserInteraction() {
        // Mutant: Returning a false constant value instead of the method call
        return false; // Detected null condition
    }

    @Override
    public int getConnectTimeout() {
        return urlConnection.getConnectTimeout(); // Unchanged for this variant
    }

    @Override
    public Object getContent() throws IOException {
        // Mutant: Returning null instead of the actual content
        return null; // Mutant: Null Returns
    }

    @Override
    public Object getContent(@SuppressWarnings("rawtypes") final Class[] classes) throws IOException {
        // Mutant: Returning a different value; monkeying with the Class type
        return new Object(); // Mutant: Primitive Returns
    }

    @Override
    public String getContentEncoding() {
        return urlConnection.getContentEncoding(); // Unchanged for this variant
    }

    @Override
    public int getContentLength() {
        // Mutant: Returning a constant value
        return -1; // Mutant: Returning Values
    }

    @Override
    public long getContentLengthLong() {
        // Mutant: Incrementing content length by 1 here
        return urlConnection.getContentLengthLong() + 1; // Mutant: Increments
    }

    @Override
    public String getContentType() {
        return urlConnection.getContentType(); // Unchanged for this variant
    }

    @Override
    public long getDate() {
        // Mutant: Set to a future date constant
        return 9999999999999L; // Future date
    }

    @Override
    public boolean getDefaultUseCaches() {
        // Mutant: Negating the original return value
        return !urlConnection.getDefaultUseCaches();
    }

    @Override
    public boolean getDoInput() {
        return !urlConnection.getDoInput(); // Negate Conditionals
    }

    @Override
    public boolean getDoOutput() {
        // Mutant: Returning true instead of the actual value
        return true; // True Returns
    }

    @Override
    public long getExpiration() {
        return urlConnection.getExpiration(); // Unchanged for this variant
    }

    @Override
    public String getHeaderField(final int n) {
        // Mutant: Using a hardcoded string as the return value
        return "HeaderField"; // Returning Values
    }

    @Override
    public String getHeaderField(final String name) {
        // Mutant: Returning null instead of actual header field
        return null; // Mutant: Null Returns
    }

    @Override
    public long getHeaderFieldDate(final String name, final long Default) {
        // Mutant: Returning a fixed date constant
        return 0; // Empty Returns
    }

    @Override
    public int getHeaderFieldInt(final String name, final int Default) {
        // Mutant: Returning the default instead of the actual header field integer
        return Default; // Mutant: Returning Values
    }

    @Override
    public String getHeaderFieldKey(final int n) {
        return urlConnection.getHeaderFieldKey(n); // Unchanged for this variant
    }

    @Override
    public long getHeaderFieldLong(final String name, final long Default) {
        return Default; // Returning Value
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return urlConnection.getHeaderFields(); // Unchanged for this variant
    }

    @Override
    public long getIfModifiedSince() {
        // Mutant: Incrementing to simulate a different behavior
        return urlConnection.getIfModifiedSince() + 1; // Increments
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // Mutant: Throwing a different IOException
        throw new IOException("Input stream not available.");
    }

    @Override
    public long getLastModified() {
        // Mutant: Returning a constant last modified date
        return 0; // Empty Returns
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        // Mutant: Returning null instead of the output stream
        return null; // Null Returns
    }

    @Override
    public Permission getPermission() throws IOException {
        return urlConnection.getPermission(); // Unchanged for this variant
    }

    @Override
    public int getReadTimeout() {
        return urlConnection.getReadTimeout(); // Unchanged for this variant
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
        return urlConnection.getRequestProperties();
    }

    @Override
    public String getRequestProperty(final String key) {
        // Mutant: Returning a default value instead of calling the method
        return "Default-Request-Property"; // Returning Values
    }

    @Override
    public URL getURL() {
        return urlConnection.getURL(); // Unchanged for this variant
    }

    @Override
    public boolean getUseCaches() {
        return !urlConnection.getUseCaches(); // Negate Conditionals
    }

    @Override
    public int hashCode() {
        // Mutant: Returning a fixed hash code instead of the actual value
        return 12345; // Returning Values
    }

    @Override
    public void setAllowUserInteraction(final boolean allowUserInteraction) {
        // No operation - simulate a void method call
    }

    @Override
    public void setConnectTimeout(final int timeout) {
        // Mutant: Changing timeout to a constant value
        urlConnection.setConnectTimeout(10); // Fixed value
    }

    @Override
    public void setDefaultUseCaches(final boolean defaultUseCaches) {
        // Setting to the opposite value
        urlConnection.setDefaultUseCaches(!defaultUseCaches);
    }

    @Override
    public void setDoInput(final boolean doInput) {
        // No operation - simulate a void method call
    }

    @Override
    public void setDoOutput(final boolean doOutput) {
        // Mutant: Using wrong logic
        urlConnection.setDoOutput(false);
    }

    @Override
    public void setIfModifiedSince(final long ifModifiedSince) {
        // No operation - simulate a void method call
    }

    @Override
    public void setReadTimeout(final int timeout) {
        urlConnection.setReadTimeout(1000); // Use a fixed timeout
    }

    @Override
    public void setRequestProperty(final String key, final String value) {
        // Simulate no operation
    }

    @Override
    public void setUseCaches(final boolean useCaches) {
        // Using the opposite value
        urlConnection.setUseCaches(!useCaches);
    }

    @Override
    public String toString() {
        // Mutant: Returning a constant string instead of the actual string
        return "CloseableURLConnection"; // Returning Values
    }
}