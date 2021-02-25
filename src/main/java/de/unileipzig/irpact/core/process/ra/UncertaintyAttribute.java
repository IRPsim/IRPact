package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public class UncertaintyAttribute extends BasicConsumerAgentAttribute {

    protected boolean autoAdjustment;
    protected double convergence;

    public UncertaintyAttribute() {
    }

    public void setAutoAdjustment(boolean autoAdjustment) {
        this.autoAdjustment = autoAdjustment;
    }

    public boolean isAutoAdjustment() {
        return autoAdjustment;
    }

    public double getUncertainty() {
        return getDoubleValue();
    }

    public void setUncertainity(double value) {
        setDoubleValue(value);
    }

    public double getConvergence() {
        return convergence;
    }

    public void setConvergence(double convergence) {
        this.convergence = convergence;
    }

    @Override
    public UncertaintyAttribute copyAttribute() {
        UncertaintyAttribute copy = new UncertaintyAttribute();
        copy.setName(getName());
        copy.setUncertainity(getUncertainty());
        copy.setConvergence(getConvergence());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }
}
