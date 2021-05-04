package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.ValueAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentValueAttribute<V> extends ConsumerAgentAttribute, ValueAttribute<V> {

    @Override
    ConsumerAgentValueAttribute<V> asValueAttribute();

    @Override
    default ConsumerAgentRelatedAttribute<?> asRelatedAttribute() {
        throw new UnsupportedOperationException();
    }
}
