package de.unileipzig.irpact.core.persistence.binaryjson;

import de.unileipzig.irpact.commons.persistence.Restorer;

/**
 * @author Daniel Abitz
 */
public interface BinaryRestorer<T> extends Restorer<T> {

    void setRestoreHelper(RestoreHelper helper);
}
