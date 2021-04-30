package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * Allows to add custom attributes to the consumer agent spatial information.
 *
 * @author Daniel Abitz
 */
public interface ConsumerAgentSpatialAttributeSupplier extends Nameable {

    boolean hasAttribute(ConsumerAgent ca);

    void addAttributeTo(ConsumerAgent ca);
}
