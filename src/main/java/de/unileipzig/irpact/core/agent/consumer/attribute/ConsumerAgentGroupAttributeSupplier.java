package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAttributeSupplier extends Nameable {

    boolean hasGroupAttribute(ConsumerAgentGroup cag);

    void addGroupAttributeTo(ConsumerAgentGroup cag);
}
