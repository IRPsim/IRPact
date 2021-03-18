package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public class LinkedUncertaintyGroupAttribute extends BasicConsumerAgentGroupAttribute implements UncertaintyGroupAttribute {

    protected boolean autoAdjustment;
    protected ConsumerAgentGroupAttribute convergence;

    public LinkedUncertaintyGroupAttribute() {
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

    public void setConvergence(ConsumerAgentGroupAttribute convergence) {
        this.convergence = convergence;
    }

    public ConsumerAgentGroupAttribute getConvergence() {
        return convergence;
    }

    @Override
    public LinkedUncertaintyGroupAttribute copyAttribute() {
        LinkedUncertaintyGroupAttribute copy = new LinkedUncertaintyGroupAttribute();
        copy.setName(getName());
        copy.setUncertainty(getUncertainty().copyDistribution());
        copy.setConvergence(getConvergence());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }

    public BasicUncertaintyAttribute derive(double uncertainty, double convergence) {
        BasicUncertaintyAttribute attr = new BasicUncertaintyAttribute();
        attr.setName(getName());
        attr.setGroup(this);
        attr.setUncertainity(uncertainty);
        attr.setConvergence(convergence);
        attr.setAutoAdjustment(isAutoAdjustment());
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
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getUncertainty().getHashCode(),
                getConvergence().getHashCode()
        );
    }
}
