package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface DeffuantUncertainty extends Nameable {

    void initalize();

    void update();

    double getUncertainty(ConsumerAgentAttribute attribute);
}
