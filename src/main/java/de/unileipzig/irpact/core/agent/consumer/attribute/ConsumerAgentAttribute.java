package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.GroupEntityAttribute;

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

    default ConsumerAgentAnnualAttribute asAnnualAttribute() {
        throw new UnsupportedOperationException();
    }

    default ConsumerAgentProductRelatedAttribute asProductRelatedAttribute() {
        throw new UnsupportedOperationException();
    }
}
