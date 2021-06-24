package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.RelatedAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentRelatedAttribute<R> extends ConsumerAgentAttribute, RelatedAttribute<R> {

    @Override
    default ConsumerAgentRelatedAttribute<R> asRelatedAttribute() {
        return this;
    }
}
