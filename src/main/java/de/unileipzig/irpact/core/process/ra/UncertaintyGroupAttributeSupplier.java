package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public interface UncertaintyGroupAttributeSupplier extends Nameable {

    void applyTo(ConsumerAgentGroup cag);
}
