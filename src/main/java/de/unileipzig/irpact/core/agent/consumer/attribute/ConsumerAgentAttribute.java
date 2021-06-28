package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.GroupEntityAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAttribute extends GroupEntityAttribute<ConsumerAgentGroupAttribute> {

    @Override
    ConsumerAgentAttribute copy();

    @Override
    default ConsumerAgentValueAttribute<?> asValueAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    default ConsumerAgentRelatedAttribute<?> asRelatedAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    default ConsumerAgentAnnualAttribute asAnnualAttribute() {
        throw new UnsupportedOperationException();
    }

    default boolean isProductRelatedAttribute() {
        return false;
    }

    default ConsumerAgentProductRelatedAttribute asProductRelatedAttribute() {
        throw new UnsupportedOperationException();
    }
}
