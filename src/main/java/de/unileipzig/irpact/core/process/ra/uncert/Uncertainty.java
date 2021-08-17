package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface Uncertainty extends Nameable {

    boolean isEnabled();

    void updateUncertainty(ConsumerAgentAttribute attribute, double value);

    double getUncertainty(ConsumerAgentAttribute attribute);

    double getSpeedOfConvergence(ConsumerAgentAttribute attribute);
}
