package de.unileipzig.irpact.core.persistence.binary;

import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;

/**
 * @author Daniel Abitz
 */
public interface BinaryPersistable extends Persistable {

    BinaryPersistData toPersistData();
}
