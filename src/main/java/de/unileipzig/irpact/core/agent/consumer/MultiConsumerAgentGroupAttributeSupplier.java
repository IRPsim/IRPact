package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface MultiConsumerAgentGroupAttributeSupplier extends Nameable {

    void add(ConsumerAgentGroup cag, ConsumerAgentGroupAttributeSupplier supplier);

    void addAllGroupAttributesTo(ConsumerAgentGroup cag);
}
