package de.unileipzig.irpact.jadex.persistance.binary.io;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistDecodingInputStream extends BaseNCodecInputStream {

    public BinaryPersistDecodingInputStream(InputStream in) {
        super(in, IRPactBase32.BASE32, false);
    }
}
