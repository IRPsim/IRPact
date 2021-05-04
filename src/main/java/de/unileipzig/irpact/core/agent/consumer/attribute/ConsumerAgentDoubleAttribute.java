package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDoubleAttribute extends ConsumerAgentValueAttribute<Number>, DoubleAttribute {

    @Override
    default ConsumerAgentDoubleAttribute asValueAttribute() {
        return this;
    }
}
