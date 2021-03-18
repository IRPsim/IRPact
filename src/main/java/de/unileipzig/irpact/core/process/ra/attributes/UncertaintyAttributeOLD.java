package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentAttribute;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("ENTFERNEN -> PR, SPEC")
public class UncertaintyAttributeOLD extends BasicConsumerAgentAttribute {

    protected boolean autoAdjustment;
    protected double convergence;

    public UncertaintyAttributeOLD() {
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
    public UncertaintyAttributeOLD copyAttribute() {
        UncertaintyAttributeOLD copy = new UncertaintyAttributeOLD();
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
