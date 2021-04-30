package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.attribute.AttributeType;
import de.unileipzig.irpact.commons.distributionattribut.UnivariateDoubleDistributionAttribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentGroupValueAttribute extends ConsumerAgentGroupAttribute, UnivariateDoubleDistributionAttribute {

    @Override
    default boolean isType(AttributeType type) {
        return type == AttributeType.GROUP || type == AttributeType.VALUE;
    }
}
