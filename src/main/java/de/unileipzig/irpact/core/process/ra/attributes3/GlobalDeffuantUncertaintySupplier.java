package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class GlobalDeffuantUncertaintySupplier extends NameableBase implements UncertaintySupplier {

    protected GlobalDeffuantUncertaintyData data;
    protected double convergence;
    protected Set<ConsumerAgentGroup> cags;
    protected DeffuantUncertainty uncertainty;

    public GlobalDeffuantUncertaintySupplier() {
        this(new LinkedHashSet<>());
    }

    public GlobalDeffuantUncertaintySupplier(Set<ConsumerAgentGroup> cags) {
        this.cags = cags;
    }

    @Override
    public boolean isSupported(ConsumerAgent agent) {
        return cags.contains(agent.getGroup());
    }

    @Override
    public DeffuantUncertainty createFor(ConsumerAgent agent) {
        return uncertainty;
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

    public void setConvergence(double convergence) {
        this.convergence = convergence;
    }

    public double getConvergence() {
        return convergence;
    }

    @Override
    public void initalize() {
        data.initalize();
        uncertainty = new DeffuantUncertainty();
        uncertainty.setName(getName() + "_x");
        uncertainty.setData(data);
        uncertainty.setConvergence(convergence);
    }
}
