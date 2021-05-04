package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.DirectDerivable;
import de.unileipzig.irpact.commons.attribute.v3.GroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAttribute extends GroupAttribute, DirectDerivable<ConsumerAgentAttribute> {

    @Override
    ConsumerAgentGroupAttribute copy();
}
