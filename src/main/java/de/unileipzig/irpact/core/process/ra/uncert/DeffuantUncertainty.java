package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

/**
 * @author Daniel Abitz
 */
public class DeffuantUncertainty extends NameableBase implements Uncertainty {

    protected DeffuantUncertaintyData data;
    protected double speedOfConvergence;

    public DeffuantUncertainty() {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void updateUncertainty(ConsumerAgentAttribute attribute, double value) {
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        return data.getUncertainty(attribute);
    }

    @Override
    public double getSpeedOfConvergence(ConsumerAgentAttribute attribute) {
        return speedOfConvergence;
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    public void setData(DeffuantUncertaintyData data) {
        this.data = data;
    }

    public DeffuantUncertaintyData getData() {
        return data;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(
                name,
                data,
                speedOfConvergence
        );
    }
}
