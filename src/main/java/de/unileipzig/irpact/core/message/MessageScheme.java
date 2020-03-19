package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.Agent;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface MessageScheme extends Scheme {

    Collection<Message> createMessages(Agent sender);
}
