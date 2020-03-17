package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.core.message.MessageSystem;
import de.unileipzig.irpact.core.network.AgentNetwork;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import org.slf4j.Logger;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment {

    SpatialModel getSpatialModel();

    MessageSystem getMessageSystem();

    AgentNetwork getAgentNetwork();

    EconomicSpace getEconomicSpace();

    SimulationCache getCache();

    Logger getLogger();

    //=========================
    //time
    //=========================

    void setTimeMode(Timestamp.Mode mode);

    long getSimulationStarttime();

    default long getSimulationTimeSinceStart() {
        return getSimulationTime() - getSimulationStarttime();
    }

    long getSimulationTime();

    double getTick();

    long getSystemTime();

    Timestamp getTimestamp();

    //=========================
    //util
    //=========================

    @Experimental
    void poke();
}
