package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface Uncertainty extends Nameable {

    boolean isEnabled();

    double getUncertainty(ConsumerAgentAttribute attribute);

    double getConvergence(ConsumerAgentAttribute attribute);
}
