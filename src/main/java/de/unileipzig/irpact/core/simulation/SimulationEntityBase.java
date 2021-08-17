package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.time.TimeModel;
import de.unileipzig.irpact.core.util.AttributeHelper;

/**
 * @author Daniel Abitz
 */
public class SimulationEntityBase extends NameableBase implements SimulationEntity {

    protected SimulationEnvironment environment;

    public SimulationEntityBase() {
    }

    public SimulationEntityBase(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    protected SimulationEnvironment getValidEnvironment() {
        if(environment == null) {
            throw new NullPointerException("SimulationEnvironment");
        }
        return environment;
    }

    protected Rnd deriveNewRnd() {
        Rnd rnd = getValidEnvironment().getSimulationRandom();
        if(rnd == null) {
            throw new NullPointerException("Rnd");
        }
        return rnd.deriveInstance();
    }

    protected AttributeHelper getAttributeHelper() {
        AttributeHelper helper = getValidEnvironment().getAttributeHelper();
        if(helper == null) {
            throw new NullPointerException("AttributeHelper");
        }
        return helper;
    }
    protected SocialNetwork getNetwork() {
        SocialNetwork network = getValidEnvironment().getNetwork();
        if(network == null) {
            throw new NullPointerException("SocialNetwork");
        }
        return network;
    }

    protected SocialGraph getGraph() {
        SocialGraph graph = getNetwork().getGraph();
        if(graph == null) {
            throw new NullPointerException("SocialGraph");
        }
        return graph;
    }

    protected AgentManager getAgentManager() {
        AgentManager manager = getValidEnvironment().getAgents();
        if(manager == null) {
            throw new NullPointerException("AgentManager");
        }
        return manager;
    }

    protected TimeModel getTimeModel() {
        TimeModel model = getValidEnvironment().getTimeModel();
        if(model == null) {
            throw new NullPointerException("TimeModel");
        }
        return model;
    }

    protected Timestamp now() {
        return getTimeModel().now();
    }

    protected int getCurrentYear() {
        return getTimeModel().getCurrentYear();
    }

    protected LifeCycleControl getLifeCycleControl() {
        LifeCycleControl control = getValidEnvironment().getLifeCycleControl();
        if(control == null) {
            throw new NullPointerException("LifeCycleControl");
        }
        return control;
    }
}
