package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("ENTFERNEN -> PR, SPEC")
public class UncertaintyGroupAttributeOLD extends BasicConsumerAgentGroupAttribute {

    protected boolean autoAdjustment;
    protected UnivariateDoubleDistribution convergence;

    public UncertaintyGroupAttributeOLD() {
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
    public UncertaintyGroupAttributeOLD copyAttribute() {
        UncertaintyGroupAttributeOLD copy = new UncertaintyGroupAttributeOLD();
        copy.setName(getName());
        copy.setUncertainty(getUncertainty().copyDistribution());
        copy.setConvergence(getConvergence().copyDistribution());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }

    public UncertaintyAttributeOLD derive(double uncertainty, double convergence) {
        UncertaintyAttributeOLD attr = new UncertaintyAttributeOLD();
        attr.setName(getName());
        attr.setGroup(this);
        attr.setUncertainity(uncertainty);
        attr.setConvergence(convergence);
        attr.setAutoAdjustment(isAutoAdjustment());
        return attr;
    }

    @Override
    public UncertaintyAttributeOLD derive(double uncertainty) {
        double convergence = getConvergence().drawDoubleValue();
        return derive(uncertainty, convergence);
    }

    @Override
    public UncertaintyAttributeOLD derive() {
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
