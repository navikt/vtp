package no.nav.foreldrepenger.fpmock2.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public final class ByteArrayDataSource implements DataSource {
    private final String contentType;
    private final byte[] buf;
    private final int start;
    private final int len;

    public ByteArrayDataSource(byte[] buf, String contentType) {
        this(buf, 0, buf.length, contentType);
    }

    public ByteArrayDataSource(byte[] buf, int length, String contentType) {
        this(buf, 0, length, contentType);
    }

    public ByteArrayDataSource(byte[] buf, int start, int length, String contentType) {
        this.buf = buf;
        this.start = start;
        this.len = length;
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        if (contentType == null)
            return "application/octet-stream";
        return contentType;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(buf, start, len);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException();
    }
}
