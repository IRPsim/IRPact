package de.unileipzig.irpact.core.persistence.binary.io;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;

import java.io.*;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistEncodingOutputStream extends BaseNCodecOutputStream {

    public BinaryPersistEncodingOutputStream(OutputStream out) {
        super(out, IRPactBase32.BASE32, true);
    }
}
