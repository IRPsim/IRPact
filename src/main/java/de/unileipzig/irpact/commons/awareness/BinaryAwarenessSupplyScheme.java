package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public class BinaryAwarenessSupplyScheme<T, U> extends NameableBase implements AwarenessSupplyScheme<T> {

    public BinaryAwarenessSupplyScheme() {
    }

    @Override
    public BinaryAwareness<T, U> derive() {
        return new BinaryAwareness<>();
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.unsupportedChecksum(getClass());
    }
}
