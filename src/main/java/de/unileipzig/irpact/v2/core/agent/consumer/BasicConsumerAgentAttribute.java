package de.unileipzig.irpact.v2.core.agent.consumer;

import de.unileipzig.irpact.v2.commons.attribute.DoubleAttributeGroupEntityBase;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttribute extends DoubleAttributeGroupEntityBase<ConsumerAgentGroupAttribute> implements ConsumerAgentAttribute {

    public BasicConsumerAgentAttribute(String name, ConsumerAgentGroupAttribute groupAttribute, double value) {
        setName(name);
        setGroup(groupAttribute);
        setValue(value);
    }
}
