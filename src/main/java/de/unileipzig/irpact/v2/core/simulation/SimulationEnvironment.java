package de.unileipzig.irpact.v2.core.simulation;

import de.unileipzig.irpact.v2.core.agent.AgentManager;
import de.unileipzig.irpact.v2.core.network.SocialNetwork;
import de.unileipzig.irpact.v2.core.product.ProductManager;
import de.unileipzig.irpact.v2.core.spatial.SpatialModel;
import de.unileipzig.irpact.v2.core.time.TimeModel;

/**
 * @author Daniel Abitz
 */
public interface SimulationEnvironment {

    AgentManager getAgents();

    SocialNetwork getNetwork();

    ProductManager getProducts();

    SpatialModel getSpatialModel();

    TimeModel getTimeModel();
}
