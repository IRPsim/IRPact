package de.unileipzig.irpact.jadex.persistance;

import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;

/**
 * @author Daniel Abitz
 */
public interface JadexPersistable extends Persistable {

    BinaryPersistData toPersistData();
}
