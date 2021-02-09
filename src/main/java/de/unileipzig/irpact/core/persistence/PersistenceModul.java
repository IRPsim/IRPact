package de.unileipzig.irpact.core.persistence;

import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;

/**
 * @author Daniel Abitz
 */
public interface PersistenceModul {

    void persist(Persistable persistable) throws PersistenceException;
}
