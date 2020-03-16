package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAttribute extends Attribute {

    ConsumerAgentGroupAttribute getGroup();
}
