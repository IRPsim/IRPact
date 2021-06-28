package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public class BasicUncertainty extends NameableBase implements Uncertainty {

    protected double uncertainty;
    protected double speedOfConvergence;

    public BasicUncertainty() {
    }

    public BasicUncertainty(String name, double uncertainty, double speedOfConvergence) {
        setName(name);
        setUncertainty(uncertainty);
        setSpeedOfConvergence(speedOfConvergence);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void updateUncertainty(ConsumerAgentAttribute attribute, double value) {
        setUncertainty(value);
    }

    public double getUncertainty() {
        return uncertainty;
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        return uncertainty;
    }

    public void setUncertainty(double uncertainty) {
        this.uncertainty = uncertainty;
    }

    @Override
    public double getSpeedOfConvergence(ConsumerAgentAttribute attribute) {
        return speedOfConvergence;
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }
}
