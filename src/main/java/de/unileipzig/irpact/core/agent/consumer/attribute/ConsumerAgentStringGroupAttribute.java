package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentStringGroupAttribute
        extends ConsumerAgentValueGroupAttribute {

    @Override
    default boolean isStringGroupAttribute() {
        return true;
    }

    @Override
    default ConsumerAgentStringGroupAttribute asStringGroupAttribute() {
        return this;
    }

    @Override
    ConsumerAgentStringAttribute derive();

    UnivariateDistribution<String> getDistribution();
}
