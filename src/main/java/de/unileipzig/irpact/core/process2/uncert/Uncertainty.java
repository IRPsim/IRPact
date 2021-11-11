package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface Uncertainty extends Nameable {

    void updateUncertainty(ConsumerAgentAttribute attribute, double value);

    void setUncertainty(ConsumerAgentAttribute attribute, double value);

    double getUncertainty(ConsumerAgentAttribute attribute);
}
