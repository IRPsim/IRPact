package de.unileipzig.irpact.core.misc.supplement;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentGroupAttributeSupplier implements DataSupplier {

    protected SimulationEnvironment environment;
    protected String name;
    protected UnivariateDoubleDistribution distribution;
    protected ConsumerAgentGroupAttribute newCagAttr;
    protected boolean disabled = false;

    public ConsumerAgentGroupAttributeSupplier() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistribution(UnivariateDoubleDistribution distribution) {
        this.distribution = distribution;
    }

    @Override
    public void initialize() {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(cag.hasGroupAttribute(name)) {
                disabled = true;
                return;
            }
        }
        BasicConsumerAgentGroupAttribute attr = new BasicConsumerAgentGroupAttribute();
        attr.setName(name);
        attr.setDistribution(distribution);
        newCagAttr = attr;
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public void execute() {
        if(disabled) {
            return;
        }
        environment.getAgents()
                .streamConsumerAgentGroups()
                .forEach(this::addGroupAttribute);
    }

    protected void addGroupAttribute(ConsumerAgentGroup cag) {
        if(!cag.addGroupAttribute(newCagAttr)) {
            throw new IllegalStateException("adding group attribute '" + name + "' failed; group: '" + cag.getName() + "'");
        }
    }
}
