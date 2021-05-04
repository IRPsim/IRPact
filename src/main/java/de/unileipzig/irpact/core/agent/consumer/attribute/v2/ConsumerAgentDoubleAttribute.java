package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.DoubleAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDoubleAttribute extends ConsumerAgentValueAttribute<Number>, DoubleAttribute {

    @Override
    default ConsumerAgentDoubleAttribute asValueAttribute() {
        return this;
    }
}
