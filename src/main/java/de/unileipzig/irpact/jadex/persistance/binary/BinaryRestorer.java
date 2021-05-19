package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.Restorer;

/**
 * @author Daniel Abitz
 */
public interface BinaryRestorer<T> extends Restorer<T> {

    void setRestoreHelper(RestoreHelper helper);
}
