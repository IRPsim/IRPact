package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.attribute.v3.AbstractAttribute;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentAttribute extends AbstractAttribute implements ConsumerAgentAttribute {

    protected ConsumerAgentGroupAttribute group;

    @Override
    public abstract AbstractConsumerAgentAttribute copy();

    @Override
    public ConsumerAgentGroupAttribute getGroup() {
        return group;
    }

    public void setGroup(ConsumerAgentGroupAttribute group) {
        this.group = group;
    }
}
