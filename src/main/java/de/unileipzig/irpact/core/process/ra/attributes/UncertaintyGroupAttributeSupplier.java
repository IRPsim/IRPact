package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public interface UncertaintyGroupAttributeSupplier extends Nameable {

    void applyTo(ConsumerAgentGroup cag);

    void add(ConsumerAgentGroup cag, String attrName, UnivariateDoubleDistribution uncertDist);

    void add(ConsumerAgentGroup cag, String attrName, UnivariateDoubleDistribution uncertDist, UnivariateDoubleDistribution convDist);
}
