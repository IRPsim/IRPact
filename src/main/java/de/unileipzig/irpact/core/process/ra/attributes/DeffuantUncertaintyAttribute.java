package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.develop.Dev;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class DeffuantUncertaintyAttribute extends BasicConsumerAgentDoubleAttribute implements UncertaintyAttribute {

    protected ConsumerAgentDoubleAttribute relatedAttribute;
    protected DoubleRange range;
    protected double extremistUncertainty;
    protected double moderateUncertainty;
    protected double convergence;

    //!!!!
    //nochmal nachfragen wegen der range bei extremist und co.

    public DeffuantUncertaintyAttribute() {
    }

    @Override
    public boolean isAutoAdjustment() {
        return false;
    }

    public void setRelatedAttribute(ConsumerAgentDoubleAttribute relatedAttribute) {
        this.relatedAttribute = relatedAttribute;
    }

    public ConsumerAgentDoubleAttribute getRelatedAttribute() {
        return relatedAttribute;
    }

    public double getRelatedAttributeValue() {
        return getRelatedAttribute().getDoubleValue();
    }

    public void setRange(DoubleRange range) {
        this.range = range;
    }

    public DoubleRange getRange() {
        return range;
    }

    public boolean isModerate() {
        return range.isInRange(getRelatedAttributeValue());
    }

    public boolean isExtremist() {
        return range.isOutOfRange(getRelatedAttributeValue());
    }

    @Override
    public double getUncertainty() {
        return isExtremist() ? getExtremistUncertainty() : getModerateUncertainty();
    }

    public void setUncertainity(double value) {
        setDoubleValue(value);
    }

    public void setExtremistUncertainty(double extremistUncertainty) {
        this.extremistUncertainty = extremistUncertainty;
    }

    public double getExtremistUncertainty() {
        return extremistUncertainty;
    }

    public void setModerateUncertainty(double moderateUncertainty) {
        this.moderateUncertainty = moderateUncertainty;
    }

    public double getModerateUncertainty() {
        return moderateUncertainty;
    }

    @Override
    public double getConvergence() {
        return convergence;
    }

    public void setConvergence(double convergence) {
        this.convergence = convergence;
    }

    @Override
    public DeffuantUncertaintyAttribute copy() {
        return Dev.throwException();
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
