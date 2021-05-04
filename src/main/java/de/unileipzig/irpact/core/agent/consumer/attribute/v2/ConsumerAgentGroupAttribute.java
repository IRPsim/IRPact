package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.DerivableBase;
import de.unileipzig.irpact.commons.attribute.v3.GroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupAttribute extends GroupAttribute, DerivableBase {

    @Override
    ConsumerAgentGroupAttribute copy();
}
