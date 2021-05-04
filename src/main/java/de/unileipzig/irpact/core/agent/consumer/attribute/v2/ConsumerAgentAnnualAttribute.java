package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.AnnualAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAnnualAttribute extends ConsumerAgentRelatedAttribute<Number>, AnnualAttribute {

    @Override
    default ConsumerAgentAnnualAttribute asRelatedAttribute() {
        return this;
    }
}
