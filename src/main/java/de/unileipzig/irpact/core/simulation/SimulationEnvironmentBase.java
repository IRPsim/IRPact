package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.concurrent.ResettableTimer;
import de.unileipzig.irpact.core.network.AgentNetwork;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import org.slf4j.Logger;

/**
 * @author Daniel Abitz
 */
public abstract class SimulationEnvironmentBase implements SimulationEnvironment {

    //para
    protected Timestamp.Mode mode = Timestamp.Mode.SYSTEM;
    protected SpatialModel spatialModel;
    protected AgentNetwork agentNetwork;
    protected EconomicSpace economicSpace;
    protected SimulationCache cache;
    protected ResettableTimer aktivityTimer;
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

    public void setTimer(ResettableTimer aktivityTimer) {
        this.aktivityTimer = aktivityTimer;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    //=========================
    //SimulationEnvironment
    //=========================

    @Override
    public void setTimeMode(Timestamp.Mode mode) {
        this.mode = mode;
    }

    @Override
    public Timestamp getTimestamp() {
        return new Timestamp(
                mode,
                getSystemTime(),
                getSimulationTime(),
                getTick()
        );
    }

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
    public SimulationCache getCache() {
        return cache;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void poke() {
        if(aktivityTimer != null) {
            aktivityTimer.reset();
        }
    }
}
