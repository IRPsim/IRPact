package de.unileipzig.irpact.core.simulation;

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

    TimeModule getTimeModule();

    SimulationConfiguration getConfiguration();

    EventManager getEventManager();

    Logger getLogger();

    //=========================
    //util
    //=========================

    void validate();
}
