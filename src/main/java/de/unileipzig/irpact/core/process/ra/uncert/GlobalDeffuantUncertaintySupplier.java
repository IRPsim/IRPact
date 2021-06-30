package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class GlobalDeffuantUncertaintySupplier extends NameableBase implements UncertaintySupplier {

    protected GlobalDeffuantUncertaintyData data;
    protected double speedOfConvergence;
    protected Set<ConsumerAgentGroup> cags;
    protected DeffuantUncertainty uncertainty;

    public GlobalDeffuantUncertaintySupplier() {
        this(new LinkedHashSet<>());
    }

    public GlobalDeffuantUncertaintySupplier(Set<ConsumerAgentGroup> cags) {
        this.cags = cags;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                data,
                speedOfConvergence,
                Checksums.SMART.getSetChecksum(cags, Nameable::getName),
                uncertainty
        );
    }

    @Override
    public boolean isSupported(ConsumerAgent agent) {
        return cags.contains(agent.getGroup());
    }

    @Override
    public DeffuantUncertainty createFor(ConsumerAgent agent) {
        return uncertainty;
    }

    public DeffuantUncertainty getUncertainty() {
        return uncertainty;
    }

    public void setUncertainty(DeffuantUncertainty uncertainty) {
        this.uncertainty = uncertainty;
    }

    public Set<ConsumerAgentGroup> getConsumerAgentGroups() {
        return cags;
    }

    public void setConsumerAgentGroups(Set<ConsumerAgentGroup> cags) {
        this.cags = cags;
    }

    public void setData(GlobalDeffuantUncertaintyData data) {
        this.data = data;
    }

    public GlobalDeffuantUncertaintyData getData() {
        return data;
    }

    public boolean addConsumerAgentGroup(ConsumerAgentGroup cag) {
        return cags.add(cag);
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    @Override
    public void initalize() {
        data.initalize();
        uncertainty = new DeffuantUncertainty();
        uncertainty.setName(getName() + "_x");
        uncertainty.setData(data);
        uncertainty.setSpeedOfConvergence(speedOfConvergence);
    }
}
