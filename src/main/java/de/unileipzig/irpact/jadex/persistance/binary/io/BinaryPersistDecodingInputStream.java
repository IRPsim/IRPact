package de.unileipzig.irpact.jadex.persistance.binary.io;

import org.apache.commons.codec.binary.BaseNCodecInputStream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistDecodingInputStream extends FilterInputStream {

    /**
     * @author Daniel Abitz
     */
    private static final class Internal extends BaseNCodecInputStream {
        protected Internal(InputStream input) {
            super(input, BinaryPersistBase32.BASE32, false);
        }
    }

    public BinaryPersistDecodingInputStream(InputStream in) {
        super(new Internal(in));
    }

    @Override
    public int read() throws IOException {
        int b = in.read();
        if(b == BinaryPersistBase32.FILL_IRP32_INT) {
            return BinaryPersistBase32.FILL_BASE32_INT;
        } else {
            return b;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int l = in.read(b, off, len);
        for(int i = l - 1; i >= off; i--) {
            if(b[i] == BinaryPersistBase32.FILL_IRP32) {
                b[i] = BinaryPersistBase32.FILL_BASE32;
            } else {
                break;
            }
        }
        return l;
    }
}
