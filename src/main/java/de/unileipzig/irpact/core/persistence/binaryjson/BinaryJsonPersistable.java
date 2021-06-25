package de.unileipzig.irpact.core.persistence.binaryjson;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;

/**
 * @author Daniel Abitz
 */
public interface BinaryJsonPersistable extends Persistable {

    JsonNode getNode();

    BinaryPersistData toPersistData();
}
