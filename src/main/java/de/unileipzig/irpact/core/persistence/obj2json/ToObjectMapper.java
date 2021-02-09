package de.unileipzig.irpact.core.persistence.obj2json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public interface ToObjectMapper {

    Persistable initalize(long uid, ObjectNode node) throws PersistenceException;

    void finalize(Map<Long, Persistable> cache, ObjectNode node, Persistable persistable) throws PersistenceException;
}
