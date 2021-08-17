package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AnnualAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAnnualAttribute extends ConsumerAgentRelatedAttribute<Number>, AnnualAttribute {

    @Override
    default ConsumerAgentAnnualAttribute asRelatedAttribute() {
        return this;
    }

    @Override
    default ConsumerAgentAnnualAttribute asAnnualAttribute() {
        return this;
    }

    @Override
    default ConsumerAgentAttribute getAttribute(Number related) {
        return getAttribute(related.intValue());
    }

    @Override
    ConsumerAgentAttribute getAttribute(int year);

    @Override
    default ConsumerAgentAttribute removeAttribute(Number related) {
        return removeAttribute(related.intValue());
    }

    @Override
    ConsumerAgentAttribute removeAttribute(int year);
}
