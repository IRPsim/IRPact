package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.develop.AddToPersist;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class GroupBasedDeffuantUncertaintySupplier extends NameableBase implements UncertaintySupplier {

    protected GroupBasedDeffuantUncertaintyData data;
    protected double convergence;
    protected ConsumerAgentGroup cag;
    protected DeffuantUncertainty uncertainty;

    public GroupBasedDeffuantUncertaintySupplier() {
    }

    @Override
    public boolean isSupported(ConsumerAgent agent) {
        return cag == agent.getGroup();
    }

    @Override
    public DeffuantUncertainty createFor(ConsumerAgent agent) {
        return uncertainty;
    }

    public void setData(GroupBasedDeffuantUncertaintyData data) {
        this.data = data;
    }

    public GroupBasedDeffuantUncertaintyData getData() {
        return data;
    }

    public void setConsumerAgentGroup(ConsumerAgentGroup cag) {
        this.cag = cag;
    }

    public ConsumerAgentGroup getConsumerAgentGroup() {
        return cag;
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
