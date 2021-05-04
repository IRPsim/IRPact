package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface MultiConsumerAgentGroupAttributeSupplier extends Nameable {

    void add(ConsumerAgentGroup cag, ConsumerAgentGroupAttributeSupplier supplier);

    void addAllGroupAttributesTo(ConsumerAgentGroup cag);
}
