package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public class BasicUncertaintyAttribute extends BasicConsumerAgentAttribute {

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
    public BasicUncertaintyAttribute copyAttribute() {
        BasicUncertaintyAttribute copy = new BasicUncertaintyAttribute();
        copy.setName(getName());
        copy.setGroup(getGroup());
        copy.setUncertainity(getUncertainty());
        copy.setConvergence(getConvergence());
        copy.setAutoAdjustment(isAutoAdjustment());
        return copy;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                getGroup().getName(),
                getUncertainty(),
                getConvergence()
        );
    }
}
