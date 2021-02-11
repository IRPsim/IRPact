package de.unileipzig.irpact.core.persistence.obj2json;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;

/**
 * @author Daniel Abitz
 */
public interface ToJsonMapper {

    ObjectNode toJson(JsonNodeCreator creator, Persistable obj) throws PersistenceException;
}
