package de.unileipzig.irpact.core.agent.consumer.attribute;

import de.unileipzig.irpact.commons.derivable.NamendDependentDoubleDerivable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDoubleGroupAttribute
        extends ConsumerAgentValueGroupAttribute, UnivariateDoubleDistribution, NamendDependentDoubleDerivable<ConsumerAgentAttribute> {

    @Override
    default boolean isDoubleGroupAttribute() {
        return true;
    }

    @Override
    default ConsumerAgentDoubleGroupAttribute asDoubleGroupAttribute() {
        return this;
    }

    @Override
    ConsumerAgentDoubleAttribute derive();

    @Override
    ConsumerAgentDoubleAttribute derive(double value);

    @Override
    ConsumerAgentDoubleAttribute derive(Number input);

    @Override
    ConsumerAgentDoubleAttribute derive(String str, Number value);

    @Override
    ConsumerAgentDoubleAttribute derive(String str, double value);

    UnivariateDoubleDistribution getDistribution();
}
