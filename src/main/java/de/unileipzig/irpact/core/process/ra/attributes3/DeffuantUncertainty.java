package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class DeffuantUncertainty extends NameableBase implements Uncertainty{

    protected DeffuantUncertaintyData data;
    protected double convergence;

    public DeffuantUncertainty() {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        return data.getUncertainty(attribute);
    }

    @Override
    public double getConvergence(ConsumerAgentAttribute attribute) {
        return convergence;
    }

    public void setConvergence(double convergence) {
        this.convergence = convergence;
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
                convergence
        );
    }
}
