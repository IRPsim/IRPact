package de.unileipzig.irpact.core.message.communication;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.message.Message;

/**
 * @author Daniel Abitz
 */
public interface CommunicationScheme extends Scheme {

    CommunicationEvent createEvent(Agent sender, Message msg);
}
