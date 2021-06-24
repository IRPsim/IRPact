package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.develop.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public class UncertaintyWithConvergenceGroupAttribute extends BasicConsumerAgentDoubleGroupAttribute implements UncertaintyGroupAttribute {

    protected UnivariateDoubleDistribution convergence;

    public UncertaintyWithConvergenceGroupAttribute() {
    }

    public UnivariateDoubleDistribution getUncertainty() {
        return getDistribution();
    }

    public void setUncertainty(UnivariateDoubleDistribution distribution) {
        setDistribution(distribution);
    }

    public void setConvergence(UnivariateDoubleDistribution convergence) {
        this.convergence = convergence;
    }

    public UnivariateDoubleDistribution getConvergence() {
        return convergence;
    }

    @Override
    public UncertaintyWithConvergenceGroupAttribute copy() {
        UncertaintyWithConvergenceGroupAttribute copy = new UncertaintyWithConvergenceGroupAttribute();
        copy.setName(getName());
        copy.setUncertainty(getUncertainty().copyDistribution());
        copy.setConvergence(getConvergence().copyDistribution());
        return copy;
    }

    public BasicUncertaintyAttribute derive(double uncertainty, double convergence) {
        BasicUncertaintyAttribute attr = new BasicUncertaintyAttribute();
        attr.setName(getName());
        attr.setGroup(this);
        attr.setUncertainity(uncertainty);
        attr.setConvergence(convergence);
        return attr;
    }

    @Override
    public BasicUncertaintyAttribute derive(double uncertainty) {
        double convergence = getConvergence().drawDoubleValue();
        return derive(uncertainty, convergence);
    }

    @Override
    public BasicUncertaintyAttribute derive() {
        double uncertainty = getUncertainty().drawDoubleValue();
        return derive(uncertainty);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getUncertainty().getChecksum(),
                getConvergence().getChecksum()
        );
    }
}
