package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAttributeSupplier extends Nameable {

    boolean hasGroupAttribute(ConsumerAgentGroup cag);

    void addGroupAttributeTo(ConsumerAgentGroup cag);
}
