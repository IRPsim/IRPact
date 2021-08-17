package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.StringAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentStringAttribute extends ConsumerAgentValueAttribute<String>, StringAttribute {

    @Override
    default ConsumerAgentStringAttribute asValueAttribute() {
        return this;
    }
}
