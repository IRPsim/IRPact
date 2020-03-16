package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.attribute.Attribute;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentAttribute implements ConsumerAgentAttribute {

    private ConsumerAgentGroupAttribute groupAttribute;
    private double value;

    public BasicConsumerAgentAttribute(ConsumerAgentGroupAttribute groupAttribute, double value) {
        this.groupAttribute = Check.requireNonNull(groupAttribute, "groupAttribute");
        this.value = value;
    }

    @Override
    public ConsumerAgentGroupAttribute getGroup() {
        return groupAttribute;
    }

    @Override
    public BasicConsumerAgentAttribute copy() {
        return new BasicConsumerAgentAttribute(groupAttribute, value);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public String getName() {
        return getGroup().getName();
    }
}
