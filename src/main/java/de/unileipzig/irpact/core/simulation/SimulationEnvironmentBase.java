package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.v2.commons.Check;
import de.unileipzig.irpact.core.message.MessageSystem;
import de.unileipzig.irpact.core.network.AgentNetwork;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import org.slf4j.Logger;

/**
 * @author Daniel Abitz
 */
public abstract class SimulationEnvironmentBase implements SimulationEnvironment {

    //para
    protected SpatialModel spatialModel;
    protected AgentNetwork agentNetwork;
    protected EconomicSpace economicSpace;
    protected MessageSystem messageSystem;
    protected TimeModule timeModule;
    protected SimulationConfiguration simulationConfiguration;
    protected EventManager eventManager;
    protected Logger logger;

    public SimulationEnvironmentBase() {
    }

    //=========================
    //new
    //=========================

    public void setSpatialModel(SpatialModel spatialModel) {
        this.spatialModel = spatialModel;
    }

    public void setEconomicSpace(EconomicSpace economicSpace) {
        this.economicSpace = economicSpace;
    }

    public void setAgentNetwork(AgentNetwork agentNetwork) {
        this.agentNetwork = agentNetwork;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    //=========================
    //SimulationEnvironment
    //=========================

    @Override
    public SpatialModel getSpatialModel() {
        return spatialModel;
    }

    @Override
    public EconomicSpace getEconomicSpace() {
        return economicSpace;
    }

    @Override
    public AgentNetwork getAgentNetwork() {
        return agentNetwork;
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public TimeModule getTimeModule() {
        return timeModule;
    }

    @Override
    public SimulationConfiguration getConfiguration() {
        return simulationConfiguration;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    //=========================
    //util
    //=========================

    @Override
    public void validate() {
        Check.requireNonNull(spatialModel, "spatialModel");
        Check.requireNonNull(agentNetwork, "agentNetwork");
        Check.requireNonNull(economicSpace, "economicSpace");
        Check.requireNonNull(messageSystem, "messageSystem");
        Check.requireNonNull(timeModule, "timeModule");
        Check.requireNonNull(simulationConfiguration, "simulationConfig");
        Check.requireNonNull(logger, "logger");
    }
}
