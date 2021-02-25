package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Nameable;

/**
 * Allows to add custom attributes to the consumer agent spatial information.
 *
 * @author Daniel Abitz
 */
public interface ConsumerAgentSpatialAttributeSupplier extends Nameable {

    boolean hasAttribute(ConsumerAgent ca);

    void addAttributeTo(ConsumerAgent ca);
}
