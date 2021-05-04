package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleAttribute;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyAttribute extends BasicConsumerAgentDoubleAttribute {

    protected boolean autoAdjustment;
    protected double convergence;

    public BasicUncertaintyAttribute() {
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
    public BasicUncertaintyAttribute copy() {
        BasicUncertaintyAttribute copy = new BasicUncertaintyAttribute();
        copy.setName(getName());
        copy.setGroup(getGroup());
        copy.setUncertainity(getUncertainty());
        copy.setConvergence(getConvergence());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getGroup().getName(),
                getUncertainty(),
                getConvergence()
        );
    }
}
