package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDoubleGroupAttribute extends ConsumerAgentValueGroupAttribute<Number>, UnivariateDoubleDistribution {

    @Override
    ConsumerAgentDoubleAttribute derive();

    ConsumerAgentDoubleAttribute derive(double value);

    UnivariateDoubleDistribution getDistribution();
}
