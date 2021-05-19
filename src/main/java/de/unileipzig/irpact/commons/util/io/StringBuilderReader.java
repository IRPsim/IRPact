package de.unileipzig.irpact.commons.util.io;

import java.io.IOException;
import java.io.Reader;

/**
 * Copy of {@link java.io.StringReader}.
 *
 * @author Daniel Abitz
 */
@SuppressWarnings({"SynchronizeOnNonFinalField", "NullableProblems", "FieldMayBeFinal"})
public class StringBuilderReader extends Reader {

    private StringBuilder builder;
    private int length;
    private int next = 0;
    private int mark = 0;

    public StringBuilderReader(StringBuilder builder) {
        this.builder = builder;
        this.length = builder.length();
    }

    private void ensureOpen() throws IOException {
        if(builder == null) {
            throw new IOException("Stream closed");
        }
    }

    public int read() throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length)
                return -1;
            return builder.charAt(next++);
        }
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                    ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            if (next >= length)
                return -1;
            int n = Math.min(length - next, len);
            builder.getChars(next, next + n, cbuf, off);
            next += n;
            return n;
        }
    }

    public long skip(long ns) throws IOException {
        synchronized (lock) {
            ensureOpen();
            if (next >= length)
                return 0;
            // Bound skip by beginning and end of the source
            long n = Math.min(length - next, ns);
            n = Math.max(-next, n);
            next += n;
            return n;
        }
    }

    public boolean ready() throws IOException {
        synchronized (lock) {
            ensureOpen();
            return true;
        }
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0){
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (lock) {
            ensureOpen();
            mark = next;
        }
    }

    public void reset() throws IOException {
        synchronized (lock) {
            ensureOpen();
            next = mark;
        }
    }

    public void close() {
        synchronized (lock) {
            builder = null;
        }
    }
}
