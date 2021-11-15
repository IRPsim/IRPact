package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface Uncertainty extends Nameable {

    void updateOpinion(ConsumerAgentAttribute attribute, double oldValue, double newValue);

    void setUncertainty(ConsumerAgentAttribute attribute, double value);

    double getUncertainty(ConsumerAgentAttribute attribute);
}
