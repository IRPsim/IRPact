package de.unileipzig.irpact.commons.util.irpact32;

import org.apache.commons.codec.binary.BaseNCodecOutputStream;

import java.io.OutputStream;

/**
 * @author Daniel Abitz
 */
public final class IRPactBase32OutputStream extends BaseNCodecOutputStream {

    public IRPactBase32OutputStream(OutputStream out) {
        super(out, IRPactBase32.BASE32, true);
    }
}
