package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.GroupEntityAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAttribute extends GroupEntityAttribute<ConsumerAgentGroupAttribute> {

    @Override
    ConsumerAgentAttribute copy();

    @Override
    ConsumerAgentValueAttribute<?> asValueAttribute();

    @Override
     ConsumerAgentRelatedAttribute<?> asRelatedAttribute();
}
