package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.misc.Initialization;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.product.ProductManager;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.core.time.TimeModel;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment extends Initialization {

    //=========================
    //general
    //=========================

    void setup();

    //=========================
    //main components
    //=========================

    AgentManager getAgents();

    SocialNetwork getNetwork();

    ProductManager getProducts();

    SpatialModel getSpatialModel();

    TimeModel getTimeModel();

    //=========================
    //util
    //=========================

    Rnd getSimulationRandom();
}
