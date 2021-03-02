package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttribute extends BasicConsumerAgentGroupAttribute {

    protected boolean autoAdjustment;
    protected UnivariateDoubleDistribution convergence;

    public UncertaintyGroupAttribute() {
    }

    public void setAutoAdjustment(boolean autoAdjustment) {
        this.autoAdjustment = autoAdjustment;
    }

    public boolean isAutoAdjustment() {
        return autoAdjustment;
    }

    public UnivariateDoubleDistribution getUncertainty() {
        return getValue();
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
    public UncertaintyGroupAttribute copyAttribute() {
        UncertaintyGroupAttribute copy = new UncertaintyGroupAttribute();
        copy.setName(getName());
        copy.setUncertainty(getUncertainty().copyDistribution());
        copy.setConvergence(getConvergence().copyDistribution());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }

    public UncertaintyAttribute derive(double uncertainty, double convergence) {
        UncertaintyAttribute attr = new UncertaintyAttribute();
        attr.setName(getName());
        attr.setGroup(this);
        attr.setUncertainity(uncertainty);
        attr.setConvergence(convergence);
        attr.setAutoAdjustment(isAutoAdjustment());
        return attr;
    }

    @Override
    public UncertaintyAttribute derive(double uncertainty) {
        double convergence = getConvergence().drawDoubleValue();
        return derive(uncertainty, convergence);
    }

    @Override
    public UncertaintyAttribute derive() {
        double uncertainty = getUncertainty().drawDoubleValue();
        return derive(uncertainty);
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getUncertainty().getHashCode(),
                getConvergence().getHashCode()
        );
    }
}
