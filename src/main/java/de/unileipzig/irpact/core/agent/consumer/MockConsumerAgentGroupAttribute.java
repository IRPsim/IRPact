package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.distribution.DistributionBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public final class MockConsumerAgentGroupAttribute implements ConsumerAgentGroupAttribute {

    public static final MockConsumerAgentGroupAttribute INSTANCE = new MockConsumerAgentGroupAttribute();

    public MockConsumerAgentGroupAttribute() {
    }

    public static boolean isMock(ConsumerAgentAttribute caAttr) {
        ConsumerAgentGroupAttribute cagAttr = caAttr.getGroup();
        return isMock(cagAttr);
    }

    public static boolean isMock(ConsumerAgentGroupAttribute cagAttr) {
        return cagAttr == INSTANCE || (cagAttr != null && cagAttr.getClass() == MockConsumerAgentGroupAttribute.class);
    }

    protected UnsupportedOperationException newException() {
        throw new UnsupportedOperationException("mock group");
    }

    @Override
    public ConsumerAgentAttribute derive() {
        throw newException();
    }

    @Override
    public String getName() {
        throw newException();
    }

    @Override
    public boolean hasName(String input) {
        throw newException();
    }

    @Override
    public void setValue(DistributionBase value) {
        throw newException();
    }

    @Override
    public ConsumerAgentAttribute derive(double value) {
        throw newException();
    }

    @Override
    public UnivariateDoubleDistribution getValue() {
        throw newException();
    }

    @Override
    public double drawDoubleValue() {
        throw newException();
    }
}
