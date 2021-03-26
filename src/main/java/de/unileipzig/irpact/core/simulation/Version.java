package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public interface Version extends ChecksumComparable {

    boolean isMismatch(Version other);

    String print();
}
