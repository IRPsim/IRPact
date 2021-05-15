package de.unileipzig.irpact.jadex.persistance.binary.io;

import org.apache.commons.codec.binary.BaseNCodecOutputStream;

import java.io.*;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistEncodingOutputStream extends FilterOutputStream {

    /**
     * @author Daniel Abitz
     */
    private static final class Internal extends BaseNCodecOutputStream {
        protected Internal(OutputStream output) {
            super(output, BinaryPersistBase32.BASE32, true);
        }
    }

    public BinaryPersistEncodingOutputStream(OutputStream out) {
        super(new Internal(out));
    }

    @Override
    public void write(int b) throws IOException {
        if(b == BinaryPersistBase32.FILL_BASE32_INT) {
            out.write(BinaryPersistBase32.FILL_IRP32_INT);
        } else {
            out.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for(int i = len - 1; i >= off; i--) {
            if(b[i] == BinaryPersistBase32.FILL_BASE32_INT) {
                b[i] = BinaryPersistBase32.FILL_IRP32_INT;
            } else {
                break;
            }
        }
        out.write(b, off, len);
    }
}
