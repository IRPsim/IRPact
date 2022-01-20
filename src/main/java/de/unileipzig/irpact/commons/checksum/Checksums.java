package de.unileipzig.irpact.commons.checksum;

/**
 * @author Daniel Abitz
 */
public final class Checksums {

    public static final ChecksumCalculator DEFAULT = DefaultChecksumCalculator.INSTANCE;
    public static final ChecksumCalculator SMART = SmartChecksumCalculator.INSTANCE;
    public static final ChecksumCalculator ANNOTATED = AnnotatedChecksumCalculator.INSTANCE;

    private Checksums() {
    }
}
