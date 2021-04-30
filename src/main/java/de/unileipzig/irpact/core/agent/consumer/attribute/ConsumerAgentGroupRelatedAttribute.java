package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AttributeType;
import de.unileipzig.irpact.commons.attribute.RelatedGroupAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupRelatedAttribute<T> extends ConsumerAgentGroupAttribute, RelatedGroupAttribute<T> {

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.GROUP || type == AttributeType.RELATED;
    }
}
