package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public interface DeffuantUncertaintyData extends Nameable {

    void initalize();

    void update();

    double getUncertainty(ConsumerAgentAttribute attribute);
}
