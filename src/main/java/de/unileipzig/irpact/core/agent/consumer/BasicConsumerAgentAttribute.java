package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.attribute.DoubleAttributeGroupEntityBase;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttribute extends DoubleAttributeGroupEntityBase<ConsumerAgentGroupAttribute> implements ConsumerAgentAttribute {

    public BasicConsumerAgentAttribute() {
    }

    public BasicConsumerAgentAttribute(String name, ConsumerAgentGroupAttribute groupAttribute, double value) {
        setName(name);
        setGroup(groupAttribute);
        setDoubleValue(value);
    }
}
