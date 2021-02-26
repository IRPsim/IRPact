package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.IsEquals;

/**
 * @author Daniel Abitz
 */
public interface Version extends IsEquals {

    boolean isMismatch(Version other);

    String print();
}
