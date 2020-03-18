package de.unileipzig.irpact.jadex.message;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.message.Message;

/**
 * @author Daniel Abitz
 */
@ToDo("vllt einbauen")
public interface MessageDeserializer {

    Message deserialize(String serializedMessage);
}
